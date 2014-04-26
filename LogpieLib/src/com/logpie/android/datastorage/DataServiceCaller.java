package com.logpie.android.datastorage;

import java.util.concurrent.CountDownLatch;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.logpie.android.util.LogpieLog;

public class DataServiceCaller
{
    private static final String TAG = DataServiceCaller.class.getName();
    // max try times to bind to service;
    private static final int sMaxTryTimes = 3;

    private final Context mContext;
    private ServiceConnection mConnection;
    private DataPlatform mDataPlatform;
    private Intent mIntent;

    private CountDownLatch mLatch = new CountDownLatch(1);

    public DataServiceCaller(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    /**
     * get the CentralDataService Object
     */
    public DataPlatform syncGetDataPlatform()
    {
        Runnable bindRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                bindToService();
            }
        };

        try
        {
            mLatch.await();
        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException when waiting to bind data service");
            return mDataPlatform;
        }

        if (mDataPlatform == null)
            LogpieLog.e(TAG, "Error happens when binding data service, returning null");
        return mDataPlatform;

    }

    private boolean bindToService()
    {
        // TODO: refactor this to intentFactory;
        mIntent = new Intent(mContext, CentralDataService.class);
        boolean bindSuccess = false;
        buildConnection();

        int countTry = 0;
        try
        {
            while (!bindSuccess && countTry < sMaxTryTimes)
            {
                bindSuccess = mContext.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
                countTry++;
            }
        } catch (SecurityException e)
        {
            mLatch.countDown();
            LogpieLog.e(TAG, "SecurityExcpetion when try to bind service, please check permission");
            LogpieLog.e(TAG, e.getMessage());
            return false;
        }

        if (!bindSuccess)
        {
            LogpieLog.e(TAG, "error happend when trying to bind CentralDataService!");
            return false;
        }

        return true;

    }

    private void buildConnection()
    {
        mConnection = new ServiceConnection()
        {
            @Override
            public void onServiceConnected(ComponentName className, IBinder service)
            {
                LogpieLog.d(TAG, "onServiceConnected");
                LogpieLog.d(TAG, "ComponentName" + className.toString());
                mDataPlatform = (DataPlatform) service;
                mLatch.countDown();
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0)
            {
                LogpieLog.d(TAG, "onServiceDisconnected");
                mDataPlatform = null;
            }
        };

    }
}
