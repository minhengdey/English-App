package com.noface.demo.user;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.noface.demo.resource.TokenManager;

public class UserInfo
{
    private String username, name, gender, email, phone;
    private int day, month, year;

    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String token = TokenManager.getInstance().getToken(), apiUri = "http://localhost:8080/";

    private String normalize_name(String s)
    {
        String[] k = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String i : k)
        {
            sb.append(i.substring(0, 1).toUpperCase()).append(i.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

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

    public UserInfo updateUserInfo(String name, String gender, String email, String phone, int day, int month, int year)
    {
        UserInfo userInfo = null;
        try
        {
            name = normalize_name(name);
            String requestBody = String.format (
                    "{" +
                            "\"name\":\"%s\"," +
                            "\"day\":%d," +
                            "\"month\":%d," +
                            "\"year\":%d," +
                            "\"gender\":\"%s\"," +
                            "\"email\":\"%s\"," +
                            "\"phone\":\"%s\"" +
                    "}",
                    name,
                    day,
                    month,
                    year,
                    gender,
                    email,
                    phone
            );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "users"))
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) userInfo = new UserInfo(username, name, gender, email, phone, day, month, year);
            else
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                int code = jsonNode.get("code").asInt();
                if (code == 1006) System.out.println ("Email không hợp lệ");
                else System.out.println ("Có lỗi xảy ra");
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return userInfo;
    }
}
