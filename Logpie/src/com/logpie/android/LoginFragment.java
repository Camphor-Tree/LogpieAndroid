package com.logpie.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class LoginFragment extends Fragment
{
    private EditText mUsername;
    private EditText mPassword;
    private TextView mRegister;
    private TextView mLogin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mUsername = (EditText) getActivity().findViewById(R.id.login_username);
        mPassword = (EditText) getActivity().findViewById(R.id.login_password);

        mLogin = (TextView) getActivity().findViewById(R.id.login_button);
        mLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (mUsername.getText() != null && mPassword.getText() != null)
                {
                    // TODO
                }
            }
        });

        mRegister = (TextView) getActivity().findViewById(R.id.register_button);
        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub

            }
        });
    }
}
