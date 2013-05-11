package org.libgit2.jagged;

import org.libgit2.jagged.core.GitException;
import org.libgit2.jagged.core.NativeHandle;
import org.libgit2.jagged.core.NativeMethods;
import org.libgit2.jagged.core.ReferenceType;

/**
 * A Reference to another git object.
 */
public abstract class Reference
{
    private final NativeHandle handle;

    protected static Reference newFromHandle(NativeHandle handle)
    {
        ReferenceType type = NativeMethods.referenceType(handle);

        if (type == ReferenceType.OID)
        {
            return new DirectReference(handle);
        }
        else if (type == ReferenceType.SYMBOLIC)
        {
            return new SymbolicReference(handle);
        }
        else
        {
            throw new GitException("Unknown reference type");
        }
    }

    protected Reference(NativeHandle handle)
    {
        assert (handle != null);

        this.handle = handle;
    }

    protected NativeHandle getHandle()
    {
        return handle;
    }

    /**
     * Resolves ("peels") a reference by following the targets of symbolic
     * references until a {@link DirectReference} is found, returning that. The
     * resultant Reference must be {@link #dispose()}d.
     * 
     * @return the resolved {@link DirectReference}
     */
    public abstract DirectReference resolve();

    /**
     * Disposes the underlying Reference object.
     */
    public final void dispose()
    {
        NativeMethods.referenceFree(handle);
    }

    /**
     * A DirectReference is a {@link Reference} that points immediately to an
     * OID.
     */
    public static class DirectReference
        extends Reference
    {
        DirectReference(NativeHandle handle)
        {
            super(handle);
        }

        @Override
        public final DirectReference resolve()
        {
            return this;
        }
    }

    /**
     * A SymbolicReference is a {@link Reference} to another reference.
     */
    public static class SymbolicReference
        extends Reference
    {
        SymbolicReference(NativeHandle handle)
        {
            super(handle);
        }

        @Override
        public final DirectReference resolve()
        {
            Reference resolved = Reference.newFromHandle(NativeMethods.referenceResolve(getHandle()));
            return (DirectReference) resolved;
        }
    }
}
