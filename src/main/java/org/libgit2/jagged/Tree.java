package org.libgit2.jagged;

import java.util.Iterator;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A tree represents a folder in a git repository, containing files and
 * subtrees.
 */
public class Tree
    extends GitObject
{
    private final Lazy<Long> entryCount = new Lazy<Long>()
    {
        @Override
        protected Long call()
        {
            return NativeMethods.treeGetEntryCount(Tree.this.getRepository(), Tree.this);
        }
    };

    Tree(Repository repo, ObjectId id)
    {
        super(repo, id);
    }

    /**
     * Returns the number of entries in this tree.
     * 
     * @return The number of entries in this tree.
     */
    public long getEntryCount()
    {
        return entryCount.getValue().longValue();
    }

    /**
     * Retrieves the entry in this tree by name.
     * 
     * @param name
     *        The name of the entry
     * @return The entry in question or {@code null} if it was not found.
     */
    public TreeEntry getEntry(String name)
    {
        return NativeMethods.treeGetEntryByName(getRepository(), this, name);
    }

    /**
     * Returns the entries in this tree.
     * 
     * @return An {@code Iterable} for the {@link TreeEntry} objects.
     */
    public Iterable<TreeEntry> getEntries()
    {
        return new Iterable<TreeEntry>()
        {
            public Iterator<TreeEntry> iterator()
            {
                return new TreeEntryIterator();
            }
        };
    }

    private class TreeEntryIterator
        implements Iterator<TreeEntry>
    {
        long index = 0;

        public boolean hasNext()
        {
            return (index < Tree.this.getEntryCount());
        }

        public TreeEntry next()
        {
            return NativeMethods.treeGetEntryByIndex(Tree.this.getRepository(), Tree.this, index++);
        }

        public void remove()
        {
            throw new IllegalStateException("Cannot remove a TreeEntry");
        }
    }
}
