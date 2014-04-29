package com.logpie.android.datastorage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.logpie.android.util.LogpieCallback;

public class KeyValueStorage
{
    public static final String SUCCESS_KEY = "com.logpie.storage.keyvalue.success";
    public static final String ERROR_KEY = "com.logpie.storage.keyvalue.error";

    private static String sSharedPreferencesPath;

    private static KeyValueStorage sKeyValueStorage;
    private static Context mContext;
    public Map<DataLevel, SharedPreferences> mDataMap = new HashMap<DataLevel, SharedPreferences>();

    private KeyValueStorage()
    {
        initialize();
    }

    public static synchronized KeyValueStorage getInstance(Context context)
    {
        if (sKeyValueStorage == null)
        {
            mContext = context.getApplicationContext();
            sKeyValueStorage = new KeyValueStorage();
            sSharedPreferencesPath = "/data/data/" + mContext.getApplicationInfo().packageName
                    + "/shared_prefs";
        }
        return sKeyValueStorage;

    }

    boolean isSharedPreferencesExist()
    {
        for (DataLevel dataLevel : DataLevel.values())
        {
            String filePath = sSharedPreferencesPath + "/" + dataLevel.toString() + ".xml";
            File f = new File(filePath);
            if (!f.exists())
                return false;
        }
        return true;
    }

    boolean isDirectoryExist()
    {
        File root = new File(sSharedPreferencesPath);
        return root.isDirectory();
    }

    /**
     * Initialize the key-value storage. All all dataLevel's storage into
     * dataMap;
     */
    public void initialize()
    {
        // If map already contains reference to shared preferences, just
        // return.
        if (isSharedPreferencesExist() && mDataMap.size() == DataLevel.values().length)
        {
            return;
        }
        // Each DataLevel will create a separate file
        for (DataLevel dataLevel : DataLevel.values())
        {
            mDataMap.put(dataLevel,
                    mContext.getSharedPreferences(dataLevel.name(), Context.MODE_PRIVATE));
        }
    }

