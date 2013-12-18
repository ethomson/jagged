package org.libgit2.jagged;

import java.io.InputStream;

import org.libgit2.jagged.core.BlobMetadata;
import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A blob represents a file in a git repository.
 */
public class Blob
    extends GitObject
{
    private final Lazy<BlobMetadata> metadata = new Lazy<BlobMetadata>()
    {
        @Override
        protected BlobMetadata call()
        {
            return NativeMethods.blobGetMetadata(Blob.this.getRepository(), Blob.this);
        }
    };

    Blob(Repository repo, ObjectId id)
    {
        super(repo, id);
    }

    public boolean isBinary()
    {
        return metadata.getValue().isBinary();
    }

    public long getSize()
    {
        return metadata.getValue().getSize();
    }

    public InputStream getContentStream()
    {
        return NativeMethods.blobGetRawContentStream(Blob.this.getRepository(), Blob.this);
    }
}
