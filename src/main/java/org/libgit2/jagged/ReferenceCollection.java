package org.libgit2.jagged;

import java.util.Iterator;

import org.libgit2.jagged.core.NativeMethods;

public class ReferenceCollection
    implements Iterable<Reference>
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

    public Iterator<Reference> iterator()
    {
        final String[] referenceNames = NativeMethods.referenceList(repository);
        return new ReferenceIterator(referenceNames);
    }

    public class ReferenceIterator
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