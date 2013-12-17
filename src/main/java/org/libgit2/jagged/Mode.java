package org.libgit2.jagged;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum Mode
{
    INVALID(0),

    /* A normal file */
    FILE(0100644),

    /* A file with the executable bit set */
    EXECUTABLE_FILE(0100755);

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

    int getValue()
    {
        return value;
    }

    static Mode getMode(int value)
    {
        Mode mode = modes.get(value);
        return mode != null ? mode : INVALID;
    }
}
