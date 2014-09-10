package com.logpie.android.datastorage;

public class SQLiteConfig
{
    /**
     * SQL for creating tables
     */

    /* Table 'city' */
    private final static String SQL_CREATE_CITY_TABLE = "CREATE TABLE city (cid INTEGER PRIMARY KEY, city TEXT NOT NULL, grade INTEGER NOT NULL, province TEXT NOT NULL)";
    private final static String SQL_INSERT_CITY_TABLE = "";

    public final static String NAME_CITY_TABLE = "city";
    public final static String SQL_INIT_CITY_TABLE = SQL_CREATE_CITY_TABLE + SQL_INSERT_CITY_TABLE;

    /* Table 'category' & 'subcategory' */
    private final static String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE category (amcid INTEGER PRIMARY KEY, category TEXT NOT NULL); CREATE TABLE if not exists subcategory (ascid INTEGER PRIMARY KEY, subcategory TEXT NOT NULL, category TEXT NOT NULL)";
    private final static String SQL_INSERT_CATEGORY_TABLE = "";

    public final static String NAME_CATEGORY_TABLE = "category";
    public final static String SQL_INIT_CATEGORY_TABLE = SQL_CREATE_CATEGORY_TABLE
            + SQL_INSERT_CATEGORY_TABLE;

    /* Table 'user' & 'organization' */
    public final static String SQL_INIT_USER_TABLE = "CREATE TABLE if not exists user (uid INTEGER PRIMARY KEY, email TEXT NOT NULL, nickname TEXT NOT NULL, gender INTEGER DEFAULT 1, birthday DATE, city TEXT, country TEXT, lastupdatedtime DATETIME DEFAULE CURRENT_DATETIME, isorganization INTEGER DEFAULT 0)";
    public final static String SQL_INIT_ORGANIZATION_TABLE = "CREATE TABLE if not exists organization (oid INTEGER PRIMARY KEY, organization TEXT NOT NULL, description TEXT NOT NULL, grade INTEGER, isvalid INTEGER DEFAULT 0)";

    /* Table 'activity' */
    public final static String SQL_INIT_ACTIVITY_TABLE = "CREATE TABLE if not exists activity (aid INTEGER PRIMARY KEY, activity TEXT NOT NULL, description TEXT NOT NULL, createtime DATETIME DEFAULT CURRENT_DATETIME, starttime DATETIME NOT NULL, endtime DATETIME NOT NULL, location TEXT NOT NULL, creator TEXT NOT NULL, city TEXT NOT NULL, category TEXT, subcategory TEXT, countlike INTEGER DEFAULT 0, countdislike INTEGER DEFAULT 0, activated INTEGER DEFAULT 0, lati REAL, long REAL";

    /* Table 'comment' */
    public final static String SQL_INIT_COMMENT_TABLE = "CREATE TABLE if not exists comment(acid INTEGER PRIMARY KEY, aid INTEGER NOT NULL, uid INTEGER NOT NULL, user TEXT NOT NULL, time DATETIME NOT NULL, content TEXT NOT NULL, replytoid INTEGER, replytoname TEXT, readbyreply INTEGER DEFAULT 0, readbyactivityholder INTEGER DEFAULT 0)";
}
