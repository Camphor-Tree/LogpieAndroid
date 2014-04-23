package com.logpie.android.datastorage;

public class DataPlatform
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
