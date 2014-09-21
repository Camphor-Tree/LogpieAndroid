package com.logpie.android.util;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper
{
    public static JSONArray buildInsertKeyValue(ArrayList<String> columns,
            ArrayList<String> values) throws JSONException
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_INSERT_COLUMN, columns.get(i));
            o.put(RequestKeys.KEY_INSERT_VALUE, values.get(i));
        }
        return array;
    }

    public static JSONArray buildUpdateKeyValue(ArrayList<String> columns,
            ArrayList<String> values) throws JSONException
    {
        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_UPDATE_COLUMN, columns.get(i));
            o.put(RequestKeys.KEY_UPDATE_VALUE, values.get(i));
        }
        return array;
    }

    public static JSONArray buildQueryKey(ArrayList<String> columns) throws JSONException
    {
        if (columns == null)
        {
            return null;
        }

        JSONArray array = new JSONArray();
        for (int i = 0; i < columns.size(); i++)
        {
            JSONObject o = new JSONObject();
            o.put(RequestKeys.KEY_QUERY_COLUMN, columns.get(i));
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
        }
        return array;
    }
}
