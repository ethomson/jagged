package org.libgit2.jagged;

import junit.framework.Assert;

import org.junit.Test;

public class CapabilitiesTest
{
    @Test
    public void testCanGetCapabilities()
    {
        Capabilities caps = Capabilities.getInstance();

        Assert.assertTrue(caps.contains(Capabilities.HTTPS));
        Assert.assertTrue(caps.contains(Capabilities.THREADS));
        Assert.assertTrue(caps.contains(Capabilities.combine(Capabilities.HTTPS, Capabilities.THREADS)));
    }
}
