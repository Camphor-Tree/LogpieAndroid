package com.logpie.android;

import com.logpie.android.datastorage.SQLStorage;
import com.logpie.android.exception.ThreadException;
import com.logpie.android.gis.GISManager;
import com.logpie.android.logic.LogpieLocation;
import com.logpie.android.logic.NormalUser;
import com.logpie.android.util.LogpieCallback;
import com.logpie.android.util.LogpieLog;
import com.logpie.android.util.ThreadHelper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment
{
    private static String TAG = RegisterFragment.class.getName();
    
    private TextView mWelcome;
    private EditText mEmail;
    private EditText mPassword;
    private EditText mConfirmPassword;
    private EditText mNickname;
    private TextView mRegister;
    private TextView mBackLogin;
    private String language;
    private LogpieLocation location;
    private Thread mThread;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
    	super.onCreate(savedInstanceState);
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        
        mWelcome = (TextView) v.findViewById(R.id.register_welcome);
        mEmail = (EditText) v.findViewById(R.id.register_email);
        mPassword = (EditText) v.findViewById(R.id.register_password);
        mConfirmPassword = (EditText) v.findViewById(R.id.register_confirm_password);
        mNickname = (EditText) v.findViewById(R.id.register_nickname);
        mBackLogin = (TextView) v.findViewById(R.id.register_login_button);
        mRegister = (TextView) v.findViewById(R.id.register_register_button);
        
        Fragment fragment = getFragmentManager().findFragmentById(R.id.container);
        
        // set language
    	Bundle bundle = fragment.getArguments();
        language = bundle.getString("Locale");       
        if (language.equals("zh"))
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
                transaction.replace(R.id.container, loginFrag, "loginFragment");

                // Commit the transaction
                transaction.commit();
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {                
                /*
                // check the input are correct or not
                StringBuffer text = new StringBuffer();
                if (!isValid(text))
                {
                    Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
                }
                else
                {*/ if(mThread == null) {  
                    	mThread = new Thread(runnable);  
                    	mThread.start();
                	}  
                	else {  
                		LogpieLog.d(TAG, "Cannot create a new thread.");
                	}                                            			                     		                      	
                    /*Fragment loginFrag = new LoginFragment();
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    // Replace whatever is in the fragment_container view with
                    // this fragment
                    transaction.replace(R.id.container, loginFrag);

                    // Commit the transaction
                    transaction.commit();
                    */
                }
            //}
        });
        return v;
    }

    Runnable runnable = new Runnable() {          
        @Override  
        public void run() {
        	 location = GISManager.getInstance(getActivity()).getCurrentLocation();
			 NormalUser user = NormalUser.getInstance(); 
			 final String email = mEmail.getText().toString();
			 final String password = mPassword.getText().toString();
			 final String name = mNickname.getText().toString();                    			 			 
			 final String city = location.getCurrentCity();
			 
			 final String nonNullCity;
		     if(TextUtils.isEmpty(city))
		     {
		    	 nonNullCity = "unknown";
		     }else
		     {
		    	 nonNullCity = city;
		     }

		     LogpieCallback callback = new LogpieCallback(){
				@Override
				public void onSuccess(Bundle result) {						
       			Bundle bundle = new Bundle();
       			bundle.putString("email", email);
       			bundle.putString("nickname", name);
       			bundle.putString("city", nonNullCity);
       			
       			SQLStorage.getInstance(getActivity()).insert(bundle, "user", new LogpieCallback() {		                     
       				 @Override 
       				 public void onSuccess(Bundle bundle)
       				 {
       					 LogpieLog.d(TAG, bundle.toString()); 
       				 }
         
       				 @Override 
       				 public void onError(Bundle bundle) 
       				 {
       					 LogpieLog.e(TAG, "Failed to register in Android SQLite"); 
       				 } 
       			 });
				}

				@Override
				public void onError(Bundle errorMessagge) {
					// TODO Auto-generated method stub
				}
				 
			 };
			 user.register(getActivity(), email, password, name, nonNullCity, callback);
        }
    };
    
    
    private boolean isValid(StringBuffer text)
    {
        boolean tag = true;
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches())
        {
            text.append("Your email is not correct.");
            tag = false;
        }
        if (mPassword.getText().toString().equals(""))
        {
            text.append("Your password is empty.");
            tag = false;
        }
        if (mPassword.getText().length() < 8)
        {
            text.append("Your password's length is less than 8.");
            tag = false;
        }
        if (!mPassword.getText().toString().equals(mConfirmPassword.getText().toString()))
        {
            text.append("Your re-enter password is not correct.");
            tag = false;
        }
        if (mNickname.getText().toString().equals(""))
        {
            text.append("Your nickname is empty.");
            tag = false;
        }
        return tag;
    }
}
