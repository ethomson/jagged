package org.libgit2.jagged.core;

import java.io.File;

import org.libgit2.jagged.core.Platform.OperatingSystem;

/**
 * A wrapper around the system library loader to provide improved semantics for
 * installations containing libraries for multiple platforms.
 */
public class NativeLoader
{
    /**
     * Loads the given shared library, looking in the platform-specific library
     * directory relative to the current working directory.
     * 
     * @param libraryName
     *        the name of the library to load (must not be {@code null})
     */
    public static void load(final String libraryName)
    {
    	String nativeLibraryPath = System.getProperty("org.libgit2.jagged.nativeLibraryPath");

    	/* Provide a mediocre default when the system property is unset. */
    	if (nativeLibraryPath == null)
    	{
    		nativeLibraryPath = "native";
    	}

        final File operatingSystemPath =
            new File(nativeLibraryPath, Platform.getCurrentPlatform().getOperatingSystem().getOsgiName());

        final File architecturePath =
            new File(operatingSystemPath, Platform.getCurrentPlatform().getArchitecture().getName());

        /*
         * There's indecision as to whether the file extension of a JNI library
         * on Mac OS should be ".jnilib" (Java 6 and prior) or ".dylib" (Java
         * 7). So we need to hardcode this for compatibility.
         */
        final String mappedLibraryName;

        if (Platform.getCurrentPlatform().getOperatingSystem() == OperatingSystem.MAC_OS_X)
        {
            mappedLibraryName = "lib" + libraryName + ".dylib";
        }
        else
        {
            mappedLibraryName = System.mapLibraryName(libraryName);
        }

        File libraryPath = new File(architecturePath, mappedLibraryName);

        /*
         * Fall back to the operating system path (without architecture) as some
         * platforms can support fat binaries,
         */
        if (!libraryPath.exists())
        {
            libraryPath = new File(operatingSystemPath, mappedLibraryName);
        }

        System.load(libraryPath.getAbsolutePath());
    }
}