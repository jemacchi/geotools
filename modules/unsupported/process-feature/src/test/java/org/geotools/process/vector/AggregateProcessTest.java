/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2016, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.process.vector.AggregateProcess.AggregationFunction;
import org.geotools.process.vector.AggregateProcess.Results;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AggregateProcessTest {
    DataStore bugs;

    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null);
        bugs = new PropertyDataStore(file);
    }

    @After
    public void tearDown() {
        bugs.dispose();
    }

    @Test
    public void testSum() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("bugsites");

        Set<AggregationFunction> functions = EnumSet.of(AggregationFunction.Sum);

        Results result = AggregateProcess.process(source.getFeatures(), "cat", functions, true, null);
        assertTrue(result.sum > 0);
    }

    @Test
    public void testSumArea() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("zones");

        Set<AggregationFunction> functions = EnumSet.of(AggregationFunction.SumArea);

        Results result = AggregateProcess.process(source.getFeatures(), "the_geom", functions, true, null);
        assertTrue(result.area > 0);
    }

    @Test
    public void testSumWithGroupBy() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("bugsites");
        Set<AggregationFunction> functions = EnumSet.of(AggregationFunction.Sum);
        Results result = AggregateProcess.process(
                source.getFeatures(), "cat", functions, Collections.singletonList("str1"), true, null);
        // we expect a group by result
        assertNotNull(result.getGroupByResult());
        // the group by result should not be empty
        assertFalse(result.getGroupByResult().isEmpty());
        // the size of each line result should be 2 (one group by attribute and one aggregation
        // function)
        assertEquals(2, result.getGroupByResult().get(0).length);
    }

    @Test
    public void testSumAreaWithGroupBy() throws Exception {
        SimpleFeatureSource source = bugs.getFeatureSource("groupedzones");
        Set<AggregationFunction> functions = EnumSet.of(AggregationFunction.SumArea);
        Results result = AggregateProcess.process(
                source.getFeatures(), "the_geom", functions, Collections.singletonList("str1"), true, null);
        // we expect a group by result
        assertNotNull(result.getGroupByResult());
        // the group by result should not be empty
        assertEquals(3, result.getGroupByResult().size());
        // the size of each line result should be 2 (one group by attribute and one aggregation
        // function)
        assertEquals(2, result.getGroupByResult().get(0).length);
    }
}
