package com.logpie.android.gis;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.util.LocationThread;
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
    private LogpieLocation mCurrentLocation;

    private LocationManager mLocationManager;
    private Context mContext;

    // private LogpieLocationListener mLogpieLocationListener;

    private GISManager(Context context)
    {
        mContext = context;
        mCurrentLocation = new LogpieLocation(null, null, null, null);
        mLocationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        /**
         * LogpieLocationListener keep an instance of GISManager. it will update
         * the Lat/Lng in GISManager.
         */
        LogpieLocationListener locationListener = new LogpieLocationListener(this,
                "LogpieDefaultLocationListener");
        LogpieLog.d(TAG, "location listener is instantiated.");
        addLocationListener(locationListener);
    }

    public static synchronized GISManager getInstance(Context context)
    {
        if (sGISManager == null)
        {
            sGISManager = new GISManager(context);

        }
        return sGISManager;
    }

    public void checkLocationAvailable()
    {
        LogpieLog.d(TAG, "Checking location availability...");
        if (mLocationManager == null)
        {
            LogpieLog.d(TAG, "mLocationManager is null...");
            if (mContext != null)
            {
                mLocationManager = (LocationManager) mContext
                        .getSystemService(Context.LOCATION_SERVICE);
            }
            else
            {
                mIsLocationAvailable = false;
                LogpieLog
                        .e(TAG,
                                "mLocationManger is null & mContext is also null! This should be a bug.");
                return;
            }
        }
        if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            LogpieLog.d(TAG, "Setting mIsLocationAvailable as true...");
            mIsLocationAvailable = true;
            return;
        }
        LogpieLog.d(TAG, "mIsLocationAvailable is false...");
        mIsLocationAvailable = false;
        return;
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

    public void addLocationListener(LocationListener locationListener)
    {
        if (isGPSAvailable())
        {
            LogpieLog
                    .d(TAG,
                            "GPS is enabled. Android Location Manager is starting to request location update...");
            new LocationThread(locationListener, mLocationManager, true);
        }
        else if (isNetworkAvailable())
        {
            LogpieLog
                    .d(TAG,
                            "Network is enabled. Android Location Manager is starting to request location update...");
            new LocationThread(locationListener, mLocationManager, false);
        }
        LogpieLog.d(TAG, "Finished call addLocationListerner().");

    }

    public void updateGPSCoordinates(Location location)
    {
        if (location != null)
        {
            mCurrentLocation.setLatitude(location.getLatitude());
            mCurrentLocation.setLongitude(location.getLongitude());
            LogpieLog.d(TAG, location.getLatitude() + ", " + location.getLongitude());
        }
    }

    /**
     * Will check whether the location is available or not. If not available, it
     * will return null;
     * 
     * @return
     */

    public LogpieLocation getCurrentLocation()
    {
        LogpieLog.d(TAG, "Getting current location... ");
        checkLocationAvailable();
        if (mIsLocationAvailable)
        {
            if (isGPSAvailable())
            {
                LogpieLog.d(TAG, "getting city from gps...");
                Location location = mLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                LogpieLog.d(TAG, "get location instance...");
                updateGPSCoordinates(location);
                LogpieLog.d(TAG, "finished update the coordinates.");
            }
            else
            {
                LogpieLog.d(TAG, "getting city from network...");
                Location location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                updateGPSCoordinates(location);
            }
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
    public LogpieLocation getLastKnownLocation()
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
    public void setCurrentLocation(LogpieLocation currentLocation)
    {
        mCurrentLocation = currentLocation;
    }

}
