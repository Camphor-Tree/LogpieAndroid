package com.logpie.android.datastorage;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.util.LogpieCallback;

public class DataStorage
{
    private static final String TAG = DataStorage.class.getName();
    private static final String KEY_WHERE_CLAUSE = "where_clause";

    private static DataStorage sDataStorage;
    private SQLStorage mSQLStorage;
    private Context mContext;

    private DataStorage(Context context)
    {
        mContext = context.getApplicationContext();
        mSQLStorage = SQLStorage.getInstance(context);
    }

    public synchronized static DataStorage getInstance(Context context)
    {
        if (sDataStorage == null)
        {
            sDataStorage = new DataStorage(context);
        }
        return sDataStorage;
    }

    /**
     * API for possible operations; Need to think more
     * 
     * INSERT operation
     * 
     * @param bundle
     * @param callback
     */
    public void registerUser(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, DatabaseSchema.SCHEMA_TABLE_USER, callback);
    }

    public void registerOrganization(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, DatabaseSchema.SCHEMA_TABLE_ORGANIZATION, callback);
    }

    public void createActivity(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, DatabaseSchema.SCHEMA_TABLE_ACTIVITY, callback);
    }

    public void createComment(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, DatabaseSchema.SCHEMA_TABLE_COMMENT, callback);
    }

    /**
     * UPDATE operation
     * 
     * @param bundle
     * @param callback
     */
    public void updateUserProfile(Bundle bundle, LogpieCallback callback)
    {
        // TODO
        String whereClause = bundle.getString(KEY_WHERE_CLAUSE);
        String[] whereArgs = new String[2];
        mSQLStorage.update(DatabaseSchema.SCHEMA_TABLE_USER, bundle, whereClause, whereArgs);
    }

    public void updateOrganizationProfile(Bundle bundle, LogpieCallback callback)
    {
        // TODO
        String whereClause = bundle.getString(KEY_WHERE_CLAUSE);
        String[] whereArgs = new String[2];
        mSQLStorage
                .update(DatabaseSchema.SCHEMA_TABLE_ORGANIZATION, bundle, whereClause, whereArgs);
    }

    public void editActivity(Bundle bundle, LogpieCallback callback)
    {

    }

    /**
     * QUERY operation
     * 
     * @param key
     * @return Bundle
     */
    public Bundle findUserByKey(String key)
    {
        String[] columns = new String[3];
        String[] selectionArgs = new String[1];
        return mSQLStorage.query(DatabaseSchema.SCHEMA_TABLE_USER, columns, key, selectionArgs,
                null, null, null);
    }

    public Bundle findOrganizationByKey(String key)
    {
        return null;
    }

    public Bundle findActivityByKey(String key)
    {
        return null;
    }

    public Bundle findCommentByKey(String key)
    {
        return null;
    }

    public void deleteActivity(String id)
    {
        String[] whereArgs = new String[2];
        mSQLStorage.delete(DatabaseSchema.SCHEMA_TABLE_ACTIVITY, id, whereArgs);
        // TODO: delete all comments in this activity
    }

    public Bundle getProvinceList()
    {
        String[] columns = new String[] { DatabaseSchema.SCHEMA_CITY_PROVINCE };
        String groupBy = DatabaseSchema.SCHEMA_CITY_PROVINCE;
        String orderBy = "length(" + DatabaseSchema.SCHEMA_CITY_PROVINCE + ")";

        return mSQLStorage.query(DatabaseSchema.SCHEMA_TABLE_CITY, columns, null, null, groupBy,
                null, orderBy);
    }

    public Bundle getCityList(String province)
    {
        String[] columns = new String[] { DatabaseSchema.SCHEMA_CITY_CID,
                DatabaseSchema.SCHEMA_CITY_CITY };
        String whereClause = DatabaseSchema.SCHEMA_CITY_PROVINCE + " = ?";
        String[] whereArgs = new String[] { province };

        return mSQLStorage.query(DatabaseSchema.SCHEMA_TABLE_CITY, columns, whereClause, whereArgs,
                null, null, null);
    }

    public Bundle getCategoryList()
    {
        String[] columns = new String[] { DatabaseSchema.SCHEMA_CATEGORY_CID,
                DatabaseSchema.SCHEMA_CATEGORY_CATEGORYCN,
                DatabaseSchema.SCHEMA_CATEGORY_CATEGORYUS };
        return mSQLStorage.query(DatabaseSchema.SCHEMA_TABLE_CATEGORY, columns, null, null, null,
                null, null);
    }

    public Bundle getSubcategoryList(String categoryID)
    {
        String[] columns = new String[] { DatabaseSchema.SCHEMA_SUBCATEGORY_CID,
                DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYCN,
                DatabaseSchema.SCHEMA_SUBCATEGORY_SUBCATEGORYUS };
        String whereClause = DatabaseSchema.SCHEMA_SUBCATEGORY_PARENT + " = ?";
        String[] whereArgs = new String[] { categoryID };

        return mSQLStorage.query(DatabaseSchema.SCHEMA_TABLE_SUBCATEGORY, columns, whereClause,
                whereArgs, null, null, null);
    }
}
