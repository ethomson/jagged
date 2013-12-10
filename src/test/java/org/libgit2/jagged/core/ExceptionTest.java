package org.libgit2.jagged.core;

import junit.framework.Assert;

import org.junit.Test;

public class ExceptionTest
{
    private void testThrows(Runnable runnable, Class<? extends Throwable> expectedClass, String expectedMessage)
    {
        Throwable actual = null;

        try
        {
            runnable.run();
        }
        catch (Throwable e)
        {
            actual = e;
        }

        Assert.assertNotNull(actual);
        Assert.assertEquals(expectedClass, actual.getClass());
        Assert.assertEquals(expectedMessage, actual.getMessage());
    }

    @Test
    public void testCanThrowOutOfMemory()
    {
        testThrows(new Runnable()
        {
            public void run()
            {
                NativeTestMethods.throwOutOfMemory();
            }
        }, OutOfMemoryError.class, "Native memory space");
    }

    @Test
    public void testCanThrowConstantString()
    {
        testThrows(new Runnable()
        {
            public void run()
            {
                NativeTestMethods.throwConstantString();
            }
        }, GitException.class, "This is an exception");
    }

    @Test
    public void testCanThrowFormattedString1()
    {
        testThrows(new Runnable()
        {
            public void run()
            {
                NativeTestMethods.throwFormattedString1();
            }
        }, GitException.class, "This is a formatted exception string, using printf-like syntax");
    }

    @Test
    public void testCanThrowFormattedString2()
    {
        testThrows(new Runnable()
        {
            public void run()
            {
                NativeTestMethods.throwFormattedString2();
            }
        }, GitException.class, "7 Another   7 formatted 007 exception 5.100 message");
    }

    @Test
    public void testCanThrowFormattedString3()
    {
        testThrows(new Runnable()
        {
            public void run()
            {
                NativeTestMethods.throwFormattedString3();
            }
        }, GitException.class, "This is a formatted exception string, using printf-like syntax");
    }
}
