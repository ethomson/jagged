package org.libgit2.jagged;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

/**
 * The version number of Jagged or the underlying libgit2 library. Version
 * numbers are described as {@code major.minor.revision}, for example version
 * number {@code 1.2.3} corresponds to major number {@code 1}, minor number
 * {@code 2}, revision number {@code 3}.
 */
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

    /**
     * Gets the version number of the Jagged instance.
     * 
     * @return The version number of this library
     */
    public static Version getVersion()
    {
        return version;
    }

    /**
     * Gets the version number of the underlying libgit2 instance.
     * 
     * @return The version number of libgit2
     */
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

    /**
     * The major number portion of the version number.
     * 
     * @return The major number
     */
    public int getMajor()
    {
        return major;
    }

    /**
     * The minor number portion of the version number.
     * 
     * @return The minor number
     */
    public int getMinor()
    {
        return minor;
    }

    /**
     * The revision number portion of the version number.
     * 
     * @return The revision number
     */
    public int getRevision()
    {
        return revision;
    }
}
