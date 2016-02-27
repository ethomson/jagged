package org.libgit2.jagged.core;

import java.nio.ByteBuffer;

public class BlobFilteredContentStream
    extends BlobContentStream
{
    private final NativeHandle bufHandle = new NativeHandle();

    private BlobFilteredContentStream(final ByteBuffer byteBuffer, final long blobHandle, final long bufHandle)
    {
        super(byteBuffer, blobHandle);

        this.bufHandle.set(bufHandle);
    }

    @SuppressWarnings("unused")
    private long getBufHandle()
    {
        return bufHandle.get();
    }

    @Override
    public void close()
    {
        NativeMethods.blobFilteredBufFree(bufHandle.close());
    }
}
