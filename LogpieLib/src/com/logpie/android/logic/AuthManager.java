package com.logpie.android.logic;

import java.net.HttpURLConnection;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.logpie.android.connection.GenericConnection;
import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.EncryptedDataStorage;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.EndPoint.ServiceURL;
import com.logpie.commonlib.RequestKeys;
import com.logpie.commonlib.ResponseKeys;

public class AuthManager
{
    public enum AuthType
    {
        // When user already logged in
        NormalAuth,
        // When access token is expired, need refresh_token to refresh the
        // access_token
        TokenExchange,
        // No user logged in.
        NoAuth;
    }

    private final static String TAG = AuthManager.class.getName();
    public final static String KEY_UID = "uid";
    public final static String KEY_EMAIL = "email";
    public final static String KEY_NICKNAME = "nickname";
    public final static String KEY_ACCESS_TOKEN = "access_token";
    public final static String KEY_REFRESH_TOKEN = "refresh_token";

    private static AuthManager sAuthManager;
    private LogpieAccount mAccount;
    private EncryptedDataStorage mStorage;
    private Context mContext;

    private AtomicBoolean mIsDoingClearAccount;

    private AuthManager(Context context)
    {
        mContext = context;
        mStorage = EncryptedDataStorage.getInstance(mContext);
        mIsDoingClearAccount = new AtomicBoolean(false);
        mAccount = getCurrentAccount();
    }

    public synchronized static AuthManager getInstance(Context context)
    {
        if (sAuthManager == null)
        {
            sAuthManager = new AuthManager(context);
        }
        return sAuthManager;
    }

    public LogpieAccount getCurrentAccount()
    {
        if (mAccount == null)
        {
            String userId = mStorage.getValue(DataLevel.USER_LVL, AuthManager.KEY_UID);
            if (userId == null)
            {
                return null;
            }
            String email = mStorage.getValue(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
            String nickname = mStorage.getValue(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
            String accessToken = mStorage
                    .getValue(DataLevel.USER_LVL, AuthManager.KEY_ACCESS_TOKEN);
            String refreshToken = mStorage.getValue(DataLevel.USER_LVL,
                    AuthManager.KEY_REFRESH_TOKEN);
            mAccount = new LogpieAccount(userId, email, nickname, accessToken, refreshToken);
        }
        return mAccount;
    }

    /**
     * When login or register, this function must be called
     * 
     * @param account
     */
    public synchronized boolean addAccount(LogpieAccount account)
    {
        if (mAccount != null)
        {
            LogpieLog.e(TAG, "The account already exist, please log-out first!");
            return false;
        }
        String uid = String.valueOf(account.getUid());
        String email = account.getEmail();
        String nickname = account.getNickname();
        String accessToken = account.getAccessToken();
        String refreshToken = account.getRefreshToken();

        if (uid == null || email == null || nickname == null || accessToken == null
                || refreshToken == null)
        {
            LogpieLog
                    .e(TAG,
                            "Failed to set account in key-value storage lacking of some values in Auth Service response");
            return false;
        }

        Bundle bundle = new Bundle();
        bundle.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        bundle.putString(AuthManager.KEY_UID, uid);
        bundle.putString(AuthManager.KEY_EMAIL, email);
        bundle.putString(AuthManager.KEY_NICKNAME, nickname);
        bundle.putString(AuthManager.KEY_ACCESS_TOKEN, accessToken);
        bundle.putString(AuthManager.KEY_REFRESH_TOKEN, refreshToken);
        if (mStorage.setKeyValueBundle(bundle))
        {
            mAccount = account;
            return true;
        }
        LogpieLog.e(TAG, "Failed to insert account information into key-value storage");
        return false;

    }

    /**
     * When logout or token expired, this function must be called
     */
    public synchronized void clearAccount()
    {
        mAccount = getCurrentAccount();
        mIsDoingClearAccount.set(true);
        if (mAccount == null)
        {
            return;
        }

        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_UID);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_ACCESS_TOKEN);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_REFRESH_TOKEN);

