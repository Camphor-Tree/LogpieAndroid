package com.logpie.android.datastorage;

import android.content.Context;
import android.os.Binder;

public class DataPlatform extends Binder
{
    // The only instance;
    private static DataPlatform sDataPlatform;
    private static Context mContext;
    private KeyValueStorage mKeyValueStorage;

    private DataPlatform()
    {
        mKeyValueStorage = KeyValueStorage.getInstance(mContext);
        mKeyValueStorage.initialize();
    }

    public static synchronized DataPlatform getInstance(Context context)
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new DataPlatform();
            mContext = context.getApplicationContext();
        }
        return sDataPlatform;
    }

}
