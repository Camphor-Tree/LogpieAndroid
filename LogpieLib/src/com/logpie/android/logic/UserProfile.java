package com.logpie.android.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.logpie.android.util.LogpieLog;

public class UserProfile
{
    public static String KEY_USER_ID = "com.logpie.android.profile.user.id";
    public static String KEY_USER_NAME = "com.logpie.android.profile.user.name";
    public static String KEY_USER_GENDER = "com.logpie.android.profile.user.gender";
    public static String KEY_USER_BIRTHDAY = "com.logpie.android.profile.user.birthday";
    public static String KEY_USER_EMAIL = "com.logpie.android.profile.user.email";
    public static String KEY_USER_TEL = "com.logpie.android.profile.user.tel";

    private static String TAG = UserProfile.class.getName();
    private String mUserId;
    private String mUserName;
    private String mUserGender;
    private String mUserBirthday;
    private String mUserEmail;
    private String mUserTel;

    private UserProfile(String userID, String userName, String userGender, String userBirthday,
            String userEmail, String userTel)
    {
        mUserId = userID;
        mUserName = userName;
        mUserGender = userGender;
        mUserBirthday = userBirthday;
        mUserEmail = userEmail;
        mUserTel = userTel;
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
        if (!profileBundle.containsKey(KEY_USER_ID) || !profileBundle.containsKey(KEY_USER_NAME))
        {
            LogpieLog.e(TAG, "Must contain KEY_USER_ID or KEY_USER_NAME");
            return null;
        }

        String userId = profileBundle.getString(KEY_USER_ID);
        String userName = profileBundle.getString(KEY_USER_NAME);
        String gender = profileBundle.getString(KEY_USER_GENDER);
        String birthday = profileBundle.getString(KEY_USER_BIRTHDAY);
        String email = profileBundle.getString(KEY_USER_EMAIL);
        String tel = profileBundle.getString(KEY_USER_TEL);
        return new UserProfile(userId, userName, gender, birthday, email, tel);
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
            if (!profileJSON.has(KEY_USER_ID) || !profileJSON.has(KEY_USER_NAME))
            {
                LogpieLog.e(TAG, "Must contain KEY_USER_ID or KEY_USER_NAME");
                return null;
            }
            String userId = profileJSON.getString(KEY_USER_ID);
            String userName = profileJSON.getString(KEY_USER_NAME);
            String gender = profileJSON.getString(KEY_USER_GENDER);
            String birthday = profileJSON.getString(KEY_USER_BIRTHDAY);
            String email = profileJSON.getString(KEY_USER_EMAIL);
            String tel = profileJSON.getString(KEY_USER_TEL);
            return new UserProfile(userId, userName, gender, birthday, email, tel);
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
    public String getUserGender()
    {
        return mUserGender;
    }

    /**
     * @param userGender
     *            the userGender to set
     */
    public void setUserGender(String userGender)
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
     * @return the userTel
     */
    public String getUserTel()
    {
        return mUserTel;
    }

    /**
     * @param userTel
     *            the userTel to set
     */
    public void setUserTel(String userTel)
    {
        mUserTel = userTel;
    }

}
