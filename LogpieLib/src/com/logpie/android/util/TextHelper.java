package com.logpie.android.util;

public class TextHelper
{
    public static boolean checkIfNull(String string)
    {
        if (string == null || string.equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
