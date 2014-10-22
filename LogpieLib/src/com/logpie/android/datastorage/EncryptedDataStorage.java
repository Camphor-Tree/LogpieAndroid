package com.logpie.android.datastorage;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.security.AbstractDataEncryptor;
import com.logpie.android.security.LogpieCommonEncryptor;
import com.logpie.android.util.LogpieLog;

/**
 * This storage layer is above KeyValueStorage. This layer will handle the
 * encryption/decrption. No class should be able to oeprate KeyValueStorage
 * directly. We already mark KeyValueStorage as package-private.
 * 
 * @author yilei
 * 
 */
public class EncryptedDataStorage
{
    private static String TAG = EncryptedDataStorage.class.getName();
    // The only instance;
    private static EncryptedDataStorage sDataPlatform;
    private KeyValueStorage mKeyValueStorage;
    private AbstractDataEncryptor mEncryptor;

    private EncryptedDataStorage(final Context context)
    {
        Context applicationContext = context.getApplicationContext();
        mKeyValueStorage = KeyValueStorage.getInstance(applicationContext);
        mKeyValueStorage.initialize();
        mEncryptor = new LogpieCommonEncryptor();
    }

    public static synchronized EncryptedDataStorage getInstance(final Context context)
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new EncryptedDataStorage(context);
        }
        return sDataPlatform;
    }

    public boolean setKeyValue(final DataLevel datalevel, final String key, final String value)
    {
        String encryptionValue;
        encryptionValue = mEncryptor.encryptData(value);
        return mKeyValueStorage.insert(datalevel, key, encryptionValue);
    }

    public boolean setKeyValueBundle(final Bundle entryBundle)
    {
        for (String key : entryBundle.keySet())
        {
            // Do not encrypt the data Level
            if (!TextUtils.equals(key, DataLevel.KEY_DATALEVEL))
            {
                String value = entryBundle.getString(key);
                String encryptionValue;
                encryptionValue = mEncryptor.encryptData(value);
                entryBundle.putString(key, encryptionValue);
            }
        }
        return mKeyValueStorage.insert(entryBundle);
    }

    public String getValue(final DataLevel dataLevel, final String key)
    {
        String value = null;
        String rawValue = mKeyValueStorage.query(dataLevel, key);
        if (rawValue == null)
        {
            return null;
        }
        value = mEncryptor.decryptData(rawValue);
        return value;
    }

    public ConcurrentHashMap<String, String> getAll(final DataLevel datalevel)
    {
        ConcurrentHashMap<String, String> entryMap = mKeyValueStorage.queryAll(datalevel);
        if (entryMap == null)
        {
            return null;
        }
        for (Entry<String, String> entry : entryMap.entrySet())
        {
            String key = entry.getKey();
            String rawValue = entry.getValue();
            String plainValue = null;
            plainValue = mEncryptor.decryptData(rawValue);
            if (plainValue == null)
            {
                LogpieLog
                        .e(TAG,
                                "plainValue is null! Something must be wrong with the encryption/decryption");
                return null;
            }
            entryMap.put(key, plainValue);
        }

        return entryMap;
    }

    public boolean delete(final DataLevel dataLevel, final String key)
    {
        return mKeyValueStorage.delete(dataLevel, key);
    }
}
