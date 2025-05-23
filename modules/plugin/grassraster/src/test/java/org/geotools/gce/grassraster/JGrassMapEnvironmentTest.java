/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.io.File;
import java.net.URL;
import org.geotools.util.URLs;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link JGrassMapEnvironment} class and the created paths.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class JGrassMapEnvironmentTest {

    @Test
    public void test() {
        URL pitUrl = this.getClass().getClassLoader().getResource("testlocation/test/cell/pit");
        File mapFile = URLs.urlToFile(pitUrl);
        File mapsetFile = mapFile.getParentFile().getParentFile();

        JGrassMapEnvironment jME = new JGrassMapEnvironment(mapFile);
        checkEnvironment(jME);
        jME = new JGrassMapEnvironment(mapsetFile, "pit");
        checkEnvironment(jME);
    }

    private void checkEnvironment(JGrassMapEnvironment jME) {
        File cell = jME.getCELL();
        Assert.assertTrue(cell.exists());
        Assert.assertTrue(cell.getAbsolutePath()
                .endsWith("testlocation" + File.separator + "test" + File.separator + "cell" + File.separator + "pit"));

        File cellFolder = jME.getCellFolder();
        Assert.assertTrue(cellFolder.exists() && cellFolder.isDirectory());
        Assert.assertTrue(cellFolder
                .getAbsolutePath()
                .endsWith("testlocation" + File.separator + "test" + File.separator + "cell"));

        File fcell = jME.getFCELL();
        Assert.assertTrue(fcell.exists());
        Assert.assertTrue(fcell.getAbsolutePath()
                .endsWith(
                        "testlocation" + File.separator + "test" + File.separator + "fcell" + File.separator + "pit"));

        File fcellFolder = jME.getFcellFolder();
        Assert.assertTrue(fcellFolder.exists() && fcellFolder.isDirectory());
        Assert.assertTrue(fcellFolder
                .getAbsolutePath()
                .endsWith("testlocation" + File.separator + "test" + File.separator + "fcell"));

        File colr = jME.getCOLR();
        Assert.assertTrue(colr.getAbsolutePath()
                .endsWith("testlocation" + File.separator + "test" + File.separator + "colr" + File.separator + "pit"));

        File colrFolder = jME.getColrFolder();
        Assert.assertTrue(colrFolder
                .getAbsolutePath()
                .endsWith("testlocation" + File.separator + "test" + File.separator + "colr"));

        File wind = jME.getWIND();
        Assert.assertTrue(wind.exists());
        Assert.assertTrue(
                wind.getAbsolutePath().endsWith("testlocation" + File.separator + "test" + File.separator + "WIND"));
    }
}
