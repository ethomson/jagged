package org.libgit2.jagged;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Mode
{
    /** A normal file */
    FILE(0100644),

    /** A file with the executable bit set */
    EXECUTABLE_FILE(0100755),

    /** A directory/tree */
    TREE(0040000);

    private final int value;

    private static final Map<Integer, Mode> modes = new HashMap<Integer, Mode>();

    static
    {
        for (Mode mode : EnumSet.allOf(Mode.class))
        {
            modes.put(mode.getValue(), mode);
        }
    }

    private Mode(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static Mode valueOf(int value)
    {
        Mode mode = modes.get(value);

        if (mode == null)
        {
            throw new IllegalArgumentException("Invalid mode");
        }

        return mode;
    }
}
