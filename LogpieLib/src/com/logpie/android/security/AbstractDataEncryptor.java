package com.logpie.android.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.logpie.android.util.LogpieLog;

import android.util.Base64;
import android.util.Log;

public abstract class AbstractDataEncryptor
{
    private final static String TAG = AbstractDataEncryptor.class.getName();
    private final static int INITIALIZATION_VECTOR_LENGTH = 16;
    private final static String AES_MODE = "AES/CBC/PKCS7Padding";
    private final static String UTF8 = "UTF-8";
    private byte[] mEncryptionKey;
    private SecretKeySpec mKeySpec;
    
    protected Cipher mCipher;
    
    protected AbstractDataEncryptor ()
    {
        mEncryptionKey  = Base64.decode(getEncryptionKey(), Base64.DEFAULT);
        mKeySpec = new SecretKeySpec(mEncryptionKey, "AES");
        try
        {
            mCipher  = Cipher.getInstance(AES_MODE);
        } catch (NoSuchAlgorithmException e)
        {
           LogpieLog.e(TAG,"No Such Algorithm Exception, AES/CBC is not supported");
           e.printStackTrace();
        } catch (NoSuchPaddingException e)
        {
            LogpieLog.e(TAG,"No Such Padding Exception, AES/CBC is not supported");
            e.printStackTrace();
        }
    }
    
    public abstract String getEncryptionKey();
   
    public byte[] encryptData(final String data)
    {
        if(data==null)
        {
            return null;
        }
        byte[] dataBytes = null;
        try
        {
            dataBytes = data.getBytes(UTF8);
        } catch (UnsupportedEncodingException e1)
        {
            LogpieLog.e(TAG, "UnsupportedEncodingException");
            e1.printStackTrace();
        }
        byte[] iv = generateIVForEncryption();
        try
        {
            mCipher.init(Cipher.ENCRYPT_MODE, mKeySpec, new IvParameterSpec(iv));
            byte[] cipherText = doCipherOperation(mCipher, dataBytes, 0, dataBytes.length);
            // The final encryption result consists of iv and encryption data
            // Attach iv array in front of encryption byte array.
            return concat(iv,cipherText);
            
        } catch (InvalidKeyException e)
        {
            LogpieLog.e(TAG, "InvalidKeyException");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e)
        {
            LogpieLog.e(TAG, "InvalidAlgorithmParameterException");
            e.printStackTrace();
        }
        return null;
    };
    
    public String decryptData(final byte[] dataBytes)
    {
        if(dataBytes ==null)
        {
            return null;
        }
        try
        {
            mCipher.init(Cipher.DECRYPT_MODE, mKeySpec, new IvParameterSpec(dataBytes,0,INITIALIZATION_VECTOR_LENGTH));
            byte[] plainDataBytes = doCipherOperation(mCipher, dataBytes, INITIALIZATION_VECTOR_LENGTH, dataBytes.length-INITIALIZATION_VECTOR_LENGTH);
            // The final encryption result consists of iv and encryption data
            // Attach iv array in front of encryption byte array.
            return new String(plainDataBytes,UTF8);
        } catch (InvalidKeyException e)
        {
            LogpieLog.e(TAG, "InvalidKeyException");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e)
        {
            LogpieLog.e(TAG, "InvalidAlgorithmParameterException");
            e.printStackTrace();
        } catch (UnsupportedEncodingException e)
        {
            LogpieLog.e(TAG, "Doesn't support UTF-8, which is impossible");
            e.printStackTrace();
        }
        return null;
    };
    
    private byte[] doCipherOperation(Cipher cipher, byte[] dataToEncrypt, int offset, int length)
    {
        try
        {
            return cipher.doFinal(dataToEncrypt, offset, length);
        } catch (IllegalBlockSizeException e)
        {
            e.printStackTrace();
        } catch (BadPaddingException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private byte[] generateIVForEncryption()
    {
        final SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[INITIALIZATION_VECTOR_LENGTH];
        secureRandom.nextBytes(iv);
        return iv;
    }
    
    private byte[] concat(byte[] iv, byte[] encryptedDataBytes)
    {
        byte[] finalResult = new byte[iv.length + encryptedDataBytes.length];
        try
        {
        System.arraycopy(iv, 0, finalResult, 0, iv.length);
        System.arraycopy(encryptedDataBytes, 0, finalResult, iv.length, encryptedDataBytes.length);
        }
        catch(Exception e)
        {
            LogpieLog.e(TAG, "Exception happens when try to concat two bytes array");
            e.printStackTrace();
            return null;
        }
        return finalResult;
    }

}
