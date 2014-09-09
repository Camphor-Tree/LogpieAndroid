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
        mSQLStorage.insert(bundle, "user", callback);
    }

    public void registerOrganization(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, "organization", callback);
    }

    public void createActivity(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, "activity", callback);
    }

    public void createComment(Bundle bundle, LogpieCallback callback)
    {
        mSQLStorage.insert(bundle, "comment", callback);
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
        mSQLStorage.update("user", bundle, whereClause, whereArgs);
    }

    public void updateOrganizationProfile(Bundle bundle, LogpieCallback callback)
    {
        // TODO
        String whereClause = bundle.getString(KEY_WHERE_CLAUSE);
        String[] whereArgs = new String[2];
        mSQLStorage.update("organization", bundle, whereClause, whereArgs);
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
        return mSQLStorage.query("user", columns, key, selectionArgs, null, null, null);
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
        mSQLStorage.delete("activity", id, whereArgs);
        // TODO: delete all comments in this activity
    }
}
