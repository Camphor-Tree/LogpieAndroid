package com.logpie.android.gis;

import org.json.JSONException;
import org.json.JSONObject;

import com.logpie.android.connection.SimpleAPIConnection;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.TextHelper;

public class BaiduAPIHelper
{
    private static String TAG = BaiduAPIHelper.class.getName();
    private static String APIkey = "115f833822cba206d07ec6ed92af874b";

    private static String buildQueryReverseGeocodingURL(String lat, String lon)
    {
        if (lat == null || lon == null)
        {
            LogpieLog.e(TAG,
                    "Building reverseGeocodingURL error, because the lat/lon cannot be null");
            return null;
        }
        return String
                .format("http://api.map.baidu.com/geocoder/v2/?ak=%s&callback=renderReverse&location=%s,%s&output=json&pois=0",
                        APIkey, lat, lon);
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
        // Since baidu API's response will contain:
        // renderReverse&&renderReverse(), need to remove that first.
        String resultAfterCutHead = removeHeaderInResult(resultString);
        JSONObject result = null;
        try
        {
            result = new JSONObject(resultAfterCutHead);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when build the JSON from response String.");
            e.printStackTrace();
            return null;
        }
        try
        {
            if (result.getInt("status") == 0)
            {
                JSONObject resultJSON = result.getJSONObject("result");
                JSONObject addressComponentJSON = resultJSON.getJSONObject("addressComponent");
                String city = addressComponentJSON.getString("city");
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
                            "JSONException when reading the response data. May because Baidu Server's response format is not as expected");
            e.printStackTrace();
            return null;
        }
    }

    private static String removeHeaderInResult(String result)
    {
        return result.substring(result.indexOf("(") + 1, result.length() - 1);
    }
}
