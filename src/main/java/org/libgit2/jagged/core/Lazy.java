package org.libgit2.jagged.core;

/**
 * Helper method to lazily look up a value and cache it.
 */
public abstract class Lazy<T>
{
    private final Object lock = new Object();
    private boolean set = false;
    private T value;

    /**
     * Implementations should override to provide the value they wish to return
     * to the caller.
     * 
     * @return The value to store
     */
    protected abstract T call();

    /**
     * Return the value that is looked up or cached
     * 
     * @return The value
     */
    public final T getValue()
    {
        synchronized (lock)
        {
            if (!set)
            {
                value = call();
                set = true;
            }

            return value;
        }
    }
}
