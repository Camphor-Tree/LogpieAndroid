package com.logpie.android;

import java.util.Locale;

import com.logpie.android.util.LogpieLog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginFragment extends Fragment
{
	private static String TAG = LoginFragment.class.getName();
    private ImageView mChinese;
    private ImageView mEnglish;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mRegister;
    private TextView mLogin;
    private Locale currentLanguage;
    
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        
        mEnglish = (ImageView) v.findViewById(R.id.login_language_us);
        mChinese = (ImageView) v.findViewById(R.id.login_language_cn);
        currentLanguage = getResources().getConfiguration().locale;
        // TODO: store to the key-value storage
        mEmail = (EditText) v.findViewById(R.id.login_email);
        mPassword = (EditText) v.findViewById(R.id.login_password);
        mLogin = (TextView) v.findViewById(R.id.login_login_button);
        mRegister = (TextView) v.findViewById(R.id.login_register_button);

        // initialize
        if (currentLanguage.equals(Locale.CHINA) || currentLanguage.equals(Locale.CHINESE))
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
                    // TODO
                }
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	Bundle bundle = new Bundle();
            	bundle.putString("Locale", currentLanguage.getLanguage());
            	
                // Create new fragment and transaction
                Fragment fragment = new RegisterFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                fragment.setArguments(bundle);
                
                // Replace whatever is in the fragment_container view with this
                // fragment
                transaction.replace(R.id.container, fragment);

                // Commit the transaction
                transaction.commit();
            }
        });
        return v;
    }

    private void changeLanguage(Locale lan)
    {
        if (lan.equals(currentLanguage))
        {
            return;
        }
        currentLanguage = lan;
        if (lan.equals(Locale.CHINA) || lan.equals(Locale.CHINESE))
        {
            mEnglish.setAlpha(100);
            mChinese.setAlpha(255);
            mEmail.setHint(R.string.email_cn);
            mPassword.setHint(R.string.password_cn);
            mLogin.setText(R.string.login_cn);
            mRegister.setText(R.string.register_cn);
            // TODO: store
        }
        else
        {
            mEnglish.setAlpha(255);
            mChinese.setAlpha(100);
            mEmail.setHint(R.string.email_us);
            mPassword.setHint(R.string.password_us);
            mLogin.setText(R.string.login_us);
            mRegister.setText(R.string.register_us);
        }
    }
}
