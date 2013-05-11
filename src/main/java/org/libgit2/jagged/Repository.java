package org.libgit2.jagged;

import org.libgit2.jagged.core.Ensure;
import org.libgit2.jagged.core.NativeHandle;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A Repository is the primary interface to a git repository.
 */
public class Repository
{
    private final NativeHandle handle;

    /**
     * Open an existing on-disk git repository. The returned Repository must be
     * {@link #dispose()}d.
     * 
     * @param path
     *        the path to a repository or a working directory
     */
    public Repository(final String path)
    {
        Ensure.argumentNotNullOrEmpty(path, "path");

        handle = NativeMethods.repositoryOpen(path);
    }

    /**
     * @equivalence init(path, false)
     */
    public static Repository init(final String path)
    {
        return init(path, false);
    }

    /**
     * Create a new on-disk git repository. The returned Repository must be
     * {@link #dispose()}d.
     * 
     * @param path
     *        the path to the new git repository (for bare repositories) or
     *        working directory (for non-bare)
     * @return the new Repository
     */
    public static Repository init(final String path, boolean bare)
    {
        Ensure.argumentNotNullOrEmpty(path, "path");

        return new Repository(NativeMethods.repositoryInit(path, bare));
    }

    /**
     * Clone an existing git repository to a local repository. The returned
     * Repository must be {@link #dispose()}d.
     * 
     * @param sourceUrl
     *        the path to the existing git repository
     * @param path
     *        the path to the new repository working directory
     * @return the new Repository
     */
    public static Repository clone(final String sourceUrl, final String path)
    {
        Ensure.argumentNotNull(sourceUrl, "sourceUrl");
        Ensure.argumentNotNull(path, "path");

        return new Repository(NativeMethods.repositoryClone(sourceUrl, path));
    }

    private Repository(NativeHandle handle)
    {
        this.handle = handle;
    }

    /**
     * Queries whether this repository is a "bare" repository, once that lacks a
     * working directory.
     * 
     * @return {@code true} if the given repository is bare, {@code false}
     *         otherwise
     */
    public boolean isBare()
    {
        return NativeMethods.repositoryIsBare(handle);
    }

    /**
     * Gets the HEAD {@link Reference} of this repository.
     * 
     * @return the reference pointed to by {@code HEAD}
     */
    public Reference getHead()
    {
        final NativeHandle referenceHandle = NativeMethods.repositoryHead(handle);
        return Reference.newFromHandle(referenceHandle);
    }

    /**
     * Disposes the underlying Repository object.
     */
    public void dispose()
    {
        NativeMethods.repositoryFree(handle);
    }
}
