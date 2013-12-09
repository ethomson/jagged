package org.libgit2.jagged;

import java.io.File;
import java.text.MessageFormat;

import junit.framework.Assert;

import org.junit.Test;

public class OptionsTest
{
    @Test
    public void testCanGetAndSetMmapWindowSize()
    {
        long defaultSize = Options.getMmapWindowSize();
        long newSize = defaultSize * 2;

        Options.setMmapWindowSize(newSize);

        Assert.assertEquals(newSize, Options.getMmapWindowSize());
    }

    @Test
    public void testCanGetAndSetMmapWindowMappedLimit()
    {
        long defaultLimit = Options.getMmapWindowMappedLimit();
        long newLimit = defaultLimit * 2;

        Options.setMmapWindowMappedLimit(newLimit);

        Assert.assertEquals(newLimit, Options.getMmapWindowMappedLimit());
    }

    @Test
    public void testCanGetAndSetSearchPath()
    {
        String defaultPath = Options.getSearchPath(ConfigurationLevel.SYSTEM);
        String newPath = defaultPath + "/test";

        Options.setSearchPath(ConfigurationLevel.SYSTEM, newPath);
        Assert.assertEquals(newPath, Options.getSearchPath(ConfigurationLevel.SYSTEM));

        Options.setSearchPath(ConfigurationLevel.SYSTEM, null);
    }

    @Test
    public void testCanUsePathVariableInSearchPath()
    {
        String defaultPath = Options.getSearchPath(ConfigurationLevel.SYSTEM);

        Options.setSearchPath(
            ConfigurationLevel.SYSTEM,
            MessageFormat.format("$PATH{0}{1}", File.pathSeparator, "tempDir"));
        Assert.assertEquals(
            MessageFormat.format("{0}{1}{2}", defaultPath, File.pathSeparator, "tempDir"),
            Options.getSearchPath(ConfigurationLevel.SYSTEM));

        Options.setSearchPath(ConfigurationLevel.SYSTEM, null);
    }

    @Test
    public void testCanResetSearchPath()
    {
        String defaultPath = Options.getSearchPath(ConfigurationLevel.SYSTEM);

        Options.setSearchPath(ConfigurationLevel.SYSTEM, "test");
        Assert.assertEquals("test", Options.getSearchPath(ConfigurationLevel.SYSTEM));

        Options.setSearchPath(ConfigurationLevel.SYSTEM, null);
        Assert.assertEquals(defaultPath, Options.getSearchPath(ConfigurationLevel.SYSTEM));
    }

    @Test
    public void testCanSetCacheObjectLimit()
    {
        Options.setCacheObjectLimit(ObjectType.COMMIT, (2 * 1024 * 1024));
    }

    @Test
    public void testCanSetCacheMaxSize()
    {
        long max = 128 * 1024 * 1024;

        Options.setCacheMaxSize(max);
        Assert.assertEquals(max, Options.getCachedStatistics().getMaximum());
    }

    @Test
    public void testCanSetEnableCaching()
    {
        Options.setEnableCaching(false);
        Options.setEnableCaching(true);
    }

    @Test
    public void testCanGetCachedMemory()
    {
        Options.CacheStatistics cacheStatistics = Options.getCachedStatistics();
        Assert.assertTrue(cacheStatistics.getMaximum() > 0);
    }
}
