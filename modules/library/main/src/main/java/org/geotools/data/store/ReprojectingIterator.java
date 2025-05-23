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
package org.geotools.data.store;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.OperationNotFoundException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.FactoryRegistryException;
import org.locationtech.jts.geom.Geometry;

public class ReprojectingIterator implements Iterator<SimpleFeature> {

    /** decorated iterator */
    Iterator<SimpleFeature> delegate;

    /** The target coordinate reference system */
    CoordinateReferenceSystem target;

    /** schema of reprojected features */
    SimpleFeatureType schema;

    /** Transformer */
    GeometryCoordinateSequenceTransformer tx;

    public ReprojectingIterator(
            Iterator<SimpleFeature> delegate,
            MathTransform transform,
            SimpleFeatureType schema,
            GeometryCoordinateSequenceTransformer transformer)
            throws OperationNotFoundException, FactoryRegistryException, FactoryException {
        this.delegate = delegate;
        this.schema = schema;

        tx = transformer;
        tx.setMathTransform(transform);
    }

    public ReprojectingIterator(
            Iterator<SimpleFeature> delegate,
            CoordinateReferenceSystem source,
            CoordinateReferenceSystem target,
            SimpleFeatureType schema,
            GeometryCoordinateSequenceTransformer transformer)
            throws OperationNotFoundException, FactoryRegistryException, FactoryException {
        this.delegate = delegate;
        this.target = target;
        this.schema = schema;
        tx = transformer;

        MathTransform transform = ReferencingFactoryFinder.getCoordinateOperationFactory(null)
                .createOperation(source, target)
                .getMathTransform();
        tx.setMathTransform(transform);
    }

    public Iterator<SimpleFeature> getDelegate() {
        return delegate;
    }

    @Override
    public void remove() {
        delegate.remove();
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public SimpleFeature next() {
        SimpleFeature feature = delegate.next();
        try {
            return reproject(feature);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    SimpleFeature reproject(SimpleFeature feature) throws IOException {

        List<Object> attributes = feature.getAttributes();

        for (int i = 0; i < attributes.size(); i++) {
            Object object = attributes.get(i);
            if (object instanceof Geometry) {
                // do the transformation
                Geometry geometry = (Geometry) object;
                try {
                    attributes.set(i, tx.transform(geometry));
                } catch (TransformException e) {
                    String msg = "Error occured transforming " + geometry.toString();
                    throw (IOException) new IOException(msg).initCause(e);
                }
            }
        }

        try {
            return SimpleFeatureBuilder.build(schema, attributes, feature.getID());
        } catch (IllegalAttributeException e) {
            String msg = "Error creating reprojeced feature";
            throw (IOException) new IOException(msg).initCause(e);
        }
    }
}
