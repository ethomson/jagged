package org.libgit2.jagged.core;

/**
 * A simple container for errors that occurred in libgit2, containing the return
 * code of the function (as a {@link GitErrorClass}) and the message produced by
 * libgit2, if one was set.
 * 
 * {@link GitError}s should only ever be produced by the JNI layer.
 */
class GitError
{
    private final GitErrorType errorType;
    private final String message;

    private GitError(final GitErrorType errorType, final String message)
    {
        Ensure.argumentNotNull(errorType, "errorType");

        this.errorType = (errorType != null) ? errorType : GitErrorType.GITERR_UNKNOWN;
        this.message = (message != null) ? message : "";
    }

    private GitError(final int errorType, final String message)
    {
        this(GitErrorType.getErrorType(errorType), message);
    }

    /**
     * The return code from libgit2.
     * 
     * @return the error type
     */
    public GitErrorType getErrorType()
    {
        return errorType;
    }

    /**
     * The message from libgit2.
     * 
     * @return the error message (never {@code null})
     */
    public String getMessage()
    {
        return message;
    }
}
