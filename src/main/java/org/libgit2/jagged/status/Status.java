package org.libgit2.jagged.status;

import org.libgit2.jagged.ObjectId;

public class Status
{

    public static final int GIT_STATUS_INDEX_NEW = (1 << 0);
    public static final int GIT_STATUS_INDEX_MODIFIED = (1 << 1);
    public static final int GIT_STATUS_INDEX_DELETED = (1 << 2);
    public static final int GIT_STATUS_INDEX_RENAMED = (1 << 3);
    public static final int GIT_STATUS_INDEX_TYPECHANGE = (1 << 4);
    public static final int GIT_STATUS_WT_NEW = (1 << 7);
    public static final int GIT_STATUS_WT_MODIFIED = (1 << 8);
    public static final int GIT_STATUS_WT_DELETED = (1 << 9);
    public static final int GIT_STATUS_WT_TYPECHANGE = (1 << 10);
    public static final int GIT_STATUS_WT_RENAMED = (1 << 11);
    public static final int GIT_STATUS_WT_UNREADABLE = (1 << 12);
    public static final int GIT_STATUS_IGNORED = (1 << 14);
    public static final int GIT_STATUS_CONFLICTED = (1 << 15);

    public static final int GIT_DELTA_UNMODIFIED = 0;
    public static final int GIT_DELTA_ADDED = 1;
    public static final int GIT_DELTA_DELETED = 2;
    public static final int GIT_DELTA_MODIFIED = 3;
    public static final int GIT_DELTA_RENAMED = 4;
    public static final int GIT_DELTA_COPIED = 5;
    public static final int GIT_DELTA_IGNORED = 6;
    public static final int GIT_DELTA_UNTRACKED = 7;
    public static final int GIT_DELTA_TYPECHANGE = 8;
    public static final int GIT_DELTA_UNREADABLE = 9;
    public static final int GIT_DELTA_CONFLICTED = 10;

    public static final int GIT_DIFF_FLAG_BINARY = (1 << 0);
    public static final int GIT_DIFF_FLAG_NOT_BINARY = (1 << 1);
    public static final int GIT_DIFF_FLAG_VALID_ID = (1 << 2);
    public static final int GIT_DIFF_FLAG_EXISTS = (1 << 3);

    public static final int GIT_FILEMODE_UNREADABLE = 0000000;
    public static final int GIT_FILEMODE_TREE = 0040000;
    public static final int GIT_FILEMODE_BLOB = 0100644;
    public static final int GIT_FILEMODE_BLOB_EXECUTABLE = 0100755;
    public static final int GIT_FILEMODE_LINK = 0120000;
    public static final int GIT_FILEMODE_COMMIT = 0160000;

    private final int status;
    private final String headPath;
    private final long headSize;
    private final int headFlags;
    private final int headMode;
    private final ObjectId headId;
    private final String indexPath;
    private final long indexSize;
    private final int indexFlags;
    private final int indexMode;
    private final ObjectId indexId;
    private final String wtPath;
    private final long wtSize;
    private final int wtFlags;
    private final int wtMode;
    private final ObjectId wtId;
    private final int headIndexFlags;
    private final int headIndexSimilarity;
    private final int headIndexNFiles;
    private final int indexWtFlags;
    private final int indexWtSimilarity;
    private final int indexWtNFiles;

    private Status(int status,
                   String headPath, long headSize, int headFlags, int headMode, ObjectId headId,
                   String indexPath, long indexSize, int indexFlags, int indexMode, ObjectId indexId,
                   String wtPath, long wtSize, int wtFlags, int wtMode, ObjectId wtId,
                   int headIndexFlags, int headIndexSimilarity, int headIndexNFiles,
                   int indexWtFlags, int indexWtSimilarity, int indexWtNFiles)
    {
        this.status = status;
        this.headPath = headPath;
        this.headSize = headSize;
        this.headFlags = headFlags;
        this.headMode = headMode;
        this.headId = headId;
        this.indexPath = indexPath;
        this.indexSize = indexSize;
        this.indexFlags = indexFlags;
        this.indexMode = indexMode;
        this.indexId = indexId;
        this.wtPath = wtPath;
        this.wtSize = wtSize;
        this.wtFlags = wtFlags;
        this.wtMode = wtMode;
        this.wtId = wtId;
        this.headIndexFlags = headIndexFlags;
        this.headIndexSimilarity = headIndexSimilarity;
        this.headIndexNFiles = headIndexNFiles;
        this.indexWtFlags = indexWtFlags;
        this.indexWtSimilarity = indexWtSimilarity;
        this.indexWtNFiles = indexWtNFiles;
    }

    public int getStatus()
    {
        return status;
    }

    public String getHeadPath()
    {
        return headPath;
    }

    public long getHeadSize()
    {
        return headSize;
    }

    public int getHeadFlags()
    {
        return headFlags;
    }

    public int getHeadMode()
    {
        return headMode;
    }

    public ObjectId getHeadId()
    {
        return headId;
    }

    public String getIndexPath()
    {
        return indexPath;
    }

    public long getIndexSize()
    {
        return indexSize;
    }

    public int getIndexFlags()
    {
        return indexFlags;
    }

    public int getIndexMode()
    {
        return indexMode;
    }

    public ObjectId getIndexId()
    {
        return indexId;
    }

    public String getWtPath()
    {
        return wtPath;
    }

    public long getWtSize()
    {
        return wtSize;
    }

    public int getWtFlags()
    {
        return wtFlags;
    }

    public int getWtMode()
    {
        return wtMode;
    }

    public ObjectId getWtId()
    {
        return wtId;
    }

    public int getHeadIndexFlags()
    {
        return headIndexFlags;
    }

    public int getHeadIndexSimilarity()
    {
        return headIndexSimilarity;
    }

    public int getHeadIndexNFiles()
    {
        return headIndexNFiles;
    }

    public int getIndexWtFlags()
    {
        return indexWtFlags;
    }

    public int getIndexWtSimilarity()
    {
        return indexWtSimilarity;
    }

    public int getIndexWtNFiles()
    {
        return indexWtNFiles;
    }
}