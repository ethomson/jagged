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

        ObjectId oid = new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443");
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(oid, tree.getId());

        repository.close();
    }

    @Test
    public void testGetEntryCount()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443");
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(5, tree.getEntryCount());

        repository.close();
    }

    @Test
    public void testGetEntries()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443");
        Tree tree = repository.lookup(oid);

        Iterator<TreeEntry> iterator = tree.getEntries().iterator();

        TreeEntry entry = iterator.next();

        Assert.assertEquals(".gitattributes", entry.getName());
        Assert.assertEquals(new ObjectId("176a458f94e0ea5272ce67c36bf30b6be9caf623"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("a", entry.getName());
        Assert.assertEquals(new ObjectId("32ef2016c46507680e32272204b1095cdf232f5d"), entry.getId());
        Assert.assertEquals(Mode.TREE, entry.getMode());
        Assert.assertEquals(ObjectType.TREE, entry.getType());

        entry = iterator.next();

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

        repository.close();
    }

    @Test
    public void testGetEntryByName()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443");
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

        repository.close();
    }

    @Test
    public void testCanRealizeTreeEntry()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443");
        Tree tree = repository.lookup(oid);

        TreeEntry entry = tree.getEntry("one.txt");
        Blob blob = entry.realize();

        Assert.assertEquals(new ObjectId("d1796967d47949153bb852c07304d9e5f2f0040c"), blob.getId());
    }
}
