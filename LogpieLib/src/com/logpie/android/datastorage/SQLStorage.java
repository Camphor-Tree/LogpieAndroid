package com.logpie.android.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;

public class SQLStorage
{
    private static final String TAG = SQLStorage.class.getName();
    public static final String SUCCESS_KEY = "com.logpie.storage.sql.success";
    public static final String ERROR_KEY = "com.logpie.storage.sql.error";

    private static SQLStorage sSQLStorage;
    private static Context mContext;
    private SQLiteDatabase mSQLiteDB;

    // database name
    public static final String DATABASE_NAME = "logpie.db";

    // SQL to initial tables
    // Table city (cid, city, level, province)
    private static String mCreateCityTableSQL = "CREATE TABLE city "
            + "(cid INTEGER PRIMARY KEY AUTOINCREMENT, city TEXT NOT NULL, "
            + "level INTEGER DEFAULT 1, province TEXT NOT NULL)";
    // Table category (amcid, category)
    private static String mCreateCategoryTableSQL = "CREATE TABLE category "
            + "(amcid INTEGER PRIMARY KEY AUTOINCREMENT, category TEXT NOT NULL)";
    // Table subcategory (ascid, subcategory, pid, category)
    private static String mCreateSubCategoryTableSQL = "CREATE TABLE subcategory "
            + "(ascid INTEGER PRIMARY KEY AUTOINCREMENT, subcategory TEXT NOT NULL, "
            + "pid INTEGER NOT NULL, category TEXT NOT NULL, FOREIGN KEY (pid) REFERENCES category (amcid))";
    // Table user (uid, email, password, nickname, gender, birthday, cid, city,
    // country, lastupdatedtime, isorganization)
    private static String mCreateUserTableSQL = "CREATE TABLE user "
            + "(uid INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, "
            + "nickname TEXT NOT NULL, gender INTEGER DEFAULT 1, "
            + "birthday DATE, cid INTEGER, city TEXT, country TEXT, "
            + "lastupdatedtime DATETIME DEFAULE CURRENT_DATETIME, isorganization INTEGER DEFAULT 0, "
            + "FOREIGN KEY (cid) REFERENCES city (cid))";
    // Table organization (oid, organization, description, grade, isvalid)
    private static String mCreateOrganizationTableSQL = "CREATE TABLE organization "
            + "(oid INTEGER PRIMARY KEY AUTOINCREMENT, organization TEXT NOT NULL, "
            + "description TEXT NOT NULL, grade INTEGER, isvalid INTEGER DEFAULT 0)";
    // Table activity (aid, activity, description, createtime, starttime,
    // endtime, location, uid, creator, city, country, category, subcategory,
    // countlike, countdislike, activated, lati, long)
    private static String mCreateActivityTableSQL = "CREATE TABLE activity "
            + "(aid INTEGER PRIMARY KEY AUTOINCREMENT, activity TEXT NOT NULL, "
            + "description TEXT NOT NULL, createtime DATETIME DEFAULT CURRENT_DATETIME, "
            + "starttime DATETIME NOT NULL, endtime DATETIME NOT NULL, location TEXT NOT NULL,"
            + "uid INTEGER NOT NULL, creator TEXT NOT NULL, city TEXT NOT NULL, "
            + "country TEXT NOT NULL, category TEXT, subcategory TEXT, countlike INTEGER DEFAULT 0, "
            + "countdislike INTEGER DEFAULT 0, activated INTEGER DEFAULT 0, lati REAL, "
            + "long REAL, FOREIGN KEY (uid) REFERENCES user (uid))";
    // Table comment (acid, activity, uid, user, time, content, replytoid,
    // replytoname, readbyreply, readbycreator)
    private static String mCreateCommentTableSQL = "CREATE TABLE comment "
            + "(acid INTEGER PRIMARY KEY AUTOINCREMENT, activity INTEGER NOT NULL, "
            + "uid INTEGER NOT NULL, user TEXT NOT NULL, time DATETIME NOT NULL, "
            + "content TEXT NOT NULL, replytoid INTEGER, replytoname TEXT, readbyreply INTEGER, "
            + "readbycreator INTEGER DEFAULT 0)";

    private boolean testTag = false;

    private SQLStorage()
    {
        initialize();
    }

    public static synchronized SQLStorage getInstance(Context context)
    {
        if (sSQLStorage == null)
        {
            mContext = context.getApplicationContext();
            sSQLStorage = new SQLStorage();
        }
        return sSQLStorage;

    }

    /**
     * Initialize the SQLite storage
     */
    public boolean initialize()
    {
        mSQLiteDB = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        createTable();
        return testTag;
    }

