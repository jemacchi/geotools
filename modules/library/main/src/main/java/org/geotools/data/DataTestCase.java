/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.Id;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Before;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;

/**
 * A set of constructs and utility methods used to test the data module.
 *
 * <p>By isolating a common set of {@link SimpleFeature}s, {@link SimpleFeatureType}s and {@link Filter}s we are able to
 * reduce the amount of overhead in setting up new tests.
 *
 * <p>This code has been made part of the public {@code geotools.jar} to provide a starting point for test cases
 * involving Data constructs.
 *
 * @version $Id$
 * @author Jody Garnett, Refractions Research
 * @todo It should be possible to move this class in the {@code sample-data} module.
 */
public abstract class DataTestCase {
    protected GeometryFactory gf;
    protected SimpleFeatureType roadType; // road: id,geom,name
    protected SimpleFeatureType subRoadType; // road: id,geom
    protected SimpleFeature[] roadFeatures;
    protected ReferencedEnvelope roadBounds;
    protected ReferencedEnvelope rd12Bounds;
    protected Filter rd1Filter;
    protected Filter rd2Filter;
    protected Filter rd12Filter;
    protected SimpleFeature newRoad;

    protected SimpleFeatureType riverType; // river: id, geom, river, flow
    protected SimpleFeatureType subRiverType; // river: river, flow
    protected SimpleFeature[] riverFeatures;
    protected ReferencedEnvelope riverBounds;
    protected Filter rv1Filter;
    protected SimpleFeature newRiver;

    protected SimpleFeatureType lakeType; // lake: id, geom, name
    protected SimpleFeature[] lakeFeatures;
    protected ReferencedEnvelope lakeBounds;

    protected SimpleFeatureType invalidGeomType; // invalidGeom: id, geom, name
    protected SimpleFeature[] invalidGeomFeatures;
    protected ReferencedEnvelope invalidGeomBounds;

    protected SimpleFeatureType buildingType; // building: id, geom, name
    protected SimpleFeature[] buildingFeatures;
    protected ReferencedEnvelope buildingBounds;
    protected FilterFactory ff;

    public DataTestCase() {}

    protected int expected(Filter filter) {
        if (filter instanceof Id) {
            Id id = (Id) filter;
            return id.getIDs().size();
        }
        return -1;
    }

    /** Invoked before a test is run. The default implementation invokes {@link #dataSetUp}. */
    @Before
    public void init() throws Exception {
        ff = CommonFactoryFinder.getFilterFactory(null);
        dataSetUp();
    }

