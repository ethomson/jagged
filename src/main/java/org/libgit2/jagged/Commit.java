package org.libgit2.jagged;

import java.util.Arrays;
import java.util.Collection;

import org.libgit2.jagged.core.CommitMetadata;
import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

/**
 * Representation of a git commit object.
 */
public class Commit
    extends GitObject
{
    private final Lazy<CommitMetadata> metadata = new Lazy<CommitMetadata>()
    {
        @Override
        protected CommitMetadata call()
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

    /**
     * The committer of a git commit, the person who injected this commit into
     * the repository.
     * 
     * @return The committer
     */
    public Signature getCommitter()
    {
        return metadata.getValue().getCommitter();
    }

    /**
     * The author of a git commit, the person responsible for crafting the
     * commit itself.
     * 
     * @return The author
     */
    public Signature getAuthor()
    {
        return metadata.getValue().getAuthor();
    }

    /**
     * The parents of this git commit.
     * 
     * @return The parent {@link Commit}s of this commit.
     */
    public Iterable<Commit> getParents()
    {
        return parents.getValue();
    }

    /**
     * The tree for this git commit. This points to the root of the repository
     * at this commit.
     * 
     * @return The {@link Tree} for this commit.
     */
    public Tree getTree()
    {
        return tree.getValue();
    }
}
