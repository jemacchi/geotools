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

import static org.geotools.api.referencing.ReferenceSystem.DOMAIN_OF_VALIDITY_KEY;
import static org.geotools.api.referencing.ReferenceSystem.SCOPE_KEY;
import static org.junit.Assert.assertSame;

import org.geotools.api.referencing.datum.Datum;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.junit.Test;

/**
 * Tests the value of key constants.
 *
 * @author Martin Desruisseaux (Geomatys)
 */
public final class KeyConstantsTest {
    /**
     * Ensures that the key that are expected to be the same are really the same. We use {@code assertSame} instead than
     * {@code assertEquals} because we expect the JVM to have {@linkplain String#intern internalized} the strings.
     */
    @Test
    public void testSame() {
        assertSame(SCOPE_KEY, Datum.SCOPE_KEY);
        assertSame(SCOPE_KEY, CoordinateOperation.SCOPE_KEY);
        assertSame(DOMAIN_OF_VALIDITY_KEY, Datum.DOMAIN_OF_VALIDITY_KEY);
        assertSame(DOMAIN_OF_VALIDITY_KEY, CoordinateOperation.DOMAIN_OF_VALIDITY_KEY);
    }
}
