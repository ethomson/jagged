package org.libgit2.jagged.core;

import java.text.MessageFormat;

/**
 * Static utility methods for checking method inputs and outputs for valid
 * states. Unlike assertions (the language keyword) these keywords remain in
 * production.
 */
public final class Ensure
{
    private Ensure()
    {
    }

    /**
     * Throws {@link NullPointerException} if the given object is null.
     * 
     * @param obj
     *        the object to check for null
     * @param name
     *        the name of the variable to check
     */
    public static final void argumentNotNull(final Object obj, final String name)
    {
        if (obj == null)
        {
            throw new NullPointerException(MessageFormat.format("{0} may not be null", name));
        }
    }

    /**
     * Throws {@link NullPointerException} if the given object is null or
     * {@link IllegalArgumentException} if the given object is of zero length.
     * 
     * @param obj
     *        the object to check for null or empty
     * @param name
     *        the name of the variable to check
     */
    public static final void argumentNotNullOrEmpty(final String str, final String name)
    {
        if (str == null)
        {
            throw new NullPointerException(MessageFormat.format("{0} may not be null", name));
        }

        if (str.trim().length() == 0)
        {
            throw new IllegalArgumentException(MessageFormat.format("{0} may not be empty", name));
        }
    }

    /**
     * Throws {@link IllegalArgumentException} if the given object is less than
     * zero.
     * 
     * @param value
     *        the value to check for negativity
     * @param name
     *        the name of the variable to check
     */
    public static final void argumentNotNegative(final int value, final String name)
    {
        if (value < 0)
        {
            throw new IllegalArgumentException(MessageFormat.format("{0} may not be negative", name));
        }
    }

    /**
     * Throws {@link IllegalArgumentException} if the given object is less than
     * zero.
     * 
     * @param value
     *        the value to check for negativity
     * @param name
     *        the name of the variable to check
     */
    public static final void argumentNotNegative(final long value, final String name)
    {
        if (value < 0)
        {
            throw new IllegalArgumentException(MessageFormat.format("{0} may not be negative", name));
        }
    }

    public static final void nativeNotNull(Object obj)
    {
        if (obj == null)
        {
            throw new GitException("An unknown error occurred");
        }
    }

    public static final void nativeNotNull(NativeHandle handle)
    {
        if (handle.get() == 0)
        {
            throw new GitException("An unknown error occurred");
        }
    }

    /**
     * Ensures that the {@code int} result from a native libgit2 call is a zero
     * result.
     * 
     * @param result
     *        the result from a libgit2 call
     */
    public static final void nativeZeroResult(int result)
    {
        if (result == 0)
        {
            return;
        }

        nativeError(result);
    }

    /**
     * Ensures that the {@code int} result from a native libgit2 call is a
     * boolean result (0 or 1).
     * 
     * @param result
     *        the result from a libgit2 call
     */
    public static final void nativeBooleanResult(int result)
    {
        if (result == 0 || result == 1)
        {
            return;
        }

        nativeError(result);
    }

    /**
     * Ensures that the {@code int} result from a native libgit2 call is a
     * positive result (greater than 0).
     * 
     * @param result
     *        the result from a libgit2 call
     */
    public static final void nativePositiveResult(int result)
    {
        if (result >= 0)
        {
            return;
        }

        nativeError(result);
    }

    private static final void nativeError(int result)
    {
        GitError error = NativeMethods.globalErrorLast();

        if (error == null)
        {
            throw new GitException("No error message was provided by the native library.");
        }

        /* TODO: more types here */
        throw new GitException(error.getMessage());
    }
}
