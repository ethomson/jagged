package org.libgit2.jagged;

import org.libgit2.jagged.core.Lazy;
import org.libgit2.jagged.core.NativeMethods;

public class GlobalSettings {
	/* Prevent instantiation */
	private GlobalSettings()
	{
	}
	

    private static final Lazy<BuiltInFeatures> features = new Lazy<BuiltInFeatures>()
    {
        @Override
        protected BuiltInFeatures call()
        {
            return new BuiltInFeatures(NativeMethods.globalGetFeatures());
        }
    };

    /**
     * Gets the set of features supported by this instance of libgit2.
     * 
     * @return The features
     */
    public static BuiltInFeatures getFeatures()
    {
        return features.getValue();
    }
}
