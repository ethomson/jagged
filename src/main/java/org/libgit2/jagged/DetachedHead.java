package org.libgit2.jagged;

/**
 * A pointer to the commit that is the current {@code HEAD} commit, which does
 * not correspond to an actual branch.
 */
public class DetachedHead
    extends Branch
{
    DetachedHead(final Repository repository, final Reference reference)
    {
        super(repository, reference);
    }
}
