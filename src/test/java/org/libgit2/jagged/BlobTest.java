package org.libgit2.jagged;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.libgit2.jagged.core.Platform;
import org.libgit2.jagged.core.Platform.OperatingSystem;

public class BlobTest
    extends GitTest
{
    @Test
    public void testLookupBlob()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1_README);
        Blob blob = repository.lookup(oid);

        Assert.assertEquals(oid, blob.getId());

        repository.close();
    }

    @Test
    public void testGetBlobMetadata()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1_README);
        Blob blob = repository.lookup(oid);

        Assert.assertEquals(4, blob.getSize());
        Assert.assertEquals(false, blob.isBinary());

        repository.close();
    }

    @Test
    public void testGetRawContent()
        throws IOException
    {
        byte[] expected = "hey\n".getBytes();

        final File repoPath = setupRepository("testrepo");
        write(new File(repoPath, ".gitattributes"), "* text");

        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1_README);
        Blob blob = repository.lookup(oid);

        InputStream contentStream = blob.getContentStream();

        try
        {
            byte[] buf = new byte[expected.length];

            Assert.assertEquals(expected.length, contentStream.read(buf, 0, expected.length));
            Assert.assertEquals(-1, contentStream.read());
            Assert.assertArrayEquals(expected, buf);
        }
        finally
        {
            contentStream.close();
        }

        repository.close();
    }

    @Test
    public void testGetFilteredContent()
        throws IOException
    {
        byte[] expected;

        if (Platform.getCurrentPlatform().getOperatingSystem().equals(OperatingSystem.WINDOWS))
        {
            expected = "hey\r\n".getBytes();
        }
        else
        {
            expected = "hey\n".getBytes();
        }

        final File repoPath = setupRepository("testrepo");
        write(new File(repoPath, ".gitattributes"), "* text");

        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId(C1_README);
        Blob blob = repository.lookup(oid);

        InputStream contentStream = blob.getContentStream(new FilteringOptions("two.txt"));

        try
        {
            byte[] buf = new byte[expected.length];

            Assert.assertEquals(expected.length, contentStream.read(buf, 0, expected.length));
            Assert.assertEquals(-1, contentStream.read());
            Assert.assertArrayEquals(expected, buf);
        }
        finally
        {
            contentStream.close();
        }

        repository.close();
    }
}
