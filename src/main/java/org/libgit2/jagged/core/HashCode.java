package org.libgit2.jagged.core;

/**
 * Hash code helper methods.
 */
public final class HashCode
{
    private HashCode()
    {
    }

    /**
     * Computes a hash code with the given components, per Josh Bloch's general
     * hash code generation algorithm.
     * 
     * @param components
     *        The components to include when creating the hash code
     * @return A hash code
     */
    public static final int getHashCode(final Object... components)
    {
        int result = 17;

        for (Object o : components)
        {
            result = result * 37 + ((o == null) ? 0 : o.hashCode());
        }

        return result;
    }
}
