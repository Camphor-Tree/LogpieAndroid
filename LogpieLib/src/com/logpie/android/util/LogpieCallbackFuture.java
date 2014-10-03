package com.logpie.android.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import android.os.Bundle;

/**
 * This CallbackFuture class is used to help handle both async and sync result.
 * You can call logpieCallbackFuture.get() to blocking wait the result.
 * 
 * Note1: you should never call get() on MainThread since it will cause the
 * application ANR.
 * 
 * Note2: In this implementation, if you just rely on async result, then we will
 * just use this as a normal callback, when async task is done, we will just
 * call the callback.onSuccess() But if you are calling get() or get(time,
 * timeunit), which means you can get a sync result, we will also call the
 * callback.onSuccess(); So, basically don't handle the result based on both
 * return value and callback.
 * 
 * @author yilei
 * 
 */
public class LogpieCallbackFuture implements LogpieCallback, Future<Bundle>
{
    private static final int DEFAULT_LATCH_COUNT_DOWN_NUMBER = 1;

    private LogpieCallback mCallback;
    private CountDownLatch mLatch;

    // store the result
    private Bundle mResult;
    // indicate whether is canceled
    private AtomicBoolean mIsCanceled;
    // indicate whether the whole task is finished.
    private AtomicBoolean mIsFinished;

    public LogpieCallbackFuture()
    {
        this(null);
    }

    public LogpieCallbackFuture(LogpieCallback callback)
    {
        // If callback is null, we will create an empty callback. You still can
        // call CallbackFuture.get() to get bundle result
        if (callback == null)
        {
            callback = new LogpieEmptyCallback();
        }
        mCallback = callback;
        mLatch = new CountDownLatch(DEFAULT_LATCH_COUNT_DOWN_NUMBER);
        mIsCanceled = new AtomicBoolean(false);
        mIsFinished = new AtomicBoolean(false);
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning)
    {
        mLatch.countDown();
        mIsCanceled.set(true);
        return true;
    }

    @Override
    public boolean isCancelled()
    {
        return mIsCanceled.get();
    }

    @Override
    public boolean isDone()
    {
        return mIsFinished.get();
    }

    @Override
    public Bundle get() throws InterruptedException, ExecutionException
    {
        ThreadHelper.throwIfMainThread();
        mLatch.await();
        return mResult;
    }

    @Override
    public Bundle get(final long timeout, final TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException
    {
        ThreadHelper.throwIfMainThread();
        mLatch.await(timeout, unit);
        return mResult;
    }

    @Override
    public void onSuccess(final Bundle result)
    {
        mIsFinished.set(true);
        mResult = result;
        mCallback.onSuccess(result);
        mLatch.countDown();
    }

    @Override
    public void onError(final Bundle errorMessage)
    {
        mIsFinished.set(true);
        mResult = errorMessage;
        mCallback.onError(errorMessage);
        mLatch.countDown();
    }

}
