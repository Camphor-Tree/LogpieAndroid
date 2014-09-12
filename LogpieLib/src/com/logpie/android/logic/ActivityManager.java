package com.logpie.android.logic;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.logpie.android.connection.EndPoint.ServiceURL;
import com.logpie.android.connection.GenericConnection;
import com.logpie.android.util.LogpieCallback;

public class ActivityManager
{
    private static ActivityManager sActivityManager;
    private Context mContext;
    private ArrayList<LogpieActivity> mActivityList;

    private ActivityManager(Context context)
    {
        mContext = context;
        mActivityList = new ArrayList<LogpieActivity>();

        /**
         * Generate some crimes
         */

    }

    public synchronized static ActivityManager getInstance(Context context)
    {
        if (sActivityManager == null)
        {
            sActivityManager = new ActivityManager(context);
        }
        return sActivityManager;
    }

    public ArrayList<LogpieActivity> getActivityList()
    {
        return mActivityList;
    }

    public ArrayList<LogpieActivity> getActivityListFromServer(String uid,
            String requestType, LogpieCallback callback)
    {
        JSONObject RegData = new JSONObject();

        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.LogpieService, mContext);
        try
        {
            RegData.put("", "");

            connection.setRequestData(RegData);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(callback);

        String response = connection.getResponse();
        ArrayList<LogpieActivity> activityList = new ArrayList<LogpieActivity>();

        return activityList;
    }
}
