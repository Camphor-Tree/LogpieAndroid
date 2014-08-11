package com.logpie.android.datastorage;

import android.content.Context;
import android.os.Bundle;
import android.test.AndroidTestCase;

public class SQLStorageTests extends AndroidTestCase
{

    public void test1_Initialize()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);
    }

    public void test2_Insert()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);

        // start test normal case
        Bundle data1 = new Bundle();
        data1.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data1.putString("email", "xxx@hotmail.com");
        data1.putString("nickname", "xxxxx");
        assertTrue(storage.insert(data1, "user"));

        // start test incorrect case -- lack of some columns which can not be
        // null
        Bundle data2 = new Bundle();
        data2.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data2.putString("email", "xxx@gmail.com");
        assertFalse(storage.insert(data2, "user"));

        // start test incorrect case -- insert incorrect parameters
        Bundle data3 = new Bundle();
        data3.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data3.putString("emailll", "xxx@222.com");
        data3.putString("nickname", "hey");
        assertFalse(storage.insert(data3, "user"));
    }

    public void test3_Query()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);

        String[] columns = new String[] { "email", "nickname" };
        String whereClause = "email = ?";
        // start test normal case
        String[] whereArgs1 = new String[] { "xxx@hotmail.com" };
        // start test incorrect case
        String[] whereArgs2 = new String[] { "xxx@222.com" };
        Bundle expect_value1 = storage.query("user", columns, whereClause, whereArgs1, null, null,
                null);
        Bundle expect_value2 = storage.query("user", columns, whereClause, whereArgs2, null, null,
                null);

        assertNotNull(expect_value1);
        // the bundle of record should be null
        assertNull(expect_value2.get("0"));

        Bundle expect_value3 = expect_value1.getBundle("0");
        assertNotNull(expect_value3);

        assertEquals("xxxxx", expect_value3.get("nickname"));
    }

    public void test4_Update()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);

        // start test normal case
        Bundle data = new Bundle();
        data.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data.putString("nickname", "Fresh");
        String whereClause = "email = ?";
        String[] whereArgs1 = new String[] { "xxx@hotmail.com" };

        assertTrue(storage.update("user", data, whereClause, whereArgs1));

        // start test incorrect case
        Bundle data2 = new Bundle();
        data2.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data2.putString("nickname", "Fresh");
        String[] whereArgs2 = new String[] { "xxx@qq.com" };

        assertFalse(storage.update("user", data, whereClause, whereArgs2));
    }

    public void test5_Delete()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);

        String whereClause = "email = ?";
        String[] whereArgs1 = new String[] { "xxx@hotmail.com" };
        assertTrue(storage.delete("user", whereClause, whereArgs1));
    }

    public void test6_Clear()
    {
        Context context = getContext();
        SQLStorage storage = SQLStorage.getInstance(context);

        assertTrue(storage.clearAll());
    }
}
