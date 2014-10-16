package com.logpie.android.gis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.logpie.android.connection.SimpleAPIConnection;
import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.TextHelper;

public class BaiduAPIHelper
{
    private static String TAG = BaiduAPIHelper.class.getName();
    private static String APIkey = "q8GK6Ou4uk92xewM8Mgyemtv";

    private static String KEY_FORMATTED_ADDRESS = "formatted_address";

    private static String buildQueryReverseGeocodingURL(final String lat, final String lon)
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

    private static String buildQueryGeocodingURL(final String address, final String city)
    {
        if (TextUtils.isEmpty(address) && TextUtils.isEmpty(city))
        {
            LogpieLog.e(TAG, "Address and city cannot both be null!");
            return null;
        }
        return String
                .format("http://api.map.baidu.com/geocoder/v2/?ak=%s&callback=renderOption&output=json&address=%s&city=%s",
                        APIkey, encodeChineseToUTF8(address), encodeChineseToUTF8(city));
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
    /* package-private */static String getCityFromLatLon(final Double lat, final Double lon)
    {
        JSONObject result = queryBaiduServerForReverseGeocodingResult(lat, lon);

        return parseBaiduReverseGeocodingResult(result, "addressComponent", "city");
    }

    private static String parseBaiduReverseGeocodingResult(final JSONObject result,
            final String keyWanted, final String subKey)
    {
        try
        {
            if (checkWhetherSuccess(result))
            {
                JSONObject resultJSON = result.getJSONObject("result");
                String resultValue = null;
                if (!TextUtils.isEmpty(subKey))
                {
                    JSONObject addressComponentJSON = resultJSON.getJSONObject(keyWanted);
                    resultValue = addressComponentJSON.getString(subKey);
                }
                else
                {
                    resultValue = resultJSON.getString(keyWanted);
                }

                if (TextHelper.checkIfNull(resultValue))
                {
                    return null;
                }
                else
                {
                    return resultValue;
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
                            "JSONException when reading the response data. May because Baidu Server's response format is not as expected",
                            e);
            return null;
        }
    }

    private static JSONObject queryBaiduServerForReverseGeocodingResult(final Double lat,
            final Double lon)
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
        JSONObject result = getResultJSON(resultAfterCutHead);
        return result;
    }

    /* package-private */static String getAddressFromLatLon(final Double lat, final Double lon)
    {
        JSONObject result = queryBaiduServerForReverseGeocodingResult(lat, lon);

        return parseBaiduReverseGeocodingResult(result, KEY_FORMATTED_ADDRESS, null);
    }

    /* package-private */static LogpieLocation getLatLonFromAddressAndCity(final String address,
            final String city)
    {
        String queryURL = buildQueryGeocodingURL(address, city);
        if (queryURL == null)
        {
            LogpieLog.e(TAG, "queryURL cannot be null. Returning null");
            return null;
        }

        String resultString = SimpleAPIConnection.doGetQuery(queryURL);
        // Since baidu API's response will contain:
        // renderReverse&&renderReverse(), need to remove that first.
        String resultAfterCutHead = removeHeaderInResult(resultString);
        JSONObject result = getResultJSON(resultAfterCutHead);

        try
        {
            if (checkWhetherSuccess(result))
            {
                JSONObject resultJSON = result.getJSONObject("result");
                JSONObject locationComponentJSON = resultJSON.getJSONObject("location");
                String longitude = locationComponentJSON.getString("lng");
                String latitude = locationComponentJSON.getString("lat");
                if (TextHelper.checkIfNull(longitude) || TextHelper.checkIfNull(latitude))
                {
                    LogpieLog.e(TAG, "Latitude or longitude is null!");
                    return null;
                }
                else
                {
                    return new LogpieLocation(Double.valueOf(latitude), Double.valueOf(longitude),
                            address, city);
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

    private static JSONObject getResultJSON(String resultString)
    {
        JSONObject result = null;
        try
        {
            result = new JSONObject(resultString);
        } catch (JSONException e)
        {
            LogpieLog.e(TAG, "JSONException when build the JSON from response String.");
            e.printStackTrace();
        }
        return result;
    }

    private static boolean checkWhetherSuccess(JSONObject resultJSON)
    {
        try
        {
            if (resultJSON.getInt("status") == 0)
            {
                return true;
            }
        } catch (JSONException e)
        {
            LogpieLog.e(TAG,
                    "The result may not contain status, something wrong with the baidu server", e);
        }
        return false;
    }

    private static String encodeChineseToUTF8(String chinese)
    {
        // if the city is null, then just leave an empty string
        if (chinese == null)
        {
            return "";
        }
        try
        {
            return URLEncoder.encode(chinese, "utf-8");
        } catch (UnsupportedEncodingException e)
        {
            return null;
        }
    }
}
