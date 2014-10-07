package com.logpie.android.logic;

public class LogpieAccount
{
    private String uid;
    private String email;
    private String nickname;
    private String accessToken;
    private String refreshToken;

    public LogpieAccount(String uid, String email, String nickname, String accessToken,
            String refreshToken)
    {
        this.uid = uid;
        this.email = email;
        this.nickname = nickname;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getUid()
    {
        return uid;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public String getRefreshToken()
    {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken)
    {
        this.refreshToken = refreshToken;
    }

}
