package com.logpie.android.datastorage;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.util.LogpieCallback;

public class KeyValueStorage
{
    private static KeyValueStorage sKeyValueStorage;
    private Context mContext;
    Map<DataLevel, SharedPreferences> mDataMap = new HashMap<DataLevel, SharedPreferences>();

    private KeyValueStorage()
    {
        initialize();
    }

    public synchronized KeyValueStorage getInstance(Context context)
    {
        if (sKeyValueStorage == null)
        {
            sKeyValueStorage = new KeyValueStorage();
            mContext = context.getApplicationContext();
        }
        return sKeyValueStorage;

    }

    public void initialize()
    {
        // Each DataLevel will create a separate file
        for (DataLevel dataLevel : DataLevel.values())
        {
            mDataMap.put(dataLevel, mContext.getSharedPreferences(DataLevel.SYSTEM_LVL.name(),
                    Context.MODE_PRIVATE));
        }
    }

    public void insert(Bundle bundle, LogpieCallback callback)
    {
        // get the data level
        String dataLevel = bundle.getString(DataLevel.KEY_DATALEVEL);
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);
        // start read the insert data from bundle
        if (dataLevel != null)
        {
            SharedPreferences sharedPreferences = mDataMap.get(dataLevel);

            for (String key : bundle.keySet())
            {
                if (TextUtils.equals(key, DataLevel.KEY_DATALEVEL))
                {
                    dataLevel = key;
                    break;
                }

            }
        }
    }

    public void insert(String dataLevel, String key, String value)
    {

    }

    public void query(String dataLevel, String key)
    {

    }

    public void update(String dataLevel, String key, String value)
    {

    }

    public void delete(String dataLevel, String key)
    {

    }

    public void clear()
    {

    }
}
