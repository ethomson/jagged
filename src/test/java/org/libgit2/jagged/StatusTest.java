package org.libgit2.jagged;

import org.junit.Before;
import org.junit.Test;
import org.libgit2.jagged.status.Status;
import org.libgit2.jagged.status.StatusOptions;

public class StatusTest extends GitLocalCommandTest
{

    private static String[] ENTRY_PATHS = new String[]{
        "file_deleted",
        "ignored_file",
        "modified_file",
        "new_file",
        "staged_changes",
        "staged_changes_file_deleted",
        "staged_changes_modified_file",
        "staged_delete_file_deleted",
        "staged_delete_modified_file",
        "staged_new_file",
        "staged_new_file_deleted_file",
        "staged_new_file_modified_file",

        "subdir/deleted_file",
        "subdir/modified_file",
        "subdir/new_file",

        "" + (char)36825,
    };

    private static int ENTRY_STATUSES[] = {
        Status.GIT_STATUS_WT_DELETED,
        Status.GIT_STATUS_IGNORED,
        Status.GIT_STATUS_WT_MODIFIED,
        Status.GIT_STATUS_WT_NEW,
        Status.GIT_STATUS_INDEX_MODIFIED,
        Status.GIT_STATUS_INDEX_MODIFIED | Status.GIT_STATUS_WT_DELETED,
        Status.GIT_STATUS_INDEX_MODIFIED | Status.GIT_STATUS_WT_MODIFIED,
        Status.GIT_STATUS_INDEX_DELETED,
        Status.GIT_STATUS_INDEX_DELETED | Status.GIT_STATUS_WT_NEW,
        Status.GIT_STATUS_INDEX_NEW,
        Status.GIT_STATUS_INDEX_NEW | Status.GIT_STATUS_WT_DELETED,
        Status.GIT_STATUS_INDEX_NEW | Status.GIT_STATUS_WT_MODIFIED,

        Status.GIT_STATUS_WT_DELETED,
        Status.GIT_STATUS_WT_MODIFIED,
        Status.GIT_STATUS_WT_NEW,

        Status.GIT_STATUS_WT_NEW,
    };

    @Before
    public void before()
    {
        repoPath = setupRepository("status");
    }

    @Test
    public void testStatuses()
    {
        final StatusOptions options = StatusOptions.createInitializedInstance();
        options.setFlags(options.getFlags()
            | StatusOptions.GIT_STATUS_OPT_INCLUDE_UNTRACKED
            | StatusOptions.GIT_STATUS_OPT_INCLUDE_IGNORED);

        status(options);
        for (int index = 0; index < ENTRY_PATHS.length; index++)
        {
            String entryPath = ENTRY_PATHS[index];
            assertStatus(entryPath, ENTRY_STATUSES[index]);
        }

        assertEmpty();
    }

    @Test
    public void testSingleStatusDetails()
    {
        final StatusOptions options = StatusOptions.createInitializedInstance();
        options.setPathSpecs(new String[]{"current_file"});
        options.setFlags(options.getFlags() | StatusOptions.GIT_STATUS_OPT_INCLUDE_UNMODIFIED);

        status(options);
        assertStatus("current_file", 0,
            "current_file", 0, Status.GIT_DIFF_FLAG_EXISTS | Status.GIT_DIFF_FLAG_VALID_ID, Status.GIT_FILEMODE_BLOB, new ObjectId("a0de7e0ac200c489c41c59dfa910154a70264e6e"),
            "current_file", 13, Status.GIT_DIFF_FLAG_EXISTS | Status.GIT_DIFF_FLAG_VALID_ID, Status.GIT_FILEMODE_BLOB, new ObjectId("a0de7e0ac200c489c41c59dfa910154a70264e6e"),
            "current_file", null, Status.GIT_DIFF_FLAG_EXISTS, Status.GIT_FILEMODE_BLOB, new ObjectId("a0de7e0ac200c489c41c59dfa910154a70264e6e"),
            0, 0, 0, 0, 0, 0);
    }

    @Test
    public void testStatusForMultipleSingleFilesAtOnce()
    {
        final StatusOptions options = StatusOptions.createInitializedInstance();
        options.setFlags(options.getFlags() | StatusOptions.GIT_STATUS_OPT_INCLUDE_IGNORED);
        options.setPathSpecs(new String[]{ENTRY_PATHS[0]});

        status(options);
        assertStatus(ENTRY_PATHS[0], ENTRY_STATUSES[0]);
        assertEmpty();

        options.setPathSpecs(new String[]{ENTRY_PATHS[0], ENTRY_PATHS[1]});

        status(options);
        assertStatus(ENTRY_PATHS[0], ENTRY_STATUSES[0]);
        assertStatus(ENTRY_PATHS[1], ENTRY_STATUSES[1]);
        assertEmpty();

        options.setPathSpecs(new String[]{ENTRY_PATHS[0], ENTRY_PATHS[1], ENTRY_PATHS[13]});

        status(options);
        assertStatus(ENTRY_PATHS[0], ENTRY_STATUSES[0]);
        assertStatus(ENTRY_PATHS[1], ENTRY_STATUSES[1]);
        assertStatus(ENTRY_PATHS[13], ENTRY_STATUSES[13]);
        assertEmpty();
    }
}
