package com.noface.demo.resource;

public class TokenManager
{
    private static TokenManager instance;
    private String token;

    private TokenManager()
    {
    }

    public static synchronized TokenManager getInstance()
    {
        if (instance == null) instance = new TokenManager();
        return instance;
    }

    public String getToken()
    {
        return this.token;
    }

    public void setToken(String token)
    {
        if (this.token != null) clearToken();
        this.token = token;
    }

    public void clearToken()
    {
        this.token = null;
    }
}
