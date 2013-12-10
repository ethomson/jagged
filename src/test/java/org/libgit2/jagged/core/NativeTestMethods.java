package org.libgit2.jagged.core;

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
}
