package com.logpie.android.security;

import java.util.HashSet;

public class PackageWhiteList
{
    public static HashSet<String> packageWhiteList = new HashSet<String>();
    static
    {
        packageWhiteList.add("com.logpie.android");
        packageWhiteList.add("com.logpie.android.testapk");
    }
}
