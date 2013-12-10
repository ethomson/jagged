package org.libgit2.jagged.core;

public abstract class Lazy<T>
{
    private T value = null;
    private final Object lock = new Object();

    protected abstract T call();

    public final T getValue()
    {
        synchronized (lock)
        {
            if (value == null)
            {
                value = call();
            }
            return value;
        }
    }
}
