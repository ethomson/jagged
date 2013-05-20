package org.libgit2.jagged.core;

public class HashCode
{
    private HashCode()
    {
    }

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
