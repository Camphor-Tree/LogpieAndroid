package com.logpie.android.connection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.UUID;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.exception.ThreadException;
import com.logpie.android.logic.AuthManager;
import com.logpie.android.logic.AuthManager.AuthType;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieCallbackFuture;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.ThreadHelper;
import com.logpie.commonlib.EndPoint.ServiceURL;

public class GenericConnection
{
    // If the service call has the response value, can use this key to get
    // the result
    public static final String KEY_RESPONSE_DATA = "com.logpie.connection.response.key";
    // If the service call doesn't need return value, can use this key to get
    // the boolean result
    public static final String KEY_BOOLEAN_RESULT = "com.logpie.connection.result.boolean";
    public static final String KEY_REQUEST_ID = "request_id";
    public static final String STATIC_REQUEST_ID = "5U2VydmljZSRVc2VyU2VydmljZSR";

    private static final String TAG = GenericConnection.class.getName();

    private HttpURLConnection mHttpURLConnection;

    private ServiceURL mServiceURL;
    private int mTimeout = 100 * 1000;
    // logpie default verb is post
    private String mHttpVerb = "POST";
    private JSONObject mRequestData;
    private String mResponseString;
    private AuthType mAuthType;
    private AuthManager mAuthManager;

    /**
     * Initialize the HttpURLConnection. It will set all necessary parameter
     * based on the serviceURL and also handle the authentication
     * 
     * @param serviceURL
     * @param authType
     * @param context
     */
    public void initialize(final ServiceURL serviceURL, final AuthType authType,
            final Context context)
    {
        // Check all the parameters are non-null
        checkParameterAndThrowIfIllegal(serviceURL, authType, context);
        try
        {
            mAuthType = authType;
            mServiceURL = serviceURL;
            mAuthManager = AuthManager.getInstance(context);

            // initialize the HttpURLConnection based on the url
            URL url = serviceURL.getURL();
            boolean isUsingSSL = serviceURL.isUsingHttps();
            if (isUsingSSL)
            {
                // TODO: Should turn off when release;
                disableSSLClientCertificate();
                mHttpURLConnection = (HttpsURLConnection) url.openConnection();
            }
            else
            {
                mHttpURLConnection = (HttpURLConnection) url.openConnection();
            }

            setInputOutput();
            setRequestParameters();
            handleAuthentication();

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void initialize(ServiceURL serviceURL, Context context)
    {
        // Default is no auth.
        initialize(serviceURL, AuthType.NoAuth, context);
    }

    /**
     * Send data to the server, Based on the DoInput attribute to determine
     * whether need to return data or data. If need read response data, it will
     * parse into the callback's onSuccess bundle with Key @link
     * GenericConnection.KEY_RESPONSE_DATA
     * 
     * This method will trigger the service call, so this method will send the
     * task into background thread. If you need a sync result, you can just call
     * LogpieCallbackFuture.get() to blocking wait the result.
     * 
     * @param callback
     *            Logpie callback.
     */
    public LogpieCallbackFuture send(LogpieCallback callback)
    {
        final LogpieCallbackFuture callbackFuture = new LogpieCallbackFuture(callback);

        try
        {
            ThreadHelper.runOnBackgroundThread(false, new Runnable()
            {

                @Override
                public void run()
                {
                    syncSendDataAndGetResult(callbackFuture);
                }
            });
        } catch (ThreadException e)
        {
            LogpieLog.e(TAG, "Thread Exception when make service call");
            handleCallback(false, "Thread Exception when make service call",
                    callbackFuture);
            e.printStackTrace();
        }

        return callbackFuture;
    }

    /**
     * This api must be called off-main thread.
     */
    public LogpieCallbackFuture syncSendDataAndGetResult(LogpieCallback callback)
    {
        if (ThreadHelper.isRunningOnMainThread())
        {
            throw new IllegalStateException(
                    "This function cannot be called on main thread.");
        }

        LogpieCallbackFuture callbackFuture;

        if (callback instanceof LogpieCallbackFuture)
        {
            callbackFuture = (LogpieCallbackFuture) callback;
        }
        else
        {
            callbackFuture = new LogpieCallbackFuture(callback);
        }

        try
        {
            mRequestData.put(KEY_REQUEST_ID, UUID.randomUUID().toString());
        } catch (JSONException e1)
        {
            // Do nothing if cannot add requestID
            LogpieLog.e(TAG,
                    "JSONException when putting request_id. Putting empty request_id");
        }
        String data = mRequestData.toString();
        if (data == null)
        {
            // TODO: we should put the error message in one place
            Bundle error = new Bundle();
            error.putString("error", "cannot send empty data");
            handleCallback(false, "cannot send empty data", callbackFuture);
            return callbackFuture;
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

            handleCallback(false, "IOException when trying to output the data",
                    callbackFuture);
            return callbackFuture;
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
                // Check whether the end point service need read the input
                if (mServiceURL.isDoInput())
                {
                    // read the response data from server.
                    mResponseString = inputStringReader(mHttpURLConnection
                            .getInputStream());
                    LogpieLog.d(TAG, "The response from server:" + mServiceURL.getURL()
                            + " is: " + mResponseString);
                    handleCallbackWithResponseData(mResponseString, callbackFuture);
                }
                else
                {
                    handleCallback(true, "succesfully sending data to server",
                            callbackFuture);
                    LogpieLog.i(TAG,
                            "successful sending data to: " + mServiceURL.getServiceName()
                                    + "<--->hitting url:"
                                    + mServiceURL.getURL().toString());
                }
            }
            else if (responsecode >= 300 && responsecode < 400)
            {
                handleCallback(false,
                        "redirection happen when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG,
                        "redirection happen when sending data to server. error code:"
                                + responsecode);
            }
            else if (responsecode >= 400 && responsecode < 500)
            {
                handleCallback(false,
                        "client error happen when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG,
                        "client error happen when sending data to server. error code:"
                                + responsecode);
            }
            else if (responsecode >= 500)
            {
                handleCallback(false,
                        "server error when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG, "server error when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode == -1)
            {
                handleCallback(false,
                        "no valid response code when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG,
                        "no valid response code when sending data to server. error code:"
                                + responsecode);
            }
            else
            {
                handleCallback(false,
                        "unknown error when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG, "unknown error when sending data to server. error code:"
                        + responsecode);
            }
        } catch (IOException e)
        {
            handleCallback(false,
                    "IOException when sending data to server and getresponseCode",
                    callbackFuture);
            e.printStackTrace();
        }
        return callbackFuture;
    }

    private void handleCallback(boolean isSuccess, String message, LogpieCallback callback)

    {
        Bundle returnMessage = new Bundle();
        if (isSuccess)
        {
            returnMessage.putBoolean(KEY_BOOLEAN_RESULT, true);
            callback.onSuccess(returnMessage);
        }
        else
        {
            returnMessage.putBoolean(KEY_BOOLEAN_RESULT, false);
            callback.onError(returnMessage);
        }

    }

    private void handleCallbackWithResponseData(String message, LogpieCallback callback)

    {
        Bundle returnMessage = new Bundle();
        returnMessage.putString(KEY_RESPONSE_DATA, message);
        callback.onSuccess(returnMessage);
    }

    public String getResponse()
    {
        return mResponseString;
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

    private void disableSSLClientCertificate()
    {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException
            {

            }
        } };

        SSLContext sc;
        try
        {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier()
            {

                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            });
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (KeyManagementException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private String inputStringReader(InputStream inputStream) throws IOException
    {
        if (inputStream != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                builder.append(line);
            }
            return builder.toString();
        }
        return null;
    }

    // handle the necessary authentication in request header
    private void handleAuthentication()
    {
        mAuthManager.authenticateHttpURLConnection(mHttpURLConnection, mAuthType);
    }

    private void setInputOutput()
    {
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
    }

    private void setRequestParameters() throws IOException
    {
        mHttpURLConnection.setChunkedStreamingMode(0);
        // set the timeout
        mHttpURLConnection.setConnectTimeout(mTimeout);
        // set http verb
        mHttpURLConnection.setRequestMethod(mHttpVerb);
        // set charset, we should use UTF-8
        mHttpURLConnection.setRequestProperty("Charset", "UTF-8");
        // set Content-Type, logpie's default sending data format is JSON

        mHttpURLConnection.setRequestProperty("Content-Type", "application/json");
    }

    private void checkParameterAndThrowIfIllegal(final ServiceURL serviceURL,
            final AuthType authType, final Context context)
    {
        if (serviceURL == null || authType == null || context == null)
        {
            LogpieLog
                    .e(TAG,
                            "Please check your parameter! ServiceURL, AuthType and context all cannot be null");
            throw new IllegalArgumentException(
                    "ServiceURL, authType and context all cannot be null!");
        }
    }
}
