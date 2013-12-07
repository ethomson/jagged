package org.libgit2.jagged.core;

/**
 * This is passed as the first argument to the callback to allow the user to see
 * the progress.
 */
public class TransferProgress
{
    private int total_objects;
    private int indexed_objects;
    private int received_objects;
    private int received_bytes;

    public TransferProgress(int total_objects, int indexed_objects, int received_objects, int received_bytes)
    {
        super();
        this.total_objects = total_objects;
        this.indexed_objects = indexed_objects;
        this.received_objects = received_objects;
        this.received_bytes = received_bytes;
    }

    public int getTotal_objects()
    {
        return total_objects;
    }

    public int getIndexed_objects()
    {
        return indexed_objects;
    }

    public int getReceived_objects()
    {
        return received_objects;
    }

    public int getReceived_bytes()
    {
        return received_bytes;
    }

}
