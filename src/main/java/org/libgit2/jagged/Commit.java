package org.libgit2.jagged;

public class Commit
    extends GitObject
{
    private Commit(Repository repo, ObjectId id)
    {
        super(repo, id);
    }
}
