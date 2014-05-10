package com.logpie.android.logic;

public class OrganizationUser extends User
{
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
