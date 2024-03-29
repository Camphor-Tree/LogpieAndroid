package com.logpie.android.gis;

import com.logpie.android.util.LogpieLog;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LogpieLocationListener implements LocationListener
{
	private String TAG = LogpieLocationListener.class.getName();
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
    LogpieLog.d(TAG, "Provider is disabled: "+ provider);
        if (mGISManager != null)
        {
            if (provider.equals(LocationManager.GPS_PROVIDER))
            {
            	LogpieLog.d(TAG, "GPS is disabled.");
                mGISManager.setIsGPSLocationAvailable(false);
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER))
            {
            	LogpieLog.d(TAG, "Network provider is disabled.");
                mGISManager.setIsNetworkLocationAvailable(false);
            }
        }

    }

    @Override
    public void onProviderEnabled(String provider)
    {
    	LogpieLog.d(TAG, "Provider is enabled: "+ provider);
        if (mGISManager != null)
        {
            if (provider.equals(LocationManager.GPS_PROVIDER))
            {
            	LogpieLog.d(TAG, "GPS is enabled.");
                mGISManager.setIsGPSLocationAvailable(true);
            }
            if (provider.equals(LocationManager.NETWORK_PROVIDER))
            {
            	LogpieLog.d(TAG, "Network provider is enabled.");
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
    	LogpieLog.d(TAG, "Location changed.");
        mGISManager.getCurrentLocation().setLatitude(location.getLatitude());
        mGISManager.getCurrentLocation().setLongitude(location.getLongitude());
    }
}
