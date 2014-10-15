package com.logpie.android.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.util.JSONHelper;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;
import com.logpie.commonlib.RequestKeys;
import com.logpie.commonlib.ResponseKeys;

public class ActivityManager
{
    private static final String TAG = ActivityManager.class.getName();
    // Each request will just request 25 records.
    private static final int MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL = 25;
    public static final int MODE_INITIAL = 0;
    public static final int MODE_LOAD_MORE = 1;
    public static final int MODE_REFRESH_MORE = 2;

    private static ActivityManager sActivityManager;
    private Context mContext;
    private long mTopActivityID;
    private long mBottomActivityID;

    private ActivityManager(Context context)
    {
        mContext = context;
    }

    public synchronized static ActivityManager getInstance(Context context)
    {
        if (sActivityManager == null)
        {
            sActivityManager = new ActivityManager(context);
        }
        return sActivityManager;
    }

    /**
     * Return the 25 latest activities from server
     * 
     * @param user
     * @param mode
     * @param callback
     * @return
     */
    public void getNearbyActivityList(User user, String lat, String lon, int mode,
            LogpieCallback callback)
    {
        JSONObject postData = new JSONObject();

        // TODO: Authenticate the token
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, mContext);

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE, RequestKeys.SERVICE_FIND_NEARBY_ACTIVITY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_QUERY);

            JSONArray queryKey = JSONHelper.buildQueryKey(null);
            postData.put(RequestKeys.KEY_QUERY_KEY, queryKey);

            ArrayList<String> column = new ArrayList<String>();
            ArrayList<String> operator = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();

            lat = "1";
            lon = "2";

            if (lat == null || lon == null)
            {
                LogpieLog.e(TAG, "Cannot get the latitude & longitude.");
                return;
            }

            column.add(RequestKeys.KEY_LATITUDE);
            operator.add(RequestKeys.KEY_EQUAL);
            value.add(lat);
            column.add(RequestKeys.KEY_LONGITUDE);
            operator.add(RequestKeys.KEY_EQUAL);
            value.add(lon);

            switchMode(mode, column, operator, value);

            JSONArray constraintKeyValue = JSONHelper.buildConstraintKeyValue(column, operator,
                    value);
            postData.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constraintKeyValue);

            postData.put(RequestKeys.KEY_LIMIT_NUMBER,
                    Integer.toString(MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL));

            connection.setRequestData(postData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when get nearby activity list");
            return;
        }
        try
        {
            connection.send(callback).get();
        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException happened when get nearby activity list");
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            LogpieLog.e(TAG, "ExecutionException happened when get nearby activity list");
            e.printStackTrace();
        }
    }

    public void getActivityListByCity(User user, int mode, String city, LogpieCallback callback)
    {
        JSONObject postData = new JSONObject();

        // TODO: Authenticate the token
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, mContext);

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE, RequestKeys.SERVICE_FIND_ACTIVITY_BY_CITY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_QUERY);

            JSONArray queryKey = JSONHelper.buildQueryKey(null);
            postData.put(RequestKeys.KEY_QUERY_KEY, queryKey);

            ArrayList<String> column = new ArrayList<String>();
            ArrayList<String> operator = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();

            if (city == null)
            {
                if (user.getUserProfile() == null)
                {
                    LogpieLog.e(TAG, "Cannot find the profile when trying to get the city");
                    return;
                }
                else
                {
                    UserProfile profile = user.getUserProfile();
                    city = profile.getUserCity();
                    if (city == null)
                    {
                        LogpieLog.e(TAG, "Cannot find the city from the user profile.");
                        return;
                    }

                }
            }
            column.add(RequestKeys.KEY_CITY);
            operator.add(RequestKeys.KEY_EQUAL);
            value.add(city);

            switchMode(mode, column, operator, value);

            JSONArray constraintKeyValue = JSONHelper.buildConstraintKeyValue(column, operator,
                    value);
            postData.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constraintKeyValue);

            postData.put(RequestKeys.KEY_LIMIT_NUMBER,
                    Integer.toString(MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL));

            connection.setRequestData(postData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when get activity list by city", e);
            return;
        }
        try
        {
            connection.send(callback).get();
        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException happened when get activity list by city", e);
        } catch (ExecutionException e)
        {
            LogpieLog.e(TAG, "ExecutionException happened when get activity list by city", e);
        }
    }

    public void getActivityListByCategory(User user, int mode, String category, String subCategory,
            LogpieCallback callback)
    {
        JSONObject postData = new JSONObject();

        // TODO: Authenticate the token
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, mContext);

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE,
                    RequestKeys.SERVICE_FIND_ACTIVITY_BY_CATEGORY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_QUERY);

            JSONArray queryKey = JSONHelper.buildQueryKey(null);
            postData.put(RequestKeys.KEY_QUERY_KEY, queryKey);

            ArrayList<String> column = new ArrayList<String>();
            ArrayList<String> operator = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();

            if (category == null)
            {
                LogpieLog.e(TAG, "Cannot find the category");
                return;
            }
            column.add(RequestKeys.KEY_CATEGORY);
            operator.add(RequestKeys.KEY_EQUAL);
            value.add(category);

            if (subCategory != null)
            {
                column.add(RequestKeys.KEY_SUBCATEGORY);
                operator.add(RequestKeys.KEY_EQUAL);
                value.add(subCategory);
            }

            switchMode(mode, column, operator, value);

            JSONArray constraintKeyValue = JSONHelper.buildConstraintKeyValue(column, operator,
                    value);
            postData.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constraintKeyValue);

            postData.put(RequestKeys.KEY_LIMIT_NUMBER,
                    Integer.toString(MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL));

            connection.setRequestData(postData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when get activity list by category");
            return;
        }
        try
        {
            connection.send(callback).get();
        } catch (InterruptedException e)
        {
            LogpieLog.e(TAG, "InterruptedException happened when get activity list by category");
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            LogpieLog.e(TAG, "ExecutionException happened when get activity list by category");
            e.printStackTrace();
        }
    }

    public void createActivity(final LogpieActivity logpieActivity, final LogpieCallback callback)
    {
        JSONObject postData = new JSONObject();

        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, mContext);

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE, RequestKeys.SERVICE_CREATE_ACTIVITY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_INSERT);
            ArrayList<String> values = logpieActivity.getCreateAcitivtyValues();
            if (values == null)
            {
                LogpieLog.e(TAG, "The values of the LogpieActivity is null!");
                return;
            }
            JSONArray insertKeyValuePair = JSONHelper.buildInsertKeyValue(
                    logpieActivity.getCreateAcitivtyKeys(), values);
            postData.put(RequestKeys.KEY_INSERT_KEYVALUE_PAIR, insertKeyValuePair);

        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when create activity!");
            return;
        }
        connection.setRequestData(postData);
        connection.send(callback);
    }

    private void switchMode(int mode, ArrayList<String> column, ArrayList<String> operator,
            ArrayList<String> value)
    {
        switch (mode)
        {
        case 0:
            break;
        case 1:
            if (mBottomActivityID > 0)
            {
                column.add(RequestKeys.KEY_AID);
                operator.add(RequestKeys.KEY_LESS_THAN);
                value.add(String.valueOf(mBottomActivityID));
            }
            else
            {
                LogpieLog.e(TAG, "Cannot find the bottom activity ID. Should switch to Mode 0.");
            }
            break;
        case 2:
            if (mTopActivityID > 0)
            {
                column.add(RequestKeys.KEY_AID);
                operator.add(RequestKeys.KEY_MORE_THAN);
                value.add(String.valueOf(mTopActivityID));
            }
            else
            {
                LogpieLog.e(TAG, "Cannot find the top activity ID. Should switch to Mode 0.");
            }
            break;
        default:
            break;
        }
    }

    public class ActivityCallbackAdapter implements LogpieCallback
    {
        ActivityCallback mActivityCallback;

        public ActivityCallbackAdapter(ActivityCallback callback)
        {
            mActivityCallback = callback;
        }

        @Override
        public void onSuccess(Bundle result)
        {
            ArrayList<LogpieActivity> activityList = new ArrayList<LogpieActivity>();
            String responseData = result.getString(GenericConnection.KEY_RESPONSE_DATA);
            if (responseData == null)
            {
                Bundle errorMessage = new Bundle();
                LogpieLog.e(TAG, "The metadata is null when parsing the response data.");
                mActivityCallback.onError(errorMessage);
            }
            else
            {
                try
                {
                    JSONObject data = new JSONObject(responseData);
                    parseJSONData(data, activityList);
                    LogpieLog.d(TAG, "Successfully parsed response data.");
                    mActivityCallback.onSuccess(activityList);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                    LogpieLog
                            .e(TAG,
                                    "JSONException happened when building a JSON object from the string metada");
                }
            }

        }

        @Override
        public void onError(Bundle errorMessage)
        {
            mActivityCallback.onError(errorMessage);
        }

        private ArrayList<LogpieActivity> parseJSONData(JSONObject data,
                ArrayList<LogpieActivity> activityList)
        {
            try
            {
                // Log the request id, and the response data.
                if (data.isNull(ResponseKeys.KEY_RESPONSE_ID))
                {
                    LogpieLog.e(TAG, "Cannot find the response ID from the response data.");
                }
                else
                {
                    String requestID = data.getString(ResponseKeys.KEY_RESPONSE_ID);
                    LogpieLog.d(TAG, "Receiving response from server, request_id is:" + requestID);
                }
                LogpieLog.d(TAG,
                        "Receiving response from server, responseData is:" + data.toString());

                if (data.isNull(ResponseKeys.KEY_ACTIVITY_RESULT)
                        || !data.getString(ResponseKeys.KEY_ACTIVITY_RESULT).equals(
                                ResponseKeys.RESULT_SUCCESS))
                {
                    LogpieLog.e(TAG,
                            "Failed to get the successful activity result from the response data.");
                    return null;
                }

                if (data.isNull(ResponseKeys.KEY_REQUEST_TYPE)
                        || !data.getString(ResponseKeys.KEY_REQUEST_TYPE).equals(
                                ResponseKeys.REQUEST_TYPE_QUERY))
                {
                    LogpieLog.e(TAG,
                            "Failed to get the correct request type from the response data.");
                    return null;
                }

                if (data.isNull(ResponseKeys.KEY_METADATA)
                        || data.getJSONArray(ResponseKeys.KEY_METADATA) == null)
                {
                    LogpieLog.e(TAG, "Failed to get the metadata from the response data.");
                    return null;
                }

                JSONArray metadata = data.getJSONArray(ResponseKeys.KEY_METADATA);
                for (int i = 0; i < metadata.length(); i++)
                {
                    LogpieLog.d(TAG, "Metadata has " + String.valueOf(metadata.length())
                            + " JSON objects.");
                    JSONObject o = metadata.getJSONObject(i);
                    LogpieActivity activity = LogpieActivity.ActivityJSONHelper(o);
                    activityList.add(activity);
                }
                return activityList;
            } catch (JSONException e)
            {
                LogpieLog.e(TAG, "JSONException happened when parsing the response data.", e);
            }
            return null;
        }
    }

    public interface ActivityCallback
    {
        public void onSuccess(List<LogpieActivity> activityList);

        public void onError(Bundle errorMessage);
    }
}
