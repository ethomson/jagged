package org.libgit2.jagged;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class BlobTest
    extends GitTest
{
    @Test
    public void testLookupBlob()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("dc48b6c38e967e57965e36c6f7a1c3ec5c3e1ff4");
        Blob blob = repository.lookup(oid);

        Assert.assertEquals(oid, blob.getId());

        repository.dispose();
    }

    @Test
    public void testGetBlobMetadata()
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("dc48b6c38e967e57965e36c6f7a1c3ec5c3e1ff4");
        Blob blob = repository.lookup(oid);

        Assert.assertEquals(18, blob.getSize());
        Assert.assertEquals(false, blob.isBinary());

        repository.dispose();
    }

    @Test
    public void testGetRawContent()
        throws IOException
    {
        final File repoPath = setupRepository("testrepo");
        Repository repository = new Repository(repoPath.getAbsolutePath());

        ObjectId oid = new ObjectId("dc48b6c38e967e57965e36c6f7a1c3ec5c3e1ff4");
        Blob blob = repository.lookup(oid);

        InputStream contentStream = blob.getContentStream();

        try
        {
            byte[] buf = new byte[18];

            Assert.assertEquals(18, contentStream.read(buf, 0, 32));
            Assert.assertArrayEquals("This is file two!\n".getBytes(), buf);
        }
        finally
        {
            contentStream.close();
        }

        repository.dispose();
    }
}
