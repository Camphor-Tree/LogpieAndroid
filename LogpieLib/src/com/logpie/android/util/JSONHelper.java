package com.logpie.android.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.logpie.commonlib.RequestKeys;

// TODO: refactory this class to use Map<String,String>
public class JSONHelper
{
    private static final String TAG = JSONHelper.class.getName();

    public static JSONArray buildInsertKeyValue(Map<String, String> insertKeyvalue)
            throws JSONException
    {
        if (insertKeyvalue.isEmpty())
        {
            LogpieLog.e(TAG, "There is no key value mappings in Insert Keyvalue Pair.");
            return null;
        }

        JSONArray array = new JSONArray();
        Set<String> keys = insertKeyvalue.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext())
        {
            String key = i.next();
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_INSERT_COLUMN, key);
            o.put(RequestKeys.KEY_INSERT_VALUE, insertKeyvalue.get(key));
            array.put(o);
        }
        return array;
    }

    public static JSONArray buildUpdateKeyValue(Map<String, String> updateKeyvalue)
            throws JSONException
    {
        if (updateKeyvalue.isEmpty())
        {
            LogpieLog.e(TAG, "There is no key value mappings in Update Keyvalue Pair.");
            return null;
        }

        JSONArray array = new JSONArray();
        Set<String> keys = updateKeyvalue.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext())
        {
            String key = i.next();
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_UPDATE_COLUMN, key);
            o.put(RequestKeys.KEY_UPDATE_VALUE, updateKeyvalue.get(key));
            array.put(o);
        }
        return array;
    }

    public static JSONArray buildQueryKey(List<String> columns) throws JSONException
    {
        JSONArray array = new JSONArray();
        if (columns == null)
        {
            return array;
        }

        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_QUERY_COLUMN, columns.get(i));
            array.put(o);
        }
        return array;
    }

    public static JSONArray buildConstraintKeyValue(Map<String, Map<String, String>> constraints,
            Map<String, String> tableLinkConstraint) throws JSONException
    {
        if (constraints.isEmpty())
        {
            LogpieLog.e(TAG, "There is no key value mappings in Constraint Keyvalue Pair.");
            return null;
        }

        JSONArray array = new JSONArray();
        Set<String> keys = constraints.keySet();
        Iterator<String> i = keys.iterator();
        while (i.hasNext())
        {
            JSONObject o = new JSONObject();
            String key = i.next();
            Map<String, String> map = constraints.get(key);
            if (map.isEmpty())
            {
                LogpieLog.e(TAG, "There is no operator mappings in Constraint Keyvalue Pair.");
                return null;
            }

            Set<String> operators = map.keySet();
            if (operators.size() == 0 || operators.size() > 1)
            {
                LogpieLog
                        .e(TAG, "There is error in operator mappings of Constraint Keyvalue Pair.");
                return null;
            }
            String operator = operators.iterator().next();
            String value = map.get(operator);

            o.put(RequestKeys.KEY_CONSTRAINT_COLUMN, key);
            o.put(RequestKeys.KEY_CONSTRAINT_OPERATOR, operator);
            o.put(RequestKeys.KEY_CONSTRAINT_VALUE, value);
            array.put(o);
        }

        if (tableLinkConstraint != null && tableLinkConstraint.size() > 0)
        {
            Set<String> tableLinkKeySet = tableLinkConstraint.keySet();
            for (String key : tableLinkKeySet)
            {
                JSONObject tableLinkObject = new JSONObject();
                tableLinkObject.put(RequestKeys.KEY_CONSTRAINT_COLUMN, key);
                tableLinkObject.put(RequestKeys.KEY_CONSTRAINT_OPERATOR, RequestKeys.KEY_EQUAL);
                tableLinkObject.put(RequestKeys.KEY_CONSTRAINT_LINK_COLUMN,
                        tableLinkConstraint.get(key));
                array.put(tableLinkObject);
            }

        }
        return array;
    }
}
