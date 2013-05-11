package org.libgit2.jagged.core;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A "native handle" contains a native pointer.
 */
public final class NativeHandle
{
    private final AtomicLong ptr = new AtomicLong(0);

    /**
     * Sets the pointer. To be called from JNI only.
     * 
     * @param handle
     *        the new handle (not {@code 0})
     */
    @SuppressWarnings("unused")
    private void set(long handle)
    {
        if (handle == 0)
        {
            throw new NullPointerException("handle is null");
        }

        if (!ptr.compareAndSet(0, handle))
        {
            throw new IllegalStateException("handle is not null");
        }
    }

    /**
     * Gets the pointer. The pointer must have already been set.
     * 
     * @return the handle (never {@code 0})
     */
    long get()
    {
        long handle = ptr.get();

        if (handle == 0)
        {
            throw new NullPointerException("handle is null");
        }

        return handle;
    }

    /**
     * Sets the pointer to {@code 0}, returning the original value.
     * 
     * @return the handle (never {@code 0})
     */
    long dispose()
    {
        long handle = ptr.getAndSet(0);

        if (handle == 0)
        {
            throw new NullPointerException("handle is null");
        }

        return handle;
    }
}
