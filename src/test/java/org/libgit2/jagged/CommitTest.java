package org.libgit2.jagged;

import java.io.File;

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
}
