package org.libgit2.jagged;

import org.libgit2.jagged.core.Ensure;

public class FilteringOptions
{
    private final String hintPath;

    public FilteringOptions(final String hintPath)
    {
        Ensure.argumentNotNull(hintPath, "hintPath");

        this.hintPath = hintPath;
    }

    public String getHintPath()
    {
        return hintPath;
    }
}
