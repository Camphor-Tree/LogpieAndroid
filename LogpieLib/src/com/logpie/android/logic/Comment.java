package com.logpie.android.logic;

public class Comment
{
    private String mContent;
    private String mUserName;
    private String mCommentTime;

    public Comment(String userName, String content)
    {
        mContent = content;
        mUserName = userName;
    }

    public Comment(String content, String userName, String commentTime)
    {
        mContent = content;
        mUserName = userName;
        mCommentTime = commentTime;
    }

    public String getContent()
    {
        return mContent;
    }

    public void setContent(String content)
    {
        mContent = content;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public void setUserName(String userName)
    {
        mUserName = userName;
    }

    public String getCommentTime()
    {
        return mCommentTime;
    }

    public void setCommentTime(String commentTime)
    {
        mCommentTime = commentTime;
    }

}
