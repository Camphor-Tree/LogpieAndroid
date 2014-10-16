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

    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_SUBCATEGORY_ID = "subcategory_id";
    public static final String KEY_CATEGORY_STRING_CN = "category_cn";
    public static final String KEY_CATEGORY_STRING_US = "category_us";
    public static final String KEY_SUBCATEGORY_STRING_CN = "subcategory_cn";
    public static final String KEY_SUBCATEGORY_STRING_US = "subcategory_us";

    private static CategoryManager sCategoryManager;

    private Context mContext;
    private boolean mIsCN;
    private List<Map<String, String>> mCategoryCNList;
    private List<Map<String, String>> mCategoryUSList;
    private List<List<Map<String, String>>> mSubcategoryCNList;
    private List<List<Map<String, String>>> mSubcategoryUSList;

    private CategoryManager(Context context, boolean isCN)
    {
        mContext = context;
        mIsCN = isCN;
        mCategoryCNList = new ArrayList<Map<String, String>>();
        mCategoryUSList = new ArrayList<Map<String, String>>();
        mSubcategoryCNList = new ArrayList<List<Map<String, String>>>();
        mSubcategoryUSList = new ArrayList<List<Map<String, String>>>();
    }

    public static synchronized CategoryManager getInstance(Context context, boolean isCN)
    {
        if (sCategoryManager == null)
        {
            sCategoryManager = new CategoryManager(context, isCN);
        }
        return sCategoryManager;
    }

    public List<Map<String, String>> getCategoryList()
    {
        if (mIsCN)
        {
            if (mCategoryCNList == null || mCategoryCNList.size() == 0)
            {
                setData();
            }
            return mCategoryCNList;
        }
        else
        {
            if (mCategoryUSList == null || mCategoryUSList.size() == 0)
            {
                setData();
            }
            return mCategoryUSList;
        }
    }

    public List<List<Map<String, String>>> getSubcategoryList()
    {
        if (mIsCN)
        {
            if (mSubcategoryCNList == null || mSubcategoryCNList.size() == 0)
            {
                setData();
            }
            return mSubcategoryCNList;
        }
        else
        {
            if (mSubcategoryUSList == null || mSubcategoryUSList.size() == 0)
            {
                setData();
            }
            return mSubcategoryUSList;
        }
    }

    public String getCategoryById(String categoryId)
    {
        if (mIsCN)
        {
            for (Map<String, String> entry : mCategoryCNList)
            {
                if (entry.get(KEY_CATEGORY_ID).equals(categoryId))
                {
                    return entry.get(KEY_CATEGORY_STRING_CN);
                }
            }
        }
        else
        {
            for (Map<String, String> entry : mCategoryUSList)
            {
                if (entry.get(KEY_CATEGORY_ID).equals(categoryId))
                {
                    return entry.get(KEY_CATEGORY_STRING_US);
                }
            }
        }
        return null;
    }

    public String getSubcategoryById(String subcategoryId)
    {
        if (mIsCN)
        {
            for (List<Map<String, String>> list : mSubcategoryCNList)
            {
                for (Map<String, String> entry : list)
                {
                    if (entry.get(KEY_SUBCATEGORY_ID).equals(subcategoryId))
                    {
                        return entry.get(KEY_SUBCATEGORY_STRING_CN);
                    }
                }
            }
        }
        else
        {
            for (List<Map<String, String>> list : mSubcategoryUSList)
            {
                for (Map<String, String> entry : list)
                {
                    if (entry.get(KEY_SUBCATEGORY_ID).equals(subcategoryId))
                    {
                        return entry.get(KEY_SUBCATEGORY_STRING_US);
                    }
                }
            }
        }
        return null;
    }

    public synchronized void setData()
    {
        mCategoryCNList.clear();
        mCategoryUSList.clear();
        mSubcategoryCNList.clear();
        mSubcategoryUSList.clear();

        DataStorage storage = DataStorage.getInstance(mContext);

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

                String category_id = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CID);
                String category_cn = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CATEGORYCN);
                String category_us = record.getString(DatabaseSchema.SCHEMA_CATEGORY_CATEGORYUS);
                if (category_cn == null || category_cn.equals("") || category_us == null
                        || category_us.equals(""))
                {
                    LogpieLog.e(TAG, "cannot get the category name from the record bundle.");
                    return;
                }

                HashMap<String, String> hs_c_c = new HashMap<String, String>();
                hs_c_c.put(KEY_CATEGORY_ID, category_id);
                hs_c_c.put(KEY_CATEGORY_STRING_CN, category_cn);
                HashMap<String, String> hs_c_u = new HashMap<String, String>();
                hs_c_u.put(KEY_CATEGORY_ID, category_id);
                hs_c_u.put(KEY_CATEGORY_STRING_US, category_us);
                mCategoryCNList.add(hs_c_c);
                mCategoryUSList.add(hs_c_u);

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
                    if (record.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_CID)
                            && record.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYCN)
                            && record.containsKey(DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYUS))
                    {
                        String subcategory_id = record
                                .getString(DatabaseSchema.SCHEMA_SUBCATEGORY_CID);
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
                        hs_s_c.put(KEY_SUBCATEGORY_ID, subcategory_id);
                        hs_s_c.put(KEY_CATEGORY_STRING_CN, subcategory_cn);
                        HashMap<String, String> hs_s_u = new HashMap<String, String>();
                        hs_s_u.put(KEY_SUBCATEGORY_ID, subcategory_id);
                        hs_s_u.put(KEY_SUBCATEGORY_STRING_US, subcategory_us);
                        subcategorys_c.add(hs_s_c);
                        subcategorys_u.add(hs_s_u);
                    }
                }
                mSubcategoryCNList.add(subcategorys_c);
                mSubcategoryUSList.add(subcategorys_u);
            }
        }
    }
}
