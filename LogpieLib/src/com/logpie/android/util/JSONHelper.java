package com.logpie.android.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.logpie.commonlib.RequestKeys;

//TODO: refactory this class to use Map<String,String>
public class JSONHelper
{
    public static JSONArray buildInsertKeyValue(ArrayList<String> columns, ArrayList<String> values)
            throws JSONException
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_INSERT_COLUMN, columns.get(i));
            o.put(RequestKeys.KEY_INSERT_VALUE, values.get(i));
            array.put(o);
        }
        return array;
    }

    public static JSONArray buildUpdateKeyValue(ArrayList<String> columns, ArrayList<String> values)
            throws JSONException
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_UPDATE_COLUMN, columns.get(i));
            o.put(RequestKeys.KEY_UPDATE_VALUE, values.get(i));
            array.put(0);
        }
        return array;
    }

    public static JSONArray buildQueryKey(ArrayList<String> columns) throws JSONException
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

    public static JSONArray buildConstraintKeyValue(ArrayList<String> columns,
            ArrayList<String> operators, ArrayList<String> values) throws JSONException
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_CONSTRAINT_COLUMN, columns.get(i));
            o.put(RequestKeys.KEY_CONSTRAINT_OPERATOR, operators.get(i));
            o.put(RequestKeys.KEY_CONSTRAINT_VALUE, values.get(i));
            array.put(o);
        }
        return array;
    }
}
