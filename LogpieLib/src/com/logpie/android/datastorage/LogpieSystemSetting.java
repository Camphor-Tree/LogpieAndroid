package com.logpie.android.datastorage;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

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

    private EncryptedDataStorage mEncryptedDataStorage;
    private ConcurrentHashMap<String, String> mSettingsMap;

    public final static String KEY_LANGUAGE = "com.logpie.android.language";
    public final static String ENGLISH = "english";
    public final static String CHINESE = "chinese";

    private LogpieSystemSetting(Context context)
    {
        mEncryptedDataStorage = EncryptedDataStorage.getInstance(context);
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
        boolean success = mEncryptedDataStorage.setKeyValue(DataLevel.SYSTEM_LVL, key, value);
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
        boolean success = mEncryptedDataStorage.setKeyValueBundle(settings);
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
            mSettingsMap = mEncryptedDataStorage.getAll(DataLevel.SYSTEM_LVL);
        }
    }
}
