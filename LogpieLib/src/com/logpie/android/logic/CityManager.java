package com.logpie.android.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.datastorage.DataStorage;
import com.logpie.android.datastorage.DatabaseSchema;
import com.logpie.android.util.LogpieLog;

public class CityManager
{
    private static final String TAG = CityManager.class.getName();
    public static final String KEY_PROVINCE_GROUP = "province";
    public static final String KEY_CITY_GROUP = "city";

    private static CityManager sCityManager;

    private static Context mContext;
    private static List<Map<String, String>> mProvinceList;
    private static List<List<Map<String, String>>> mCityList;

    private CityManager()
    {

    }

    public static synchronized CityManager getInstance(Context context)
    {
        if (sCityManager == null)
        {
            sCityManager = new CityManager();
            mContext = context;
            mProvinceList = new ArrayList<Map<String, String>>();
            mCityList = new ArrayList<List<Map<String, String>>>();
        }
        return sCityManager;
    }

    public List<Map<String, String>> getProvinceList()
    {
        if (mProvinceList == null || mProvinceList.size() == 0)
        {
            setData();
        }
        return mProvinceList;
    }

    public List<List<Map<String, String>>> getCityList()
    {
        if (mCityList == null || mCityList.size() == 0)
        {
            setData();
        }

        return mCityList;
    }

    public synchronized void setData()
    {
        mProvinceList.clear();
        mCityList.clear();

        DataStorage storage = DataStorage.getInstance(mContext);

        Bundle provinceBundle = storage.getProvinceList();
        if (provinceBundle == null)
        {
            LogpieLog.e(TAG, "Cannot get the province list from the sqlite database.");
            return;
        }
        for (int i = 0; i < provinceBundle.size(); i++)
        {
            // Set province
            Bundle record = provinceBundle.getBundle(String.valueOf(i));
            if (record.containsKey(DatabaseSchema.SCHEMA_CITY_PROVINCE))
            {
                String province = record.getString(DatabaseSchema.SCHEMA_CITY_PROVINCE);
                if (province == null || province.equals(""))
                {
                    LogpieLog.e(TAG, "cannot get the province name from the record bundle.");
                    return;
                }
                HashMap<String, String> hs_p = new HashMap<String, String>();
                hs_p.put(KEY_PROVINCE_GROUP, province);
                mProvinceList.add(hs_p);

                // Set city
                Bundle cityBundle = storage.getCityList(province);
                if (cityBundle == null)
                {
                    LogpieLog.e(TAG, "Cannot get the city list from the sqlite database.");
                    return;
                }
                List<Map<String, String>> cities = new ArrayList<Map<String, String>>();
                for (int j = 0; j < cityBundle.size(); j++)
                {
                    record = cityBundle.getBundle(String.valueOf(j));
                    if (record.containsKey(DatabaseSchema.SCHEMA_CITY_CITY))
                    {
                        String city = record.getString(DatabaseSchema.SCHEMA_CITY_CITY);
                        if (city == null || city.equals(""))
                        {
                            LogpieLog.e(TAG, "cannot get the city name from the record bundle.");
                            return;
                        }
                        HashMap<String, String> hs_c = new HashMap<String, String>();
                        hs_c.put(KEY_CITY_GROUP, city);
                        cities.add(hs_c);
                    }
                }
                mCityList.add(cities);
            }
        }

    }
}
