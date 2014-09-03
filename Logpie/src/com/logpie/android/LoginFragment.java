package com.logpie.android;

import java.util.Locale;

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

import com.logpie.android.ui.SquareActivity;
import com.logpie.android.ui.base.LogpieBaseFragment;
import com.logpie.android.ui.helper.ActivityOpenHelper;

public class LoginFragment extends LogpieBaseFragment
{
    private final static String TAG = LoginFragment.class.getName();
    private ImageView mChinese;
    private ImageView mEnglish;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mRegister;
    private TextView mLogin;
    private Locale mCurrentLanguage;
    private Activity mActivity;

    @SuppressLint("NewApi")
    private void changeLanguage(Locale lan)
    {
        if (lan.equals(mCurrentLanguage))
        {
            return;
        }
        mCurrentLanguage = lan;
        if (lan.equals(Locale.CHINA) || lan.equals(Locale.CHINESE))
        {
            mEnglish.setAlpha(0.3f);
            mChinese.setAlpha(1.0f);
            mEmail.setHint(R.string.email_cn);
            mPassword.setHint(R.string.password_cn);
            mLogin.setText(R.string.login_cn);
            mRegister.setText(R.string.register_cn);
            // TODO: store
        }
        else
        {
            mEnglish.setAlpha(1.0f);
            mChinese.setAlpha(0.3f);
            mEmail.setHint(R.string.email_us);
            mPassword.setHint(R.string.password_us);
            mLogin.setText(R.string.login_us);
            mRegister.setText(R.string.register_us);
        }
    }

    @Override
    public void handleOnCreate(Bundle savedInstanceState)
    {
        mActivity = getActivity();

    }

    @Override
    public View handleOnCreateView(LayoutInflater inflater, ViewGroup parent,
            Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);

        mEnglish = (ImageView) v.findViewById(R.id.login_language_us);
        mChinese = (ImageView) v.findViewById(R.id.login_language_cn);
        mCurrentLanguage = getResources().getConfiguration().locale;
        // TODO: store to the key-value storage
        mEmail = (EditText) v.findViewById(R.id.login_email);
        mPassword = (EditText) v.findViewById(R.id.login_password);
        mLogin = (TextView) v.findViewById(R.id.login_login_button);
        mRegister = (TextView) v.findViewById(R.id.login_register_button);

        // initialize
        if (mCurrentLanguage.equals(Locale.CHINA) || mCurrentLanguage.equals(Locale.CHINESE))
        {
            mEnglish.setAlpha(100);
            mEmail.setHint(R.string.email_cn);
            mPassword.setHint(R.string.password_cn);
            mLogin.setText(R.string.login_cn);
            mRegister.setText(R.string.register_cn);
        }
        else
        {
            mChinese.setAlpha(100);
            mEmail.setHint(R.string.email_us);
            mPassword.setHint(R.string.password_us);
            mLogin.setText(R.string.login_us);
            mRegister.setText(R.string.register_us);
        }

        mEnglish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeLanguage(Locale.ENGLISH);
            }
        });

        mChinese.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeLanguage(Locale.CHINESE);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mEmail.getText() != null && mPassword.getText() != null)
                {
                    // TODO add Login Service call
                    ActivityOpenHelper.openActivityAndFinishPreviousActivity(mActivity,
                            SquareActivity.class);

                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Bundle bundle = new Bundle();
                bundle.putString("Locale", mCurrentLanguage.getLanguage());

                // Create new fragment and transaction
                Fragment fragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                fragment.setArguments(bundle);

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
