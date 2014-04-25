package com.logpie.android.datastorage;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.logpie.android.util.LogpieLog;

public class CentralDataService extends Service
{
    private static final String TAG = CentralDataService.class.getName();
    private IBinder mBinder;

    @Override
    public IBinder onBind(Intent intent)
    {
        LogpieLog.d(TAG, "onBindSuccess");
        mBinder = DataPlatform.getInstance();
        String packageName = intent.getComponent().getPackageName();
        LogpieLog.i(TAG, "onBindSuccess");
        LogpieLog.i(TAG, packageName);
        return mBinder;
    }

}
