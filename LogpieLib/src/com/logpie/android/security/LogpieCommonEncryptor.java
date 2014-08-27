package com.logpie.android.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.logpie.android.util.LogpieLog;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.Base64;

public class LogpieCommonEncryptor extends AbstractDataEncryptor
{
    private static final String TAG = LogpieCommonEncryptor.class.getName();
    private static final String sAlgorithm = "PBKDF2WithHmacSHA1";
    private static final int sEncryptionKeyLength = 128;
    private static final int sIterationNum = 1000;
    public LogpieCommonEncryptor()
    {
        super();
    }

    @SuppressLint("NewApi")
    @Override
    public String getEncryptionKey()
    {
        try
        {
            final SecretKeyFactory factory = SecretKeyFactory.getInstance(sAlgorithm);
            //use device secret to encrypt data
            final KeySpec keySpec = new PBEKeySpec(Build.MANUFACTURER.toCharArray(),Build.SERIAL.getBytes(),sIterationNum,sEncryptionKeyLength);
            final SecretKey key = factory.generateSecret(keySpec);
            return Base64.encodeToString(key.getEncoded(), Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e1)
        {
            LogpieLog.e(TAG, "Doesn't support such algorithm");
            e1.printStackTrace();
        } catch (InvalidKeySpecException e2)
        {
            LogpieLog.e(TAG, "InvalidKeySpecException");
            e2.printStackTrace();
        }
        return  null;
    }
}
