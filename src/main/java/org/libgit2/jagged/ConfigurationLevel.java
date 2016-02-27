package org.libgit2.jagged;

/**
 * A scope for a group of Git configuration settings, ranging from the local
 * repository to the entire system.
 */
public enum ConfigurationLevel
{
    /** System-wide configuration on Windows */
    PROGRAMDATA(1),

    /** The system-wide configuration */
    SYSTEM(2),

    /** User configuration per X Desktop Group specifications */
    XDG(3),

    /** User configuration */
    GLOBAL(4),

    /** Repository configuration */
    LOCAL(5),

    /** Application specific configuration */
    APPLICATION(6);

    private final int value;

    private ConfigurationLevel(int value)
    {
        this.value = value;
    }

    int getValue()
    {
        return value;
    }
}
