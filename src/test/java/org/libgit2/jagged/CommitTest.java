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

        ObjectId oid = new ObjectId(FIRST_COMMIT_ID);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals(oid, commit.getId());

        repository.close();
    }

    @Test
    public void testGetCommitter()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(FIRST_COMMIT_ID);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals(AUTHOR_NAME, commit.getCommitter().getName());
        Assert.assertEquals(AUTHOR_EMAIL, commit.getCommitter().getEmail());

        repository.close();
    }

    @Test
    public void testGetAuthor()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(SECOND_COMMIT_ID);
        Commit commit = repository.lookup(oid);

        Assert.assertEquals(AUTHOR_NAME, commit.getAuthor().getName());
        Assert.assertEquals(AUTHOR_EMAIL, commit.getAuthor().getEmail());

        repository.close();
    }

    @Test
    public void testGetParents()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId parentOid = new ObjectId(FIRST_COMMIT_ID);
        Commit parent = repository.lookup(parentOid);

        Assert.assertEquals(false, parent.getParents().iterator().hasNext());

        ObjectId childOid = new ObjectId(SECOND_COMMIT_ID);
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

        ObjectId oid = new ObjectId(FIRST_COMMIT_ID);
        Commit commit = repository.lookup(oid);

        Tree tree = repository.lookup(new ObjectId("ff77323c5557be69500eb91efa418074fd3f0443"));

        Assert.assertEquals(tree, commit.getTree());

        repository.close();
    }
}
