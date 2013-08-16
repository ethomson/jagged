package org.libgit2.jagged;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.libgit2.jagged.Common.CachedMemory;

public class CommonTest extends GitTest
{

    @Test
    public void testCanGetVersion()
    {
        Common.Version version = Common.getLibgit2Version();

        assertEquals(0, version.getMajor());
        assertEquals(19, version.getMinor());
        assertEquals(0, version.getRev());
    }

    @Test
    public void testCanGetCapabilities()
    {
        Set<Common.Capabilities> capabilities = Common.getLibgit2Capabilities();

        assertTrue(capabilities.contains(Common.Capabilities.HTTPS));
        assertTrue(capabilities.contains(Common.Capabilities.THREADS));
    }

    @Test
    public void testCanGetAndSetMmapWindowSize()
    {
        int size = Common.getMmapWindowSize();
        Common.setMmapWindowSize(size + 2);
        int newSize = Common.getMmapWindowSize();
        assertEquals(newSize, size + 2);
    }

    @Test
    public void testCanGetAndSetMmapWindowMappedLimit()
    {
        int limit = Common.getMmapWindowMappedLimit();
        Common.setMmapWindowMappedLimit(limit + 2);
        int newLimit = Common.getMmapWindowMappedLimit();
        assertEquals(newLimit, limit + 2);
    }

    @Test
    public void testCanGetAndSetSearchPath()
    {
        String path = Common.getSearchPath(Common.ConfigLevel.SYSTEM);
        Common.setSearchPath(Common.ConfigLevel.SYSTEM, path + "/git");
        String newPath = Common.getSearchPath(Common.ConfigLevel.SYSTEM);
        assertTrue(newPath.endsWith(path + "/git"));
    }

    @Test
    public void testCanSetCacheObjectLimit()
    {
        Common.setCacheObjectLimit(Common.ObjType.BLOB, 2 * 1024 * 1024);
    }

    @Test
    public void testCanSetCacheMaxSize()
    {
        Common.setCacheMaxSize(128 * 1024 * 1024);
        CachedMemory cachedMemory = Common.getCachedMemory();
        assertEquals(128 * 1024 * 1024, cachedMemory.getAllowed());
    }

    @Test
    public void testCanSetEnableCaching()
    {
        Common.setEnableCaching(false);
        Common.setEnableCaching(true);
    }

    @Test
    public void testCanGetCachedMemory()
    {
        CachedMemory cachedMemory = Common.getCachedMemory();
        assertTrue(cachedMemory.getAllowed() > 0);
    }

}
