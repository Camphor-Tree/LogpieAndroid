package com.logpie.android.datastorage;

import java.util.concurrent.atomic.AtomicBoolean;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.logpie.android.exception.ThreadException;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.ThreadHelper;

public class DataServiceCaller
{
    private static final String TAG = DataServiceCaller.class.getName();
    // max try times to bind to service;
    private static final int sMaxTryTimes = 3;

    private final Context mContext;
    private ServiceConnection mConnection;
    private DataPlatform mDataPlatform;
    private Intent mIntent;

    private AtomicBoolean mConnected = new AtomicBoolean(false);

    public DataServiceCaller(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    /**
     * get the CentralDataService Object
     * 
     * @throws ThreadException
     */
    public void asyncConnectDataService() throws ThreadException
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

    public void asyncDisconnectDataService() throws ThreadException
    {
        // if the connection haven't been established, then just return
        if (!mConnected.get())
        {
            return;
        }
        ThreadHelper.runOffMainThread(new Runnable()
        {
            @Override
            public void run()
            {
                unbindService();
            }
        });
    }

    public void syncDisconnectDataService() throws ThreadException
    {
        // if the connection haven't been established, then just return
        if (!mConnected.get())
        {
            return;
        }
        ThreadHelper.runOnMainThread(new Runnable()
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
            LogpieLog.e(TAG, "The Service haven't been established or just dropped the connection");
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
                mConnected.set(true);
            }

            // this method is not supposed to be raised when you unbind your
            // service, so don't rely on it. It is supposed to inform you in
            // case the connection between your Service and ServiceConnection is
            // dropped.
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
