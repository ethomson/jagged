package org.libgit2.jagged;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.libgit2.jagged.core.GitException;

public class RepositoryTest
    extends GitTest
{
    @Test
    public void testInitializeBareRepository()
    {
        Repository newRepository = Repository.init(new File(getTempDir(), "repo-init").getAbsolutePath(), true);
        Assert.assertTrue(newRepository.isBare());
        newRepository.dispose();
    }

    @Test
    public void testInitializeNonBareRepository()
    {
        Repository newRepository = Repository.init(new File(getTempDir(), "repo-init").getAbsolutePath(), false);
        Assert.assertFalse(newRepository.isBare());
        newRepository.dispose();
    }

    @Test
    public void testOpenNonExistingRepositoryFails()
    {
        boolean caught = false;

        try
        {
            @SuppressWarnings("unused")
            Repository failure = new Repository(new File(getTempDir(), "doesnotexist").getAbsolutePath());
        }
        catch (GitException e)
        {
            caught = e.getMessage().endsWith("No such file or directory");

            if (!caught)
                throw e;
        }

        Assert.assertTrue(caught);
    }

    @Test
    public void testOpenExistingRepository()
    {
        final File repoPath = setupRepository("testrepo");

        Repository repository = new Repository(repoPath.getAbsolutePath());
        repository.dispose();
    }

    @Test
    public void testClone()
    {
        final File repoPath = setupRepository("testrepo");

        Repository repository =
            Repository.clone(repoPath.getAbsolutePath(), new File(getTempDir(), "cloned").getAbsolutePath());
        repository.dispose();
    }
}
