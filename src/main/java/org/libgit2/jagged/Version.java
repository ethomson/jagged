package org.libgit2.jagged;

import org.libgit2.jagged.core.NativeMethods;

public class Version
{
    private static final Version version = new Version(0, 0, 1);

    private static Version libGit2Version = null;
    private static final Object libGit2VersionLock = new Object();

    public static Version getVersion()
    {
        return version;
    }

    public static Version getLibGit2Version()
    {
        synchronized (libGit2VersionLock)
        {
            if (libGit2Version == null)
            {
                libGit2Version = NativeMethods.getLibGit2Version();
            }

            return libGit2Version;
        }
    }

    private final int major;
    private final int minor;
    private final int revision;

    private Version(int major, int minor, int revision)
    {
        this.major = major;
        this.minor = minor;
        this.revision = revision;
    }

    public int getMajor()
    {
        return major;
    }

    public int getMinor()
    {
        return minor;
    }

    public int getRevision()
    {
        return revision;
    }
}
