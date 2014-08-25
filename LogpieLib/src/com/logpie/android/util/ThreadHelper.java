package com.logpie.android.util;

import android.os.Handler;
import android.os.Looper;

import com.logpie.android.exception.ThreadException;

public final class ThreadHelper
{
    private static ThreadPoolManager sThreadPoolManager = new ThreadPoolManager();

    public static boolean isRunningOnMainThread()
    {
        return Looper.getMainLooper() != null && Looper.myLooper() == Looper.getMainLooper();
    }

    public static void throwIfMainThread() throws ThreadException
    {
        if (isRunningOnMainThread())
            throw new ThreadException("Cannot run on main thread.");
    }

    public static void runOnMainThread(Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    public static void runOffMainThread(boolean withLooper, Runnable runnable) throws ThreadException
    {
        if (!isRunningOnMainThread())
        {
            runnable.run();
        }
        else
        {        	
        	sThreadPoolManager.safeExecute(runnable);                  	
        }
    }
}
