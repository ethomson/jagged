package org.libgit2.jagged;

import java.util.Iterator;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

public class Tree
    extends GitObject
    implements Iterable<TreeEntry>
{
    private final Lazy<Long> entryCount = new Lazy<Long>()
    {
        @Override
        protected Long call()
        {
            return NativeMethods.treeGetEntryCount(Tree.this.getRepository(), Tree.this);
        }
    };

    private Tree(Repository repo, ObjectId id)
    {
        super(repo, id);
    }

    public long getEntryCount()
    {
        return entryCount.getValue().longValue();
    }

    public Iterator<TreeEntry> iterator()
    {
        return new TreeEntryIterator();
    }

    public class TreeEntryIterator
        implements Iterator<TreeEntry>
    {
        long index = 0;

        public boolean hasNext()
        {
            return (index < Tree.this.getEntryCount());
        }

        public TreeEntry next()
        {
            return NativeMethods.treeGetEntry(Tree.this.getRepository(), Tree.this, index++);
        }

        public void remove()
        {
            throw new IllegalStateException("Cannot remove a TreeEntry");
        }
    }
}
