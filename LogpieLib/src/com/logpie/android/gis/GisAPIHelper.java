package com.logpie.android.gis;

import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.util.LogpieLog;

public class GisAPIHelper
{
    /**
     * This method will judge whether it is in China or not. If in China, then
     * use BaiduAPIHelper, otherwise use GoogleAPIHelper.
     * 
     * @param lat
     * @param lon
     * @return
     */
    private static String TAG = GisAPIHelper.class.getName();

    public static String getCityFromLatLon(Double lat, Double lon)
    {
        if (inChina(lat, lon))
        {
            return BaiduAPIHelper.getCityFromLatLon(lat, lon);
        }
        else
        {
            String city = GoogleAPIHelper.getCityFromLatLon(lat, lon);
            LogpieLog.d(TAG, "city is: " + city);
            return city;
        }
    }

    public static String getAddressFromLatLon(Double lat, Double lon)
    {

        if (inChina(lat, lon))
        {
            return BaiduAPIHelper.getCityFromLatLon(lat, lon);
        }
        else
        {
            String city = GoogleAPIHelper.getCityFromLatLon(lat, lon);
            LogpieLog.d(TAG, "city is: " + city);
            return city;
        }
    }

    public static LogpieLocation getLatLonFromAddressAndCity(final String address, final String city)
    {
        LogpieLocation googleResult = GoogleAPIHelper.getLatLonFromAddressAndCity(address, city);
        if (googleResult == null)
        {
            return BaiduAPIHelper.getLatLonFromAddressAndCity(address, city);
        }
        else
        {
            return googleResult;
        }
    }

    // TODO: figure out a good way to judge whether it is in China or not.
    private static boolean inChina(Double lat, Double lon)
    {
        return (lat > 4 && lat < 53.5 && lon < 135.05 && lon > 73.66);
    }

}
