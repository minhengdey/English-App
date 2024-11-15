package com.noface.demo.resource;

public class TokenManager
{
    private static TokenManager instance;
    private String token;

    private TokenManager()
    {
    }

    public static TokenManager getInstance()
    {
        if (instance == null) instance = new TokenManager();
        return instance;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public void clearToken()
    {
        token = null;
    }
}
