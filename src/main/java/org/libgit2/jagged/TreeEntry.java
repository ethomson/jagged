package org.libgit2.jagged;

import java.text.MessageFormat;

import org.libgit2.jagged.core.HashCode;

/**
 * A tree entry represents a file or folder in a tree.
 */
public class TreeEntry
{
    private final Repository repository;

    private final String name;
    private final ObjectId id;
    private final ObjectType type;
    private final Mode mode;

    private TreeEntry(Repository repository, String name, ObjectId id, int type, int mode)
    {
        this.repository = repository;
        this.name = name;
        this.id = id;
        this.type = ObjectType.valueOf(type);
        this.mode = Mode.valueOf(mode);
    }

    /**
     * The name of a tree entry is where the entry will be checked out.
     * 
     * @return The name of the entry.
     */
    public String getName()
    {
        return name;
    }

    /**
     * The object in this tree.
     * 
     * @return The object ID of the entry.
     */
    public ObjectId getId()
    {
        return id;
    }

    /**
     * The type of tree entry.
     * 
     * @return The tree entry type
     */
    public ObjectType getType()
    {
        return type;
    }

    /**
     * The mode of the tree entry
     * 
     * @return The file mode
     */
    public Mode getMode()
    {
        return mode;
    }

    @SuppressWarnings("unchecked")
    public <T extends GitObject> T realize()
    {
        switch (type)
        {
            case COMMIT:
                return (T) new Commit(repository, id);
            case TREE:
                return (T) new Tree(repository, id);
            case BLOB:
                return (T) new Blob(repository, id);
            default:
                throw new IllegalStateException(MessageFormat.format("Unknown git object type: {0}", type));
        }
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
