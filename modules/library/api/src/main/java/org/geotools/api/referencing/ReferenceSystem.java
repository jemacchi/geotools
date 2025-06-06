/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.referencing;

import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.util.InternationalString;

/**
 * Description of a spatial and temporal reference system used by a dataset.
 *
 * @departure This interface was initially derived from an ISO 19111 specification published in 2003. Later revisions
 *     (in 2005) rely on an interface defined in ISO 19115 instead. The annotations were updated accordingly, but this
 *     interface is still defined in the referencing package instead of metadata and the {@link #getScope()} method
 *     still defined here for this historical reason.
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 1.0
 * @see org.geotools.api.referencing.crs.CoordinateReferenceSystem
 */
public interface ReferenceSystem extends IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by
     * {@link #getDomainOfValidity}.
     *
     * @see #getDomainOfValidity
     * @since GeoAPI 2.1
     */
    String DOMAIN_OF_VALIDITY_KEY = "domainOfValidity";

    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by {@link #getScope}.
     *
     * @see #getScope
     */
    String SCOPE_KEY = "scope";

    /**
     * Area or region or timeframe in which this (coordinate) reference system is valid.
     *
     * @return The reference system valid domain, or {@code null} if not available.
     * @since GeoAPI 2.1
     */
    Extent getDomainOfValidity();

    /**
     * Description of domain of usage, or limitations of usage, for which this (coordinate) reference system object is
     * valid.
     *
     * @return The domain of usage, or {@code null} if none.
     */
    InternationalString getScope();
}
