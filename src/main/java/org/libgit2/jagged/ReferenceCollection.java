package org.libgit2.jagged;

import org.libgit2.jagged.core.NativeMethods;

public class ReferenceCollection
{
    private final Repository repository;

    ReferenceCollection(Repository repository)
    {
        this.repository = repository;
    }

    public Reference get(String name)
    {
        return NativeMethods.referenceLookup(repository, name);
    }

    public Reference getHead()
    {
        return get("HEAD");
    }
}