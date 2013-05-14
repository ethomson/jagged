package org.libgit2.jagged;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.MessageFormat;

import org.junit.After;
import org.junit.Before;

public abstract class GitTest
{
    private static File resourcesRoot;
    private static File tempRoot;
    private static File tempDir;

    static
    {
    	try
    	{
	        resourcesRoot = new File("src/test/resources");
	        
	        if (!resourcesRoot.exists())
	        {
	        	resourcesRoot = new File(GitTest.class.getResource("/testrepo").getFile()).getParentFile();
	        }
	
	        if (System.getenv("TMPDIR") != null)
	        {
	            tempRoot = new File(System.getenv("TMPDIR"));
	        }
	        else if (System.getenv("TEMP") != null)
	        {
	        	tempRoot = new File(System.getenv("TEMP"));
	        }
	        else
	        {
	            throw new RuntimeException("Unable to determine temporary directory. Please define TMPDIR or TEMP environment variable");
	        }
	
	        String instanceTempDir = "jagged_test_" + Integer.toString((int) (Math.random() * Integer.MAX_VALUE));
	        tempDir = new File(tempRoot, instanceTempDir);
	        
    	}
    	catch (RuntimeException e)
    	{
    		e.printStackTrace();
    		throw e;
    	}
    }

    @Before
    public void setupTempDir()
    {
        if (tempDir.exists())
        {
            throw new RuntimeException(MessageFormat.format(
                "Test directory {0} already exists",
                tempDir.getAbsolutePath()));
        }

        tempDir.mkdir();
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

    @After
    public void cleanupTempDir()
    {
        cleanupDirectory(tempDir);
        tempDir.delete();
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
