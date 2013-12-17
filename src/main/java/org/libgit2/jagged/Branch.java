package org.libgit2.jagged;

import java.text.MessageFormat;

import org.libgit2.jagged.core.GitException;
import org.libgit2.jagged.core.HashCode;

/**
 * A representation of a git branch, a reference in the {@code refs/heads}
 * namespace.
 */
public class Branch
{
    private final Repository repository;
    private final Reference reference;

    private String name;

    Branch(final Repository repository, final Reference reference)
    {
        this.repository = repository;
        this.reference = reference;
    }

    /**
     * Gets the canonical branch name, including the leading namespace, eg
     * {@code refs/heads/master}.
     * 
     * @return The canonical branch name
     */
    public String getCanonicalName()
    {
        return reference.getCanonicalName();
    }

    /**
     * Gets the branch name, the reference name without the namespace, eg
     * {@code master}.
     * 
     * @return The branch name
     */
    public String getName()
    {
        if (name == null)
        {
            final String canonicalName = getCanonicalName();

            if (canonicalName.startsWith(Reference.PREFIX_BRANCH))
            {
                return canonicalName.substring(Reference.PREFIX_BRANCH.length());
            }

            if (canonicalName.startsWith(Reference.PREFIX_REMOTE_TRACKING))
            {
                return canonicalName.substring(Reference.PREFIX_REMOTE_TRACKING.length());
            }

            throw new GitException(MessageFormat.format("'{0}' is not a valid branch name", canonicalName));
        }

        return name;
    }

    /**
     * Queries whether this branch is the current repository head, that is
     * whether the special {@code HEAD} reference points to this branch or not.
     * 
     * @return {@code true} if this is the repository head, {@code false}
     *         otherwise.
     */
    public boolean isCurrentRepositoryHead()
    {
        return this.equals(repository.getHead());
    }

    @Override
    public int hashCode()
    {
        return HashCode.getHashCode(repository, reference);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        Branch other = (Branch) o;

        return (repository.equals(other.repository) && reference.equals(other.reference));
    }
}
