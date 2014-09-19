package com.logpie.android.logic;

import android.content.Context;

public class NormalUser extends User
{
    private static NormalUser sNormalUser;

    private NormalUser(Context context)
    {
        super(context);
    }

    public static synchronized NormalUser getInstance(Context context)
    {
        if (sNormalUser == null)
        {
            sNormalUser = new NormalUser(context);
            sNormalUser.syncInitialize();
            sNormalUser.asyncInitialize();
        }
        return sNormalUser;
    }

    // Do something important need to be sync call.
    private void syncInitialize()
    {

    }

    // Do something not important we can just initialize lazily
    private void asyncInitialize()
    {

    }

    @Override
    public void getJoinedOrganizationList()
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void joinOrganization(Organization organization)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void quitOrganization(Organization organization)
    {
        // TODO Auto-generated method stub

    }

}
