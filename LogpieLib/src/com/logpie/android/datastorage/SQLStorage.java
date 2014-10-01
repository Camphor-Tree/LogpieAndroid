package com.logpie.android.datastorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;

class SQLStorage
{
    protected static final class LogpieSQLiteOpenHelper extends SQLiteOpenHelper
    {
        private static final int LOGPIE_DB_VERSION = 1;

        public LogpieSQLiteOpenHelper(Context context, String name, CursorFactory factory)
        {
            super(context, name, factory, LOGPIE_DB_VERSION);

            LogpieLog.d(TAG, "SQLiteOpenHelper is trying to get the database...");
            getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try
            {
                LogpieLog
                        .d(TAG,
                                "Database is not found. SQLiteOpenHelper starts to initialize the database...");
                if (!isTableExists(db, SQLiteConfig.NAME_CITY_TABLE))
                {
                    db.execSQL(SQLiteConfig.SQL_CREATE_CITY_TABLE);
                    db.execSQL(SQLiteConfig.SQL_INSERT_PART_1_CITY_TABLE);
                    db.execSQL(SQLiteConfig.SQL_INSERT_PART_2_CITY_TABLE);
                }
                if (!isTableExists(db, SQLiteConfig.NAME_CATEGORY_TABLE))
                {
                    db.execSQL(SQLiteConfig.SQL_CREATE_CATEGORY_TABLE);
                    db.execSQL(SQLiteConfig.SQL_CREATE_SUBCATEGORY_TABLE);
                    db.execSQL(SQLiteConfig.SQL_INSERT_CATEGORY_TABLE);
                    db.execSQL(SQLiteConfig.SQL_INSERT_SUBCATEGORY_TABLE);
                }
                db.execSQL(SQLiteConfig.SQL_INIT_USER_TABLE);
                db.execSQL(SQLiteConfig.SQL_INIT_ORGANIZATION_TABLE);
                db.execSQL(SQLiteConfig.SQL_INIT_ACTIVITY_TABLE);
                db.execSQL(SQLiteConfig.SQL_INIT_COMMENT_TABLE);

                LogpieLog.d(TAG, "SQLiteOpenHelper finished database initialization.");

            } catch (Exception e)
            {
                LogpieLog.e(TAG,
                        "Cannot create the table during the SQLiteOpenHelper onCreate()");
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            // This is first version of Logpie, won't upgrate
            throw new IllegalArgumentException("Not supported");
        }

        private boolean isTableExists(SQLiteDatabase db, final String tableName)
        {
            if (db == null || !db.isOpen())
            {
                db = getReadableDatabase();
            }
            LogpieLog.d(TAG, "Check the table '" + tableName + "' is existed or not...");
            Cursor cursor = db.rawQuery(
                    "SELECT name FROM sqlite_master WHERE type='table' AND name='"
                            + tableName + "';", null);
            if (cursor != null)
            {
                if (cursor.getCount() > 0)
                {
                    LogpieLog.d(TAG, "The table '" + tableName + "' already exists.");
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
            LogpieLog.d(TAG, "The table '" + tableName
                    + "' does not exist. Need to create...");
            return false;
        }
    }

    private static final String TAG = SQLStorage.class.getName();
    public static final String SUCCESS_KEY = "com.logpie.storage.sql.success";
    public static final String ERROR_KEY = "com.logpie.storage.sql.error";
    private final String DATABASE_NAME = "logpie.db";

    private static SQLStorage sSQLStorage;
    private Context mContext;
    // Count the database opened times;
    // Since we just have one instance for the db connection, we cannot just
    // close it if another thread is still occupying it.
    // http://stackoverflow.com/questions/2493331/what-are-the-best-practices-for-sqlite-on-android
    private volatile int mOpenCounter;
    private LogpieSQLiteOpenHelper mSQLiteOpenHelper;

    private SQLStorage(Context context)
    {
        mContext = context.getApplicationContext();
        mSQLiteOpenHelper = new LogpieSQLiteOpenHelper(mContext, DATABASE_NAME, null);
    }

    public static synchronized SQLStorage getInstance(Context context)
    {
        if (sSQLStorage == null)
        {
            sSQLStorage = new SQLStorage(context);
        }
        return sSQLStorage;
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
        SQLiteDatabase db = openDatabase();
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        // start read the insert data from bundle
        ContentValues values = new ContentValues();
        for (String key : bundle.keySet())
        {
            // transfer bundle to contentValues
            values.put(key, bundle.getString(key));
        }

        long res = db.insert(table, null, values);
        if (res != -1)
        {
            handleSuccessCallback(callback, "Insert successfully");
        }
        else
        {
            LogpieLog.e(TAG, "insert Table " + table + " error");
            handleErrorCallback(callback, "Insert error");
        }
        closeDatabase();
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
        SQLiteDatabase db = openDatabase();

        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        // start read the insert data from bundle
        ContentValues values = new ContentValues();
        for (String key : bundle.keySet())
        {
            // transfer bundle to contentValues
            values.put(key, bundle.getString(key));
        }

        long res = db.insert(table, null, values);
        closeDatabase();
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
    public Bundle query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy)
    {
        SQLiteDatabase db = openDatabase();
        // query the dabatase
        Cursor result = db.query(table, columns, selection, selectionArgs, groupBy,
                having, orderBy);

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
        closeDatabase();
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
    public boolean update(String table, Bundle bundle, String whereClause,
            String[] whereArgs)
    {
        SQLiteDatabase db = openDatabase();
        // remove the data level information from bundle
        bundle.remove(DataLevel.KEY_DATALEVEL);

        ContentValues values = new ContentValues();
        // start read the insert data from bundle
        for (String key : bundle.keySet())
        {
            values.put(key, bundle.getString(key));
        }
        int res = db.update(table, values, whereClause, whereArgs);
        closeDatabase();
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
    public void delete(String table, String whereClause, String[] whereArgs,
            LogpieCallback callback)
    {
        SQLiteDatabase db = openDatabase();
        int res = db.delete(table, whereClause, whereArgs);
        if (res != 0)
        {
            handleSuccessCallback(callback, "Delete successfully");
        }
        else
        {
            LogpieLog.i(TAG, "delete Table " + table + " error, where " + whereClause);
            handleErrorCallback(callback, "Delete error");
        }
        closeDatabase();
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
        SQLiteDatabase db = openDatabase();
        int res = db.delete(table, whereClause, whereArgs);
        closeDatabase();
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

    private synchronized SQLiteDatabase openDatabase()
    {
        mOpenCounter++;
        return mSQLiteOpenHelper.getWritableDatabase();
    }

    private synchronized void closeDatabase()
    {
        mOpenCounter--;
        if (mOpenCounter == 0)
        {
            // Closing database
            mSQLiteOpenHelper.close();
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
