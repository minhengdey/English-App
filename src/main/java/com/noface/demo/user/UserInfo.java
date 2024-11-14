package com.noface.demo.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.noface.demo.utils.TokenManager;

public class UserInfo
{
    private String username, name, gender, email, phone;
    private int day, month, year;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String token = TokenManager.getInstance().getToken(), apiUri = "http://localhost:8080/";

    public UserInfo(String username, String name, String gender, String email, String phone, int day, int month, int year)
    {
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public UserInfo getUserInfo()
    {
        UserInfo userInfo = null;
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "myInfo"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                username = jsonNode.get("username").asText();
                name = jsonNode.get("name").asText();
                gender = jsonNode.get("gender").asText();
                email = jsonNode.get("email").asText();
                phone = jsonNode.get("phone").asText();
                day = jsonNode.get("day").asInt();
                month = jsonNode.get("month").asInt();
                year = jsonNode.get("year").asInt();
                userInfo = new UserInfo(username, name, gender, email, phone, day, month, year);
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return userInfo;
    }
}
