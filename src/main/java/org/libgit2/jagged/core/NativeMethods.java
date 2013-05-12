package org.libgit2.jagged.core;

import org.libgit2.jagged.Reference;
import org.libgit2.jagged.Reference.DirectReference;
import org.libgit2.jagged.Repository;

public class NativeMethods
{
    static
    {
        NativeLoader.load("jagged");
    }

    public static native GitError errorLast();

    /*
     * Reference operations
     */
    
    public static native DirectReference referenceResolve(String refName);

    /*
     * Repository operations
     */
    
    public static native Repository repositoryClone(String sourceUrl, String path);

    public static native void repositoryFree(Repository repository);

    public static native Reference repositoryHead(Repository repository);

    public static native Repository repositoryInit(String path, boolean bare);
    
    public static native void repositoryOpen(Repository repository, String path);
}
