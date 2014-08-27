package com.logpie.android.datastorage;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Binder;

import com.logpie.android.security.AbstractDataEncryptor;
import com.logpie.android.security.LogpieCommonEncryptor;
import com.logpie.android.util.LogpieLog;

public class DataPlatform extends Binder
{
    private static String TAG = DataPlatform.class.getName();
    // The only instance;
    private static DataPlatform sDataPlatform;
    private Context mContext;
    private KeyValueStorage mKeyValueStorage;
    private SQLStorage mSqlStorage;
    private AbstractDataEncryptor mEncryptor;

    private DataPlatform(Context context, KeyValueStorage keyValueStorage, SQLStorage sqlStorage)
    {
        // get the unique Application Context
        mContext = context.getApplicationContext();
        mKeyValueStorage = keyValueStorage;
        mKeyValueStorage.initialize();
        mSqlStorage = sqlStorage;
        mSqlStorage.initialize();
        mEncryptor = new LogpieCommonEncryptor();
    }

    public static synchronized DataPlatform getInstance(Context context,
            KeyValueStorage keyValueStorage, SQLStorage sqlStorage)
    {
        if (sDataPlatform == null)
        {
            sDataPlatform = new DataPlatform(context, keyValueStorage, sqlStorage);
        }
        return sDataPlatform;
    }

    public KeyValueStorage getKeyValueStorage()
    {
        if (mKeyValueStorage == null)
        {
            LogpieLog.e(TAG, "The mKeyValueStorage is not set. It is a bug!");
        }
        return mKeyValueStorage;
    }

    public void setKeyValueStorage(KeyValueStorage keyValueStorage)
    {
        mKeyValueStorage = keyValueStorage;
    }
    
    public String getSystemSettingValue(final String key)
    {
        String value = null;
        String rawValue = mKeyValueStorage.query(DataLevel.SYSTEM_LVL, key);
        try
        {
            value = mEncryptor.decryptData(rawValue.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            LogpieLog.e(TAG, "Not support UTF-8, it is impossible");
            e.printStackTrace();
        }
        return value;
        
    }
    
    public boolean setSystemSettingValue(final String key, final String value)
    {
        String encryptionValue;
        try
        {
            encryptionValue = new String(mEncryptor.encryptData(value),"UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            LogpieLog.e(TAG, "Not support UTF-8, it is impossible");
            e.printStackTrace();
            return false;
        }
        return mKeyValueStorage.insert(DataLevel.SYSTEM_LVL, key, encryptionValue);
    }
}
