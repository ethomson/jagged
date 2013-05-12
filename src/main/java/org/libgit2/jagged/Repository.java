package org.libgit2.jagged;

import org.libgit2.jagged.core.Ensure;
import org.libgit2.jagged.core.NativeHandle;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A Repository is the primary interface to a git repository.
 */
public class Repository
{
	private boolean bare;
	
    private final NativeHandle handle = new NativeHandle();

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
        
        NativeMethods.repositoryOpen(this, path);

        Ensure.nativeNotNull(handle);
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
        
        return NativeMethods.repositoryInit(path, bare);
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
        
        return NativeMethods.repositoryClone(sourceUrl, path);
    }

	@SuppressWarnings("unused")
	private Repository(final long handle, final boolean bare)
    {
		this.handle.set(handle);
		this.bare = bare;
    }

	@SuppressWarnings("unused")
	private void setHandle(long handle)
	{
		this.handle.set(handle);
	}
	
	@SuppressWarnings("unused")
	private long getHandle()
	{
		return handle.get();
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
    	return bare;
    }

    /**
     * Gets the HEAD {@link Reference} of this repository.
     * 
     * @return the reference pointed to by {@code HEAD}
     */
    public Reference getHead()
    {
    	return NativeMethods.repositoryHead(this);
    }

    /**
     * Disposes the underlying Repository object.
     */
    public void dispose()
    {
        NativeMethods.repositoryFree(this);
    }
}
