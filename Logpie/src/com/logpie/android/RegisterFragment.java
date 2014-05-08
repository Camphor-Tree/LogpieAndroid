package com.logpie.android;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterFragment extends Fragment
{
    private TextView mWelcome;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mNickname;
    private TextView mRegister;
    private TextView mBackLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        mWelcome = (TextView) getActivity().findViewById(R.id.register_welcome);
        mEmail = (EditText) getActivity().findViewById(R.id.register_email);
        mPassword = (EditText) getActivity().findViewById(R.id.register_password);
        mConfirmPassword = (EditText) getActivity().findViewById(R.id.register_comfirm_password);
        mNickname = (EditText) getActivity().findViewById(R.id.register_nickname);
        mBackLogin = (TextView) getActivity().findViewById(R.id.register_login_button);
        mRegister = (TextView) getActivity().findViewById(R.id.register_register_button);

        // set language
        if (getResources().getConfiguration().locale.equals(Locale.CHINA)
                || getResources().getConfiguration().locale.equals(Locale.CHINESE))
        {
            mWelcome.setText(R.string.welcome_cn);
            mEmail.setHint(R.string.email_cn);
            mPassword.setHint(R.string.password_cn);
            mConfirmPassword.setHint(R.string.confirm_password_cn);
            mNickname.setHint(R.string.nickname_cn);
            mBackLogin.setText(R.string.back_cn);
            mRegister.setText(R.string.register_cn);
        }
        else
        {
            mWelcome.setText(R.string.welcome_us);
            mEmail.setHint(R.string.email_us);
            mPassword.setHint(R.string.password_us);
            mConfirmPassword.setHint(R.string.confirm_password_us);
            mNickname.setHint(R.string.nickname_us);
            mBackLogin.setText(R.string.back_us);
            mRegister.setText(R.string.register_us);
        }

        mBackLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Create new fragment and transaction
                Fragment loginFrag = new LoginFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this
                // fragment
                transaction.replace(R.id.container, loginFrag);

                // Commit the transaction
                transaction.commit();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO: store the data
            }
        });
    }
}
