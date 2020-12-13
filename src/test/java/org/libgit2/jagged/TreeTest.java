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

        ObjectId oid = new ObjectId(C8_ROOT);
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(oid, tree.getId());

        repository.close();
    }

    @Test
    public void testGetEntryCount()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C8_ROOT);
        Tree tree = repository.lookup(oid);

        Assert.assertEquals(4, tree.getEntryCount());

        repository.close();
    }

    @Test
    public void testGetEntries()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C8_ROOT);
        Tree tree = repository.lookup(oid);

        Iterator<TreeEntry> iterator = tree.getEntries().iterator();

        TreeEntry entry = iterator.next();

        Assert.assertEquals("README", entry.getName());
        Assert.assertEquals(new ObjectId("a8233120f6ad708f843d861ce2b7228ec4e3dec6"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("branch_file.txt", entry.getName());
        Assert.assertEquals(new ObjectId("3697d64be941a53d4ae8f6a271e4e3fa56b022cc"), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("link_to_new.txt", entry.getName());
        Assert.assertEquals(new ObjectId("c0528fd6cc988c0a40ce0be11bc192fc8dc5346e"), entry.getId());
        Assert.assertEquals(Mode.LINK, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        entry = iterator.next();

        Assert.assertEquals("new.txt", entry.getName());
        Assert.assertEquals(new ObjectId("a71586c1dfe8a71c6cbf6c129f404c5642ff31bd"), entry.getId());
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

        ObjectId oid = new ObjectId(C8_ROOT);
        Tree tree = repository.lookup(oid);

        TreeEntry entry = tree.getEntry(README);

        Assert.assertEquals(README, entry.getName());
        Assert.assertEquals(new ObjectId(C8_README), entry.getId());
        Assert.assertEquals(Mode.FILE, entry.getMode());
        Assert.assertEquals(ObjectType.BLOB, entry.getType());

        repository.close();
    }

    @Test
    public void testCanRealizeTreeEntry()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C8_ROOT);
        Tree tree = repository.lookup(oid);

        TreeEntry entry = tree.getEntry(README);
        Blob blob = entry.realize();

        Assert.assertEquals(new ObjectId(C8_README), blob.getId());
    }
}
