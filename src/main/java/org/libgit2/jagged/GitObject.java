package org.libgit2.jagged;

/**
 * Base class for git objects (commits, trees, blobs, etc.)
 */
public abstract class GitObject
{
    private final Repository repository;
    private final ObjectId id;

    GitObject(Repository repository, ObjectId id)
    {
        this.repository = repository;
        this.id = id;
    }

    /**
     * The repository that this object belongs to.
     * 
     * @return The {@link Repository}
     */
    public final Repository getRepository()
    {
        return repository;
    }

    /**
     * The object ID of this object, a/k/a the "hash" or the "sha1".
     * 
     * @return The {@link ObjectId} of this object.
     */
    public final ObjectId getId()
    {
        return id;
    }

    @Override
    public int hashCode()
    {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || obj.getClass() != getClass())
        {
            return false;
        }

        return id.equals(((GitObject) obj).id);
    }
}
