package org.libgit2.jagged;

import org.libgit2.jagged.core.Ensure;
import org.libgit2.jagged.core.HashCode;

/**
 * The signature of the author or committer of a git object.
 */
public class Signature
{
    private final String name;
    private final String email;

    /**
     * Create a signature.
     * 
     * @param name
     *        The full name of the author or committer (not {@code null})
     * @param email
     *        The email address of the author or committer (not {@code null})
     */
    public Signature(String name, String email)
    {
        Ensure.argumentNotNull(name, "name");
        Ensure.argumentNotNull(email, "email");

        this.name = name;
        this.email = email;
    }

    /**
     * Gets the name of the author or committer.
     * 
     * @return The signature's name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the email address of the author or committer.
     * 
     * @return The signature's email address
     */
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
