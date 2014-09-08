package com.logpie.android.components;

import android.app.Application;
import android.content.Context;

import com.logpie.android.datastorage.DataEncryptionStorage;
import com.logpie.android.datastorage.SQLStorage;
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
        DataEncryptionStorage encryptionStorage = DataEncryptionStorage.getInstance(mContext);
        SQLStorage sqlStorage = SQLStorage.getInstance(mContext);
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
