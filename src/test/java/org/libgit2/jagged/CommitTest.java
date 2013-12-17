package org.libgit2.jagged;

import java.io.File;
import java.util.Iterator;

import junit.framework.Assert;

import org.junit.Test;

public class CommitTest
    extends GitTest
{
    @Test
    public void testLookupCommit()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("055fe18dd1aef07991ebd08b4d54fc761dd022fb");
        Commit commit = repository.lookup(oid);

        Assert.assertEquals(oid, commit.getId());

        repository.dispose();
    }

    @Test
    public void testGetCommitter()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("055fe18dd1aef07991ebd08b4d54fc761dd022fb");
        Commit commit = repository.lookup(oid);

        Assert.assertEquals("Edward Thomson", commit.getCommitter().getName());
        Assert.assertEquals("ethomson@microsoft.com", commit.getCommitter().getEmail());

        repository.dispose();
    }

    @Test
    public void testGetAuthor()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("5eab02d63a3676df528bcd878ac935ec0c4d5bdc");
        Commit commit = repository.lookup(oid);

        Assert.assertEquals("Edward Thomson", commit.getAuthor().getName());
        Assert.assertEquals("ethomson@microsoft.com", commit.getAuthor().getEmail());

        repository.dispose();
    }

    @Test
    public void testGetParents()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId parentOid = new ObjectId("055fe18dd1aef07991ebd08b4d54fc761dd022fb");
        Commit parent = repository.lookup(parentOid);

        Assert.assertEquals(false, parent.getParents().iterator().hasNext());

        ObjectId childOid = new ObjectId("5eab02d63a3676df528bcd878ac935ec0c4d5bdc");
        Commit child = repository.lookup(childOid);

        Iterator<Commit> parents = child.getParents().iterator();

        Assert.assertEquals(parent, parents.next());
        Assert.assertEquals(false, parents.hasNext());

        repository.dispose();
    }

    @Test
    public void testGetTree()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("055fe18dd1aef07991ebd08b4d54fc761dd022fb");
        Commit commit = repository.lookup(oid);

        Tree tree = repository.lookup(new ObjectId("e77ab1c63f3fbde9c5ef9972939aa0717012d7c0"));

        Assert.assertEquals(tree, commit.getTree());

        repository.dispose();
    }
}
