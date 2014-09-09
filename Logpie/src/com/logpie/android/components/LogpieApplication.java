package com.logpie.android.components;

import android.app.Application;
import android.content.Context;

import com.logpie.android.datastorage.DataStorage;
import com.logpie.android.datastorage.EncryptedDataStorage;
import com.logpie.android.ui.helper.LanguageHelper;

public class LogpieApplication extends Application
{
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
        EncryptedDataStorage encryptionStorage = EncryptedDataStorage
                .getInstance(mContext);
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
