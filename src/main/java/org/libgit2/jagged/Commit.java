package org.libgit2.jagged;

import java.util.Arrays;
import java.util.Collection;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

public class Commit
    extends GitObject
{
    private final Lazy<Metadata> metadata = new Lazy<Metadata>()
    {
        @Override
        protected Metadata call()
        {
            return NativeMethods.commitGetMetadata(Commit.this.getRepository(), Commit.this);
        }
    };

    private final Lazy<Collection<Commit>> parents = new Lazy<Collection<Commit>>()
    {
        @Override
        protected Collection<Commit> call()
        {
            return Arrays.asList(NativeMethods.commitGetParents(Commit.this.getRepository(), Commit.this));
        }
    };

    private final Lazy<Tree> tree = new Lazy<Tree>()
    {
        @Override
        protected Tree call()
        {
            return NativeMethods.commitGetTree(Commit.this.getRepository(), Commit.this);
        }
    };

    private Commit(Repository repo, ObjectId id)
    {
        super(repo, id);
    }

    public Signature getCommitter()
    {
        return metadata.getValue().getCommitter();
    }

    public Signature getAuthor()
    {
        return metadata.getValue().getAuthor();
    }

    public Collection<Commit> getParents()
    {
        return parents.getValue();
    }

    public Tree getTree()
    {
        return tree.getValue();
    }

    public static class Metadata
    {
        private final Signature committer;
        private final Signature author;

        public Metadata(Signature committer, Signature author)
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
}
