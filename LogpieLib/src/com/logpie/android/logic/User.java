package com.logpie.android.logic;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.logpie.android.connection.EndPoint.ServiceURL;
import com.logpie.android.connection.GenericConnection;
import com.logpie.android.util.LogpieCallback;

/**
 * Central User, abstract user's behavior.
 * 
 * @author yilei
 */
public abstract class User
{
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

}
