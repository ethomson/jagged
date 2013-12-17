package org.libgit2.jagged;

import org.libgit2.jagged.core.HashCode;

public class TreeEntry
{
    private final String name;
    private final ObjectId id;
    private final ObjectType type;
    private final Mode mode;

    private TreeEntry(String name, ObjectId id, int type, int mode)
    {
        this.name = name;
        this.id = id;
        this.type = ObjectType.getType(type);
        this.mode = Mode.getMode(mode);
    }

    public String getName()
    {
        return name;
    }

    public ObjectId getId()
    {
        return id;
    }

    public ObjectType getType()
    {
        return type;
    }

    public Mode getMode()
    {
        return mode;
    }

    @Override
    public int hashCode()
    {
        return HashCode.getHashCode(name, id, type, mode);
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

        TreeEntry other = (TreeEntry) obj;

        return (name.equals(other.name) && id.equals(other.id) && type.equals(other.type) && mode.equals(other.mode));
    }
}
