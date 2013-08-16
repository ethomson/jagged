package org.libgit2.jagged;

import java.util.HashSet;
import java.util.Set;

import org.libgit2.jagged.core.NativeMethods;

public class Common
{

    public static class Version
    {
        private final int major;
        private final int minor;
        private final int rev;

        public Version(int major, int minor, int rev)
        {

            this.major = major;
            this.minor = minor;
            this.rev = rev;
        }

        int getMajor()
        {
            return major;
        }

        int getMinor()
        {
            return minor;
        }

        int getRev()
        {
            return rev;
        }
    }

    public static Version getLibgit2Version()
    {
        return NativeMethods.getLibgit2Version();
    }

    enum Capabilities
    {
        THREADS(1 << 0),
        HTTPS(1 << 1);

        private final int value;

        private Capabilities(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }
    }

    public static Set<Capabilities> getLibgit2Capabilities()
    {
        int cap = NativeMethods.getLibgit2Capabilities();
        Set<Capabilities> capabilities = new HashSet<Capabilities>();

        for (Capabilities c : Capabilities.values())
        {
            if ((c.getValue() & cap) > 0)
            {
                capabilities.add(c);
            }
        }
        return capabilities;
    }

    /**
     * Get the maximum mmap window size
     */
    public static int getMmapWindowSize()
    {
        return NativeMethods.getMmapWindowSize();
    }

    /**
     * Set the maximum mmap window size
     */
    public static void setMmapWindowSize(int size)
    {
        NativeMethods.setMmapWindowSize(size);
    }

    /**
     * Get the maximum memory that will be mapped in total by the library
     */
    public static int getMmapWindowMappedLimit()
    {
        return NativeMethods.getMmapWindowMappedLimit();
    }

    /**
     * Set the maximum amount of memory that can be mapped at any time by the
     * library
     */
    public static void setMmapWindowMappedLimit(int size)
    {
        NativeMethods.setMmapWindowMappedLimit(size);
    }

    enum ConfigLevel
    {
        SYSTEM(1),
        GLOBAL(3),
        XDG(2);

        private final int value;

        private ConfigLevel(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }
    }

    /**
     * Get the search path for a given level of config data.
     */
    public static String getSearchPath(ConfigLevel level)
    {
        return NativeMethods.getSearchPath(level.getValue());
    }

    /**
     * Set the search path for a level of config data. The search path applied
     * to shared attributes and ignore files, too. - `path` lists directories
     * delimited by GIT_PATH_LIST_SEPARATOR. Pass NULL to reset to the default
     * (generally based on environment variables). Use magic path `$PATH` to
     * include the old value of the path (if you want to prepend or append, for
     * instance).
     */
    public static void setSearchPath(ConfigLevel level, String path)
    {
        NativeMethods.setSearchPath(level.getValue(), path);
    }

    /** Basic type (loose or packed) of any Git object. */
    enum ObjType
    {
        /** < Object can be any of the following */
        ANY(-2),
        /** < Object is invalid. */
        BAD(-1),
        /** < Reserved for future use. */
        EXT1(0),
        /** < A commit object. */
        COMMIT(1),
        /** < A tree (directory listing) object. */
        TREE(2),
        /** < A file revision object. */
        BLOB(3),
        /** < An annotated tag object. */
        TAG(4),
        /** < Reserved for future use. */
        EXT2(5),
        /** < A delta, base is given by an offset. */
        OFS_DELTA(6),
        /** < A delta, base is given by object id. */
        REF_DELTA(7);

        private final int value;

        private ObjType(int value)
        {
            this.value = value;
        }

        int getValue()
        {
            return value;
        }

    };

    /**
     * Set the maximum data size for the given type of object to be considered
     * eligible for caching in memory. Setting to value to zero means that that
     * type of object will not be cached. Defaults to 0 for BLOB (i.e. won't
     * cache blobs) and 4k for COMMIT, TREE, and TAG.
     */
    public static void setCacheObjectLimit(ObjType type, int size)
    {
        NativeMethods.setCacheObjectLimit(type.getValue(), size);
    }

    /**
     * Set the maximum total data size that will be cached in memory across all
     * repositories before libgit2 starts evicting objects from the cache. This
     * is a soft limit, in that the library might briefly exceed it, but will
     * start aggressively evicting objects from cache when that happens. The
     * default cache size is 256Mb.
     */
    public static void setCacheMaxSize(int maxStorageBytes)
    {
        NativeMethods.setCacheMaxSize(maxStorageBytes);
    }

    /**
     * Enable or disable caching completely. Because caches are
     * repository-specific, disabling the cache cannot immediately clear all
     * cached objects, but each cache will be cleared on the next attempt to
     * update anything in it.
     */
    public static void setEnableCaching(boolean enabled)
    {
        NativeMethods.setEnableCaching(enabled);
    }

    public static class CachedMemory
    {
        private int current;
        private int allowed;

        public CachedMemory(int current, int allowed)
        {
            super();
            this.current = current;
            this.allowed = allowed;
        }

        public int getCurrent()
        {
            return current;
        }

        public int getAllowed()
        {
            return allowed;
        }
    }

    /**
     * Get the current bytes in cache and the maximum that would be allowed in
     * the cache
     */
    public static CachedMemory getCachedMemory()
    {
        return NativeMethods.getCachedMemory();
    }

}
