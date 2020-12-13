package org.libgit2.jagged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class GitTest
{
    public static String README = "README";
    public static String C1 = "8496071c1b46c854b31185ea97743be6a8774479";
    public static String C1_ROOT = "181037049a54a1eb5fab404658a3a250b44335d7";
    public static String C1_README = "1385f264afb75a56a5bec74243be9b367ba4ca08";

    public static String C2 = "5b5b025afb0b4c913b4c338a42934a3863bf3644";

    public static String C8_ROOT = "45dd856fdd4d89b884c340ba0e047752d9b085d6";
    public static String C8_README = "a8233120f6ad708f843d861ce2b7228ec4e3dec6";

    private static File testRepositoriesRoot;
    private static File tempRoot;
    private static File tempDir;
    private static File tempConfigurationDir;

    static
    {
        try
        {
            File systemTempDir;

            testRepositoriesRoot = new File("src/main/native/libgit2/tests/resources");
            if (!testRepositoriesRoot.exists())
            {
                throw new RuntimeException("Can't find test resources at " + testRepositoriesRoot.getAbsolutePath());
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

    public void write(File file, String content) throws IOException
    {
        FileWriter writer = new FileWriter(file);
        try
        {
            writer.write(content);
        } finally
        {
            writer.close();
        }
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
        return copyRecursive(testRepositoriesRoot, tempDir, name);
    }
}
