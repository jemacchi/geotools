/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import static org.junit.Assert.assertEquals;

import org.geotools.api.data.Query;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsLike;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;

public class GeoPkgFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    public GeoPkgFeatureSourceOnlineTest() {
        super();
        this.forceLongitudeFirst = true;
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    @Override
    @Test
    public void testGetFeaturesWithArithmeticOpFilter() throws Exception {
        // seems there are rounding issues here - consider new test
    }

    @Override
    @Test
    public void testConversionFilter() throws Exception {
        // seems there are rounding issues here - consider new test
    }
    /**
     * SQLite's LIKE is usually case insensitive - there are many possible "fixes" out there but all are hard to
     * implement or seem not to work for all CharacterSets.
     */
    @Override
    public void testLikeFilter() throws Exception {
        FilterFactory ff = dataStore.getFilterFactory();
        //        PropertyIsLike caseSensitiveLike =
        //                ff.like(ff.property(aname("stringProperty")), "Z*", "*", "?", "\\", true);
        PropertyIsLike caseInsensitiveLike = ff.like(ff.property(aname("stringProperty")), "Z*", "*", "?", "\\", false);
        PropertyIsLike caseInsensitiveLike2 =
                ff.like(ff.property(aname("stringProperty")), "z*", "*", "?", "\\", false);
        // SQLLITE LIKE is always case insensitive
        // assertEquals(0, featureSource.getCount(new Query(null, caseSensitiveLike)));
        assertEquals(1, featureSource.getCount(new Query(null, caseInsensitiveLike)));
        assertEquals(1, featureSource.getCount(new Query(null, caseInsensitiveLike2)));
    }
}
