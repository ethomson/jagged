package org.libgit2.jagged.core;

public class BlobMetadata
{
    private final long size;
    private final boolean binary;

    private BlobMetadata(long size, boolean binary)
    {
        this.size = size;
        this.binary = binary;
    }

    public long getSize()
    {
        return size;
    }

    public boolean isBinary()
    {
        return binary;
    }
}
