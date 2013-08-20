package org.libgit2.jagged.core;

import java.util.Date;

/** An action signature (e.g. for committers, taggers, etc) */
public class Signature
{

    /** full name of the author */
    private String name;
    /** email of the author */
    private String email;
    /** time when the action happened */
    private Date when;

    public Signature(String name, String email, Date when)
    {
        super();
        this.name = name;
        this.email = email;
        this.when = when;
    }

    public String getName()
    {
        return name;
    }

    public String getEmail()
    {
        return email;
    }

    public Date getWhen()
    {
        return when;
    }

}
