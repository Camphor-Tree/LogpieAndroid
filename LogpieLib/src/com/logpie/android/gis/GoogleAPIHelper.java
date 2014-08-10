package com.logpie.android.gis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.logpie.android.connection.SimpleAPIConnection;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.TextHelper;

public class GoogleAPIHelper
{

    private static String TAG = GoogleAPIHelper.class.getName();
    private static String APIkey = "AIzaSyC_uIDsvuOMsf0XgZEZRW2vgA4EJb7gFq4";

    private static String buildQueryReverseGeocodingURL(String lat, String lon)
    {
        if (lat == null || lon == null)
        {
            LogpieLog.e(TAG,
                    "Building reverseGeocodingURL error, because the lat/lon cannot be null");
            return null;
        }
        return String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat,
                lon);
    }

    /**
     * Pass the lat,lon getting the city name using baidu API.
     * 
     * Note: The baidu API cannot reverse-geocoding the lat/lon in foreign
     * country, such as US
     * 
     * @param lat
     * @param lon
     * @return city of the given lat&lon
     */
    public static String getCityFromLatLon(Double lat, Double lon)
    {
        if (lat == null || lon == null)
        {
            LogpieLog.e(TAG,
                    "Building reverseGeocodingURL error, because the lat/lon cannot be null");
            return null;
        }

        String queryURL = buildQueryReverseGeocodingURL(lat.toString(), lon.toString());
        if (queryURL == null)
        {
            LogpieLog.e(TAG, "queryURL cannot be null. Returning null");
            return null;
        }

        String resultString = SimpleAPIConnection.doGetQuery(queryURL);

        JSONObject result = null;
        try
        {
            result = new JSONObject(resultString);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when build the JSON from response String.");
            e.printStackTrace();
            return null;
        }
        try
        {
            if (result.getString("status").equals("OK"))
            {
                JSONArray resultArray = result.getJSONArray("results");
                // Google will return a bunch of results as candidates. We
                // default pick the first one.
                JSONObject singleResult = resultArray.getJSONObject(0);
                JSONArray components = singleResult.getJSONArray("address_components");
                int i = 0;
                for (; i < components.length(); i++)
                {
                    JSONArray types = components.getJSONObject(i).getJSONArray("types");
                    if (types.getString(0).equals("locality"))
                    {
                        break;
                    }
                }

                String city = components.getJSONObject(i).getString("long_name");
                if (TextHelper.checkIfNull(city))
                {
                    return null;
                }
                else
                {
                    return city;
                }
            }
            else
            {
                LogpieLog.e(TAG, "Baidu Server's error");
                return null;
            }
        } catch (JSONException e)
        {

            LogpieLog
                    .e(TAG,
                            "JSONException when reading the response data. May because Google Server's response format is not as expected");
            e.printStackTrace();
            return null;
        }
    }
}
