package org.libgit2.jagged.core;

import org.libgit2.jagged.Signature;

public class CommitMetadata
{
    private final Signature committer;
    private final Signature author;

    private CommitMetadata(Signature committer, Signature author)
    {
        this.committer = committer;
        this.author = author;
    }

    public Signature getCommitter()
    {
        return committer;
    }

    public Signature getAuthor()
    {
        return author;
    }
}