        mAccount = null;
        mIsDoingClearAccount.set(false);
    }

    public String getUID()
    {
        mAccount = getCurrentAccount();
        if (mAccount == null)
        {
            return null;
        }
        return mAccount.getUid();
    }

    public String getAccessToken()
    {
        mAccount = getCurrentAccount();
        if (mAccount == null)
        {
            return null;
        }
        return mAccount.getAccessToken();
    }

    public String getRefreshToken()
    {
        mAccount = getCurrentAccount();
        if (mAccount == null)
        {
            return null;
        }
        return mAccount.getRefreshToken();
    }

    /**
     * Add the necessary auth header to the connection
     * 
     * @param httpURLConnection
     */
    public void authenticateHttpURLConnection(final HttpURLConnection httpURLConnection,
            final AuthType authType)
    {
        switch (authType)
        {
        case NoAuth:
        {
            return;
        }
        case TokenExchange:
        {
            mAccount = getCurrentAccount();

            if (!mIsDoingClearAccount.get() && mAccount != null)
            {
                String refresh_token = mAccount.getRefreshToken();
                if (refresh_token == null)
                {
                    logErrorAndClearAccount("refresh_token is missing! Clear the account");
                    return;
                }
                httpURLConnection.setRequestProperty("refresh_token", refresh_token);
                httpURLConnection.setRequestProperty("uid", mAccount.getUid());
            }
            else
            {
                throw new UnsupportedOperationException(
                        "Not supported this operation when user is already logged out");
            }
            return;
        }
        case NormalAuth:
        {
            mAccount = getCurrentAccount();
            if (!mIsDoingClearAccount.get() && mAccount != null)
            {
                String access_token = mAccount.getAccessToken();
                if (access_token == null)
                {
                    logErrorAndClearAccount("access_tokenn is missing! Clear the account");
                    return;
                }
                httpURLConnection.setRequestProperty("access_token", access_token);
                httpURLConnection.setRequestProperty("uid", mAccount.getUid());
            }
            else
            {
                throw new UnsupportedOperationException(
                        "Not supported this operation when user is already logged out");
            }
            return;
        }
        }
    }

    public boolean doTokenExchange()
    {
        JSONObject tokenExchangeData = new JSONObject();
        try
        {
            GenericConnection connection = new GenericConnection();
            connection.initialize(ServiceURL.AuthenticationService, mContext);
            tokenExchangeData.put(RequestKeys.KEY_REQUEST_TYPE, "TOKEN_EXCHANGE");
            tokenExchangeData.put(RequestKeys.KEY_DECLARE_UID, getUID());
            tokenExchangeData.put(RequestKeys.KEY_ACCESS_TOKEN, getAccessToken());
            tokenExchangeData.put(RequestKeys.KEY_REFRESH_TOKEN, getRefreshToken());
            tokenExchangeData.put(RequestKeys.KEY_REQUEST_ID, UUID.randomUUID().toString());
            connection.setRequestData(tokenExchangeData);
            Bundle tokenExchangeBundle = connection.send(null).get();
            boolean tokenExchangeSuccess = parseTokenExchangeBundle(tokenExchangeBundle);
            if (tokenExchangeSuccess)
            {
                return true;
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        } catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public void updateAccessToken(final String access_token)
    {
        mAccount = getCurrentAccount();
        if (mAccount == null)
        {
            LogpieLog.e(TAG, "Currently there's no account in device. Cannot update access_token");
            return;
        }
        mAccount.setAccessToken(access_token);
    }

    public void updateRefreshToken(final String refresh_token)
    {
        mAccount = getCurrentAccount();
        if (mAccount == null)
        {
            LogpieLog.e(TAG, "Currently there's no account in device. Cannot update refresh_token");
            return;
        }
        mAccount.setRefreshToken(refresh_token);
    }

    private boolean parseTokenExchangeBundle(Bundle bundle)
    {
        if (bundle == null)
        {
            LogpieLog.e(TAG, "Toen exchange bundle is null");
            return false;
        }
        String tokenExchangeResultString = bundle.getString(GenericConnection.KEY_RESPONSE_DATA);
        if (tokenExchangeResultString == null)
        {
            LogpieLog.e(TAG, "Token exchange response data is null");
            return false;
        }
        try
        {
            JSONObject tokenExchangeResultJSON = new JSONObject(tokenExchangeResultString);
            String result = tokenExchangeResultJSON
                    .getString(ResponseKeys.KEY_AUTHENTICATION_RESULT);
            if (TextUtils.equals(result, ResponseKeys.RESULT_SUCCESS))
            {
                String new_access_token = tokenExchangeResultJSON
                        .getString(ResponseKeys.KEY_ACCESS_TOKEN);
                if (new_access_token != null)
                {
                    updateAccessToken(new_access_token);
                    return true;
                }
            }
            else
            {
                LogpieLog.e(TAG,
                        "Error happened when try to refresh access_token! The fail response is: "
                                + tokenExchangeResultString);
            }
        } catch (JSONException e)
        {
            LogpieLog
                    .e(TAG,
                            "JSONException happed when parse the token exchange result! Maybe the response from server is mal-formated"
                                    + tokenExchangeResultString, e);
        }
        return false;
    }

    private void logErrorAndClearAccount(final String errorMessage)
    {
        LogpieLog.e("TAG", errorMessage);
        clearAccount();
    }
}
