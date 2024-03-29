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
        // Assert.assertFalse(storage.isSharedPreferencesExist());
        storage.initialize();

        assertEquals(storage.getDataMap().size(), DataLevel.values().length);
        for (DataLevel dataLevel : DataLevel.values())
        {
            assertNotNull(storage.getDataMap().get(dataLevel));
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

        Assert.assertTrue(storage.isDirectoryExist());
        Assert.assertTrue(storage.isSharedPreferencesExist());
    }

    public void testUpdate()
    {
        Context context = getContext();
        KeyValueStorage storage = KeyValueStorage.getInstance(context);
        storage.initialize();

        // Test for System Level
        storage.insert(DataLevel.SYSTEM_LVL, "test_system_key1", "test_system_value1");
        storage.update(DataLevel.SYSTEM_LVL, "test_system_key1", "new_test_system_value1");
        Assert.assertEquals("new_test_system_value1",
                storage.query(DataLevel.SYSTEM_LVL, "test_system_key1"));

        // Test for User Level
        storage.insert(DataLevel.USER_LVL, "test_user_key1", "test_user_value1");
        Assert.assertNotSame("new_test_system_value1",
                storage.query(DataLevel.USER_LVL, "test_user_key1"));
        storage.update(DataLevel.USER_LVL, "test_user_key1", "new_test_user_value1");
        Assert.assertEquals("new_test_user_value1",
                storage.query(DataLevel.USER_LVL, "test_user_key1"));
    }

    public void testDelete()
    {
        Context context = getContext();
        KeyValueStorage storage = KeyValueStorage.getInstance(context);
        storage.initialize();
        // Test for System Level
        storage.insert(DataLevel.SYSTEM_LVL, "test_system_key1_to_delete",
                "test_system_value1_to_delete");
        Assert.assertTrue(storage.delete(DataLevel.SYSTEM_LVL, "test_system_key1_to_delete"));
        Assert.assertEquals(storage.query(DataLevel.SYSTEM_LVL, "test_system_key1_to_delete"), null);

        // Test for User Level
        storage.insert(DataLevel.USER_LVL, "test_user_key1_to_delete", "test_user_value1_to_delete");
        Assert.assertEquals(storage.query(DataLevel.USER_LVL, "test_user_key1_to_delete"),
                "test_user_value1_to_delete");
        Assert.assertTrue(storage.delete(DataLevel.USER_LVL, "test_user_key1_to_delete"));
        Assert.assertEquals(storage.query(DataLevel.USER_LVL, "test_user_key1_to_delete"), null);

    }
}
