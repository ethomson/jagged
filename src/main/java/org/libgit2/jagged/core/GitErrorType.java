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
     * libgit2 has run out of memory.
     */
    GITERR_NOMEMORY(0),

    /**
     * An error occurred with an operating system call.
     */
    GITERR_OS(1),

	GITERR_INVALID(2),
	GITERR_REFERENCE(3),
	GITERR_ZLIB(4),
	GITERR_REPOSITORY(5),
	GITERR_CONFIG(6),
	GITERR_REGEX(7),
	GITERR_ODB(8),
	GITERR_INDEX(9),
	GITERR_OBJECT(10),
	GITERR_NET(11),
	GITERR_TAG(12),
	GITERR_TREE(13),
	GITERR_INDEXER(14),
	GITERR_SSL(15),
	GITERR_SUBMODULE(16),
	GITERR_THREAD(17),
	GITERR_STASH(18),
	GITERR_CHECKOUT(19),
	GITERR_FETCHHEAD(20),
	GITERR_MERGE(21),

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
