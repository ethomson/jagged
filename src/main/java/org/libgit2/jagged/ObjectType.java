package org.libgit2.jagged;

public enum ObjectType
{
    /* An object of any type */
    ANY(-2),

    /* An invalid object */
    INVALID(-1),

    /* A commit */
    COMMIT(1),

    /* A tree */
    TREE(2),

    /* A blob */
    BLOB(3),

    /* A tag */
    TAG(4),

    /*
     * In a pack file, this object is defined as a delta from another object
     * identified by its offset into the pack file.
     */
    OFFSET_DELTA(6),

    /*
     * In a pack file, this object is defined as a delta from another object
     * identified by its ID.
     */
    REFERENCE_DELTA(7);

    private final int value;

    private ObjectType(int value)
    {
        this.value = value;
    }

    int getValue()
    {
        return value;
    }
}
