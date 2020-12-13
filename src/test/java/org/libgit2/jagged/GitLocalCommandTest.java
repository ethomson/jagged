package org.libgit2.jagged;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.libgit2.jagged.status.Status;
import org.libgit2.jagged.status.StatusOptions;

public abstract class GitLocalCommandTest extends GitTest
{

    private final Map<String, Status> pathToStatus = new HashMap<String, Status>();
    protected File repoPath;

    @Before
    public void before()
    {
        pathToStatus.clear();
    }

    protected final void write(String path, String content) throws IOException
    {
        final File file = new File(repoPath, path);
        file.getParentFile().mkdirs();

        FileWriter fileWriter = new FileWriter(file);
        try
        {
            fileWriter.write(content);
        } finally
        {
            fileWriter.close();
        }
    }
    
    protected final void statusChanged()
    {
        final StatusOptions options = StatusOptions.createInitializedInstance();
        options.setFlags(options.getFlags() | StatusOptions.GIT_STATUS_OPT_RECURSE_UNTRACKED_DIRS | StatusOptions.GIT_STATUS_OPT_INCLUDE_UNTRACKED);
        status(options);
    }

    protected final void status(StatusOptions options)
    {
        pathToStatus.clear();

        Repository repository = new Repository(repoPath.getPath());
        final Status[] status = repository.statusListNew(options, true);
        for (Status s : status)
        {
            String path = null;
            if (s.getWtPath() != null)
            {
                path = s.getWtPath();
            } else if (s.getIndexPath() != null)
            {
                path = s.getIndexPath();
            } else
            {
                Assert.fail("either indexPath or wtPath must be set");
            }
            pathToStatus.put(path, s);
        }
        repository.close();
    }

    protected final void assertStatus(String mainPath, int status)
    {
        final Status s = pathToStatus.remove(mainPath);
        Assert.assertNotNull(s);
        Assert.assertEquals(status, s.getStatus());
    }

    protected final void assertStatus(String mainPath, int status,
                              String headPath, long headSize, int headFlags, int headMode, ObjectId headId,
                              String indexPath, long indexSize, int indexFlags, int indexMode, ObjectId indexId,
                              String wtPath, Long wtSize, int wtFlags, int wtMode, ObjectId wtId,
                              int headIndexFlags, int headIndexSimilarity, int headIndexNFiles,
                              int indexWtFlags, int indexWtSimilarity, int indexWtNFiles)
    {
        final Status s = pathToStatus.remove(mainPath);
        Assert.assertNotNull(s);
        Assert.assertEquals(status, s.getStatus());
        Assert.assertEquals(headPath, s.getHeadPath());
        Assert.assertEquals(headSize, s.getHeadSize());
        Assert.assertEquals(headFlags, s.getHeadFlags());
        Assert.assertEquals(headMode, s.getHeadMode());
        Assert.assertEquals(headId, s.getHeadId());
        Assert.assertEquals(indexPath, s.getIndexPath());
        Assert.assertEquals(indexSize, s.getIndexSize());
        Assert.assertEquals(indexFlags, s.getIndexFlags());
        Assert.assertEquals(indexMode, s.getIndexMode());
        Assert.assertEquals(indexId, s.getIndexId());
        Assert.assertEquals(wtPath, s.getWtPath());
        if (wtSize != null)
        {
            Assert.assertEquals(wtSize.longValue(), s.getWtSize());
        }
        Assert.assertEquals(wtFlags, s.getWtFlags());
        Assert.assertEquals(wtMode, s.getWtMode());
        Assert.assertEquals(wtId, s.getWtId());
        Assert.assertEquals(headIndexFlags, s.getHeadIndexFlags());
        Assert.assertEquals(headIndexSimilarity, s.getHeadIndexSimilarity());
        Assert.assertEquals(headIndexNFiles, s.getHeadIndexNFiles());
        Assert.assertEquals(indexWtFlags, s.getIndexWtFlags());
        Assert.assertEquals(indexWtSimilarity, s.getIndexWtSimilarity());
        Assert.assertEquals(indexWtNFiles, s.getIndexWtNFiles());
    }

    protected final void assertEmpty()
    {
        Assert.assertTrue(pathToStatus.isEmpty());
    }
}
