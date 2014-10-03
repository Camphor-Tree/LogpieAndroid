package com.logpie.android.sync;

import android.content.Context;

import com.logpie.android.datastorage.LogpieSystemSetting;

/**
 * This class is used to sync the database with server
 * 
 * @author yilei
 * 
 */
public abstract class LogpieSilentSyncManager
{
    private LogpieSystemSetting mSetting;

    protected LogpieSilentSyncManager(Context context)
    {
        mSetting = LogpieSystemSetting.getInstance(context);
    }

    public class CitySyncManager extends LogpieSilentSyncManager
    {

        protected CitySyncManager(Context context)
        {
            super(context);
        }
    }

}
