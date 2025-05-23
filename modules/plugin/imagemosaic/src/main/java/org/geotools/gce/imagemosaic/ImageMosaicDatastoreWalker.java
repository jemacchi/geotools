/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.gce.imagemosaic.Utils.Prop;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.index.IndexerUtils;

/**
 * This class is responsible for walking through the target schema and check all the located granules.
 *
 * <p>Its role is basically to simplify the construction of the mosaic by implementing a visitor pattern for the
 * elements that we have to use for the index.
 *
 * @author Carlo Cancellieri - GeoSolutions SAS @TODO check the schema structure
 */
class ImageMosaicDatastoreWalker extends ImageMosaicWalker implements Runnable {

    /** The datastore walker will provide SimpleFeatures to the consumer */
    protected ImageMosaicElementConsumer<SimpleFeature> consumer;

    /** Default Logger * */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicDatastoreWalker.class);

    public ImageMosaicDatastoreWalker(
            ImageMosaicConfigHandler configHandler,
            ImageMosaicEventHandlers eventHandler,
            ImageMosaicElementConsumer<SimpleFeature> consumer) {
        super(configHandler, eventHandler);
        this.consumer = consumer;
    }

    /** run the walker on the store */
    @Override
    public void run() {
        GranuleCatalog catalog = null;
        try {

            configHandler.indexingPreamble();
            startTransaction();

            // start looking into catalog
            catalog = configHandler.getCatalog();
            String locationAttrName = configHandler.getRunConfiguration().getParameter(Prop.LOCATION_ATTRIBUTE);
            String requestedTypeName = configHandler.getRunConfiguration().getParameter(Prop.TYPENAME);

            if (requestedTypeName != null) {
                SimpleFeatureType type = catalog.getType(requestedTypeName);
                if (!Utils.isValidMosaicSchema(type, locationAttrName)) {
                    LOGGER.log(Level.FINE, "Skipping invalid mosaic index table " + requestedTypeName);
                } else {
                    processGranules(catalog, requestedTypeName);
                }
            } else {
                String[] typeNames = catalog.getTypeNames();
                if (typeNames != null) {
                    for (String typeName : typeNames) {
                        if (!Utils.isValidMosaicSchema(catalog.getType(typeName), locationAttrName)) {
                            LOGGER.log(Level.FINE, "Skipping invalid mosaic index table " + typeName);
                            continue;
                        }
                        processGranules(catalog, typeName);
                    }
                }
            }

            // close transaction
            // did we cancel?
            if (getStop()) {
                rollbackTransaction();
            } else {
                commitTransaction();
            }

        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failure occurred while collecting the granules", e);
            try {
                rollbackTransaction();
            } catch (IOException e1) {
                throw new IllegalStateException(e1);
            }
        } finally {
            // close transaction
            try {
                closeTransaction();
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            // close indexing
            try {
                configHandler.indexingPostamble(!getStop());
            } catch (Exception e) {
                final String message = "Unable to close indexing" + e.getLocalizedMessage();
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
                // notify listeners
                eventHandler.fireException(e);
            }

            try {
                if (catalog != null) {
                    catalog.dispose();
                }
            } catch (RuntimeException e) {
                String message = "Failed to dispose harvesting catalog";
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING, message, e);
                }
            }
        }
    }

    private void processGranules(GranuleCatalog catalog, String requestedTypeName) throws IOException {
        // how many rows for this feature type?
        final Query query = new Query(requestedTypeName);
        Integer maxInitializationTiles = IndexerUtils.getParameterAsInteger(
                Prop.MAX_INIT_TILES, configHandler.getRunConfiguration().getIndexer());
        if (maxInitializationTiles != null) query.setMaxFeatures(maxInitializationTiles);

        // cool, now let's walk over the features
        final SimpleFeatureCollection coll = catalog.getGranules(query);

        // create an iterator
        int numFiles = 0;
        try (SimpleFeatureIterator it = coll.features()) {
            // TODO setup index name
            while (it.hasNext()) {
                // get next element
                final SimpleFeature feature = it.next();
                consumer.handleElement(feature, this);
                numFiles++;
                if (getStop()) break;
            }
        }
        setNumElements(numFiles);
    }
}
