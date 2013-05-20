package org.libgit2.jagged;

import java.text.MessageFormat;

import org.libgit2.jagged.core.GitException;
import org.libgit2.jagged.core.HashCode;

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

    public String getCanonicalName()
    {
        return reference.getCanonicalName();
    }

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
