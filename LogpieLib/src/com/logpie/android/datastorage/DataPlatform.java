package com.logpie.android.datastorage;

import android.os.Binder;

public class DataPlatform extends Binder
{
    // The only instance;
    private static DataPlatform sDataPlatform;

    private DataPlatform()
    {
    }

    public static synchronized DataPlatform getInstance()
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new DataPlatform();
        }
        return sDataPlatform;
    }
}
