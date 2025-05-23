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
package org.geotools.sld.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.api.style.Mark;
import org.geotools.api.style.ResourceLocator;
import org.geotools.api.style.StyleFactory;
import org.geotools.filter.Filters;
import org.geotools.sld.SLDConfiguration;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.xsd.Configuration;
import org.junit.Test;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Element;

public class SLDMarkBindingTest extends SLDTestSupport {

    @Test
    public void testType() throws Exception {
        assertEquals(Mark.class, new SLDMarkBinding(null, null, null).getType());
    }

    @Test
    public void testWithoutStroke() throws Exception {
        SLDMockData.mark(document, document);

        Mark mark = (Mark) parse();
        assertNotNull(mark);

        assertNotNull(mark.getFill());

        Color c = org.geotools.styling.SLD.color(mark.getFill().getColor());

        assertEquals(Integer.parseInt("12", 16), c.getRed());
        assertEquals(Integer.parseInt("34", 16), c.getGreen());
        assertEquals(Integer.parseInt("56", 16), c.getBlue());

        assertNotNull(mark.getWellKnownName());
        assertEquals(Filters.asString(mark.getWellKnownName()), "wellKnownName");
    }

    @Test
    public void testResourceLocator() throws Exception {
        Element mark1 = SLDMockData.element(SLD.MARK, document, document);
        SLDMockData.fill(document, mark1);
        Element wkn = SLDMockData.element(SLD.WELLKNOWNNAME, document, mark1);
        wkn.appendChild(document.createTextNode("file://foo.svg"));

        Mark mark = (Mark) parse();
        assertNotNull(mark);

        assertNotNull(mark.getFill());

        Color c = org.geotools.styling.SLD.color(mark.getFill().getColor());

        assertEquals(Integer.parseInt("12", 16), c.getRed());
        assertEquals(Integer.parseInt("34", 16), c.getGreen());
        assertEquals(Integer.parseInt("56", 16), c.getBlue());

        assertNotNull(mark.getWellKnownName());
        assertEquals("file://test/foo.svg", Filters.asString(mark.getWellKnownName()));
    }

    @Override
    protected Configuration createConfiguration() {
        return new SLDConfiguration() {
            @Override
            protected void configureContext(MutablePicoContainer container) {
                container.registerComponentImplementation(StyleFactory.class, StyleFactoryImpl.class);

                container.registerComponentInstance(ResourceLocator.class, (ResourceLocator) uri -> {
                    if ("file://foo.svg".equals(uri)) {
                        try {
                            return new URL("file://test/foo.svg");
                        } catch (MalformedURLException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        return null;
                    }
                });
            }
        };
    }
}
