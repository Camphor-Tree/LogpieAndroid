package com.logpie.android.datastorage;

import junit.framework.Assert;
import android.content.Context;
import android.os.Bundle;
import android.test.AndroidTestCase;

public class KeyValueStorageTests extends AndroidTestCase
{
    public void testInitialize()
    {
        Context context = getContext();
        KeyValueStorage storage = KeyValueStorage.getInstance(context);
        storage.initialize();
        assertEquals(storage.mDataMap.size(), DataLevel.values().length);
        for (DataLevel dataLevel : DataLevel.values())
        {
            assertNotNull(storage.mDataMap.get(dataLevel));
        }
    }

    public void testInsertAndQuery()
    {
        Context context = getContext();
        KeyValueStorage storage = KeyValueStorage.getInstance(context);
        storage.initialize();

        // test System level's storage
        storage.insert(DataLevel.SYSTEM_LVL, "test_system_key1", "test_system_value1");

        Bundle data = new Bundle();
        data.putString(DataLevel.KEY_DATALEVEL, DataLevel.SYSTEM_LVL.toString());
        data.putString("test_system_key2", "test_system_value2");
        storage.insert(data);

        String expect_value1 = storage.query(DataLevel.SYSTEM_LVL, "test_system_key1");
        String expect_value2 = storage.query(DataLevel.SYSTEM_LVL, "test_system_key2");

        Assert.assertEquals("test_system_value1", expect_value1);
        Assert.assertEquals("test_system_value2", expect_value2);

        storage.insert(DataLevel.USER_LVL, "test_user_key1", "test_user_value1");

        // start test user level's storage
        Bundle data2 = new Bundle();
        data2.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        data2.putString("test_user_key2", "test_user_value2");
        storage.insert(data2);

        String expect_value3 = storage.query(DataLevel.USER_LVL, "test_user_key1");
        String expect_value4 = storage.query(DataLevel.USER_LVL, "test_user_key2");

        Assert.assertEquals("test_user_value1", expect_value3);
        Assert.assertEquals("test_user_value2", expect_value4);
    }

    public void testUpdate()
    {
        Context context = getContext();
        KeyValueStorage storage = KeyValueStorage.getInstance(context);
        storage.initialize();

        storage.insert(DataLevel.SYSTEM_LVL, "test_system_key1", "test_system_value1");

        storage.update(DataLevel.SYSTEM_LVL, "test_system_key1", "new_test_system_value1");

        Assert.assertEquals("new_test_system_value1",
                storage.query(DataLevel.SYSTEM_LVL, "test_system_key1"));
    }
}
