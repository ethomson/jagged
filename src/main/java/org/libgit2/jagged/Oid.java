package org.libgit2.jagged;

import java.util.Arrays;

public class Oid
{
	private final byte[] oid;
	
	private Oid(byte[] oid)
	{
		this.oid = oid;
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
		
		return Arrays.equals(((Oid)obj).oid, oid);		
	}
}
