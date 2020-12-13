package org.libgit2.jagged;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class ReferenceTest
    extends GitTest
{
    @Test
    public void testCanGetHead()
    {
        Repository repository = new Repository(setupRepository("testrepo").getAbsolutePath());
        Branch headBranch = repository.getHead();

        Assert.assertTrue(headBranch.isCurrentRepositoryHead());
    }

    @Test
    public void testCanGetDetachedHead()
        throws Exception
    {
        final String repositoryPath = setupRepository("testrepo").getAbsolutePath();

        Repository repository = new Repository(repositoryPath);

        BufferedWriter w = new BufferedWriter(new FileWriter(repositoryPath + "/.git/HEAD"));
        w.write("055fe18dd1aef07991ebd08b4d54fc761dd022fb\n");
        w.close();

        Branch headBranch = repository.getHead();

        Assert.assertTrue(headBranch instanceof DetachedHead);
        Assert.assertTrue(headBranch.isCurrentRepositoryHead());
    }

    @Test
    public void testCanIterateReferences()
    {
        final String[] expected = new String[]
        {
            "refs/heads/br2",
            "refs/heads/dir",
            "refs/heads/ident",
            "refs/heads/long-file-name",
            "refs/heads/master",
            "refs/heads/packed-test",
            "refs/heads/subtrees",
            "refs/heads/test",
            "refs/tags/e90810b",
            "refs/tags/foo/bar",
            "refs/tags/foo/foo/bar",
            "refs/tags/point_to_blob",
            "refs/tags/test",
            "refs/heads/packed",
            "refs/tags/packed-tag"
        };
        Repository repository = new Repository(setupRepository("testrepo").getAbsolutePath());

        List<String> found = new ArrayList<String>();

        for (Reference r : repository.getReferences())
        {
            found.add(r.getCanonicalName());
        }

        Assert.assertTrue(Arrays.equals(expected, found.toArray(new String[found.size()])));
    }
}
