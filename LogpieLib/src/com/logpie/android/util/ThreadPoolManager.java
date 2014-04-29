package com.logpie.android.util;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;

import com.logpie.android.exception.ThreadException;

public class ThreadPoolManager implements Executor
{
    private static final String TAG = ThreadPoolManager.class.getName();
    private static final int sMaxSize = 15;
    private final Queue<Runnable> mThreadPool;
    private Runnable mActive;

    public ThreadPoolManager()
    {
        mThreadPool = new ArrayBlockingQueue<Runnable>(sMaxSize);
    }

    public void safeExecute(final Runnable command) throws ThreadException
    {
        if (isFull())
        {
            LogpieLog.e(TAG, "The thread pool currently is full.");
            throw new ThreadException("The thread pool currently is full.");
        }
        execute(command);
    }

    @Override
    public void execute(final Runnable command)
    {

        mThreadPool.offer(command);
        scheduleNext();
        /*
         * if (active == null) { scheduleNext(); }
         */
    }

    private void scheduleNext()
    {
        while ((mActive = mThreadPool.poll()) != null)
        {
            Thread thread = new Thread(mActive);
            thread.start();
        }
    }

    public boolean isFull()
    {
        return mThreadPool.size() == sMaxSize;
    }

    public boolean isEmpty()
    {
        return mThreadPool.size() == 0;
    }

}
