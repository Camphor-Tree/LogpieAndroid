package com.logpie.android.components;

import android.app.Application;
import android.content.Context;

import com.logpie.android.datastorage.DataStorage;
import com.logpie.android.datastorage.EncryptedDataStorage;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.util.LogpieLog;

public class LogpieApplication extends Application
{
    private static final String TAG = LogpieApplication.class.getName();
    Context mContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mContext = getApplicationContext();
        logpieInit();
    }

    private void logpieInit()
    {
        LogpieLog.d(TAG, "Logpie starts to initialize database and language...");
        EncryptedDataStorage encryptionStorage = EncryptedDataStorage.getInstance(mContext);
        DataStorage Storage = DataStorage.getInstance(mContext);
        /**
         * Initialized system setting on Logpie
         */
        LogpieSystemSetting setting = LogpieSystemSetting.getInstance(mContext);
        setting.initialize();
        /**
         * Initialized default language on Logpie
         */
        LanguageHelper.initialSystemLocale(mContext);
    }
}
