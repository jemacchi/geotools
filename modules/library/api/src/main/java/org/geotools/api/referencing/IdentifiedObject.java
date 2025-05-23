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

import java.util.Collection;
import java.util.Set;
import org.geotools.api.util.GenericName;
import org.geotools.api.util.InternationalString;

/**
 * Supplementary identification and remarks information for a CRS or CRS-related object. When
 * {@link org.geotools.api.referencing.crs.CRSAuthorityFactory} is used to create an object, the
 * {@linkplain ReferenceIdentifier#getAuthority authority} and {@linkplain ReferenceIdentifier#getCode authority code}
 * values should be set to the authority name of the factory object, and the authority code supplied by the client,
 * respectively. The other values may or may not be set. If the authority is EPSG, the implementer may consider using
 * the corresponding metadata values in the EPSG tables.
 *
 * @departure ISO 19111 defines also an {@code IdentifiedObjectBase} interface. The later is omitted in GeoAPI because
 *     the split between {@code IdentifiedObject} and {@code IdentifiedObjectBase} in OGC/ISO specification was mostly a
 *     workaround for introducing {@code IdentifiedObject} in ISO 19111 without changing the {@code ReferenceSystem}
 *     definition in ISO 19115.
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface IdentifiedObject {
    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by {@link #getName}.
     *
     * @see #getName
     */
    String NAME_KEY = "name";

    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by {@link #getAlias}.
     *
     * @see #getAlias
     */
    String ALIAS_KEY = "alias";

    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by {@link #getIdentifiers}.
     *
     * @see #getIdentifiers
     */
    String IDENTIFIERS_KEY = "identifiers";

    /**
     * Key for the <code>{@value}</code> property to be given to the {@linkplain ObjectFactory object factory} <code>
     * createFoo(&hellip;)</code> methods. This is used for setting the value to be returned by {@link #getRemarks}.
     *
     * @see #getRemarks
     */
    String REMARKS_KEY = "remarks";

    /**
     * The primary name by which this object is identified.
     *
     * @return The primary name.
     */
    ReferenceIdentifier getName();

    /**
     * An alternative name by which this object is identified.
     *
     * @return The aliases, or an empty collection if there is none.
     */
    Collection<GenericName> getAlias();

    /**
     * An identifier which references elsewhere the object's defining information. Alternatively an identifier by which
     * this object can be referenced.
     *
     * @return This object identifiers, or an empty set if there is none.
     */
    Set<ReferenceIdentifier> getIdentifiers();

    /**
     * Comments on or information about this object, including data source information.
     *
     * @return The remarks, or {@code null} if none.
     */
    InternationalString getRemarks();

    /**
     * Returns a <A HREF="doc-files/WKT.html"><cite>Well Known Text</cite> (WKT)</A> for this object. This operation may
     * fails if an object is too complex for the WKT format capability (for example an
     * {@linkplain org.geotools.api.referencing.crs.EngineeringCRS engineering CRS} with different unit for each axis).
     *
     * @return The Well Know Text for this object.
     * @throws UnsupportedOperationException If this object can't be formatted as WKT.
     */
    String toWKT() throws UnsupportedOperationException;
}
