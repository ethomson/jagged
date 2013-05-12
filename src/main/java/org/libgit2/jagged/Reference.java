package org.libgit2.jagged;

import org.libgit2.jagged.core.NativeMethods;

/**
 * A Reference to another git object.
 */
public interface Reference
{
    /**
     * Resolves ("peels") a reference by following the targets of symbolic
     * references until a {@link DirectReference} is found, returning that. The
     * resultant Reference must be {@link #dispose()}d.
     * 
     * @return the resolved {@link DirectReference}
     */
    public abstract DirectReference resolve();
    
    /**
     * A DirectReference is a {@link Reference} that points immediately to an
     * OID.
     */
    public static class DirectReference
	    implements Reference
	{
		private final Oid oid;
		
	    private DirectReference(Oid oid)
	    {
	    	this.oid = oid;
	    }
	    
	    public Oid getTarget()
	    {
	    	return oid;
	    }
	
	    public final DirectReference resolve()
	    {
	        return this;
	    }
	}
    
    /**
     * A SymbolicReference is a {@link Reference} to another reference.
     */
    public class SymbolicReference
        implements Reference
    {
    	private final String target;
    	
    	private SymbolicReference(final String target)
        {
    		this.target = target;
        }
    	
    	public String getTarget()
    	{
    		return target;
    	}

        public final DirectReference resolve()
        {
        	return NativeMethods.referenceResolve(target);
        }
    }
}
