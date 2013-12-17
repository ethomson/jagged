package org.libgit2.jagged;

import java.util.Iterator;

import org.libgit2.jagged.core.NativeMethods;

/**
 * The references in a repository.
 */
public class ReferenceCollection
    implements Iterable<Reference>
{
    private final Repository repository;

    ReferenceCollection(Repository repository)
    {
        this.repository = repository;
    }

    /**
     * Gets the given reference name by name. This is the canonical name, for
     * example {@code refs/heads/master} for the master branch.
     * 
     * @param name
     *        The canonical reference name to lookup
     * @return The corresponding {@link Reference}
     */
    public Reference get(String name)
    {
        return NativeMethods.referenceLookup(repository, name);
    }

    /**
     * Returns the head reference for the {@link Repository}, the one pointed to
     * by {@code HEAD}.
     * 
     * @return The head reference for the repository
     */
    public Reference getHead()
    {
        return get("HEAD");
    }

    /**
     * {@inheritDoc}
     */
    public Iterator<Reference> iterator()
    {
        final String[] referenceNames = NativeMethods.referenceList(repository);
        return new ReferenceIterator(referenceNames);
    }

    private class ReferenceIterator
        implements Iterator<Reference>
    {
        private final String[] referenceNames;
        int position = 0;

        private ReferenceIterator(final String[] referenceNames)
        {
            this.referenceNames = referenceNames;
        }

        public boolean hasNext()
        {
            return (position < referenceNames.length);
        }

        public Reference next()
        {
            return get(referenceNames[position++]);
        }

        public void remove()
        {
            /* TODO */
            throw new UnsupportedOperationException("Remove is not yet implemented");
        }
    }
}