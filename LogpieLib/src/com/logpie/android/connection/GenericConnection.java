package com.logpie.android.connection;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.os.Bundle;

import com.logpie.android.connection.EndPoint.ServiceURL;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;

public class GenericConnection
{
    private static final String TAG = GenericConnection.class.getName();

    private HttpURLConnection mHttpURLConnection;
    private ServiceURL mServiceURL;
    private int mTimeout = 10 * 1000;
    // logpie default verb is post
    private String mHttpVerb = "POST";
    private JSONObject mRequestData;

    public void initialize(ServiceURL serviceURL)
    {
        try
        {
            mServiceURL = serviceURL;
            // initialize the HttpURLConnection based on the url
            URL url = serviceURL.getURL();
            mHttpURLConnection = (HttpURLConnection) url.openConnection();
            // check whether need to do input
            if (mServiceURL.needDoOutput())
            {
                mHttpURLConnection.setDoOutput(true);
            }
            else
            {
                mHttpURLConnection.setDoOutput(false);
            }
            // check whether nned to do output
            if (mServiceURL.needDoInput())
            {
                mHttpURLConnection.setDoInput(true);
            }
            else
            {
                mHttpURLConnection.setDoInput(false);
            }
            mHttpURLConnection.setChunkedStreamingMode(0);
            // set the timeout
            mHttpURLConnection.setConnectTimeout(mTimeout);
            // set http verb
            mHttpURLConnection.setRequestMethod(mHttpVerb);
            // set charset, we should use UTF-8
            mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
            // set Content-Type, logpie's default sending data format is JSON

            mHttpURLConnection.setRequestProperty("Content-Type", "application/json");

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void send(LogpieCallback callback)
    {
        String data = mRequestData.toString();
        if (data == null)
        {
            // TODO: we should put the error message in one place
            Bundle error = new Bundle();
            error.putString("error", "cannot send empty data");
            callback.onError(error);
            return;
        }
        BufferedWriter writer = null;
        try
        {
            OutputStream outputStream = mHttpURLConnection.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            writer = new BufferedWriter(outputStreamWriter);
            writer.write(data);
        } catch (IOException e)
        {
            e.printStackTrace();
            LogpieLog.e(TAG, "geOutputStream occured error");

            handleCallback(false, "IOException when trying to output the data", callback);
        } finally
        {
            try
            {
                if (writer != null)
                {
                    writer.flush();
                    writer.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
                LogpieLog.e(TAG, "error when try to close BufferedWriter");
            }
        }

        try
        {
            int responsecode = mHttpURLConnection.getResponseCode();
            if (responsecode >= 200 && responsecode < 300)
            {
                handleCallback(true, "succesfully sending data to server", callback);
                LogpieLog.i(TAG, "successful sending data to: " + mServiceURL.getServiceName()
                        + "<--->hitting url:" + mServiceURL.getURL().toString());
            }
            else if (responsecode >= 300 && responsecode < 400)
            {
                handleCallback(false, "redirection happen when sending data to server. error code:"
                        + responsecode, callback);
                LogpieLog.e(TAG, "redirection happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 400 && responsecode < 500)
            {
                handleCallback(false,
                        "client error happen when sending data to server. error code:"
                                + responsecode, callback);
                LogpieLog.e(TAG, "client error happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 500)
            {
                handleCallback(false, "server error when sending data to server. error code:"
                        + responsecode, callback);
                LogpieLog.e(TAG, "server error when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode == -1)
            {
                handleCallback(false,
                        "no valid response code when sending data to server. error code:"
                                + responsecode, callback);
                LogpieLog.e(TAG, "no valid response code when sending data to server. error code:"
                        + responsecode);
            }
            else
            {
                handleCallback(false, "unknown error when sending data to server. error code:"
                        + responsecode, callback);
                LogpieLog.e(TAG, "unknown error when sending data to server. error code:"
                        + responsecode);
            }
        } catch (IOException e)
        {
            Bundle error = new Bundle();
            error.putString("error", "IOException when sending data to server and getresponseCode");

            callback.onError(error);
            e.printStackTrace();
        }
    }

    private void handleCallback(boolean isSuccess, String message, LogpieCallback callback)

    {
        Bundle returnMessage = new Bundle();
        if (isSuccess)
        {
            returnMessage.putString("success", message);
            callback.onSuccess(returnMessage);
        }
        else
        {
            returnMessage.putString("error", message);
            callback.onError(returnMessage);
        }

    }

    public JSONObject getResponse()
    {
        return new JSONObject();
    }

    public JSONObject sendAndGetResult(LogpieCallback callback)
    {
        return new JSONObject();
    }

    public int getTimeout()
    {
        return mTimeout;
    }

    public void setTimeout(int timeout)
    {
        mTimeout = timeout;
    }

    public String getHttpVerb()
    {
        return mHttpVerb;
    }

    public void setHttpVerb(String httpVerb)
    {
        mHttpVerb = httpVerb;
    }

    public JSONObject getRequestData()
    {
        return mRequestData;
    }

    public void setRequestData(JSONObject mRequestData)
    {
        this.mRequestData = mRequestData;
    }

}
