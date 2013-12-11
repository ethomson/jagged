package org.libgit2.jagged;

public abstract class GitObject
{
    private final Repository repository;
    private final ObjectId id;

    GitObject(Repository repository, ObjectId id)
    {
        this.repository = repository;
        this.id = id;
    }

    public final Repository getRepository()
    {
        return repository;
    }

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
