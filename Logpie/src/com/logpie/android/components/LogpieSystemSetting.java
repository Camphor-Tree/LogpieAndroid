package com.logpie.android.components;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.datastorage.DataEncryptionStorage;
import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.util.LogpieLog;

/**
 * This class is to query the system level settings, such as language setting.
 * 
 * @author yilei
 * 
 */
public class LogpieSystemSetting
{
    private static LogpieSystemSetting sSingletonSetting;
    private static final String TAG = LogpieSystemSetting.class.getName();

    private DataEncryptionStorage mDataEncryptionStorage;
    private ConcurrentHashMap<String, String> mSettingsMap;

    private LogpieSystemSetting(Context context)
    {
        mDataEncryptionStorage = DataEncryptionStorage.getInstance(context);
    }

    public synchronized static LogpieSystemSetting getInstance(Context context)
    {
        if (sSingletonSetting == null)
        {
            sSingletonSetting = new LogpieSystemSetting(context);
        }
        return sSingletonSetting;
    }

    // When doing initialization, it will populate all the key-value stored in
    // shared preferences
    public void initialize()
    {
        populateAllSystemSettings();
    }

    /**
     * Get the setting string
     * 
     * @param key
     * @return
     */
    public String getSystemSetting(final String key)
    {
        if (mSettingsMap == null)
        {
            populateAllSystemSettings();
        }
        return mSettingsMap.get(key);
    }

    /**
     * 
     * Set the setting string.
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setSystemSetting(final String key, final String value)
    {
        if (mSettingsMap.containsKey(key) && TextUtils.equals(mSettingsMap.get(key), value))
        {
            // If the cache key,value already exist, then just return;
            return true;
        }
        boolean success = mDataEncryptionStorage.setKeyValue(DataLevel.SYSTEM_LVL, key, value);
        if (success)
        {
            mSettingsMap.put(key, value);
        }
        else
        {
            LogpieLog.e(TAG, "Set the system settings fail!");
        }
        return success;
    }

    /**
     * 
     * Set the setting string.
     * 
     * @param key
     * @param value
     * @return
     */
    public boolean setSystemSetting(final Bundle settings)
    {
        Set<String> keySet = settings.keySet();
        for (String key : keySet)
        {
            if (mSettingsMap.containsKey(key)
                    && TextUtils.equals(mSettingsMap.get(key), settings.getString(key)))
            {
                settings.remove(key);
            }
        }
        // Add datalevel into the bundle
        settings.putString(DataLevel.KEY_DATALEVEL, DataLevel.SYSTEM_LVL.toString());
        boolean success = mDataEncryptionStorage.setKeyValueBundle(settings);
        if (success)
        {
            for (String key : keySet)
            {
                mSettingsMap.put(key, settings.getString(key));
            }
        }
        else
        {
            LogpieLog.e(TAG, "Set the system settings from Bundle fail!");
        }
        return success;
    }

    /**
     * populate all the entries in shared preferences into memory cache.
     */
    private void populateAllSystemSettings()
    {
        if (mSettingsMap == null)
        {
            mSettingsMap = mDataEncryptionStorage.getAll(DataLevel.SYSTEM_LVL);
        }
    }
}
