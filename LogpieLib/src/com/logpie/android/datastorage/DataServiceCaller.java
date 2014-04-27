package com.logpie.android.datastorage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.logpie.android.connection.ThreadHelper;
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

    public DataServiceCaller(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    /**
     * get the CentralDataService Object
     */
    public void asyncConnectDataService()
    {
        ThreadHelper.runOffMainThread(new Runnable()
        {
            @Override
            public void run()
            {
                bindToService();
            }
        });
    }

    public void asyncDisconnectDataService()
    {
        ThreadHelper.runOffMainThread(new Runnable()
        {
            @Override
            public void run()
            {
                unbindService();
            }
        });
    }

    // get the DataPlatform from data service
    public DataPlatform getDataPlatform()
    {
        if (mDataPlatform == null)
        {
            LogpieLog.e(TAG, "The Service haven't been established");
        }
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
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0)
            {
                LogpieLog.d(TAG, "onServiceDisconnected");
                mDataPlatform = null;
            }
        };
    }

    private void unbindService()
    {
        if (mContext != null && mConnection != null)
        {
            mContext.unbindService(mConnection);
        }
    }
}
