package com.logpie.android.gis;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.logpie.android.connection.SimpleAPIConnection;
import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.TextHelper;

public class GoogleAPIHelper
{

    private static String TAG = GoogleAPIHelper.class.getName();
    private static String APIkey = "AIzaSyC_uIDsvuOMsf0XgZEZRW2vgA4EJb7gFq4";

    private static String sFormattedAddress = "formatted_address";

    private static String buildQueryReverseGeocodingURL(String lat, String lon)
    {
        return String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s", lat,
                lon);
    }

    private static String buildQueryGeocodingURL(final String address, final String city)
    {
        if (address == null)
        {
            LogpieLog.e(TAG, "address and city cannot be null");
        }
        String queryAddress = address.trim().replace(" ", "+");
        if (!TextUtils.isEmpty(city))
        {
            queryAddress = address + "," + city;
        }
        return String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                queryAddress, APIkey);
    }

    /* package-private */static LogpieLocation getLatLonFromAddressAndCity(Context context,
            final String address, final String city)
    {
        String queryURL = buildQueryGeocodingURL(address, city);
        String resultString = SimpleAPIConnection.doGetQuery(queryURL);

        JSONObject result;
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
            if (result != null && TextUtils.equals(result.getString("status"), "OK"))
            {
                JSONArray resultArray = result.getJSONArray("results");
                // Google will return a bunch of results as candidates. We
                // default pick the first one.
                if (resultArray != null)
                {
                    JSONObject singleResult = resultArray.getJSONObject(0);
                    if (singleResult != null)
                    {
                        JSONObject geometry = singleResult.getJSONObject("geometry");
                        if (geometry != null)
                        {
                            JSONObject location = geometry.getJSONObject("location");
                            if (location != null)
                            {
                                Double lat = location.getDouble("lat");
                                Double lon = location.getDouble("lng");
                                return new LogpieLocation(context, lat, lon, address, city);
                            }
                        }
                    }
                }
            }
            else
            {
                LogpieLog.e(TAG, "Google Server's error:" + result.getString("status"));
            }
        } catch (JSONException e)
        {

            LogpieLog
                    .e(TAG,
                            "JSONException when reading the response data. May because Google Server's response format is not as expected",
                            e);
        } catch (Exception e)
        {
            LogpieLog
                    .e(TAG,
                            "Exception happened when parse the response from Google. Something wrong returned by Google!",
                            e);
        }

        return null;
    }

    /* package-private */static String getAddressFromLatLon(Double lat, Double lon)
    {
        if (lat == null || lon == null)
        {
            LogpieLog.e(TAG,
                    "Building reverseGeocodingURL error, because the lat/lon cannot be null");
            return null;
        }
        String queryURL = buildQueryReverseGeocodingURL(lat.toString(), lon.toString());
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
                String formatted_address = singleResult.getString(sFormattedAddress);
                return formatted_address;
            }
            else
            {
                LogpieLog.e(TAG, "Google Server's error");
                return null;
            }
        } catch (JSONException e)
        {

            LogpieLog
                    .e(TAG,
                            "JSONException when reading the response data. May because Google Server's response format is not as expected",
                            e);
            return null;
        }
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
    /* package-private */static String getCityFromLatLon(Double lat, Double lon)
    {
        if (lat == null || lon == null)
        {
            LogpieLog.e(TAG,
                    "Building reverseGeocodingURL error, because the lat/lon cannot be null");
            return null;
        }

        String queryURL = buildQueryReverseGeocodingURL(lat.toString(), lon.toString());

        String resultString = SimpleAPIConnection.doGetQuery(queryURL);

        JSONObject result = null;
        try
        {
            result = new JSONObject(resultString);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when build the JSON from response String.", e);
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
                            "JSONException when reading the response data. May because Google Server's response format is not as expected",
                            e);
            return null;
        }
    }
}
