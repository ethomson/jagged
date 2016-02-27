package org.libgit2.jagged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class GitTest
{
    private static File resourcesRoot;
    private static File tempRoot;
    private static File tempDir;
    private static File tempConfigurationDir;

    static
    {
        try
        {
            File systemTempDir;

            resourcesRoot = new File("src/test/resources");

            if (!resourcesRoot.exists())
            {
                resourcesRoot = new File(GitTest.class.getResource("/testrepo").getFile()).getParentFile();
            }

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

    private static File copyRecursive(final File source, final File target, final String item)
    {
        final String sourceItem = item;
        final String targetItem = (item.equals(".gitted") ? ".git" : item);

        final File sourceFile = new File(source, sourceItem);
        final File targetFile = new File(target, targetItem);

        if (sourceFile.isDirectory())
        {
            targetFile.mkdir();

            for (String child : sourceFile.list())
            {
                copyRecursive(sourceFile, targetFile, child);
            }

            return targetFile;
        }
        else
        {
            try
            {
                @SuppressWarnings("resource")
                FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
                @SuppressWarnings("resource")
                FileChannel targetChannel = new FileOutputStream(targetFile).getChannel();
                targetChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
                sourceChannel.close();
                targetChannel.close();

                return targetFile;
            }
            catch (Exception e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    public File setupRepository(final String name)
    {
        return copyRecursive(resourcesRoot, tempDir, name);
    }
}
