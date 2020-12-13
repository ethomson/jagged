package org.libgit2.jagged.core;

import org.libgit2.jagged.Signature;

public class NativeTestMethods
{
    static
    {
        NativeLoader.load("jagged_test");
    }

    /*
     * Exceptions
     */

    public static native void throwOutOfMemory();

    public static native void throwConstantString();

    public static native void throwFormattedString1();

    public static native void throwFormattedString2();

    public static native void throwFormattedString3();

    public static native void createTestRepository(String path, Signature signature);
}
