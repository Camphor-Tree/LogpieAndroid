package com.logpie.android.logic;

import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.EncryptedDataStorage;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;
import com.logpie.commonlib.RequestKeys;
import com.logpie.commonlib.ResponseKeys;

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
    private AuthManager mAuthManager;
    private EncryptedDataStorage mDataStorage;

    protected User(Context context)
    {
        mContext = context;
        mAuthManager = AuthManager.getInstance(mContext);
        mDataStorage = EncryptedDataStorage.getInstance(mContext.getApplicationContext());
    }

    public void login()
    {

    }

    public void login(final String email, final String password, final LogpieCallback callback)
    {
        LogpieCallback wrapCallback = new LogpieCallback()
        {

            @Override
            public void onSuccess(Bundle result)
            {
                String responseString = result.getString(GenericConnection.KEY_RESPONSE_DATA);
                LogpieLog.d(TAG, "The login response from server:" + responseString);
                try
                {
                    JSONObject responseJSON = new JSONObject(responseString);
                    String accessToken = responseJSON.getString(ResponseKeys.KEY_ACCESS_TOKEN);
                    String refreshToken = responseJSON.getString(ResponseKeys.KEY_REFRESH_TOKEN);
                    String uid = responseJSON.getString(ResponseKeys.KEY_UID);
                    boolean addAccountSuccess = mAuthManager.addAccount(new LogpieAccount(uid,
                            email, "Tester name", accessToken, refreshToken));
                    LogpieLog.d(TAG, "AuthManager addAccount result:" + addAccountSuccess);
                    if (!addAccountSuccess)
                    {
                        return;
                    }
                    UserProfile userProfile = new UserProfile(uid, "Tester Name", true, email, null);
                    setUserProfile(userProfile);
                } catch (JSONException e)
                {
                    LogpieLog.e(TAG, "JSONException happened when parsing the response", e);
                    return;
                }
                callback.onSuccess(null);
            }

            @Override
            public void onError(Bundle errorMessage)
            {
                callback.onError(null);
            }
        };
        GenericConnection connection = new GenericConnection();
        connection.initialize(ServiceURL.AuthenticationService, mContext);
        JSONObject authenticateData = new JSONObject();
        try
        {
            authenticateData.put(RequestKeys.KEY_REQUEST_TYPE, "AUTHENTICATE");
            authenticateData.put(RequestKeys.KEY_EMAIL, email);
            authenticateData.put(RequestKeys.KEY_PASSWORD, password);
            authenticateData.put(RequestKeys.KEY_REQUEST_ID, UUID.randomUUID().toString());
            LogpieLog.d(TAG, "Register String" + authenticateData.toString());
            connection.setRequestData(authenticateData);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException happened when try to build the requeset data", e);
        }

        connection.send(wrapCallback);
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

    /**
     * Update the user profile
     * 
     * Note: this call will sync the data to server and also to local data
     * storage.
     * 
     * @param key
     * @param value
     */
    public synchronized void updateProfile(final String key, final String value)
    {
        final String previousValue = mDataStorage.getValue(DataLevel.USER_LVL, key);

        // Update cache first
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
        // Sync to local data storage
        boolean syncLocalDataBaseSuccess = syncUserProfileToDataStore(key, value);
        // If success then sync to server
        if (syncLocalDataBaseSuccess)
        {
            syncUserProfileToServer(key, value, new LogpieCallback()
            {

                @Override
                public void onSuccess(Bundle result)
                {
                    LogpieLog.e(TAG, "Update successful");
                }

                @Override
                public void onError(Bundle errorMessage)
                {
                    // If sync server fail, then set the cache back.
                    localUpdateProfileCache(key, previousValue);
                    // If sync server fail, then set the lcoal data storage
                    // back.
                    localUpdateProfileCache(key, previousValue);
                }
            });
        }
        else
        {
            // If sync local data storage fail, then set the cache back.
            localUpdateProfileCache(key, previousValue);
        }
    }

    private void localUpdateProfileCache(final String key, final String value)
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
    }

    public void changePassword()
    {

    }

    public void findPasswordBack()
    {

    }

    public void setUserProfile(final UserProfile userProfile)
    {
        mUserProfile = userProfile;
        String uid = userProfile.getUserId();
        String nickname = userProfile.getUserName();
        boolean gender = userProfile.getUserGender();
        String genderString = Boolean.toString(gender);
        String email = userProfile.getUserEmail();
        String city = userProfile.getUserCity();
        Bundle profileBundle = buildDataStoreProfileBundle(uid, nickname, genderString, email, city);
        mDataStorage.setKeyValueBundle(profileBundle);
    }

    public UserProfile getUserProfile()
    {
        if (mUserProfile == null)
        {
            String uid = mDataStorage.getValue(DataLevel.USER_LVL, KEY_USER_ID);
            String nickname = mDataStorage.getValue(DataLevel.USER_LVL, KEY_USER_NICKNAME);
            String genderString = mDataStorage.getValue(DataLevel.USER_LVL, KEY_USER_GENDER);
            boolean gender = Boolean.valueOf(genderString);
            String email = mDataStorage.getValue(DataLevel.USER_LVL, KEY_USER_EMAIL);
            String city = mDataStorage.getValue(DataLevel.USER_LVL, KEY_USER_CITY);
            mUserProfile = new UserProfile(uid, nickname, gender, email, city);
        }
        return mUserProfile;
    }

    public void cleanUserProfile()
    {
        mDataStorage.delete(DataLevel.USER_LVL, KEY_USER_ID);
        mDataStorage.delete(DataLevel.USER_LVL, KEY_USER_NICKNAME);
        mDataStorage.delete(DataLevel.USER_LVL, KEY_USER_GENDER);
        mDataStorage.delete(DataLevel.USER_LVL, KEY_USER_EMAIL);
        mDataStorage.delete(DataLevel.USER_LVL, KEY_USER_CITY);
        mUserProfile = null;
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

    private boolean syncUserProfileToDataStore(final String key, final String value)
    {
        return mDataStorage.setKeyValue(DataLevel.USER_LVL, key, value);
    }

    private void syncUserProfileToServer(final String key, final String value,
            final LogpieCallback callback)
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
        connection.send(callback);
    }

    private Bundle buildDataStoreProfileBundle(final String uid, final String nickname,
            final String gender, final String email, final String city)
    {
        Bundle profileBundle = new Bundle();
        profileBundle.putString(KEY_USER_ID, uid);
        profileBundle.putString(KEY_USER_NICKNAME, nickname);
        profileBundle.putString(KEY_USER_GENDER, gender);
        profileBundle.putString(KEY_USER_EMAIL, email);
        profileBundle.putString(KEY_USER_CITY, city);
        profileBundle.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        return profileBundle;
    }
}
