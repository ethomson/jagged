package org.libgit2.jagged;

import java.util.Arrays;

import org.libgit2.jagged.core.Ensure;

public final class ObjectId
{
    public final static int SIZE = 20;
    public final static int HEX_SIZE = 40;

    private final byte[] oid;
    private final String hex;

    public ObjectId(String hex)
    {
        Ensure.argumentNotNull(hex, "hex");
        Ensure.argument(hex.length() == HEX_SIZE, "hex.length");

        this.oid = buildOid(hex);
        this.hex = hex.toLowerCase();
    }

    public ObjectId(byte[] oid)
    {
        Ensure.argumentNotNull(oid, "oid");
        Ensure.argument(oid.length == SIZE, "oid.length");

        this.oid = oid;
        this.hex = buildHex(oid);
    }

    private static byte[] buildOid(String hex)
    {
        byte[] oid = new byte[SIZE];

        for (int i = 0; i < HEX_SIZE; ++i)
        {
            char c = hex.charAt(i);

            if (c >= '0' && c <= '9')
            {
                c -= '0';
            }
            else if (c >= 'a' && c <= 'f')
            {
                c -= 'a' - 10;
            }
            else if (c >= 'A' && c <= 'F')
            {
                c -= 'A' - 10;
            }
            else
            {
                throw new IllegalArgumentException(String.format("Invalid character in sha1: {0}", c));
            }

            oid[i / 2] |= (i % 2) == 0 ? (byte) (c << 4) : (byte) c;
        }

        return oid;
    }

    private static String buildHex(byte[] oid)
    {
        char[] hex = new char[]
        {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
        };
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < oid.length; i++)
        {
            str.append(hex[(oid[i] & 0xf0) >> 4]);
            str.append(hex[(oid[i] & 0x0f)]);
        }

        return str.toString();
    }

    public byte[] getOid()
    {
        return oid;
    }

    @Override
    public String toString()
    {
        return hex;
    }

    @Override
    public int hashCode()
    {
        return (oid[0] << 24) | (oid[1] << 16) | (oid[2] << 8) | (oid[3] << 0);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || obj.getClass() != getClass())
        {
            return false;
        }

        return Arrays.equals(((ObjectId) obj).oid, oid);
    }
}
