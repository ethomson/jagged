package org.libgit2.jagged;

import org.libgit2.jagged.core.HashCode;
import org.libgit2.jagged.core.NativeMethods;

/**
 * A Reference to another git object.
 */
public abstract class Reference
{
    /**
     * Namespace prefix indicating that the given reference is a branch (
     * {@code refs/heads/}).
     */
    public static final String PREFIX_BRANCH = "refs/heads/";

    /**
     * Namespace prefix indicating that the given reference is a tag (
     * {@code refs/tags/}).
     */
    public static final String PREFIX_TAG = "refs/tags/";

    /**
     * Namespace prefix indicating that the given reference is a remote-tracking
     * branch ({@code refs/remotes/}).
     */
    public static final String PREFIX_REMOTE_TRACKING = "refs/remotes/";

    private final Repository repository;
    private final String canonicalName;

    private Reference(final Repository repository, final String canonicalName)
    {
        this.repository = repository;
        this.canonicalName = canonicalName;
    }

    public final Repository getRepository()
    {
        return repository;
    }

    /**
     * Gets the full name of this reference.
     * 
     * @return the name of this reference
     */
    public final String getCanonicalName()
    {
        return canonicalName;
    }

    /**
     * Resolves ("peels") a reference by following the targets of symbolic
     * references until a {@link DirectReference} is found, returning that.
     * 
     * @return the resolved {@link DirectReference}
     */
    public abstract DirectReference resolveToDirectReference();

    @Override
    public boolean equals(Object o)
    {
        if (o == this)
        {
            return true;
        }

        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final Reference other = (Reference) o;

        return repository.equals(other.repository) && canonicalName.equals(other.canonicalName);
    }

    /**
     * A DirectReference is a {@link Reference} that points immediately to an
     * OID.
     */
    public static class DirectReference
        extends Reference
    {
        private final ObjectId target;

        private DirectReference(final Repository repository, final String canonicalName, final ObjectId target)
        {
            super(repository, canonicalName);

            this.target = target;
        }

        public ObjectId getTarget()
        {
            return target;
        }

        @Override
        public final DirectReference resolveToDirectReference()
        {
            return this;
        }

        @Override
        public int hashCode()
        {
            return HashCode.getHashCode(getRepository(), getCanonicalName(), target);
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == this)
            {
                return true;
            }

            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            return (super.equals(o) && target.equals(((DirectReference) o).target));
        }
    }

    /**
     * A SymbolicReference is a {@link Reference} to another reference.
     */
    public static class SymbolicReference
        extends Reference
    {
        private final String target;

        private SymbolicReference(final Repository repository, final String canonicalName, final String target)
        {
            super(repository, canonicalName);

            this.target = target;
        }

        public String getTarget()
        {
            return target;
        }

        @Override
        public final DirectReference resolveToDirectReference()
        {
            return NativeMethods.referenceResolve(getRepository(), target);
        }

        @Override
        public int hashCode()
        {
            return HashCode.getHashCode(getRepository(), getCanonicalName(), target);
        }

        @Override
        public boolean equals(Object o)
        {
            if (o == this)
            {
                return true;
            }

            if (o == null || getClass() != o.getClass())
            {
                return false;
            }

            return (super.equals(o) && target.equals(((SymbolicReference) o).target));
        }
    }
}
