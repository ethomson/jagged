package org.libgit2.jagged;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

public class Version
{
    private static final Version version = new Version(0, 0, 1);

    private static final Lazy<Version> libGit2Version = new Lazy<Version>()
    {
        @Override
        protected Version call()
        {
            return NativeMethods.globalGetLibGit2Version();
        }
    };

    public static Version getVersion()
    {
        return version;
    }

    public static Version getLibGit2Version()
    {
        return libGit2Version.getValue();
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
