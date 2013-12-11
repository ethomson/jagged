package org.libgit2.jagged.core;

public abstract class Lazy<T>
{
    private final Object lock = new Object();
    private boolean set = false;
    private T value;

    protected abstract T call();

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