    /**
     * You should always add key:DataLevel.KEY_DATALEVEL value: DataLevel into
     * your bundle
     * 
     * @param bundle
     *            the data you need to insert into keyValueStorage
     * @param callback
     *            callback you need keyValueStorage to execute when finishing
     *            insertion.
     */
    public void insert(Bundle bundle, LogpieCallback callback)
    {
        // get the data level
        String dataLevel = bundle.getString(DataLevel.KEY_DATALEVEL);
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);
        // start read the insert data from bundle
        if (dataLevel != null)
        {
            SharedPreferences sharedPreferences = mDataMap.get(DataLevel.valueOf(dataLevel));

            Editor editor = sharedPreferences.edit();
            for (String key : bundle.keySet())
            {
                editor.putString(key, bundle.getString(key));
            }
            editor.commit();

            handleSuccessCallback(callback, "Insert successfully");
        }
        else
        {
            handleErrorCallback(callback,
                    "Data level is missing, please add DataLevel.KEY_DATALEVEL");
        }
    }

    /**
     * You should always add key:DataLevel.KEY_DATALEVEL value: DataLevel into
     * your bundle
     * 
     * @param bundle
     *            the data you need to insert into keyValueStorage
     * @return insert result
     */
    public boolean insert(Bundle bundle)
    {
        // get the data level
        String dataLevel = bundle.getString(DataLevel.KEY_DATALEVEL);
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);
        // start read the insert data from bundle
        if (dataLevel != null)
        {
            SharedPreferences sharedPreferences = mDataMap.get(DataLevel.valueOf(dataLevel));

            Editor editor = sharedPreferences.edit();
            for (String key : bundle.keySet())
            {
                editor.putString(key, bundle.getString(key));
            }
            editor.commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * 
     * @param dataLevel
     * @param key
     * @param value
     * @param callback
     */
    public void insert(DataLevel dataLevel, String key, String value, LogpieCallback callback)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            sharedPreferences.edit().putString(key, value).commit();
            handleSuccessCallback(callback, "Insert successfully");
        }
        else
        {
            handleErrorCallback(callback, "No such DataLevel");
        }
    }

    /**
     * Insert into key-value storage
     * 
     * @param dataLevel
     * @param key
     * @param value
     * @return insert result
     */
    public boolean insert(DataLevel dataLevel, String key, String value)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            sharedPreferences.edit().putString(key, value).commit();
            return true;
        }
        return false;
    }

    /**
     * @param dataLevel
     * @param key
     * @param callback
     */
    public void query(DataLevel dataLevel, String key, LogpieCallback callback)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            if (sharedPreferences.contains(key))
            {
                String value = sharedPreferences.getString(key, null);
                handleSuccessCallback(callback, value);
            }
            else
            {
                // TODO: Refactor all the error code;
                handleErrorCallback(callback, "The key doesn't exist");
            }
        }
        else
        {
            handleErrorCallback(callback, "No such DataLevel");
        }
    }

    /**
     * @param dataLevel
     * @param key
     * @return result string. If no key find, return null
     */
    public String query(DataLevel dataLevel, String key)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            String value = sharedPreferences.getString(key, null);
            return value;
        }
        return null;
    }

    /**
     * update the key-value pair if the key exist.
     * 
     * @param dataLevel
     * @param key
     * @param value
     * @param callback
     */
    public void update(DataLevel dataLevel, String key, String value, LogpieCallback callback)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            if (sharedPreferences.contains(key))
            {
                sharedPreferences.edit().putString(key, value).commit();
                handleSuccessCallback(callback, "Update successfully");
            }
            else
            {
                handleErrorCallback(callback, "Update fail, because no such key in storage");
            }

        }
        else
        {
            handleErrorCallback(callback, "No such DataLevel");
        }
    }

    /**
     * update the key-value pair if the key exist. Return true, if update
     * successfully. Return false, if do not find the key or sharedPreferences.
     * 
     * @param dataLevel
     * @param key
     *            the key you want to update
     * @param value
     *            new value you want to update to
     */
    public boolean update(DataLevel dataLevel, String key, String value)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null && sharedPreferences.contains(key))
        {

            sharedPreferences.edit().putString(key, value).commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Delete the key-value pair given the key.
     * 
     * @param dataLevel
     * @param key
     * @param callback
     */
    public void delete(DataLevel dataLevel, String key, LogpieCallback callback)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null)
        {
            if (sharedPreferences.contains(key))
            {
                sharedPreferences.edit().remove(key).commit();
                handleSuccessCallback(callback, "delete key success");
            }
            else
            {
                // TODO: Refactor all the error code;
                handleErrorCallback(callback, "The key doesn't exist");
            }
        }
        else
        {
            handleErrorCallback(callback, "No such DataLevel");
        }
    }

    /**
     * Delete the key-value pair given the key.
     * 
     * @param dataLevel
     * @param key
     */
    public boolean delete(DataLevel dataLevel, String key)
    {
        SharedPreferences sharedPreferences = mDataMap.get(dataLevel);
        if (sharedPreferences != null && sharedPreferences.contains(key))
        {
            sharedPreferences.edit().remove(key).commit();
            return true;
        }
        else
        {
            return false;
        }
    }

    /* packaged private */void clearAll()
    {
        // Each DataLevel will create a separate file
        for (DataLevel dataLevel : DataLevel.values())
        {
            mDataMap.get(dataLevel).edit().clear().commit();
        }
    }

    /* packaged private */void clear(DataLevel datalevel)
    {
        if (mDataMap.containsKey(datalevel))
        {
            mDataMap.get(datalevel).edit().clear().commit();
        }
    }

    private void handleErrorCallback(LogpieCallback callback, String message)
    {
        Bundle errorBundle = new Bundle();
        errorBundle.putString(ERROR_KEY, message);
        callback.onError(errorBundle);
    }

    private void handleSuccessCallback(LogpieCallback callback, String message)
    {
        Bundle successBundle = new Bundle();
        successBundle.putString(SUCCESS_KEY, message);
        callback.onError(successBundle);
    }
}
