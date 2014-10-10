package com.logpie.android.datastorage;

public class DatabaseSchema
{
    public static final String SCHEMA_TABLE_USER = "user";
    public static final String SCHEMA_USER_UID = "uid";
    public static final String SCHEMA_USER_EMAIL = "email";
    public static final String SCHEMA_USER_NICKNAME = "nickname";
    public static final String SCHEMA_USER_GENDER = "gender";
    public static final String SCHEMA_USER_BIRTHDAY = "birthday";
    public static final String SCHEMA_USER_CITY = "city";
    public static final String SCHEMA_USER_COUNTRY = "country";
    public static final String SCHEMA_USER_LAST_UPDATE_TIME = "lastupdatetime";
    public static final String SCHEMA_USER_IS_ORGANIZATION = "isorganization";

    public static final String SCHEMA_TABLE_ACTIVITY = "activity";
    public static final String SCHEMA_ACTIVITY_AID = "aid";
    public static final String SCHEMA_ACTIVITY_CREATOR = "creator";
    public static final String SCHEMA_ACTIVITY_DESCRIPTION = "description";
    public static final String SCHEMA_ACTIVITY_CITY = "city";
    public static final String SCHEMA_ACTIVITY_LOCATION = "location";
    public static final String SCHEMA_ACTIVITY_LAT = "lati";
    public static final String SCHEMA_ACTIVITY_LON = "long";
    public static final String SCHEMA_ACTIVITY_CREATE_TIME = "createtime";
    public static final String SCHEMA_ACTIVITY_START_TIME = "starttime";
    public static final String SCHEMA_ACTIVITY_END_TIME = "endtime";
    public static final String SCHEMA_ACTIVITY_COMMENT = "comment";
    public static final String SCHEMA_ACTIVITY_COUNT_LIKE = "countlike";
    public static final String SCHEMA_ACTIVITY_COUNT_DISLIKE = "countdislike";
    public static final String SCHEMA_ACTIVITY_ACTIVATED = "activated";
    public static final String SCHEMA_ACTIVITY_CATEGORY = "category";
    public static final String SCHEMA_ACTIVITY_SUBCATEGORY = "subcategory";

    // TODO: modify
    public static final String SCHEMA_TABLE_COMMENT = "comment";
    public static final String SCHEMA_COMMENTS_USER_ID = "user_id";
    public static final String SCHEMA_COMMENTS_ACTIVITY_ID = "activity";
    public static final String SCHEMA_COMMENTS_COMMENT_CONTENT = "comment_content";
    public static final String SCHEMA_COMMENTS_COMMENT_TIME = "comment_time";
    public static final String SCHEMA_COMMENTS_REPLY_TO = "reply_to";
    public static final String SCHEMA_COMMENTS_READ_BY_REPLY = "read_by_reply";
    public static final String SCHEMA_COMMENTS_READ_BY_HOST = "read_by_host";

    // TODO: modify
    public static final String SCHEMA_TABLE_USER_LIKE_ACTIVITY = "user_like_activity";
    public static final String SCHEMA_TABLE_USER_DISLIKE_ACTIVITY = "user_dislike_activity";
    public static final String SCHEMA_TABLE_ORGANIZATION = "organization";

    public static final String SCHEMA_TABLE_CITY = "city";
    public static final String SCHEMA_CITY_CID = "cid";
    public static final String SCHEMA_CITY_CITY = "city";
    public static final String SCHEMA_CITY_GRADE = "grade";
    public static final String SCHEMA_CITY_PROVINCE = "province";

    public static final String SCHEMA_TABLE_CATEGORY = "category";
    public static final String SCHEMA_CATEGORY_CID = "acpid";
    public static final String SCHEMA_CATEGORY_CATEGORYCN = "category_cn";
    public static final String SCHEMA_CATEGORY_CATEGORYUS = "category_us";
    public static final String SCHEMA_TABLE_SUBCATEGORY = "subcategory";
    public static final String SCHEMA_SUBCATEGORY_CID = "accid";
    public static final String SCHEMA_SUBCATEGORY_SUBCATEGORYCN = "subcategory_cn";
    public static final String SCHEMA_SUBCATEGORY_SUBCATEGORYUS = "subcategory_us";
    public static final String SCHEMA_SUBCATEGORY_PARENT = "parent";

}
