package org.libgit2.jagged;

import org.junit.Assert;
import org.junit.Test;
import org.libgit2.jagged.Reference.DirectReference;

public class ReferenceTest
    extends GitTest
{
    @Test
    public void testCanGetHead()
    {
        Repository repository = new Repository(setupRepository("testrepo").getAbsolutePath());
        Reference headReference = repository.getHead();

        Assert.assertTrue(headReference instanceof DirectReference);

        headReference.dispose();
    }
}
