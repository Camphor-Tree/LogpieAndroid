package com.logpie.android.datastorage;

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
            + "pid INTEGER NOT NULL, category TEXT NOT NULL, (pid) REFERENCES category(amcid))";
    // Table user (uid, email, password, nickname, gender, birthday, cid, city,
    // country, lastupdatedtime, isorganization)
    private static String mCreateUserTableSQL = "CREATE TABLE user "
            + "(uid INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT NOT NULL, "
            + "password TEXT NOT NULL, nickname TEXT NOT NULL, gender INTEGER DEFAULT 1, "
            + "birthday DATE, cid INTEGER, city TEXT, country TEXT, "
            + "lastupdatedtime DATETIME DEFAULE CURRENT_DATETIME, isorganization INTEGER DEFAULT 0, "
            + "(cid) REFERENCES city(cid))";
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
            + "long REAL, (uid) REFERENCES user(uid))";
    // Table comment (acid, activity, uid, user, time, content, replytoid,
    // replytoname, readbyreply, readbycreator)
    private static String mCreateCommentTableSQL = "CREATE TABLE comment "
            + "(acid INTEGER PRIMARY KEY AUTOINCREMENT, activity INTEGER NOT NULL, "
            + "uid INTEGER NOT NULL, user TEXT NOT NULL, time DATETIME NOT NULL, "
            + "content TEXT NOT NULL, replytoid INTEGER, replytoname TEXT, readbyreply INTEGER, "
            + "readbycreator INTEGER DEFAULT 0)";

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
     * Initialize the SQLite storage TODO: check whether the table is existed
     */
    public void initialize()
    {
        mSQLiteDB = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        createTable();
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
        String sql = "";

        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String col : bundle.keySet())
        {
            cols.append(col + ", ");
            values.append(bundle.getString("'" + col + "', "));
        }
        // delete the last two digits ", " in cols and delete last three
        // digits "', " in values
        cols.delete(cols.length() - 2, cols.length());
        values.delete(values.length() - 3, values.length());
        try
        {
            sql = "insert into " + table + "(" + cols + ") values(" + values + ")";
            mSQLiteDB.execSQL(sql);
            handleSuccessCallback(callback, "Insert successfully");
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "insert Table " + table + " error, sql: " + sql);
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
        String sql = "";

        StringBuilder cols = new StringBuilder();
        StringBuilder values = new StringBuilder();
        for (String col : bundle.keySet())
        {
            cols.append(col + ", ");
            values.append(bundle.getString("'" + col + "', "));
        }
        // delete the last two digits ", " in cols and delete last three
        // digits "', " in values
        cols.delete(cols.length() - 2, cols.length());
        values.delete(values.length() - 3, values.length());
        try
        {
            sql = "insert into " + table + "(" + cols + ") values(" + values + ")";
            mSQLiteDB.execSQL(sql);
            return true;
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "insert Table " + table + " error, sql: " + sql);
        }
        return false;

    }

    /**
     * Find all records
     * 
     * @return Cursor
     */
    public void findAll()
    {

        Cursor cur = mSQLiteDB.query("t_user", new String[] { "_ID", "NAME" }, null, null, null,
                null, null);

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
