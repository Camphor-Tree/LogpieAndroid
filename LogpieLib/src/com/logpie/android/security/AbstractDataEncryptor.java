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

import android.util.Base64;

import com.logpie.android.util.LogpieLog;

public abstract class AbstractDataEncryptor
{
    private final static String TAG = AbstractDataEncryptor.class.getName();
    private final static int INITIALIZATION_VECTOR_LENGTH = 16;
    private final static String AES_MODE = "AES/CBC/PKCS7Padding";
    private final static String UTF8 = "UTF-8";
    private byte[] mEncryptionKey;
    private SecretKeySpec mKeySpec;

    protected AbstractDataEncryptor()
    {
        mEncryptionKey = Base64.decode(getEncryptionKey(), Base64.DEFAULT);
        mKeySpec = new SecretKeySpec(mEncryptionKey, "AES");
    }

    public abstract String getEncryptionKey();

    public String encryptData(final String data)
    {
        if (data == null)
        {
            return null;
        }
        byte[] dataBytes = null;

        try
        {
            dataBytes = data.getBytes(UTF8);
        } catch (UnsupportedEncodingException e)
        {
            LogpieLog.e(TAG, "Not support UTF-8", e);
            return null;
        }

        byte[] iv = generateIVForEncryption();
        try
        {
            Cipher cipher = getCipher();
            cipher.init(Cipher.ENCRYPT_MODE, mKeySpec, new IvParameterSpec(iv));
            byte[] cipherText = doCipherOperation(cipher, dataBytes, 0, dataBytes.length);
            // The final encryption result consists of iv and encryption data
            // Attach iv array in front of encryption byte array.
            return Base64.encodeToString(concat(iv, cipherText), Base64.DEFAULT);

        } catch (InvalidKeyException e)
        {
            LogpieLog.e(TAG, "InvalidKeyException", e);
        } catch (InvalidAlgorithmParameterException e)
        {
            LogpieLog.e(TAG, "InvalidAlgorithmParameterException", e);
        }
        return null;
    };

    public String decryptData(final String dataString)
    {
        if (dataString == null)
        {
            return null;
        }
        byte[] dataBytes = Base64.decode(dataString, Base64.DEFAULT);
        if (dataBytes == null)
        {
            return null;
        }
        try
        {
            Cipher cipher = getCipher();
            cipher.init(Cipher.DECRYPT_MODE, mKeySpec, new IvParameterSpec(dataBytes, 0,
                    INITIALIZATION_VECTOR_LENGTH));
            byte[] plainDataBytes = doCipherOperation(cipher, dataBytes,
                    INITIALIZATION_VECTOR_LENGTH, dataBytes.length - INITIALIZATION_VECTOR_LENGTH);
            if (plainDataBytes == null)
            {
                return null;
            }
            // The final encryption result consists of iv and encryption data
            // Attach iv array in front of encryption byte array.
            return new String(plainDataBytes, UTF8);
        } catch (InvalidKeyException e)
        {
            LogpieLog.e(TAG, "InvalidKeyException", e);
        } catch (InvalidAlgorithmParameterException e)
        {
            LogpieLog.e(TAG, "InvalidAlgorithmParameterException", e);
        } catch (UnsupportedEncodingException e)
        {
            LogpieLog.e(TAG, "Not Support UTF-8", e);
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
            LogpieLog.e(TAG, "IllegalBlockSizeException happens when decrypt the data", e);
        } catch (BadPaddingException e)
        {
            LogpieLog.e(TAG, "BadPaddingException happens when decrypt the data", e);
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
            System.arraycopy(encryptedDataBytes, 0, finalResult, iv.length,
                    encryptedDataBytes.length);
        } catch (Exception e)
        {
            LogpieLog.e(TAG, "Exception happens when try to concat two bytes array", e);
            return null;
        }
        return finalResult;
    }

    private Cipher getCipher()
    {
        Cipher cipher = null;
        try
        {
            cipher = Cipher.getInstance(AES_MODE);
        } catch (NoSuchAlgorithmException e)
        {
            LogpieLog.e(TAG, "No Such Algorithm Exception, AES/CBC is not supported", e);
        } catch (NoSuchPaddingException e)
        {
            LogpieLog.e(TAG, "No Such Padding Exception, AES/CBC is not supported", e);
        }
        return cipher;
    }
}
