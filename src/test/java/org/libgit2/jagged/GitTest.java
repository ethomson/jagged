package org.libgit2.jagged;

import java.io.File;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.libgit2.jagged.core.NativeTestMethods;

public abstract class GitTest
{
    public static String FIRST_COMMIT_ID = "de6e275694ca4b8850f380650cf6e4e26169d15f";
    public static String SECOND_COMMIT_ID = "3dcee60faede7e1acf6058345a29748ae31f74bc";
    public static String AUTHOR_NAME = "T. E. Ster";
    public static String AUTHOR_EMAIL = "tester@domain.com";

    private static File tempRoot;
    private static File tempDir;
    private static File tempConfigurationDir;

    static
    {
        try
        {
            File systemTempDir;

            if (System.getenv("TMPDIR") != null)
            {
                systemTempDir = new File(System.getenv("TMPDIR"));
            }
            else if (System.getenv("TEMP") != null)
            {
                systemTempDir = new File(System.getenv("TEMP"));
            }
            else if (System.getProperty("java.io.tmpdir") != null)
            {
                systemTempDir = new File(System.getProperty("java.io.tmpdir"));
            }
            else
            {
                throw new RuntimeException(
                    "Unable to determine temporary directory. Please define TMPDIR or TEMP environment variable");
            }

            String classTempDir = "jagged_test_" + getRandomFilename();
            Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
            tempRoot = new File(systemTempDir, classTempDir);

            tempConfigurationDir = new File(tempRoot, "_config");
            tempConfigurationDir.mkdir();
        }
        catch (RuntimeException e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    private static String getRandomFilename()
    {
        return String.format("%010d", ((int) (Math.random() * Integer.MAX_VALUE)));
    }

    @BeforeClass
    public static void setupTempRoot()
    {
        if (tempRoot.exists())
        {
            throw new RuntimeException(
                MessageFormat.format("Test directory {0} already exists", tempRoot.getAbsolutePath()));
        }

        tempRoot.mkdir();
    }

    @AfterClass
    public static void cleanupTempRoot()
    {
        cleanupDirectory(tempRoot);
        tempRoot.delete();
    }

    @Before
    public void setupTempDir()
    {
        String tempConfigurationPath = tempConfigurationDir.getAbsolutePath();
        Options.setSearchPath(ConfigurationLevel.SYSTEM, tempConfigurationPath);
        Options.setSearchPath(ConfigurationLevel.XDG, tempConfigurationPath);
        Options.setSearchPath(ConfigurationLevel.GLOBAL, tempConfigurationPath);

        String instanceTempDir = getRandomFilename();
        tempDir = new File(tempRoot, instanceTempDir);
        tempDir.mkdir();
    }

    @After
    public void cleanupTempDir()
    {
        cleanupDirectory(tempDir);
        tempDir.delete();
    }

    public File getTempDir()
    {
        return tempDir;
    }

    private static void cleanupDirectory(final File file)
    {
        if (file.isDirectory())
        {
            for (File child : file.listFiles())
            {
                if (child.isDirectory())
                {
                    cleanupDirectory(child);
                }

                child.delete();
            }
        }
    }

    public File setupRepository(final String name)
    {
        File path = new File(tempDir, name);
        NativeTestMethods.createTestRepository(path.getAbsolutePath().replace("\\", "/"), new Signature(AUTHOR_NAME, AUTHOR_EMAIL));
        return path;
    }
}
