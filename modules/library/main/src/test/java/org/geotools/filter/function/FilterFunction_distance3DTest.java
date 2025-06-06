package org.geotools.filter.function;

import org.geotools.api.feature.Feature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.expression.Function;
import org.geotools.api.filter.expression.Literal;
import org.geotools.data.DataUtilities;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterFactoryImpl;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

/**
 * Unit tests for FilterFunction_distance3D
 *
 * @author Martin Davis
 */
public class FilterFunction_distance3DTest {

    @Test
    public void testDistance3D() {
        FilterFactoryImpl ff = new FilterFactoryImpl();
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());

        SimpleFeatureType type = null;
        try {
            type = DataUtilities.createType("testSchema", "name:String,*geom:Geometry");
        } catch (SchemaException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }

        Feature f = SimpleFeatureBuilder.build(
                type, new Object[] {"testFeature1", gf.createPoint(new Coordinate(10, 20, 30))}, null);
        Literal literal_geom = ff.literal(gf.createPoint(new Coordinate(10, 30, 40)));

        Function exp = ff.function("distance3D", ff.property("geom"), literal_geom);
        Object value = exp.evaluate(f);
        Assert.assertTrue(value instanceof Double);
        Assert.assertEquals(14.142135623730951, (Double) value, 0.00001);
    }
}