    /**
     * Loads the data.
     *
     * @see #init()
     */
    protected void dataSetUp() throws Exception {
        String namespace = getClass().getSimpleName();
        roadType = DataUtilities.createType(namespace + ".road", "id:0,geom:LineString,name:String,uuid:UUID");
        subRoadType = DataUtilities.createType(namespace + "road", "id:0,geom:LineString");
        gf = new GeometryFactory();

        roadFeatures = new SimpleFeature[3];

        //           3,2
        //  2,2 +-----+-----+ 4,2
        //     /     rd1     \
        // 1,1+               +5,1
        roadFeatures[0] = SimpleFeatureBuilder.build(
                roadType,
                new Object[] {Integer.valueOf(1), line(new int[] {1, 1, 2, 2, 4, 2, 5, 1}), "r1", UUID.randomUUID()},
                "road.rd1");

        //       + 3,4
        //       + 3,3
        //  rd2  + 3,2
        //       |
        //    3,0+
        roadFeatures[1] = SimpleFeatureBuilder.build(
                roadType,
                new Object[] {Integer.valueOf(2), line(new int[] {3, 0, 3, 2, 3, 3, 3, 4}), "r2", UUID.randomUUID()},
                "road.rd2");

        //     rd3     + 5,3
        //            /
        //  3,2 +----+ 4,2
        roadFeatures[2] = SimpleFeatureBuilder.build(
                roadType,
                new Object[] {Integer.valueOf(3), line(new int[] {3, 2, 4, 2, 5, 3}), "r3", UUID.randomUUID()},
                "road.rd3");
        roadBounds = new ReferencedEnvelope();
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[0].getBounds()));
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[1].getBounds()));
        roadBounds.expandToInclude(new ReferencedEnvelope(roadFeatures[2].getBounds()));

        rd1Filter = ff.id(Collections.singleton(ff.featureId("road.rd1")));
        rd2Filter = ff.id(Collections.singleton(ff.featureId("road.rd2")));

        Id create = ff.id(new HashSet<>(Arrays.asList(ff.featureId("road.rd1"), ff.featureId("road.rd2"))));

        rd12Filter = create;

        rd12Bounds = new ReferencedEnvelope();
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[0].getBounds()));
        rd12Bounds.expandToInclude(new ReferencedEnvelope(roadFeatures[1].getBounds()));
        //   + 2,3
        //  / rd4
        // + 1,2
        newRoad = SimpleFeatureBuilder.build(
                roadType,
                new Object[] {Integer.valueOf(4), line(new int[] {1, 2, 2, 3}), "r4", UUID.randomUUID()},
                "road.rd4");

        riverType = DataUtilities.createType(namespace + ".river", "id:0,geom:MultiLineString,river:String,flow:0.0");
        subRiverType = DataUtilities.createType(namespace + ".river", "river:String,flow:0.0");
        gf = new GeometryFactory();
        riverFeatures = new SimpleFeature[2];

        //       9,7     13,7
        //        +------+
        //  5,5  /
        //  +---+ rv1
        //   7,5 \
        //    9,3 +----+ 11,3
        riverFeatures[0] = SimpleFeatureBuilder.build(
                riverType,
                new Object[] {
                    Integer.valueOf(1),
                    lines(new int[][] {
                        {5, 5, 7, 4},
                        {7, 5, 9, 7, 13, 7},
                        {7, 5, 9, 3, 11, 3}
                    }),
                    "rv1",
                    Double.valueOf(4.5)
                },
                "river.rv1");

        //         + 6,10
        //        /
        //    rv2+ 4,8
        //       |
        //   4,6 +
        riverFeatures[1] = SimpleFeatureBuilder.build(
                riverType,
                new Object[] {Integer.valueOf(2), lines(new int[][] {{4, 6, 4, 8, 6, 10}}), "rv2", Double.valueOf(3.0)},
                "river.rv2");
        riverBounds = new ReferencedEnvelope();
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[0].getBounds()));
        riverBounds.expandToInclude(ReferencedEnvelope.reference(riverFeatures[1].getBounds()));

        rv1Filter = ff.id(Collections.singleton(ff.featureId("river.rv1")));

        //  9,5   11,5
        //   +-----+
        //     rv3  \
        //           + 13,3
        //
        newRiver = SimpleFeatureBuilder.build(
                riverType,
                new Object[] {Integer.valueOf(3), lines(new int[][] {{9, 5, 11, 5, 13, 3}}), "rv3", Double.valueOf(1.5)
                },
                "river.rv3");

        lakeType = DataUtilities.createType(namespace + ".lake", "id:0,geom:Polygon:nillable,name:String");
        lakeFeatures = new SimpleFeature[1];
        //             + 14,8
        //            / \
        //      12,6 +   + 16,6
        //            \  |
        //        14,4 +-+ 16,4
        //
        lakeFeatures[0] = SimpleFeatureBuilder.build(
                lakeType,
                new Object[] {Integer.valueOf(0), polygon(new int[] {12, 6, 14, 8, 16, 6, 16, 4, 14, 4, 12, 6}), "muddy"
                },
                "lake.lk1");
        lakeBounds = new ReferencedEnvelope();
        lakeBounds.expandToInclude(ReferencedEnvelope.reference(lakeFeatures[0].getBounds()));

        invalidGeomType = DataUtilities.createType(namespace + ".invalid", "id:0,geom:Polygon:nillable,name:String");

        invalidGeomFeatures = new SimpleFeature[1];
        //        12,8 14,8
        //          |\/\
        //          |/\_\
        //     12,6 +   + 16,6
        //
        invalidGeomFeatures[0] = SimpleFeatureBuilder.build(
                invalidGeomType,
                new Object[] {Integer.valueOf(0), polygon(new int[] {12, 6, 14, 8, 16, 6, 12, 8, 12, 6}), "notvalid"},
                "invalid.inv1");
        invalidGeomBounds = new ReferencedEnvelope();

        buildingType = DataUtilities.createType(namespace + ".building", "id:0,geom:Polygon:nillable,name:String");
        buildingFeatures = new SimpleFeature[1];
        //             + 14,8
        //            / \
        //      12,6 +   + 16,6
        //           |   |
        //      12,4 +---+ 16,4
        //
        buildingFeatures[0] = SimpleFeatureBuilder.build(
                lakeType,
                new Object[] {
                    Integer.valueOf(0), polygon(new int[] {12, 6, 14, 8, 16, 6, 16, 4, 12, 4, 12, 6}), "church"
                },
                "building.bd1");
        buildingBounds = new ReferencedEnvelope();
        buildingBounds.expandToInclude(ReferencedEnvelope.reference(buildingFeatures[0].getBounds()));
    }

    /**
     * Set all data references to {@code null}, allowing garbage collection. This method is automatically invoked after
     * each test.
     */
    @After
    public void tearDown() throws Exception {
        gf = null;
        roadType = null;
        subRoadType = null;
        roadFeatures = null;
        roadBounds = null;
        rd1Filter = null;
        rd2Filter = null;
        newRoad = null;
        riverType = null;
        subRiverType = null;
        riverFeatures = null;
        riverBounds = null;
        rv1Filter = null;
        newRiver = null;
        buildingType = null;
        buildingFeatures = null;
        buildingBounds = null;
    }

    /**
     * Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates. The coordinates are stored in a flat
     * array.
     */
    public LineString line(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLineString(coords);
    }

    /** Creates a multiline from the specified (<var>x</var>,<var>y</var>) coordinates. */
    public MultiLineString lines(int[][] xy) {
        LineString[] lines = new LineString[xy.length];

        for (int i = 0; i < xy.length; i++) {
            lines[i] = line(xy[i]);
        }

        return gf.createMultiLineString(lines);
    }

    /**
     * Creates a polygon from the specified (<var>x</var>,<var>y</var>) coordinates. The coordinates are stored in a
     * flat array.
     */
    public Polygon polygon(int[] xy) {
        LinearRing shell = ring(xy);
        return gf.createPolygon(shell, null);
    }

    /** Creates a line from the specified (<var>x</var>,<var>y</var>) coordinates and an arbitrary amount of holes. */
    public Polygon polygon(int[] xy, int[][] holes) {
        if (holes == null || holes.length == 0) {
            return polygon(xy);
        }
        LinearRing shell = ring(xy);

        LinearRing[] rings = new LinearRing[holes.length];

        for (int i = 0; i < xy.length; i++) {
            rings[i] = ring(holes[i]);
        }
        return gf.createPolygon(shell, rings);
    }

    /**
     * Creates a ring from the specified (<var>x</var>,<var>y</var>) coordinates. The coordinates are stored in a flat
     * array.
     */
    public LinearRing ring(int[] xy) {
        Coordinate[] coords = new Coordinate[xy.length / 2];

        for (int i = 0; i < xy.length; i += 2) {
            coords[i / 2] = new Coordinate(xy[i], xy[i + 1]);
        }

        return gf.createLinearRing(coords);
    }

    /** Compares two geometries for equality. */
    protected void assertGeometryEquals(Geometry expected, Geometry actual) {
        if (expected == actual) {
            return;
        }
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.equalsExact(actual));
    }

    /** Compares two geometries for equality. */
    protected void assertGeometryEquals(String message, Geometry expected, Geometry actual) {
        if (expected == actual) {
            return;
        }
        assertNotNull(message, expected);
        assertNotNull(message, actual);
        assertTrue(message, expected.equalsExact(actual));
    }

    /**
     * Counts the number of Features returned by the specified reader.
     *
     * <p>This method will close the reader.
     */
    protected int count(FeatureReader<SimpleFeatureType, SimpleFeature> reader) throws IOException {
        if (reader == null) {
            return -1;
        }
        int count = 0;
        try (reader) {
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
        } catch (NoSuchElementException e) {
            // bad dog!
            throw new DataSourceException("hasNext() lied to me at:" + count, e);
        } catch (Exception e) {
            throw new DataSourceException("next() could not understand feature at:" + count, e);
        }
        return count;
    }

    /** Counts the number of Features in the specified writer. This method will close the writer. */
    protected int count(FeatureWriter<SimpleFeatureType, SimpleFeature> writer)
            throws NoSuchElementException, IOException {
        int count = 0;

        try (writer) {
            while (writer.hasNext()) {
                writer.next();
                count++;
            }
        }

        return count;
    }
}
