package com.logpie.android.datastorage;

import android.content.Context;
import android.os.Binder;

import com.logpie.android.util.LogpieLog;

public class DataPlatform extends Binder
{
    private static String TAG = DataPlatform.class.getName();
    // The only instance;
    private static DataPlatform sDataPlatform;
    private Context mContext;
    private KeyValueStorage mKeyValueStorage;
    private SQLStorage mSqlStorage;

    private DataPlatform(Context context, KeyValueStorage keyValueStorage, SQLStorage sqlStorage)
    {
        // get the unique Application Context
        mContext = context.getApplicationContext();
        mKeyValueStorage = keyValueStorage;
        mKeyValueStorage.initialize();
        mSqlStorage = sqlStorage;
        mSqlStorage.initialize();
    }

    public static synchronized DataPlatform getInstance(Context context,
            KeyValueStorage keyValueStorage, SQLStorage sqlStorage)
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new DataPlatform(context, keyValueStorage, sqlStorage);
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

    public void setKeyValueStorage(KeyValueStorage keyValueStorage)
    {
        mKeyValueStorage = keyValueStorage;
    }
}
