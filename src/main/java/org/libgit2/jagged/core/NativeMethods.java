package org.libgit2.jagged.core;

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

    private static native void referenceFree(final long reference);

    public static void referenceFree(final NativeHandle reference)
    {
        NativeMethods.referenceFree(reference.dispose());
    }

    private static native int referenceResolve(final NativeHandle reference_out, final long reference);

    public static NativeHandle referenceResolve(NativeHandle reference)
    {
        final NativeHandle referenceHandle = new NativeHandle();

        int res = NativeMethods.referenceResolve(referenceHandle, reference.get());
        Ensure.nativeZeroResult(res);

        return referenceHandle;
    }

    private static native int referenceType(long reference);

    public static ReferenceType referenceType(final NativeHandle reference)
    {
        int res = NativeMethods.referenceType(reference.get());
        Ensure.nativePositiveResult(res);

        return ReferenceType.getReferenceType(res);
    }

    /*
     * Repository operations
     */

    private static native int repositoryClone(NativeHandle repository_out, String sourceUrl, String path);

    public static NativeHandle repositoryClone(final String sourceUrl, final String path)
    {
        final NativeHandle repositoryHandle = new NativeHandle();

        int res = NativeMethods.repositoryClone(repositoryHandle, sourceUrl, path);
        Ensure.nativeZeroResult(res);

        return repositoryHandle;
    }

    private static native void repositoryFree(long repository);

    public static void repositoryFree(final NativeHandle repository)
    {
        repositoryFree(repository.dispose());
    }

    private static native int repositoryHead(NativeHandle reference_out, long repository);

    public static NativeHandle repositoryHead(final NativeHandle repository)
    {
        final NativeHandle referenceHandle = new NativeHandle();

        int res = repositoryHead(referenceHandle, repository.get());
        Ensure.nativeZeroResult(res);

        return referenceHandle;
    }

    private static native int repositoryInit(NativeHandle repository_out, String path, boolean bare);

    public static NativeHandle repositoryInit(final String path, final boolean bare)
    {
        final NativeHandle repositoryHandle = new NativeHandle();

        int res = NativeMethods.repositoryInit(repositoryHandle, path, bare);
        Ensure.nativeZeroResult(res);

        return repositoryHandle;
    }

    private static native int repositoryIsBare(long repository);

    public static boolean repositoryIsBare(final NativeHandle repository)
    {
        int res = NativeMethods.repositoryIsBare(repository.get());
        Ensure.nativeBooleanResult(res);

        return (res == 1);
    }

    private static native int repositoryOpen(final NativeHandle repository_out, final String path);

    public static NativeHandle repositoryOpen(String path)
    {
        final NativeHandle repositoryHandle = new NativeHandle();

        int res = NativeMethods.repositoryOpen(repositoryHandle, path);
        Ensure.nativeZeroResult(res);

        return repositoryHandle;
    }
}
