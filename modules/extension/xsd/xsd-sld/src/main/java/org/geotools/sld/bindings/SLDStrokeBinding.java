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

import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.StyleFactory;
import org.geotools.filter.Filters;
import org.geotools.sld.CssParameter;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.picocontainer.MutablePicoContainer;

/**
 * Binding object for the element http://www.opengis.net/sld:Stroke.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="Stroke"&gt;
 *      &lt;xsd:annotation&gt;
 *          &lt;xsd:documentation&gt;         A &quot;Stroke&quot; specifies the
 *              appearance of a linear geometry.  It is         defined in
 *              parallel with SVG strokes.  The following CssParameters
 *              may be used: &quot;stroke&quot; (color),
 *              &quot;stroke-opacity&quot;, &quot;stroke-width&quot;,
 *              &quot;stroke-linejoin&quot;, &quot;stroke-linecap&quot;,
 *              &quot;stroke-dasharray&quot;, and
 *              &quot;stroke-dashoffset&quot;.       &lt;/xsd:documentation&gt;
 *      &lt;/xsd:annotation&gt;
 *      &lt;xsd:complexType&gt;
 *          &lt;xsd:sequence&gt;
 *              &lt;xsd:choice minOccurs="0"&gt;
 *                  &lt;xsd:element ref="sld:GraphicFill"/&gt;
 *                  &lt;xsd:element ref="sld:GraphicStroke"/&gt;
 *              &lt;/xsd:choice&gt;
 *              &lt;xsd:element ref="sld:CssParameter" minOccurs="0" maxOccurs="unbounded"/&gt;
 *          &lt;/xsd:sequence&gt;
 *      &lt;/xsd:complexType&gt;
 *  &lt;/xsd:element&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class SLDStrokeBinding extends AbstractComplexBinding {
    StyleFactory styleFactory;
    FilterFactory filterFactory;

    public SLDStrokeBinding(StyleFactory styleFactory, FilterFactory filterFactory) {
        this.styleFactory = styleFactory;
        this.filterFactory = filterFactory;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return SLD.STROKE;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return Stroke.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // The following CssParameters may be used:
        // &quot;stroke&quot; (color),
        // &quot;stroke-opacity&quot;
        // &quot;stroke-width&quot;,
        // &quot;stroke-linejoin&quot;
        // &quot;stroke-linecap&quot;,
        // &quot;stroke-dasharray&quot;,
        // &quot;stroke-dashoffset&quot;.
        Expression color = null;

        // The following CssParameters may be used:
        // &quot;stroke&quot; (color),
        // &quot;stroke-opacity&quot;
        // &quot;stroke-width&quot;,
        // &quot;stroke-linejoin&quot;
        // &quot;stroke-linecap&quot;,
        // &quot;stroke-dasharray&quot;,
        // &quot;stroke-dashoffset&quot;.
        Expression opacity = null;

        // The following CssParameters may be used:
        // &quot;stroke&quot; (color),
        // &quot;stroke-opacity&quot;
        // &quot;stroke-width&quot;,
        // &quot;stroke-linejoin&quot;
        // &quot;stroke-linecap&quot;,
        // &quot;stroke-dasharray&quot;,
        // &quot;stroke-dashoffset&quot;.
        Expression width = null;

        // The following CssParameters may be used:
        // &quot;stroke&quot; (color),
        // &quot;stroke-opacity&quot;
        // &quot;stroke-width&quot;,
        // &quot;stroke-linejoin&quot;
        // &quot;stroke-linecap&quot;,
        // &quot;stroke-dasharray&quot;,
        // &quot;stroke-dashoffset&quot;.
        Expression lineJoin = null;
        Expression lineCap = null;
        Expression dashArray = null;
        Expression dashOffset = null;

        for (CssParameter css : node.getChildValues(CssParameter.class)) {
            Expression exp = css.getExpression();
            if (exp == null) {
                continue;
            }

            if ("stroke".equals(css.getName())) {
                color = exp;
            } else if ("stroke-opacity".equals(css.getName())) {
                opacity = exp;
            } else if ("stroke-width".equals(css.getName())) {
                width = exp;
            } else if ("stroke-linejoin".equals(css.getName())) {
                lineJoin = exp;
            } else if ("stroke-linecap".equals(css.getName())) {
                lineCap = exp;
            } else if ("stroke-dasharray".equals(css.getName())) {
                dashArray = exp;
            } else if ("stroke-dashoffset".equals(css.getName())) {
                dashOffset = exp;
            }
        }

        float[] dash = null;

        if (dashArray != null) {
            String[] string = Filters.asString(dashArray).split(" +");
            dash = new float[string.length];

            for (int i = 0; i < string.length; i++) {
                dash[i] = Float.parseFloat(string[i]);
            }
        }

        // &lt;xsd:choice minOccurs="0"&gt;
        //   &lt;xsd:element ref="sld:GraphicFill"/&gt;
        //   &lt;xsd:element ref="sld:GraphicStroke"/&gt;
        // &lt;/xsd:choice&gt;
        Graphic graphicFill = (Graphic) node.getChildValue("GraphicFill");
        Graphic graphicStroke = (Graphic) node.getChildValue("GraphicStroke");

        return styleFactory.createStroke(
                color, width, opacity, lineJoin, lineCap, dash, dashOffset, graphicFill, graphicStroke);
    }
}
