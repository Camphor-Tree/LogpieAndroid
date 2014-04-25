package com.logpie.android.datastorage;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;

public class DataServiceCaller
{
    private static final String TAG = DataServiceCaller.class.getName();
    // max try times to bind to service;
    private static final int sMaxTryTimes = 3;

    private final Context mContext;
    private ServiceConnection mConnection;
    private CentralDataService mService;
    private Intent mIntent;
    private LogpieCallback mCallback;

    public DataServiceCaller(Context context)
    {
        this.mContext = context.getApplicationContext();
    }

    public void call(LogpieCallback callback)
    {
        // TODO: refactor this to intentFactory;
        mIntent = new Intent(mContext, CentralDataService.class);
        mCallback = callback;
        buildConnectionCallback(mService);
        boolean bindSuccess = false;
        int countTry = 0;
        while (bindSuccess && countTry < sMaxTryTimes)
        {
            bindSuccess = mContext.bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
        }

        if (!bindSuccess)
        {
            LogpieLog.e(TAG, "error happend when trying to bind CentralDataService!");
        }

    }

    private void buildConnectionCallback(Service service)
    {
        mConnection = new ServiceConnection()
        {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service)
            {
                mService = (CentralDataService) service;

                // Do something to consume DataSevice

                // Do the callBack based on the result;
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0)
            {
            }
        };
    }
}
