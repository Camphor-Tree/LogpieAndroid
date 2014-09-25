package com.logpie.android.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.connection.EndPoint.ServiceURL;
import com.logpie.android.connection.GenericConnection;
import com.logpie.android.util.JSONHelper;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.RequestKeys;
import com.logpie.android.util.ResponseKeys;

public class ActivityManager
{
    private static final String TAG = ActivityManager.class.getName();
    // Each request will just request 25 records.
    private static final int MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL = 25;
    public static final int MODE_INITIAL = 0;
    public static final int MODE_LOAD_MORE = 1;
    public static final int MODE_REFRESH_MORE = 2;

    private static ActivityManager sActivityManager;
    private List mActivityList;
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
    public void getNearbyActivityList(User user, int mode, LogpieCallback callback)
    {

    }

    public void getActivityListByCity(User user, int mode, String city,
            LogpieCallback callback)
    {
        JSONObject postData = new JSONObject();

        // TODO: Authenticate the token
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.ActivityService, mContext);

        try
        {
            postData.put(RequestKeys.KEY_REQUEST_SERVICE,
                    RequestKeys.SERVICE_FIND_ACTIVITY_BY_CITY);
            postData.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_QUERY);

            JSONArray queryKey = JSONHelper.buildQueryKey(null);
            postData.put(RequestKeys.KEY_QUERY_KEY, queryKey);

            ArrayList<String> column = new ArrayList<String>();
            ArrayList<String> operator = new ArrayList<String>();
            ArrayList<String> value = new ArrayList<String>();

            city = "北京市";

            if (city == null)
            {
                if (user.getUserProfile().getUserCity() == null)
                {
                    LogpieLog.e(TAG, "Cannot find the city");
                    return;
                }
                else
                {
                    city = user.getUserProfile().getUserCity();
                }
            }
            column.add(RequestKeys.KEY_CITY);
            operator.add(RequestKeys.KEY_EQUAL);
            value.add(city);

            switchMode(mode, column, operator, value);

            JSONArray constraintKeyValue = JSONHelper.buildConstraintKeyValue(column,
                    operator, value);
            postData.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constraintKeyValue);

            postData.put(RequestKeys.KEY_LIMIT_NUMBER,
                    Integer.toString(MAXIMUM_ACTIVITY_RECORD_PER_SERVICE_CALL));

            connection.setRequestData(postData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when get activity list by city");
            return;
        }
        connection.send(callback);
    }

    private void switchMode(int mode, ArrayList<String> column,
            ArrayList<String> operator, ArrayList<String> value)
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
                LogpieLog.e(TAG,
                        "Cannot find the bottom activity ID. Should switch to Mode 0.");
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
                LogpieLog.e(TAG,
                        "Cannot find the top activity ID. Should switch to Mode 0.");
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
                errorMessage.putString(ResponseKeys.KEY_ERROR_MESSAGE,
                        "Response data is null.");
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
                if (data.isNull(ResponseKeys.KEY_RESPONSE_ID))
                {
                    LogpieLog.e(TAG,
                            "Cannot find the response ID from the response data.");
                }
                String requestID = data.getString(ResponseKeys.KEY_RESPONSE_ID);

                if (data.isNull(ResponseKeys.KEY_ACTIVITY_RESULT)
                        || !data.getString(ResponseKeys.KEY_ACTIVITY_RESULT).equals(
                                ResponseKeys.RESULT_SUCCESS))
                {
                    LogpieLog
                            .e(TAG,
                                    "Failed to get the successful activity result from the response data.");
                    return null;
                }

                if (data.isNull(ResponseKeys.KEY_REQUEST_TYPE)
                        || !data.getString(ResponseKeys.KEY_REQUEST_TYPE).equals(
                                ResponseKeys.REQUEST_TYPE_QUERY))
                {
                    LogpieLog
                            .e(TAG,
                                    "Failed to get the correct request type from the response data.");
                    return null;
                }

                if (data.isNull(ResponseKeys.KEY_METADATA)
                        || data.getJSONArray(ResponseKeys.KEY_METADATA) == null)
                {
                    LogpieLog
                            .e(TAG, "Failed to get the metadata from the response data.");
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
                LogpieLog
                        .e(TAG, "JSONException happened when parsing the response data.");
                e.printStackTrace();
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
