package org.libgit2.jagged.core;

/**
 * An exception produced by libgit2. Any error message produced by the library
 * will be included.
 */
public class GitException
    extends RuntimeException
{
    private static final long serialVersionUID = 2074647122137699289L;

    public GitException(final String message)
    {
        super(message);
    }
}
