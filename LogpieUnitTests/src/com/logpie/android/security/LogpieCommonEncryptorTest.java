package com.logpie.android.security;

import java.io.UnsupportedEncodingException;

import android.annotation.SuppressLint;
import android.test.AndroidTestCase;
import android.util.Base64;

public class LogpieCommonEncryptorTest extends AndroidTestCase
{
    @SuppressLint("NewApi")
    public void testEncryptionDecryption() throws UnsupportedEncodingException
    {
        String a = Base64.encodeToString("aaaaa".getBytes("UTF-8"), Base64.DEFAULT);
        Base64.decode(a, Base64.DEFAULT);

        String testString = "testString";
        byte[] bytes = testString.getBytes();
        assertEquals(testString, new String(bytes));

        LogpieCommonEncryptor encryptor = new LogpieCommonEncryptor();
        String encryptionString = encryptor.encryptData(testString);
        assertNotNull(encryptionString);
        String decryptionData = encryptor.decryptData(encryptionString);
        assertEquals(testString, decryptionData);

        String testString2 = "abcdef#%#$%&#&@";
        String encryptionString2 = encryptor.encryptData(testString2);
        assertNotNull(encryptionString2);
        assertEquals(testString2, encryptor.decryptData(encryptionString2));

        String testString3 = "";
        String encryptionString3 = encryptor.encryptData(testString3);
        assertNotNull(encryptionString3);
        assertEquals(testString3, encryptor.decryptData(encryptionString3));

        String testString4 = null;
        String encryptionString4 = encryptor.encryptData(testString4);
        assertNull(encryptionString4);

        assertNull(encryptor.decryptData(null));
    }

}
