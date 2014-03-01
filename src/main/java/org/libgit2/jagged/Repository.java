package org.libgit2.jagged;

import org.libgit2.jagged.Reference.SymbolicReference;
import org.libgit2.jagged.core.Ensure;
import org.libgit2.jagged.core.GitException;
import org.libgit2.jagged.core.NativeHandle;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A Repository is the primary interface to a git repository.
 */
public class Repository
{
    private boolean bare;

    private final NativeHandle handle = new NativeHandle();

    private final ReferenceCollection references = new ReferenceCollection(this);

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
        bare = NativeMethods.repositoryIsBare(this);

        Ensure.nativeNotNull(handle);
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
     * @param bare
     *        {@code true} if the new repository should be a "bare" repository
     *        (lacking a working directory), {@code false} otherwise
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
     * Enumerate the references for this repository.
     * 
     * @return a collection of references for this repository
     */
    public ReferenceCollection getReferences()
    {
        return references;
    }

    /**
     * Looks up and returns the given object from the repository by id.
     * 
     * @param id
     *        the object id to query (not {@code null})
     * @return the git object
     */
    public <T extends GitObject> T lookup(ObjectId id)
    {
        return lookup(id, ObjectType.ANY);
    }

    /**
     * Looks up and returns the given object from the repository by id.
     * 
     * @param id
     *        the object id to query (not {@code null})
     * @param type
     *        the type of object to query (not {@code null})
     * @return the git object
     */
    public <T extends GitObject> T lookup(ObjectId id, ObjectType type)
    {
        Ensure.argumentNotNull(id, "id");
        Ensure.argumentNotNull(type, "type");

        return NativeMethods.objectLookup(this, id, type.getValue());
    }

    /**
     * Gets the branch of this repository pointed to by {@code HEAD}.
     * 
     * @return the branch pointed to by {@code HEAD}
     */
    public Branch getHead()
    {
        Reference reference = getReferences().getHead();

        if (reference == null)
        {
            throw new GitException("Corrupt repository; the 'HEAD' reference is missing.");
        }

        if (reference instanceof SymbolicReference)
        {
            return new Branch(this, reference);
        }

        return new DetachedHead(this, reference);
    }

    /**
     * Disposes the underlying Repository object.
     */
    public void dispose()
    {
        NativeMethods.repositoryFree(this);
    }
}
