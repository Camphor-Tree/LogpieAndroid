package com.logpie.android.logic;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;
import com.logpie.commonlib.RequestKeys;

/**
 * Central User, abstract user's behavior.
 * 
 * @author yilei
 */
public abstract class User
{
    public static String KEY_USER_ID = "com.logpie.user.uid";
    public static String KEY_USER_NICKNAME = "com.logpie.user.nickname";
    public static String KEY_USER_GENDER = "com.logpie.user.gender";
    public static String KEY_USER_EMAIL = "com.logpie.user.email";
    public static String KEY_USER_CITY = "com.logpie.user.city";

    private static String TAG = User.class.getName();
    protected UserProfile mUserProfile;
    protected Context mContext;

    protected User(Context context)
    {
        mContext = context;
    }

    public void login()
    {

    }

    public void register(Context context, String userEmail, String userPassword, String userName,
            String city, LogpieCallback callback)
    {
        JSONObject AuthRegData = new JSONObject();

        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.AuthenticationService, mContext);
        try
        {
            AuthRegData.put("auth_type", "REGISTER");
            AuthRegData.put("register_email", userEmail);
            AuthRegData.put("register_password", userPassword);
            AuthRegData.put("register_nickname", userName);
            AuthRegData.put("register_city", city);

            connection.setRequestData(AuthRegData);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        connection.send(callback);
    }

    public void updateProfile(final String key, final String value)
    {
        if (TextUtils.equals(key, KEY_USER_NICKNAME))
        {
            mUserProfile.setUserName(value);
        }
        else if (TextUtils.equals(key, KEY_USER_GENDER))
        {
            mUserProfile.setUserGender(Boolean.valueOf(value));
        }
        else if (TextUtils.equals(key, KEY_USER_EMAIL))
        {
            mUserProfile.setUserEmail(value);
        }
        else if (TextUtils.equals(key, KEY_USER_CITY))
        {
            mUserProfile.setUserCity(value);
        }
        else
        {
            LogpieLog.e(TAG, "try to update key:" + key + " value:" + value);
            throw new IllegalArgumentException(
                    "Cannot update unsupported key or the key cannot be updated");
        }
        syncUserProfileToServer(key, value);
    }

    public void changePassword()
    {

    }

    public void findPasswordBack()
    {

    }

    public void changeProfile()
    {

    }

    public UserProfile getUserProfile()
    {
        return mUserProfile;
    }

    public void changeSystemSetting()
    {

    }

    public void getUserDetail()
    {

    }

    public void shareLogpieActivity(LogpieActivity logpieActivity, SNS sns)
    {

    }

    public void rateOrganization(Organization organization, int rate)
    {

    }

    public void commentOrganization(Organization organization, Comment comment)
    {

    }

    public void createNewLogpieActivity(LogpieActivity logpieActivity)
    {

    }

    public void deleteLogpieActivity(LogpieActivity logpieActivity)
    {

    }

    public void editLogpieActivity(LogpieActivity logpieActivity)
    {
    }

    public void getLogpieActivityList(LogpieLocation location)
    {

    }

    public void getCreatedLogpieActivityList()
    {
    }

    public void getLogpieActivityDetail()
    {
    }

    public void commentLogpieActivity(LogpieActivity logpieActivity, Comment comment)
    {
    }

    public void collectLogpieActivity(LogpieActivity logpieActivity)
    {
    }

    public void getCollections()
    {
    }

    public void likeLogpieActivity(LogpieActivity logpieActivity)
    {
    }

    public void reportLogpieActivity(LogpieActivity logpieActivity, Comment comment)
    {
    }

    public abstract void joinOrganization(Organization organization);

    public abstract void getJoinedOrganizationList();

    public abstract void quitOrganization(Organization organization);

    private void syncUserProfileToServer(final String key, final String value)
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.CustomerService, mContext);
        JSONArray updateKeyValueJSONArray = new JSONArray();
        JSONObject updateKeyValueJSON = new JSONObject();
        try
        {
            updateKeyValueJSON.put(key, value);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when build the update JSON.", e);
        }
        updateKeyValueJSONArray.put(updateKeyValueJSON);

        JSONArray constaintKeyValueJSONArray = new JSONArray();
        JSONObject constaintKeyValueJSON = new JSONObject();
        try
        {
            constaintKeyValueJSON.put(RequestKeys.KEY_CONSTRAINT_COLUMN, RequestKeys.KEY_UID);
            constaintKeyValueJSON.put(RequestKeys.KEY_CONSTRAINT_OPERATOR, RequestKeys.KEY_EQUAL);
            constaintKeyValueJSON.put(RequestKeys.KEY_CONSTRAINT_VALUE, mUserProfile.getUserId());
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when build the constaint JSON.", e);
        }
        constaintKeyValueJSONArray.put(updateKeyValueJSON);

        JSONObject requestJSON = new JSONObject();
        try
        {
            requestJSON.put(RequestKeys.KEY_REQUEST_SERVICE, RequestKeys.SERVICE_EDIT_PROFILE);
            requestJSON.put(RequestKeys.KEY_REQUEST_TYPE, RequestKeys.REQUEST_TYPE_UPDATE);
            requestJSON.put(RequestKeys.KEY_REQUEST_ID, UUID.randomUUID());
            requestJSON.put(RequestKeys.KEY_UPDATE_KEYVALUE_PAIR, updateKeyValueJSONArray);
            requestJSON.put(RequestKeys.KEY_CONSTRAINT_KEYVALUE_PAIR, constaintKeyValueJSONArray);

        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when build the request JSON.", e);
        }
        connection.setRequestData(requestJSON);
        connection.send(new LogpieCallback()
        {

            @Override
            public void onSuccess(Bundle result)
            {
                LogpieLog.e(TAG, "Update successful");
            }

            @Override
            public void onError(Bundle errorMessage)
            {
                LogpieLog.e(TAG, "Update fail");
            }
        });
    }
}
