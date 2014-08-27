package com.logpie.android.security;

import java.io.UnsupportedEncodingException;

import android.test.AndroidTestCase;
import android.util.Base64;

public class LogpieCommonEncryptorTest extends AndroidTestCase
{
    public void testEncryptionDecryption() throws UnsupportedEncodingException
    {
        String a = Base64.encodeToString("aaaaa".getBytes("UTF-8"),Base64.DEFAULT);
        Base64.decode(a, Base64.DEFAULT);
        
        String testString = "testString";
        LogpieCommonEncryptor encryptor = new LogpieCommonEncryptor();
        byte[] encryptionBytes = encryptor.encryptData(testString);
        assertNotNull(encryptionBytes);
        String decryptionData = encryptor.decryptData(encryptionBytes);
        assertEquals(testString, decryptionData);
        
        String testString2 = "abcdef#%#$%&#&@";
        byte[] encryptionBytes2 = encryptor.encryptData(testString2);
        assertNotNull(encryptionBytes2);
        assertEquals(testString2, encryptor.decryptData(encryptionBytes2));
        
        String testString3 = "";
        byte[] encryptionBytes3 = encryptor.encryptData(testString3);
        assertNotNull(encryptionBytes3);
        assertEquals(testString3, encryptor.decryptData(encryptionBytes3));
        
        String testString4 = null;
        byte[] encryptionBytes4 = encryptor.encryptData(testString4);
        assertNull(encryptionBytes4);
        
        
        assertNull(encryptor.decryptData(null));
    }

}
