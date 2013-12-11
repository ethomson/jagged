package org.libgit2.jagged;

import org.libgit2.jagged.core.Ensure;
import org.libgit2.jagged.core.HashCode;

public class Signature
{
    private final String name;
    private final String email;

    public Signature(String name, String email)
    {
        Ensure.argumentNotNull(name, "name");
        Ensure.argumentNotNull(email, "email");

        this.name = name;
        this.email = email;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    @Override
    public int hashCode()
    {
        return HashCode.getHashCode(name, email);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }

        if (obj == null || getClass() != obj.getClass())
        {
            return false;
        }

        Signature other = (Signature) obj;

        return (name.equals(other.name) && email.equals(other.email));
    }
}
