package org.libgit2.jagged;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A collection of the capabilities offered by libgit2. These are typically
 * optional components that depend on specific platforms or other libraries and
 * are chosen at compile-time. that are chosen at compile-time.
 */
public class Capabilities
{
    /**
     * Threading is supported. Some time consuming or complex operations (eg,
     * packbuilding) may be able to take advantage of multiple threads.
     */
    public static final Capabilities THREADS = new Capabilities(1 << 0);

    /** SSL (HTTPS) remotes are supported. */
    public static final Capabilities HTTPS = new Capabilities(1 << 1);

    /** SSH remotes are supported. */
    public static final Capabilities SSH = new Capabilities(1 << 2);

    private static final Lazy<Capabilities> instance = new Lazy<Capabilities>()
    {
        @Override
        protected Capabilities call()
        {
            return new Capabilities(NativeMethods.globalGetCapabilities());
        }
    };

    /**
     * Gets the set of capabilities supported by this instance of libgit2.
     * 
     * @return The capabilities
     */
    public static Capabilities getInstance()
    {
        return instance.getValue();
    }

    /**
     * <p>
     * Combines the given capabilities options to indicate that multiple
     * capabilities are supported. For example, to query for both threading
     * support and SSH support:
     * </p>
     * 
     * <p>
     * {@code
     * Capabilities caps = Capabilities.combine(Capabilities.THREADS, Capabilities.SSH);
     * }
     * </p>
     * 
     * @param capabilities
     *        The capabilities to combine
     * @return The resultant combined capabilities
     */
    public static Capabilities combine(Capabilities... capabilities)
    {
        int values = 0;

        for (Capabilities capability : capabilities)
        {
            values |= capability.values;
        }

        return new Capabilities(values);
    }

    private final int values;

    private Capabilities(int values)
    {
        this.values = values;
    }

    /**
     * Queries whether the given capabilities are supported. If a set of
     * multiple capabilities have been created with the
     * {@link #combine(Capabilities...)} method, all capabilities will be
     * queried.
     * 
     * @param value
     *        The capabilities to query
     * @return {@code true} if all the given capabilities are supported,
     *         {@code false} otherwise.
     */
    public boolean contains(Capabilities value)
    {
        return ((values & value.values) == value.values);
    }

    @Override
    public int hashCode()
    {
        return values;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof Capabilities))
        {
            return false;
        }

        return values == ((Capabilities) obj).values;
    }
}
