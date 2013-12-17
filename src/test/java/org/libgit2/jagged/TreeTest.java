package org.libgit2.jagged;

import java.io.File;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

public class TreeTest
    extends GitTest
{
    @Test
    public void testLookupTree()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0");
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(oid, tree.getId());

        repository.dispose();
    }

    @Test
    public void testGetEntryCount()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0");
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(3, tree.getEntryCount());

        repository.dispose();
    }

    @Test
    public void testGetEntries()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0");
        Tree tree = repository.lookup(oid);

        Iterator<TreeEntry> iterator = tree.getEntries().iterator();

        TreeEntry entry = iterator.next();

        Assert.assertEquals("one.txt", entry.getName());
        Assert.assertEquals(new ObjectId("d1796967d47949153bb852c07304d9e5f2f0040c"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("three.txt", entry.getName());
        Assert.assertEquals(new ObjectId("8fbe49af0d14c65f881b57709acae2ea3414089a"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("two.txt", entry.getName());
        Assert.assertEquals(new ObjectId("dc48b6c38e967e57965e36c6f7a1c3ec5c3e1ff4"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        Assert.assertFalse(iterator.hasNext());

        repository.dispose();
    }

    @Test
    public void testGetEntryByName()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0");
        Tree tree = repository.lookup(oid);

        TreeEntry entry = tree.getEntry("one.txt");

        Assert.assertEquals("one.txt", entry.getName());
        Assert.assertEquals(new ObjectId("d1796967d47949153bb852c07304d9e5f2f0040c"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = tree.getEntry("two.txt");

        Assert.assertEquals("two.txt", entry.getName());
        Assert.assertEquals(new ObjectId("dc48b6c38e967e57965e36c6f7a1c3ec5c3e1ff4"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = tree.getEntry("three.txt");

        Assert.assertEquals("three.txt", entry.getName());
        Assert.assertEquals(new ObjectId("8fbe49af0d14c65f881b57709acae2ea3414089a"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        repository.dispose();
    }

    @Test
    public void testCanRealizeTreeEntry()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0");
        Tree tree = repository.lookup(oid);

        TreeEntry entry = tree.getEntry("one.txt");
        Blob blob = entry.realize();

        Assert.assertEquals(new ObjectId("d1796967d47949153bb852c07304d9e5f2f0040c"), blob.getId());
    }
}
