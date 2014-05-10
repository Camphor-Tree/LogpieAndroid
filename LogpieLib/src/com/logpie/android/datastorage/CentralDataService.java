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
        LogpieLog.d(TAG, "onBind Logpie Central DataService Success");
        KeyValueStorage keyValueStorage = KeyValueStorage.getInstance(this);
        mBinder = DataPlatform.getInstance(getApplicationContext(), keyValueStorage);

        String packageName = intent.getComponent().getPackageName();
        LogpieLog.i(TAG, "onBindSuccess");
        LogpieLog.i(TAG, packageName);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        // All clients have unbound with unbindService()
        mBinder = null;
        // Return false, next time client bind to dataService will not call
        // onRebind()
        // Just call onBind() as a single entrance.
        return false;
    }
}
