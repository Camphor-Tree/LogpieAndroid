package com.logpie.android.logic;

/**
 * Central User, abstract user's behavior.
 * 
 * @author yilei
 */
public abstract class User
{
    UserProfile mUserProfile;

    public void login()
    {
    }

    public void register()
    {
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

    public void getLogpieActivityList(Place place)
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
