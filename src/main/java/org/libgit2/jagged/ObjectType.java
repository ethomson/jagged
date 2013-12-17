package org.libgit2.jagged;

import java.text.MessageFormat;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ObjectType
{
    /**
     * An object of any type. Used only for querying by type; an object will
     * never have this type set.
     */
    ANY(-2),

    /** An invalid object */
    INVALID(-1),

    /** A commit */
    COMMIT(1),

    /** A tree */
    TREE(2),

    /** A blob */
    BLOB(3),

    /** A tag */
    TAG(4),

    /**
     * In a pack file, this object is defined as a delta from another object
     * identified by its offset into the pack file.
     */
    OFFSET_DELTA(6),

    /**
     * In a pack file, this object is defined as a delta from another object
     * identified by its ID.
     */
    REFERENCE_DELTA(7);

    private final int value;

    private static final Map<Integer, ObjectType> types = new HashMap<Integer, ObjectType>();

    static
    {
        for (ObjectType type : EnumSet.allOf(ObjectType.class))
        {
            types.put(type.getValue(), type);
        }
    }

    private ObjectType(int value)
    {
        this.value = value;
    }

    int getValue()
    {
        return value;
    }

    static ObjectType valueOf(int value)
    {
        ObjectType type = types.get(value);

        if (type == null)
        {
            throw new IllegalArgumentException(MessageFormat.format("Unknown type: {0}", value));
        }

        return type;
    }
}
