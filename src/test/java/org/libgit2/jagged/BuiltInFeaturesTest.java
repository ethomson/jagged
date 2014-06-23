package org.libgit2.jagged;

import junit.framework.Assert;

import org.junit.Test;

public class BuiltInFeaturesTest
{
    @Test
    public void testCanGetFeatures()
    {
        BuiltInFeatures features = GlobalSettings.getFeatures();

        Assert.assertTrue(features.contains(BuiltInFeatures.HTTPS));
        Assert.assertTrue(features.contains(BuiltInFeatures.THREADS));
        Assert.assertTrue(features.contains(BuiltInFeatures.combine(BuiltInFeatures.HTTPS, BuiltInFeatures.THREADS)));
    }
}
