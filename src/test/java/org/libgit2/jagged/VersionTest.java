package org.libgit2.jagged;

import junit.framework.Assert;

import org.junit.Test;

public class VersionTest
{
    @Test
    public void testGetVersion()
    {
        Version jaggedVersion = Version.getVersion();

        Assert.assertEquals(0, jaggedVersion.getMajor());
        Assert.assertEquals(0, jaggedVersion.getMinor());
        Assert.assertEquals(1, jaggedVersion.getRevision());
    }

    @Test
    public void testGetLibGit2Version()
    {
        Version libGit2Version = Version.getLibGit2Version();

        Assert.assertEquals(0, libGit2Version.getMajor());
        Assert.assertEquals(25, libGit2Version.getMinor());
        Assert.assertEquals(1, libGit2Version.getRevision());
    }
}
