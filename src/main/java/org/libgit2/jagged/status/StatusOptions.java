package org.libgit2.jagged.status;

import java.util.Arrays;

import org.libgit2.jagged.core.NativeMethods;

public class StatusOptions
{

    public static final int GIT_STATUS_OPT_INCLUDE_UNTRACKED = (1 << 0);
    public static final int GIT_STATUS_OPT_INCLUDE_IGNORED = (1 << 1);
    public static final int GIT_STATUS_OPT_INCLUDE_UNMODIFIED = (1 << 2);
    public static final int GIT_STATUS_OPT_EXCLUDE_SUBMODULES = (1 << 3);
    public static final int GIT_STATUS_OPT_RECURSE_UNTRACKED_DIRS = (1 << 4);
    public static final int GIT_STATUS_OPT_DISABLE_PATHSPEC_MATCH = (1 << 5);
    public static final int GIT_STATUS_OPT_RECURSE_IGNORED_DIRS = (1 << 6);
    public static final int GIT_STATUS_OPT_RENAMES_HEAD_TO_INDEX = (1 << 7);
    public static final int GIT_STATUS_OPT_RENAMES_INDEX_TO_WORKDIR = (1 << 8);
    public static final int GIT_STATUS_OPT_SORT_CASE_SENSITIVELY = (1 << 9);
    public static final int GIT_STATUS_OPT_SORT_CASE_INSENSITIVELY = (1 << 10);
    public static final int GIT_STATUS_OPT_RENAMES_FROM_REWRITES = (1 << 11);
    public static final int GIT_STATUS_OPT_NO_REFRESH = (1 << 12);
    public static final int GIT_STATUS_OPT_UPDATE_INDEX = (1 << 13);
    public static final int GIT_STATUS_OPT_INCLUDE_UNREADABLE = (1 << 14);
    public static final int GIT_STATUS_OPT_INCLUDE_UNREADABLE_AS_UNTRACKED = (1 << 15);

    private StatusShowOptions show;
    private int flags;
    private String[] pathSpecs;

    private StatusOptions(StatusShowOptions show, int flags, String[] pathSpecs)
    {
        this.show = show;
        this.flags = flags;
        this.pathSpecs = pathSpecs;
    }

    public static StatusOptions createInitializedInstance()
    {
        return NativeMethods.statusOptionsInit();
    }

    public StatusShowOptions getShow()
    {
        return show;
    }

    public void setShow(StatusShowOptions show)
    {
        this.show = show;
    }

    public int getFlags()
    {
        return flags;
    }

    public void setFlags(int flags)
    {
        this.flags = flags;
    }

    public String[] getPathSpecs()
    {
        return pathSpecs;
    }

    public void setPathSpecs(String[] pathSpecs)
    {
        this.pathSpecs = pathSpecs;
    }

    @Override
    public String toString()
    {
        return "show=" + show + ";flags=" + flags + ";pathSpecs=" + (pathSpecs != null ? Arrays.asList(pathSpecs) : null);
    }
}