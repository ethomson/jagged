package org.libgit2.jagged;

public class DetachedHead
    extends Branch
{
    public DetachedHead(final Repository repository, final Reference reference)
    {
        super(repository, reference);
    }
}
