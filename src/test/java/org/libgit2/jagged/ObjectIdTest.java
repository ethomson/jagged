package org.libgit2.jagged;

import org.junit.Assert;
import org.junit.Test;

public class ObjectIdTest
{
    private static final String sha = "ce08fe4884650f067bd5703b6a59a8b3b3c99a09";

    private static final byte[] bytes = new byte[]
    {
        // @formatter:off
        (byte) 206, (byte) 8,   (byte) 254, (byte) 72,  (byte) 132,
        (byte) 101, (byte) 15,  (byte) 6,   (byte) 123, (byte) 213,
        (byte) 112, (byte) 59,  (byte) 106, (byte) 89,  (byte) 168,
        (byte) 179, (byte) 179, (byte) 201, (byte) 154, (byte) 9
        // @formatter:on
        };

    @Test
    public void testObjectIdFromString1()
    {
        Assert.assertArrayEquals(new byte[]
        {
            0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        }, new ObjectId("0000000000000000000000000000000000000000").getOid());
    }

    @Test
    public void testObjectIdFromString2()
    {
        Assert.assertArrayEquals(new byte[]
        {
            // @formatter:off
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255,
            (byte) 255, (byte) 255, (byte) 255, (byte) 255, (byte) 255
            // @formatter:on
            },
            new ObjectId("ffffffffffffffffffffffffffffffffffffffff").getOid());
    }

    @Test
    public void testInvalidObjectId()
    {
        String[] invalidShas = new String[]
        {
            null, "abcdef", "a0123456789012345678901234567890123456789", "!123456789012345678901234567890123456789"
        };

        for (String sha : invalidShas)
        {
            boolean caught = false;

            try
            {
                new ObjectId(sha);
            }
            catch (Exception e)
            {
                caught = true;
            }

            Assert.assertTrue(caught);
        }
    }

    @Test
    public void testConvertObjectIdToString()
    {
        ObjectId oid = new ObjectId(bytes);
        Assert.assertEquals(sha, oid.toString());
    }

    @Test
    public void testConvertStringToObjectId()
    {
        ObjectId oid = new ObjectId(sha);
        Assert.assertArrayEquals(bytes, oid.getOid());
    }

    @Test
    public void testEqual()
    {
        ObjectId one = new ObjectId(sha);
        ObjectId two = new ObjectId(bytes);

        Assert.assertTrue(one.equals(two));
        Assert.assertTrue(two.equals(one));
    }

    @Test
    public void testNotEqual()
    {
        ObjectId one = new ObjectId(sha);
        ObjectId two = new ObjectId("0000000000000000000000000000000000000000");

        Assert.assertFalse(one.equals(two));
        Assert.assertFalse(two.equals(one));
    }

    @Test
    public void testHashCodeEqual()
    {
        ObjectId one = new ObjectId(sha);
        ObjectId two = new ObjectId(bytes);

        Assert.assertTrue(one.hashCode() == two.hashCode());
    }

    @Test
    public void testHashCodeNotEqual()
    {
        ObjectId one = new ObjectId(sha);
        ObjectId two = new ObjectId("0000000000000000000000000000000000000000");

        Assert.assertFalse(one.hashCode() == two.hashCode());
    }
}
