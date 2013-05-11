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
