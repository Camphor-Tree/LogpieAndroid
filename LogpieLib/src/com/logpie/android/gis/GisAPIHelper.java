package com.logpie.android.gis;

import com.logpie.android.util.LogpieLog;

public class GisAPIHelper
{
    /**
     * This method will judge whether it is in China or not. If in China, then use BaiduAPIHelper, otherwise use GoogleAPIHelper.
     * @param lat
     * @param lon
     * @return
     */
	private static String TAG = GisAPIHelper.class.getName();
			
    public static String getCityFromLatLon(Double lat, Double lon)
    {
        //TODO: figure out a good way to judge whether it is in China or not.
        if (lat>4&&lat<53.5&&lon<135.05&&lon>73.66)
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

}
