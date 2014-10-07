package com.logpie.android.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.ResponseKeys;

public class UserProfile
{
    private static String TAG = UserProfile.class.getName();
    private String mUserId;
    private String mUserName;
    // true => male false => female
    private boolean mUserGender;
    private String mUserBirthday;
    private String mUserEmail;
    private String mUserCity;

    private UserProfile(final String userID, final String userName, final boolean userGender,
            final String userBirthday, final String userEmail, final String userCity)
    {
        mUserId = userID;
        mUserName = userName;
        mUserGender = userGender;
        mUserBirthday = userBirthday;
        mUserEmail = userEmail;
        mUserCity = userCity;
    }

    /**
     * Build the UserProfile from bundle.
     * 
     * Bundle Keys: {@link UserProfile#KEY_USER_ID}
     * {@link UserProfile#KEY_USER_NAME} {@link UserProfile#KEY_USER_GENDER}
     * {@link UserProfile#KEY_USER_EMAIL}{@link UserProfile#KEY_USER_TEL}
     * {@link UserProfile#KEY_USER_BIRTHDAY}
     * 
     * @param profileBundle
     *            bundle must contain information about user_id & user_name
     * @return
     */
    public static UserProfile initializeUserProfile(Bundle profileBundle)
    {
        // check whether contains user_id and user_name.
        // user_id, user_name are necessary
        if (!profileBundle.containsKey(ResponseKeys.KEY_UID)
                || !profileBundle.containsKey(ResponseKeys.KEY_NICKNAME))
        {
            LogpieLog.e(TAG, "Must contain KEY_UID or KEY_NICKNAME");
            return null;
        }

        String userId = profileBundle.getString(ResponseKeys.KEY_UID);
        String userName = profileBundle.getString(ResponseKeys.KEY_NICKNAME);
        boolean gender = profileBundle.getBoolean(ResponseKeys.KEY_GENDER);
        String birthday = profileBundle.getString(ResponseKeys.KEY_BIRTHDAY);
        String email = profileBundle.getString(ResponseKeys.KEY_EMAIL);
        String city = profileBundle.getString(ResponseKeys.KEY_CITY);
        return new UserProfile(userId, userName, gender, birthday, email, city);
    }

    /**
     * Build the UserProfile from org.json.JSONObject.
     * 
     * JSON Keys: {@link UserProfile#KEY_USER_ID}
     * {@link UserProfile#KEY_USER_NAME} {@link UserProfile#KEY_USER_GENDER}
     * {@link UserProfile#KEY_USER_EMAIL}{@link UserProfile#KEY_USER_TEL}
     * {@link UserProfile#KEY_USER_BIRTHDAY}
     * 
     * @param profileJSON
     *            JSON must contain information about user_id & user_name
     * @return
     */
    public static UserProfile initializeUserProfile(JSONObject profileJSON)
    {
        try
        {
            if (!profileJSON.has(ResponseKeys.KEY_UID)
                    || !profileJSON.has(ResponseKeys.KEY_NICKNAME))
            {
                LogpieLog.e(TAG, "Must contain KEY_UID or KEY_NICKNAME");
                return null;
            }

            String userId = profileJSON.getString(ResponseKeys.KEY_UID);
            String userName = profileJSON.getString(ResponseKeys.KEY_NICKNAME);
            boolean gender = profileJSON.getBoolean(ResponseKeys.KEY_GENDER);
            String birthday = profileJSON.getString(ResponseKeys.KEY_BIRTHDAY);
            String email = profileJSON.getString(ResponseKeys.KEY_EMAIL);
            String city = profileJSON.getString(ResponseKeys.KEY_CITY);

            return new UserProfile(userId, userName, gender, birthday, email, city);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when try to retrieve the profileJSON");
            LogpieLog.e(TAG, e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return the userId
     */
    public String getUserId()
    {
        return mUserId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId)
    {
        mUserId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return mUserName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        mUserName = userName;
    }

    /**
     * @return the userGender
     */
    public boolean getUserGender()
    {
        return mUserGender;
    }

    /**
     * @param userGender
     *            the userGender to set
     */
    public void setUserGender(boolean userGender)
    {
        mUserGender = userGender;
    }

    /**
     * @return the userBirthday
     */
    public String getUserBirthday()
    {
        return mUserBirthday;
    }

    /**
     * @param userBirthday
     *            the userBirthday to set
     */
    public void setUserBirthday(String userBirthday)
    {
        mUserBirthday = userBirthday;
    }

    /**
     * @return the userEmail
     */
    public String getUserEmail()
    {
        return mUserEmail;
    }

    /**
     * @param userEmail
     *            the userEmail to set
     */
    public void setUserEmail(String userEmail)
    {
        mUserEmail = userEmail;
    }

    /**
     * @return the userCity
     */
    public String getUserCity()
    {
        return mUserCity;
    }

    /**
     * @param userCity
     *            the userCity to set
     */
    public void setUserCity(String userCity)
    {
        mUserCity = userCity;
    }

}