    /**
     * Create table SQLite 3 TEXT NUMERIC INTEGER REAL NONE
     */
    private void createTable()
    {
        try
        {
            mSQLiteDB.execSQL(mCreateCityTableSQL);
            mSQLiteDB.execSQL(mCreateCategoryTableSQL);
            mSQLiteDB.execSQL(mCreateSubCategoryTableSQL);
            mSQLiteDB.execSQL(mCreateUserTableSQL);
            mSQLiteDB.execSQL(mCreateOrganizationTableSQL);
            mSQLiteDB.execSQL(mCreateActivityTableSQL);
            mSQLiteDB.execSQL(mCreateCommentTableSQL);
            testTag = true;
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "Cannot create the table during the initial step.");
        }
    }

    /**
     * Insert into SQL storage
     * 
     * @param bundle
     * @param table
     * @param callback
     * 
     */
    public void insert(Bundle bundle, String table, LogpieCallback callback)
    {
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        // start read the insert data from bundle
        ContentValues values = new ContentValues();
        for (String key : bundle.keySet())
        {
            // transfer bundle to contentValues
            values.put(key, bundle.getString(key));
        }

        long res = mSQLiteDB.insert(table, null, values);
        if (res != -1)
        {
            handleSuccessCallback(callback, "Insert successfully");
        }
        else
        {
            LogpieLog.e(TAG, "insert Table " + table + " error");
            handleErrorCallback(callback, "Insert error");
        }
    }

    /**
     * Insert into SQL storage
     * 
     * @param id
     * @param uname
     * @return
     */
    public boolean insert(Bundle bundle, String table)
    {
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        // start read the insert data from bundle
        ContentValues values = new ContentValues();
        for (String key : bundle.keySet())
        {
            // transfer bundle to contentValues
            values.put(key, bundle.getString(key));
        }

        long res = mSQLiteDB.insert(table, null, values);
        return res != -1;
    }

    /**
     * Find all records
     * 
     * @param tableName
     * @param columnsName
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * 
     *            All strings should be the same as SQL grammar without key
     *            words
     * @return Cursor
     */
    public Bundle query(String table, String[] columns, String selection, String[] selectionArgs,
            String groupBy, String having, String orderBy)
    {
        // query the dabatase
        Cursor result = mSQLiteDB.query(table, columns, selection, selectionArgs, groupBy, having,
                orderBy);

        Bundle bundle = new Bundle();
        int id = 0;
        // transfer cursor to bundle
        while (result.moveToNext())
        {
            // bundle --> {id:record};
            // each record is also a bundle --> {column name:value}
            Bundle record = new Bundle();
            int len = result.getColumnCount();
            for (int i = 0; i < len; i++)
                record.putString(result.getColumnName(i), result.getString(i));
            bundle.putBundle(String.valueOf(id), record);
            id++;
        }
        return bundle;
    }

    /**
     * Update a record
     * 
     * @param tableName
     * @param bundle
     * @param whereClause
     * @param whereArgs
     *            All strings should be the same as SQL grammar without key
     *            words
     * @return Cursor
     */
    public boolean update(String table, Bundle bundle, String whereClause, String[] whereArgs)
    {
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        ContentValues values = new ContentValues();
        // start read the insert data from bundle
        for (String key : bundle.keySet())
        {
            values.put(key, bundle.getString(key));
        }
        int res = mSQLiteDB.update(table, values, whereClause, whereArgs);
        return res != 0;
    }

    /**
     * Delete records
     * 
     * @param tableName
     * @param callback
     * @param whereClause
     * @param whereArgs
     *            All strings should be the same as SQL grammar without key
     *            words
     */
    public void delete(String table, String whereClause, String[] whereArgs, LogpieCallback callback)
    {
        int res = mSQLiteDB.delete(table, whereClause, whereArgs);
        if (res != 0)
        {
            handleSuccessCallback(callback, "Delete successfully");
        }
        else
        {
            LogpieLog.i(TAG, "delete Table " + table + " error, where " + whereClause);
            handleErrorCallback(callback, "Delete error");
        }

    }

    /**
     * Delete records
     * 
     * @param tableName
     * @param whereClause
     * @param whereArgs
     *            All strings should be the same as SQL grammar without key
     *            words
     * 
     * @return true if it delete one or more records, else false
     */
    public boolean delete(String table, String whereClause, String[] whereArgs)
    {
        int res = mSQLiteDB.delete(table, whereClause, whereArgs);
        if (res != 0)
        {
            return true;
        }
        else
        {
            LogpieLog.i(TAG, "delete Table " + table + " error, where " + whereClause);
            return false;
        }
    }

    /**
     * Drop the database
     */
    /* packaged private */boolean clearAll()
    {
        return mContext.deleteDatabase(DATABASE_NAME);
    }

    public void close()
    {
        mSQLiteDB.close();
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