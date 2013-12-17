package org.libgit2.jagged.core;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * A reference type, as defined by libgit2.
 */
public enum ReferenceType
{
    /**
     * An invalid reference; should be unused.
     */
    INVALID(0),

    /**
     * A direct reference; a reference to an OID.
     */
    OID(1),

    /**
     * A symbolic reference; a reference to another reference.
     */
    SYMBOLIC(2);

    private final int value;

    private static final Map<Integer, ReferenceType> referenceTypes = new HashMap<Integer, ReferenceType>();

    static
    {
        for (ReferenceType referenceType : EnumSet.allOf(ReferenceType.class))
        {
            referenceTypes.put(referenceType.getValue(), referenceType);
        }
    }

    private ReferenceType(int value)
    {
        this.value = value;
    }

    private int getValue()
    {
        return value;
    }

    /**
     * Convert an integer reference type (from libgit2) into a
     * {@link ReferenceType}.
     * 
     * @param type
     *        the reference type (from libgit2)
     * @return a {@link ReferenceType} or {@link ReferenceType#INVALID} if the
     *         reference type was invalid
     */
    static ReferenceType valueOf(int value)
    {
        ReferenceType referenceType = referenceTypes.get(value);

        if (referenceType == null)
        {
            throw new IllegalArgumentException(MessageFormat.format("Unknown reference type: {0}", value));
        }

        return referenceType;
    }
}