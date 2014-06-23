package org.libgit2.jagged;

/**
 * A collection of the capabilities offered by libgit2. These are typically
 * optional components that depend on specific platforms or other libraries and
 * are chosen at compile-time. that are chosen at compile-time.
 */
public class BuiltInFeatures
{
    /**
     * Threading is supported. Some time consuming or complex operations (eg,
     * packbuilding) may be able to take advantage of multiple threads.
     */
    public static final BuiltInFeatures THREADS = new BuiltInFeatures(1 << 0);

    /** SSL (HTTPS) remotes are supported. */
    public static final BuiltInFeatures HTTPS = new BuiltInFeatures(1 << 1);

    /** SSH remotes are supported. */
    public static final BuiltInFeatures SSH = new BuiltInFeatures(1 << 2);

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
    public static BuiltInFeatures combine(BuiltInFeatures... features)
    {
        int values = 0;

        for (BuiltInFeatures feature : features)
        {
            values |= feature.values;
        }

        return new BuiltInFeatures(values);
    }

    private final int values;

    BuiltInFeatures(int values)
    {
        this.values = values;
    }

    /**
     * Queries whether the given features are supported. If a set of
     * multiple features have been created with the
     * {@link #combine(BuiltInFeatures...)} method, all features will be
     * queried.
     * 
     * @param value
     *        The features to query
     * @return {@code true} if all the given features are supported,
     *         {@code false} otherwise.
     */
    public boolean contains(BuiltInFeatures value)
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
        if (obj == null || !(obj instanceof BuiltInFeatures))
        {
            return false;
        }

        return values == ((BuiltInFeatures) obj).values;
    }
}
