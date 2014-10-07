package com.logpie.android.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.logpie.android.R;
import com.logpie.android.connection.GenericConnection;
import com.logpie.android.datastorage.LogpieSystemSetting;
import com.logpie.android.logic.AuthManager;
import com.logpie.android.logic.LogpieAccount;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.ActivityOpenHelper;
import com.logpie.android.ui.helper.LanguageHelper;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.commonlib.ResponseKeys;

public class LoginFragment extends LogpieBaseFragment
{
    private final static String TAG = LoginFragment.class.getName();

    private ImageView mChinese;
    private ImageView mEnglish;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mRegister;
    private TextView mLogin;

    private Activity mActivity;
    private AuthManager mAuthManager;

    @SuppressLint("NewApi")
    private void setLanguage(String lan)
    {
        if (lan.equals(LanguageHelper.CHINESE))
        {
            mEnglish.setAlpha(0.3f);
            mChinese.setAlpha(1.0f);
        }
        else
        {
            mEnglish.setAlpha(1.0f);
            mChinese.setAlpha(0.3f);
        }
        mEmail.setHint(LanguageHelper.getId(LanguageHelper.KEY_EMAIL,
                mActivity.getApplicationContext()));
        mPassword.setHint(LanguageHelper.getId(LanguageHelper.KEY_PASSWORD,
                mActivity.getApplicationContext()));
        mLogin.setText(LanguageHelper.getId(LanguageHelper.KEY_LOGIN,
                mActivity.getApplicationContext()));
        mRegister.setText(LanguageHelper.getId(LanguageHelper.KEY_REGISTER,
                mActivity.getApplicationContext()));
    }

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mActivity = getActivity();
        mAuthManager = AuthManager.getInstance(mActivity);
    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        mEnglish = (ImageView) v.findViewById(R.id.login_language_us);
        mChinese = (ImageView) v.findViewById(R.id.login_language_cn);

        // TODO: store to the key-value storage
        mEmail = (EditText) v.findViewById(R.id.login_email);
        mPassword = (EditText) v.findViewById(R.id.login_password);
        mLogin = (TextView) v.findViewById(R.id.login_login_button);
        mRegister = (TextView) v.findViewById(R.id.login_register_button);

        setLanguage(LogpieSystemSetting.getInstance(mActivity).getSystemSetting(
                LanguageHelper.KEY_LANGUAGE));

        mEnglish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieSystemSetting.getInstance(mActivity).setSystemSetting(
                        LanguageHelper.KEY_LANGUAGE, LanguageHelper.ENGLISH);
                setLanguage(LanguageHelper.ENGLISH);
            }
        });

        mChinese.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LogpieSystemSetting.getInstance(mActivity).setSystemSetting(
                        LanguageHelper.KEY_LANGUAGE, LanguageHelper.CHINESE);
                setLanguage(LanguageHelper.CHINESE);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final String email = mEmail.getText().toString();
                final String password = mPassword.getText().toString();

                if (mEmail.getText() != null && mPassword.getText() != null)
                {
                    LogpieCallback callback = new LogpieCallback()
                    {

                        @Override
                        public void onSuccess(Bundle result)
                        {
                            String responseString = result
                                    .getString(GenericConnection.KEY_RESPONSE_DATA);
                            LogpieLog.d(TAG, "The login response from server:" + responseString);
                            try
                            {
                                JSONObject responseJSON = new JSONObject(responseString);
                                String accessToken = responseJSON
                                        .getString(ResponseKeys.KEY_ACCESS_TOKEN);
                                String refreshToken = responseJSON
                                        .getString(ResponseKeys.KEY_REFRESH_TOKEN);
                                String uid = responseJSON.getString(ResponseKeys.KEY_UID);
                                mAuthManager.addAccount(new LogpieAccount(uid, email,
                                        "Tester name", accessToken, refreshToken));
                            } catch (JSONException e)
                            {
                                LogpieLog.e(TAG,
                                        "JSONException happened when parsing the response", e);
                                return;
                            }
                            ActivityOpenHelper.openActivityAndFinishPreviousActivity(mActivity,
                                    SquareActivity.class);

                        }

                        @Override
                        public void onError(Bundle errorMessage)
                        {
                            LogpieLog.e(TAG, "Login error!");
                            LogpieLog.d(TAG,
                                    "Login error! error message is " + errorMessage.toString());

                        }
                    };
                    mAuthManager.login(email, password, callback);

                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
                Fragment fragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view
                // with this fragment
                transaction.replace(R.id.container, fragment);

                // Commit the transaction
                transaction.commit();
            }
        });
        return v;
    }
}
