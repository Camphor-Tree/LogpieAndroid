package com.logpie.android.connection;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

public class ThreadPoolManager implements Executor
{
    private final int maxSize = 15;
    private final Queue<Runnable> mThreadPool;
    private Runnable active;

    public ThreadPoolManager()
    {
        mThreadPool = new ArrayBlockingQueue<Runnable>(maxSize);
    }

    @Override
    public void execute(final Runnable command)
    {
        // if (isFull())
        // return;

        mThreadPool.offer(command);
        scheduleNext();
        /*
         * if (active == null) { scheduleNext(); }
         */
    }

    public void scheduleNext()
    {
        while ((active = mThreadPool.poll()) != null)
        {
            Thread thread = new Thread(active);
            thread.start();
        }
    }

    public boolean isFull()
    {
        return mThreadPool.size() == maxSize;
    }

    public boolean isEmpty()
    {
        return mThreadPool.size() == 0;
    }

}
