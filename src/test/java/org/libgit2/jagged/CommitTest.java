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

        ObjectId oid = new ObjectId(C1);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals(oid, commit.getId());

        repository.close();
    }

    @Test
    public void testGetCommitter()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals("Scott Chacon", commit.getCommitter().getName());
        Assert.assertEquals("schacon@gmail.com", commit.getCommitter().getEmail());

        repository.close();
    }

    @Test
    public void testGetAuthor()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C2);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals("Scott Chacon", commit.getAuthor().getName());
        Assert.assertEquals("schacon@gmail.com", commit.getAuthor().getEmail());

        repository.close();
    }

    @Test
    public void testGetParents()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId parentOid = new ObjectId(C1);
        Commit parent = repository.lookup(parentOid);

        Assert.assertEquals(false, parent.getParents().iterator().hasNext());

        ObjectId childOid = new ObjectId(C2);
        Commit child = repository.lookup(childOid);

        Iterator<Commit> parents = child.getParents().iterator();

        Assert.assertEquals(parent, parents.next());
        Assert.assertEquals(false, parents.hasNext());

        repository.close();
    }

    @Test
    public void testGetTree()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1);
        Commit commit = repository.lookup(oid);

        Tree tree = repository.lookup(new ObjectId(C1_ROOT));

        Assert.assertEquals(tree, commit.getTree());

        repository.close();
    }
}
