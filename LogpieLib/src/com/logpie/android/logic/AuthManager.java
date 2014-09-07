package com.logpie.android.logic;

import android.content.Context;
import android.os.Bundle;

import com.logpie.android.datastorage.DataLevel;
import com.logpie.android.datastorage.DataPlatform;
import com.logpie.android.datastorage.KeyValueStorage;
import com.logpie.android.util.LogpieLog;

public class AuthManager
{
    private final static String TAG = AuthManager.class.getName();
    public final static String KEY_UID = "uid";
    public final static String KEY_EMAIL = "email";
    public final static String KEY_NICKNAME = "nickname";
    public final static String KEY_ACCESS_TOKEN = "access_token";
    public final static String KEY_REFRESH_TOKEN = "refresh_token";

    private static AuthManager sAuthManager;
    private LogpieAccount mAccount;
    private Context mContext;

    private AuthManager(Context context)
    {
        mContext = context;
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
            KeyValueStorage storage = DataPlatform.getInstance(mContext)
                    .getKeyValueStorage();
            String userId = storage.query(DataLevel.USER_LVL, AuthManager.KEY_UID);
            if (userId == null)
            {
                return null;
            }
            int uid = Integer.valueOf(userId);
            String email = storage.query(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
            String nickname = storage.query(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
            String accessToken = storage.query(DataLevel.USER_LVL,
                    AuthManager.KEY_ACCESS_TOKEN);
            String refreshToken = storage.query(DataLevel.USER_LVL,
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

        KeyValueStorage storage = DataPlatform.getInstance(mContext).getKeyValueStorage();
        Bundle bundle = new Bundle();
        bundle.putString(DataLevel.KEY_DATALEVEL, DataLevel.USER_LVL.toString());
        bundle.putString(AuthManager.KEY_UID, uid);
        bundle.putString(AuthManager.KEY_EMAIL, email);
        bundle.putString(AuthManager.KEY_NICKNAME, nickname);
        bundle.putString(AuthManager.KEY_ACCESS_TOKEN, accessToken);
        bundle.putString(AuthManager.KEY_REFRESH_TOKEN, refreshToken);
        if (storage.insert(bundle))
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
        if (mAccount == null)
            return;

        KeyValueStorage storage = DataPlatform.getInstance(mContext).getKeyValueStorage();
        storage.delete(DataLevel.USER_LVL, AuthManager.KEY_UID);
        storage.delete(DataLevel.USER_LVL, AuthManager.KEY_EMAIL);
        storage.delete(DataLevel.USER_LVL, AuthManager.KEY_NICKNAME);
        storage.delete(DataLevel.USER_LVL, AuthManager.KEY_ACCESS_TOKEN);
        storage.delete(DataLevel.USER_LVL, AuthManager.KEY_REFRESH_TOKEN);

        mAccount = null;
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
}
