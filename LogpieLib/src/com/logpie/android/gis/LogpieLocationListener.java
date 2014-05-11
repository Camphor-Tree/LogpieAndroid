package com.logpie.android.gis;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LogpieLocationListener implements LocationListener
{
    private String mListenerName;
    private GISManager mGISManager;

    public LogpieLocationListener(GISManager gisManager, String name)
    {
        mListenerName = name;
        mGISManager = gisManager;
    }

    @Override
    public void onProviderDisabled(String provider)
    {
        if (mGISManager != null)
        {
            if (provider.equals(LocationManager.GPS_PROVIDER))
            {
                mGISManager.setIsGPSLocationAvailable(false);
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER))
            {
                mGISManager.setIsNetworkLocationAvailable(false);
            }
        }

    }

    @Override
    public void onProviderEnabled(String provider)
    {
        if (mGISManager != null)
        {
            if (provider.equals(LocationManager.GPS_PROVIDER))
            {
                mGISManager.setIsGPSLocationAvailable(true);
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER))
            {
                mGISManager.setIsNetworkLocationAvailable(true);
            }
        }
    }

    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        mGISManager.getCurrentLocation().setLatitude(location.getLatitude());
        mGISManager.getCurrentLocation().setLongitude(location.getLongitude());
    }
}
