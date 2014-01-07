package org.libgit2.jagged.core;

import java.io.InputStream;

import org.libgit2.jagged.Blob;
import org.libgit2.jagged.Commit;
import org.libgit2.jagged.GitObject;
import org.libgit2.jagged.ObjectId;
import org.libgit2.jagged.Options;
import org.libgit2.jagged.Reference;
import org.libgit2.jagged.Reference.DirectReference;
import org.libgit2.jagged.Repository;
import org.libgit2.jagged.Tree;
import org.libgit2.jagged.TreeEntry;
import org.libgit2.jagged.Version;

public class NativeMethods
{
    private static final Object finalizer;

    static
    {
        NativeLoader.load("git2");
        NativeLoader.load("jagged");

        globalThreadsInit();

        finalizer = new Object()
        {
            @Override
            public void finalize()
            {
                globalThreadsShutdown();
            }
        };
    }

    /*
     * Global state
     */

    public static native GitError globalErrorLast();

    public static native void globalThreadsInit();

    public static native void globalThreadsShutdown();

    public static native int globalGetCapabilities();

    public static native Version globalGetLibGit2Version();

    /*
     * Objects
     */

    public static native <T extends GitObject> T objectLookup(Repository repository, ObjectId oid, int type);

    public static native void blobFilteredBufFree(long bufHandle);

    public static native void blobFree(long blobHandle);

    public static native InputStream blobGetRawContentStream(Repository repository, Blob blob);

    public static native void blobCloseFilteredContentStream(BlobFilteredContentStream blobContentStream);

    public static native InputStream blobGetFilteredContentStream(Repository repository, Blob blob, String hintPath);

    public static native BlobMetadata blobGetMetadata(Repository repository, Blob blob);

    public static native CommitMetadata commitGetMetadata(Repository repository, Commit commit);

    public static native Commit[] commitGetParents(Repository repository, Commit commit);

    public static native Tree commitGetTree(Repository repository, Commit commit);

    public static native TreeEntry treeGetEntryByIndex(Repository repository, Tree tree, long entryIndex);

    public static native TreeEntry treeGetEntryByName(Repository repository, Tree tree, String name);

    public static native long treeGetEntryCount(Repository repository, Tree tree);

    /*
     * Options
     */

    public static native void optionSetMmapWindowSize(long size);

    public static native long optionGetMmapWindowSize();

    public static native void optionSetMmapWindowMappedLimit(long limit);

    public static native long optionGetMmapWindowMappedLimit();

    public static native void optionSetSearchPath(int level, String path);

    public static native String optionGetSearchPath(int level);

    public static native void optionSetEnableCaching(boolean enabled);

    public static native void optionSetCacheObjectLimit(int type, long size);

    public static native void optionSetCacheMaxSize(long max);

    public static native Options.CacheStatistics optionGetCachedStatistics();

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
