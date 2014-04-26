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
        mThreadPool.offer(new Runnable()
        {
            public void run()
            {
                try
                {
                    command.run();
                } finally
                {
                    scheduleNext();
                }
            }
        });
        if (active == null)
        {
            scheduleNext();
        }
    }

    public void scheduleNext()
    {
        if ((active = mThreadPool.poll()) != null)
            this.execute(active);

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
