package com.logpie.android.logic;

import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.EncryptedDataStorage;
import com.logpie.android.util.LogpieLog;

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
            int uid = Integer.valueOf(userId);
            String email = mStorage.getValue(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
            String nickname = mStorage.getValue(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
            String accessToken = mStorage
                    .getValue(DataLevel.USER_LVL, AuthManager.KEY_ACCESS_TOKEN);
            String refreshToken = mStorage.getValue(DataLevel.USER_LVL,
                    AuthManager.KEY_REFRESH_TOKEN);
            mAccount = new LogpieAccount(uid, email, nickname, accessToken, refreshToken);
        }
        return mAccount;
    }

    /**
     * When login or register, this function must be called
     * 
     * @param account
     */
    public boolean addAccount(LogpieAccount account)
    {
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
        mIsDoingClearAccount.set(true);
        if (mAccount == null)
            return;

        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_UID);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_ACCESS_TOKEN);
        mStorage.delete(DataLevel.USER_LVL, AuthManager.KEY_REFRESH_TOKEN);

        mAccount = null;
        mIsDoingClearAccount.set(false);
    }

    public int getUID()
    {
        if (mAccount == null)
        {
            return 0;
        }
        return mAccount.getUid();
    }

    public String getAccessToken()
    {
        if (mAccount == null)
        {
            return null;
        }
        return mAccount.getAccessToken();
    }

    public String getRefreshToken()
    {
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
            LogpieAccount account = getCurrentAccount();

            if (!mIsDoingClearAccount.get() && account != null)
            {
                String refresh_token = account.getRefreshToken();
                if (refresh_token == null)
                {
                    logErrorAndClearAccount("refresh_token is missing! Clear the account");
                    return;
                }
                httpURLConnection.setRequestProperty("refresh_token", refresh_token);
                httpURLConnection.setRequestProperty("uid", Integer.toString(account.getUid()));
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
            LogpieAccount account = getCurrentAccount();
            if (!mIsDoingClearAccount.get() && account != null)
            {
                String access_token = account.getAccessToken();
                if (access_token == null)
                {
                    logErrorAndClearAccount("access_tokenn is missing! Clear the account");
                    return;
                }
                httpURLConnection.setRequestProperty("access_token", access_token);
                httpURLConnection.setRequestProperty("uid", Integer.toString(account.getUid()));
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

    private void logErrorAndClearAccount(final String errorMessage)
    {
        LogpieLog.e("TAG", errorMessage);
        clearAccount();
    }
}
