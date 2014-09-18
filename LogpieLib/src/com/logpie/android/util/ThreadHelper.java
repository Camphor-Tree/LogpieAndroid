package com.logpie.android.util;

import android.os.Handler;
import android.os.Looper;

import com.logpie.android.exception.ThreadException;

public final class ThreadHelper
{
    private static final String TAG = ThreadHelper.class.getName();
    private static ThreadPoolManager sThreadPoolManager = new ThreadPoolManager();

    public static boolean isRunningOnMainThread()
    {
        return Looper.getMainLooper() != null && Looper.myLooper() == Looper.getMainLooper();
    }

    public static void throwIfMainThread()
    {
        if (isRunningOnMainThread())
        {
            LogpieLog.e(TAG, "Error! This method is not allowed to be called on MainThread!");
            throw new IllegalStateException("You cannot call this method on MainThread!");
        }
    }

    public static void runOnMainThread(Runnable runnable)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(runnable);
    }

    public static void runOffMainThread(boolean withLooper, Runnable runnable)
            throws ThreadException
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

    public static void runOnBackgroundThread(boolean withLooper, Runnable runnable)
            throws ThreadException
    {
        sThreadPoolManager.safeExecute(runnable);
    }

}
