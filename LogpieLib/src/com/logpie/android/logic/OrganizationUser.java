package com.logpie.android.logic;

import android.content.Context;

public class OrganizationUser extends User
{
    protected OrganizationUser(Context context)
    {
        super(context);
    }

    @Override
    public void getJoinedOrganizationList()
    {
        // Do nothing, OrganizationUser cannot join Organization
    }

    @Override
    public void joinOrganization(com.logpie.android.logic.Organization organization)
    {
        // Do nothing, OrganizationUser cannot join Organization
    }

    @Override
    public void quitOrganization(com.logpie.android.logic.Organization organization)
    {
        // Do nothing, OrganizationUser cannot join Organization
    }

}
