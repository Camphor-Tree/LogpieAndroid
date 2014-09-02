package com.logpie.android.model;

/**
 * This class is used to document all the necessary text/image in the tile bar.
 * 
 * @author yilei
 * 
 */
public final class Title
{
    private String[] mTitleString;

    public Title()
    {
        mTitleString = new String[] { "�����", "ͬ�ǻ", "����" };
    }

    public String[] getTitleString()
    {
        return mTitleString;
    }
}
