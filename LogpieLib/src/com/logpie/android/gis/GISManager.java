package com.logpie.android.gis;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;

import com.logpie.android.logic.Location;
import com.logpie.android.util.LogpieLog;

/**
 * Class help to manager all GIS related information or method
 * 
 * TODO: add unit test
 * 
 * @author yilei
 */
public class GISManager
{
    private static String TAG = GISManager.class.getName();
    // Singleton GIS Manager
    private static GISManager sGISManager;

    private boolean mIsLocationAvailable;
    private boolean mIsGPSLocationAvailable;
    private boolean mIsNetworkLocationAvailable;
    private Location mCurrentLocation;

    private LocationManager mLocationManager;
    private Context mContext;
    private LogpieLocationListener mLogpieLocationListener;

    private GISManager(Context context)
    {
        mContext = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    }

    public static synchronized GISManager getInstance(Context context)
    {
        if (sGISManager == null)
        {
            sGISManager = new GISManager(context);

        }
        return sGISManager;
    }

    public void initialize()
    {
        /**
         * LogpieLocationListener keep an instance of GISManager. it will update
         * the Lat/Lng in GISManager.
         */
        // create a new listener
        mLogpieLocationListener = new LogpieLocationListener(this, "LogpieDefaultLocationListener");
        // add listener.
        addLocationListener(mLogpieLocationListener);
    }

    public boolean isLocationAvailable()
    {
        if (mLocationManager == null)
        {
            if (mContext != null)
            {
                mLocationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);
            }
            else
            {
                mIsLocationAvailable = false;
                LogpieLog.e(TAG,
                        "mLocationManger is null & mContext is also null! This should be a bug.");
                return false;
            }
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            mIsLocationAvailable = true;
            return true;
        }
        mIsLocationAvailable = false;
        return false;
    }

    public boolean isGPSAvailable()
    {
        if (mLocationManager == null)
        {
            if (mContext != null)
            {
                mLocationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);
            }
            else
            {
                return false;
            }
        }

        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            return true;
        }
        return false;
    }

    public boolean isNetworkAvailable()
    {
        if (mLocationManager == null)
        {
            if (mContext != null)
            {
                mLocationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);
            }
            else
            {
                return false;
            }
        }

        if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            return true;
        }
        return false;
    }

    public boolean addLocationListener(LocationListener locationListener)
    {
        if (isGPSAvailable())
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
                    locationListener);
            return true;
        }
        else if (isNetworkAvailable())
        {
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                    locationListener);
            return true;
        }
        return false;

    }

    /**
     * Will check whether the location is available or not. If not available, it
     * will return null;
     * 
     * @return
     */
    public Location getCurrentLocation()
    {
        if (mIsGPSLocationAvailable || mIsNetworkLocationAvailable)
        {
            return mCurrentLocation;
        }
        else
        {
            return null;
        }
    }

    /**
     * Return the mCurrentLocation anyway. Note: this api will not ensure the
     * Location is currently available
     * 
     * @return
     */
    public Location getLastKnownLocation()
    {
        return mCurrentLocation;
    }

    public static void turnOnGPS()
    {

    }

    public static String transferCoordinatesToAddress()
    {
        return null;
    }

    /**
     * 
     * @return float[0] = latitude; float[1] = longitude;
     */
    public static float[] transferAddressToCoordinates()
    {
        return new float[2];
    }

    /**
     * @return the isLocationAvailable
     */
    public boolean isIsLocationAvailable()
    {
        return mIsLocationAvailable;
    }

    /**
     * @param isLocationAvailable
     *            the isLocationAvailable to set
     */
    public void setIsLocationAvailable(boolean isLocationAvailable)
    {
        mIsLocationAvailable = isLocationAvailable;
    }

    /**
     * @return the isGPSLocationAvailable
     */
    public boolean isIsGPSLocationAvailable()
    {
        return mIsGPSLocationAvailable;
    }

    /**
     * @param isGPSLocationAvailable
     *            the isGPSLocationAvailable to set
     */
    public void setIsGPSLocationAvailable(boolean isGPSLocationAvailable)
    {
        mIsGPSLocationAvailable = isGPSLocationAvailable;
    }

    /**
     * @return the isNetworkLocationAvailable
     */
    public boolean isIsNetworkLocationAvailable()
    {
        return mIsNetworkLocationAvailable;
    }

    /**
     * @param isNetworkLocationAvailable
     *            the isNetworkLocationAvailable to set
     */
    public void setIsNetworkLocationAvailable(boolean isNetworkLocationAvailable)
    {
        mIsNetworkLocationAvailable = isNetworkLocationAvailable;
    }

    /**
     * @param currentLocation
     *            the currentLocation to set
     */
    public void setCurrentLocation(Location currentLocation)
    {
        mCurrentLocation = currentLocation;
    }
}
