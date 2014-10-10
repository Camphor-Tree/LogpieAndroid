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

public class CategoryManager
{
    private static final String TAG = CategoryManager.class.getName();
    public static final String KEY_CATEGORY_GROUP_CN = "category_cn";
    public static final String KEY_CATEGORY_GROUP_US = "category_us";
    public static final String KEY_SUBCATEGORY_GROUP_CN = "subcategory_cn";
    public static final String KEY_SUBCATEGORY_GROUP_US = "subcategory_us";

    private static CategoryManager sCategoryManager;

    private static Context sContext;
    private static boolean sIsCN;
    private static List<Map<String, String>> sCategoryCNList;
    private static List<Map<String, String>> sCategoryUSList;
    private static List<List<Map<String, String>>> sSubcategoryCNList;
    private static List<List<Map<String, String>>> sSubcategoryUSList;

    private CategoryManager()
    {

    }

    public static synchronized CategoryManager getInstance(Context context, boolean isCN)
    {
        if (sCategoryManager == null)
        {
            sCategoryManager = new CategoryManager();
            sContext = context;
            sIsCN = isCN;
            sCategoryCNList = new ArrayList<Map<String, String>>();
            sCategoryUSList = new ArrayList<Map<String, String>>();
            sSubcategoryCNList = new ArrayList<List<Map<String, String>>>();
            sSubcategoryUSList = new ArrayList<List<Map<String, String>>>();
        }
        return sCategoryManager;
    }

    public List<Map<String, String>> getCategoryList()
    {
        if (sIsCN)
        {
            if (sCategoryCNList == null || sCategoryCNList.size() == 0)
            {
                setData();
            }
            return sCategoryCNList;
        }
        else
        {
            if (sCategoryUSList == null || sCategoryUSList.size() == 0)
            {
                setData();
            }
            return sCategoryUSList;
        }
    }

    public List<List<Map<String, String>>> getSubcategoryList()
    {
        if (sIsCN)
        {
            if (sSubcategoryCNList == null || sSubcategoryCNList.size() == 0)
            {
                setData();
            }
            return sSubcategoryCNList;
        }
        else
        {
            if (sSubcategoryUSList == null || sSubcategoryUSList.size() == 0)
            {
                setData();
            }
            return sSubcategoryUSList;
        }
    }

    public synchronized void setData()
    {
        sCategoryCNList.clear();
        sCategoryUSList.clear();
        sSubcategoryCNList.clear();
        sSubcategoryUSList.clear();

        DataStorage storage = DataStorage.getInstance(sContext);

        Bundle categoryBundle = storage.getCategoryList();
        if (categoryBundle == null)
        {
            LogpieLog.e(TAG, "Cannot get the category list from the sqlite database.");
            return;
        }
        for (int i = 0; i < categoryBundle.size(); i++)
        {
            // Set province
            Bundle record = categoryBundle.getBundle(String.valueOf(i));
            if (record.containsKey(DatabaseSchema.SCHEMA_CATEGORY_CID))
            {
                String categoryID = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CID);
                if (categoryID == null || categoryID.equals(""))
                {
                    LogpieLog.e(TAG, "cannot get the category id from the record bundle.");
                    return;
                }

                String category_cn = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CATEGORYCN);
                String category_us = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CATEGORYUS);
                if (category_cn == null || category_cn.equals("") || category_us == null
                        || category_us.equals(""))
                {
                    LogpieLog.e(TAG, "cannot get the category name from the record bundle.");
                    return;
                }

                HashMap<String, String> hs_c_c = new HashMap<String, String>();
                hs_c_c.put(KEY_CATEGORY_GROUP_CN, category_cn);
                HashMap<String, String> hs_c_u = new HashMap<String, String>();
                hs_c_u.put(KEY_CATEGORY_GROUP_US, category_us);
                sCategoryCNList.add(hs_c_c);
                sCategoryUSList.add(hs_c_u);

                // Set city
                Bundle subcategoryBundle = storage.getSubcategoryList(categoryID);
                if (subcategoryBundle == null)
                {
                    LogpieLog.e(TAG, "Cannot get the subcategory list from the sqlite database.");
                    return;
                }
                List<Map<String, String>> subcategorys_c = new ArrayList<Map<String, String>>();
                List<Map<String, String>> subcategorys_u = new ArrayList<Map<String, String>>();
                for (int j = 0; j < subcategoryBundle.size(); j++)
                {
                    record = subcategoryBundle.getBundle(String.valueOf(j));
                    if (record.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYCN)
                            && record.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYUS))
                    {
                        String subcategory_cn = record
                                .getString(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYCN);
                        String subcategory_us = record
                                .getString(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYUS);
                        if (subcategory_cn == null || subcategory_cn.equals("")
                                || subcategory_us == null || subcategory_us.equals(""))
                        {
                            LogpieLog.e(TAG,
                                    "cannot get the subcategory name from the record bundle.");
                            return;
                        }
                        HashMap<String, String> hs_s_c = new HashMap<String, String>();
                        hs_s_c.put(KEY_SUBCATEGORY_GROUP_CN, subcategory_cn);
                        HashMap<String, String> hs_s_u = new HashMap<String, String>();
                        hs_s_u.put(KEY_SUBCATEGORY_GROUP_US, subcategory_us);
                        subcategorys_c.add(hs_s_c);
                        subcategorys_u.add(hs_s_u);
                    }
                }
                sSubcategoryCNList.add(subcategorys_c);
                sSubcategoryUSList.add(subcategorys_u);
            }
        }
    }

    public String getId(String parent, String child)
    {
        String pid;
        DataStorage storage = DataStorage.getInstance(sContext);
        Bundle bundle = storage.getCategoryId(parent);
        if (bundle != null)
        {
            bundle = bundle.getBundle("0");
            if (bundle.containsKey(DatabaseSchema.SCHEMA_CATEGORY_CID))
            {
                pid = bundle.getString(DatabaseSchema.SCHEMA_CATEGORY_CID);
                if (child != null)
                {
                    bundle = storage.getSubcategoryId(child, pid);
                    if (bundle != null)
                    {
                        bundle = bundle.getBundle("0");
                        if (bundle.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_CID))
                        {
                            return bundle.getString(DatabaseSchema.SCHEMA_SUBCATEGORY_CID);
                        }
                    }
                }
                else
                {
                    return pid;
                }
            }
        }

        return null;
    }
}
