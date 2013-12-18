package org.libgit2.jagged.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class BlobContentStream
    extends InputStream
{
    private final ByteBuffer byteBuffer;

    private final NativeHandle blobHandle = new NativeHandle();

    protected BlobContentStream(final ByteBuffer byteBuffer, final long blobHandle)
    {
        this.byteBuffer = byteBuffer;
        this.blobHandle.set(blobHandle);
    }

    @Override
    public int read()
        throws IOException
    {
        if (byteBuffer.hasRemaining())
        {
            return -1;
        }

        return byteBuffer.get();
    }

    @Override
    public int read(byte[] buf, int offset, int len)
    {
        if (byteBuffer.remaining() == 0)
        {
            return -1;
        }

        if (len > byteBuffer.remaining())
        {
            len = byteBuffer.remaining();
        }

        byteBuffer.get(buf, offset, len);

        return len;
    }

    @SuppressWarnings("unused")
    private long getBlobHandle()
    {
        return blobHandle.get();
    }

    @Override
    public void close()
    {
        NativeMethods.blobFree(blobHandle.get());
    }
}
