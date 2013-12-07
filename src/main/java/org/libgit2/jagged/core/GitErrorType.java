package org.libgit2.jagged.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * The error type, as defined by the libgit2 return code.
 */
enum GitErrorType
{
    /**
     * No error.
     */
    GITERR_NONE(0),

    /**
     * libgit2 has run out of memory.
     */
    GITERR_NOMEMORY(1),

    /**
     * An error occurred with an operating system call.
     */
    GITERR_OS(2),

    GITERR_INVALID(3),
    GITERR_REFERENCE(4),
    GITERR_ZLIB(5),
    GITERR_REPOSITORY(6),
    GITERR_CONFIG(7),
    GITERR_REGEX(8),
    GITERR_ODB(9),
    GITERR_INDEX(10),
    GITERR_OBJECT(11),
    GITERR_NET(12),
    GITERR_TAG(13),
    GITERR_TREE(14),
    GITERR_INDEXER(15),
    GITERR_SSL(16),
    GITERR_SUBMODULE(17),
    GITERR_THREAD(18),
    GITERR_STASH(19),
    GITERR_CHECKOUT(20),
    GITERR_FETCHHEAD(21),
    GITERR_MERGE(22),
    GITERR_SSH(23),
    GITERR_FILTER(24),
    GITERR_REVERT(25),

    /**
     * An unknown error (an error occurred marshalling the error data itself).
     */
    GITERR_UNKNOWN(Integer.MAX_VALUE);

    private final int value;

    private static final Map<Integer, GitErrorType> errorTypes = new HashMap<Integer, GitErrorType>();

    static
    {
        for (GitErrorType errorClass : EnumSet.allOf(GitErrorType.class))
        {
            errorTypes.put(errorClass.getValue(), errorClass);
        }
    }

    private GitErrorType(int value)
    {
        this.value = value;
    }

    private int getValue()
    {
        return value;
    }

    static GitErrorType getErrorType(int value)
    {
        GitErrorType errorType = errorTypes.get(value);
        return errorType != null ? errorType : GITERR_UNKNOWN;
    }
};
