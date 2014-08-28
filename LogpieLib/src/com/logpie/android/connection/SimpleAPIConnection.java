package com.logpie.android.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.logpie.android.util.LogpieLog;

//TODO: combine this into GenericConnection.
//Add end point.
public class SimpleAPIConnection
{
    private static String TAG = SimpleAPIConnection.class.getName();

    public static String doGetQuery(String stringUrl)
    {
        try
        {
            URL url = new URL(stringUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Charset", "UTF-8");  

            int responsecode = connection.getResponseCode();
            if (responsecode >= 200 && responsecode < 300)
            {
                LogpieLog.i(TAG, "Sending 'GET' request to URL : " + url);

                BufferedReader in = new BufferedReader(new InputStreamReader(
                        connection.getInputStream()));

                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null)
                {
                    response.append(inputLine);
                }
                in.close();
                String responseString = response.toString();
                // print result
                LogpieLog.i(TAG, "Receiving response from server: " + responseString);

                return responseString;
            }
            else if (responsecode >= 300 && responsecode < 400)
            {
                LogpieLog.e(TAG, "redirection happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 400 && responsecode < 500)
            {

                LogpieLog.e(TAG, "client error happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 500)
            {

                LogpieLog.e(TAG, "server error when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode == -1)
            {

                LogpieLog.e(TAG, "no valid response code when sending data to server. error code:"
                        + responsecode);
            }
            else
            {

                LogpieLog.e(TAG, "unknown error when sending data to server. error code:"
                        + responsecode);
            }
            return null;
        } catch (MalformedURLException e)
        {
            LogpieLog.e(TAG, "The query url is mal-formatted!");
            e.printStackTrace();
            return null;
        } catch (IOException e)
        {
            LogpieLog.e(TAG, "IOException when open the Connection");
            e.printStackTrace();
            return null;
        }
    }
}
