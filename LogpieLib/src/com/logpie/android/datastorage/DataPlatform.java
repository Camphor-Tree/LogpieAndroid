package com.logpie.android.datastorage;

import android.content.Context;
import android.os.Binder;

import com.logpie.android.util.LogpieLog;

public class DataPlatform extends Binder
{
    private static String TAG = DataPlatform.class.getName();
    // The only instance;
    private static DataPlatform sDataPlatform;
    private static Context sContext;
    private KeyValueStorage mKeyValueStorage;

    private DataPlatform()
    {
        mKeyValueStorage = KeyValueStorage.getInstance(sContext);
        mKeyValueStorage.initialize();
    }

    public static synchronized DataPlatform getInstance(Context context)
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new DataPlatform();
            sContext = context.getApplicationContext();
        }
        return sDataPlatform;
    }

    public KeyValueStorage getKeyValueStorage()
    {
        if (mKeyValueStorage == null)
        {
            LogpieLog.e(TAG, "The mKeyValueSed. It is a bug!");
        }
        return mKeyValueStorage;
    }
}
