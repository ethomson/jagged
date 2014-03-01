package org.libgit2.jagged;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;
import org.libgit2.jagged.core.GitException;
import org.libgit2.jagged.core.Platform;
import org.libgit2.jagged.core.Platform.OperatingSystem;

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
            if (Platform.getCurrentPlatform().getOperatingSystem().equals(OperatingSystem.WINDOWS))
            {
                caught = e.getMessage().trim().endsWith("The system cannot find the file specified.");
            }
            else
            {
                caught = e.getMessage().endsWith("No such file or directory");
            }

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
    public void testOpenBareRepository()
    {
	final File repoPath = getTempDir();

	Repository repository = Repository.init(repoPath.getAbsolutePath(), true);
	Repository repository2 = new Repository(repoPath.getAbsolutePath());

	Assert.assertTrue(repository.isBare());
	Assert.assertTrue(repository2.isBare());

	repository.dispose();
	repository2.dispose();
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
