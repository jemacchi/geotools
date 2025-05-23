/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.temporal.reference;

import java.util.Date;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.referencing.ReferenceIdentifier;
import org.geotools.api.temporal.TemporalCoordinate;
import org.geotools.api.temporal.TemporalCoordinateSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.temporal.object.DefaultTemporalCoordinate;
import org.geotools.util.Utilities;

/** @author Mehdi Sidhoum (Geomatys) */
public class DefaultTemporalCoordinateSystem extends DefaultTemporalReferenceSystem
        implements TemporalCoordinateSystem {

    /** The origin of the scale, it must be specified in the Gregorian calendar with time of day in UTC. */
    private Date origin;
    /**
     * The name of a single unit of measure used as the base interval for the scale. it shall be one of those units of
     * measure for time specified by ISO 31-1, or a multiple of one of those units, as specified by ISO 1000.
     */
    private InternationalString interval;

    public DefaultTemporalCoordinateSystem(
            ReferenceIdentifier name, Extent domainOfValidity, Date origin, InternationalString interval) {
        super(name, domainOfValidity);
        this.origin = origin;
        this.interval = interval;
    }

    public void setOrigin(Date origin) {
        this.origin = origin;
    }

    public void setInterval(InternationalString interval) {
        this.interval = interval;
    }

    @Override
    public Date getOrigin() {
        return origin;
    }

    @Override
    public InternationalString getInterval() {
        return interval;
    }

    /**
     * Returns the equivalent Date in the Gregorian calendar and UTC of a coordinate value defined in this temporal
     * coordinate system.
     */
    @Override
    public Date transformCoord(TemporalCoordinate c_value) {
        Date response;
        final long yearMS = 31536000000L;
        final long monthMS = 2628000000L;
        final long weekMS = 604800000L;
        final long dayMS = 86400000L;
        final long hourMS = 3600000L;
        final long minMS = 60000L;
        final long secondMS = 1000L;
        DefaultTemporalCoordinate value = (DefaultTemporalCoordinate) c_value;
        Number f = 0;
        if (value.getFrame() != null && value.getFrame() instanceof TemporalCoordinateSystem) {
            if (value.getCoordinateValue() != null) {
                float n = value.getCoordinateValue().floatValue();
                if (interval.toString().equals("year")) {
                    f = n * yearMS;
                } else if (interval.toString().equals("month")) {
                    f = n * monthMS;
                } else if (interval.toString().equals("week")) {
                    f = n * weekMS;
                } else if (interval.toString().equals("day")) {
                    f = n * dayMS;
                } else if (interval.toString().equals("hour")) {
                    f = n * hourMS;
                } else if (interval.toString().equals("minute")) {
                    f = n * minMS;
                } else if (interval.toString().equals("second")) {
                    f = n * secondMS;
                } else if (interval.toString().equals("millisecond")) {
                    f = n;
                } else {
                    throw new IllegalArgumentException(
                            "The name of a single unit of measure used as the base interval for the scale in this current TemporalCoordinateSystem is not supported !");
                }
                response = new Date(origin.getTime() + f.longValue());
                return response;
            } else {
                return null;
            }
        } else {
            throw new IllegalArgumentException("The TemporalCoordinate argument must be a TemporalCoordinate ! ");
        }
    }

    /** Returns the equivalent TemporalCoordinate of a Date in Gregorian Calendar. Default of unit is millisecond. */
    @Override
    public TemporalCoordinate transformDateTime(Date dateTime) {
        final long yearMS = 31536000000L;
        final long monthMS = 2628000000L;
        final long weekMS = 604800000L;
        final long dayMS = 86400000L;
        final long hourMS = 3600000L;
        final long minMS = 60000L;
        final long secondMS = 1000L;

        Number coordinateValue = Math.abs(dateTime.getTime() - origin.getTime());
        if (interval.toString().equals("year")) {
            coordinateValue = (float) coordinateValue.longValue() / yearMS;
        } else if (interval.toString().equals("month")) {
            coordinateValue = (float) coordinateValue.longValue() / monthMS;
        } else if (interval.toString().equals("week")) {
            coordinateValue = (float) coordinateValue.longValue() / weekMS;
        } else if (interval.toString().equals("day")) {
            coordinateValue = (float) coordinateValue.longValue() / dayMS;
        } else if (interval.toString().equals("hour")) {
            coordinateValue = (float) coordinateValue.longValue() / hourMS;
        } else if (interval.toString().equals("minute")) {
            coordinateValue = (float) coordinateValue.longValue() / minMS;
        } else if (interval.toString().equals("second")) {
            coordinateValue = (float) coordinateValue.longValue() / secondMS;
        }
        TemporalCoordinate response = new DefaultTemporalCoordinate(this, null, coordinateValue);
        return response;
    }

    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true;
        }
        if (object instanceof DefaultTemporalCoordinateSystem && super.equals(object)) {
            if (object instanceof DefaultTemporalCoordinateSystem) {
                final DefaultTemporalCoordinateSystem that = (DefaultTemporalCoordinateSystem) object;

                return Utilities.equals(this.interval, that.interval) && Utilities.equals(this.origin, that.origin);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.interval != null ? this.interval.hashCode() : 0);
        hash = 37 * hash + (this.origin != null ? this.origin.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("TemporalCoordinateSystem:").append('\n');
        if (interval != null) {
            s.append("interval:").append(interval).append('\n');
        }
        if (origin != null) {
            s.append("origin:").append(origin).append('\n');
        }
        return s.toString();
    }
}
