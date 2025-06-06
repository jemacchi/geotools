/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.simple.SimpleFeatureCollection;

public interface SimpleFeatureStore extends FeatureStore<SimpleFeatureType, SimpleFeature>, SimpleFeatureSource {

    public void modifyFeatures(String name, Object attributeValue, Filter filter) throws IOException;

    public void modifyFeatures(String[] names, Object[] attributeValues, Filter filter) throws IOException;

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException;

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException;

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException;
}
