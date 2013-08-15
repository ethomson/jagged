package org.libgit2.jagged.core;

import org.libgit2.jagged.Common;
import org.libgit2.jagged.Reference;
import org.libgit2.jagged.Reference.DirectReference;
import org.libgit2.jagged.Repository;

public class NativeMethods
{
    private static final Object finalizer;

    static
    {
        NativeLoader.load("git2");
        NativeLoader.load("jagged");

        threadsInit();

        finalizer = new Object()
        {
            @Override
            public void finalize()
            {
                threadsShutdown();
            }
        };
    }

    /*
     * Global state
     */

    public static native GitError errorLast();

    public static native void threadsInit();

    public static native void threadsShutdown();

    /*
     * Common operations
     */
    public static native Common.Version getLibgit2Version();

    public static native int getLibgit2Capabilities();

    public static native int getMmapWindowSize();

    public static native void setMmapWindowSize(int size);

    public static native int getMmapWindowMappedLimit();

    public static native void setMmapWindowMappedLimit(int size);

    public static native String getSearchPath(int level);

    public static native void setSearchPath(int level, String path);

    /*
     * Reference operations
     */

    public static native String[] referenceList(Repository repository);

    public static native Reference referenceLookup(Repository repository, String refName);

    public static native DirectReference referenceResolve(Repository repository, String refName);

    /*
     * Repository operations
     */

    public static native Repository repositoryClone(String sourceUrl, String path);

    public static native void repositoryFree(Repository repository);

    public static native Reference repositoryHead(Repository repository);

    public static native Repository repositoryInit(String path, boolean bare);

    public static native void repositoryOpen(Repository repository, String path);
}
