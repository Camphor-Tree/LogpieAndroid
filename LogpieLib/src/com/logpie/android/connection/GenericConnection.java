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
    private Context mContext;
    private LogpieCallback mCallback;
    private LogpieCallbackFuture mCallbackFuture;

    // This variable indicate whether the connection is retriable when meeting a
    // token expiration(Http 401 error). Notes: We should never retry for the
    // second time, since it may cause infinite-loop.
    private boolean mIsRetriable;

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
            mContext = context;
            mAuthType = authType;
            mServiceURL = serviceURL;
            mAuthManager = AuthManager.getInstance(context);

            // Retriable default to true;
            mIsRetriable = true;

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

    /**
     * Send data to the server, Based on the DoInput attribute to determine
     * whether need to return data or data. If need read response data, it will
     * parse into the callback's onSuccess bundle with Key @link
     * GenericConnection.KEY_RESPONSE_DATA
     * 
     * This method will trigger the service call, so this method will send the
     * task into background thread. If you need a sync result, you can just call
     * LogpieCallbackFuture.get() to blocking wait the result. You need a sync
     * callback, then you also need to call LogpieCallbackFuture.get() to make
     * sure the callback is called in a sync way.
     * 
     * In summary, this api support 3 return modes:
     * 
     * 1. Callback, sync (pass a callback, and also call the return
     * callbackFuture.get())
     * 
     * 2. Callback, async (just pass a callback)
     * 
     * 3. Return value, sync (NOT passing a callback, just get the return
     * callbackFuture, call callbackFuture.get())
     * 
     * @param callback
     *            Logpie callback.
     */
    public LogpieCallbackFuture send(final LogpieCallback callback)
    {
        mCallback = callback;
        mCallbackFuture = new LogpieCallbackFuture(callback);

        try
        {
            ThreadHelper.runOnBackgroundThread(false, new Runnable()
            {
                @Override
                public void run()
                {
                    syncSendDataAndGetResult(mCallbackFuture);
                }
            });
        } catch (ThreadException e)
        {
            LogpieLog.e(TAG, "Thread Exception when make service call", e);
            handleCallback(false, "Thread Exception when make service call", mCallbackFuture);
        }

        return mCallbackFuture;
    }

    /**
     * This api must be called off-main thread.
     */
    private void syncSendDataAndGetResult(final LogpieCallback callback)
    {
        if (ThreadHelper.isRunningOnMainThread())
        {
            throw new IllegalStateException("This function cannot be called on main thread.");
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
        } catch (JSONException e)
        {
            // Do nothing if cannot add requestID
            LogpieLog.e(TAG, "JSONException when putting request_id. Putting empty request_id", e);
        }
        String data = mRequestData.toString();
        if (data == null)
        {
            // TODO: we should put the error message in one place
            Bundle error = new Bundle();
            error.putString("error", "cannot send empty data");
            handleCallback(false, "cannot send empty data", callbackFuture);
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
            LogpieLog.e(TAG, "geOutputStream occured error", e);
            handleCallback(false, "IOException when trying to output the data", callbackFuture);
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
                LogpieLog.e(TAG, "error when try to close BufferedWriter", e);
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
                    mResponseString = inputStringReader(mHttpURLConnection.getInputStream());
                    LogpieLog.d(TAG, "The response from server:" + mServiceURL.getURL() + " is: "
                            + mResponseString);
                    handleCallbackWithResponseData(mResponseString, callbackFuture);
                }
                else
                {
                    handleCallback(true, "succesfully sending data to server", callbackFuture);
                    LogpieLog.d(TAG, "Successfully send data to: " + mServiceURL.getServiceName()

                    + "<--->hitting url:" + mServiceURL.getURL().toString());
                }
            }
            else if (responsecode >= 300 && responsecode < 400)
            {
                handleCallback(false, "redirection happen when sending data to server. error code:"
                        + responsecode, callbackFuture);
                LogpieLog.e(TAG, "redirection happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 400 && responsecode < 500)
            {
                /*
                 * When the error code is 401, means the token is invalid. We
                 * need to use refresh token to refresh access token. And we
                 * only do a retry for the first time to avoid potential
                 * infinite loop.
                 */
                if (responsecode == 401 && mIsRetriable)
                {
                    boolean tokenExchangeSuccess = mAuthManager.doTokenExchange();
                    /*
                     * If tokenExchangeSucceed, then we should use the new
                     * access_token to retry the connection
                     */
                    if (tokenExchangeSuccess)
                    {
                        retryConnection();
                    }
                }
                handleCallback(false,
                        "client error happen when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG, "client error happen when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode >= 500)
            {
                handleCallback(false, "server error when sending data to server. error code:"
                        + responsecode, callbackFuture);
                LogpieLog.e(TAG, "server error when sending data to server. error code:"
                        + responsecode);
            }
            else if (responsecode == -1)
            {
                handleCallback(false,
                        "no valid response code when sending data to server. error code:"
                                + responsecode, callbackFuture);
                LogpieLog.e(TAG, "no valid response code when sending data to server. error code:"
                        + responsecode);
            }
            else
            {
                handleCallback(false, "unknown error when sending data to server. error code:"
                        + responsecode, callbackFuture);
                LogpieLog.e(TAG, "unknown error when sending data to server. error code:"
                        + responsecode);
            }
        } catch (IOException e)
        {
            LogpieLog.e(TAG, "IOException when sending data to server and getresponseCode", e);
            handleCallback(false, "IOException when sending data to server and getresponseCode",
                    callbackFuture);
        }
    }

    private void handleCallback(final boolean isSuccess, final String message,
            final LogpieCallback callback)

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

    private void handleCallbackWithResponseData(final String responseData,
            final LogpieCallback callback)
    {
        Bundle returnMessage = new Bundle();
        returnMessage.putString(KEY_RESPONSE_DATA, responseData);
        callback.onSuccess(returnMessage);
    }

    private void retryConnection()
    {
        GenericConnection connection = new GenericConnection();
        connection.initialize(mServiceURL, mAuthType, mContext);
        connection.setRequestData(mRequestData);
        connection.setRetriable(false);
        connection.syncSendDataAndGetResult(mCallbackFuture);
    }

    public String getResponse()
    {
        return mResponseString;
    }

    public int getTimeout()
    {
        return mTimeout;
    }

    public void setTimeout(final int timeout)
    {
        mTimeout = timeout;
    }

    public String getHttpVerb()
    {
        return mHttpVerb;
    }

    public void setHttpVerb(final String httpVerb)
    {
        mHttpVerb = httpVerb;
    }

    public JSONObject getRequestData()
    {
        return mRequestData;
    }

    public void setRequestData(final JSONObject mRequestData)
    {
        this.mRequestData = mRequestData;
    }

    /**
     * If this is set true, then the connection will automatically retry when
     * meeting the token expriation. It will try to do token exchange first,
     * then retry the connection. But the second connection will not be able to
     * retry again.
     * 
     * @param retriable
     */
    public void setRetriable(final boolean retriable)
    {
        this.mIsRetriable = retriable;
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

    private String inputStringReader(final InputStream inputStream) throws IOException
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
