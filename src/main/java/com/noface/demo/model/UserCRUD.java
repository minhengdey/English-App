package com.noface.demo.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.noface.demo.resource.TokenManager;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

public class UserCRUD {
    public static final int USER_EDITED_SUCCESSFUL = 1;
    public static final int ERROR = 0;
    private HttpClient httpClient = HttpClient.newHttpClient();
    private ObjectMapper objectMapper = new ObjectMapper();
    private String apiUri = "http://localhost:8080/";

    public User getUserInfo() {
        try {
            String token = TokenManager.getInstance().getToken();
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(apiUri + "myInfo")).header("Authorization", "Bearer " + token).GET().build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            JsonNode rootNode = objectMapper.readTree(response.body());
            JsonNode userNode = rootNode.path("result");
            UserRequest userRequest = new UserRequest(userNode.get("username").asText(),
                    userNode.get("name").asText(),
                    userNode.get("day").asInt(),
                    userNode.get("month").asInt(),
                    userNode.get("year").asInt(),
                    userNode.get("gender").asText(),
                    userNode.get("email").asText(),
                    userNode.get("phone").asText()
                    );
            User user = new User(
               userRequest.getUsername(),
               userRequest.getGender(),
               userRequest.getName(),
               userRequest.getPhone(),
                LocalDate.of(userRequest.getYear(),
                        userRequest.getMonth(),
                        userRequest.getDay()).toString(),
               userRequest.getEmail()
            );
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        } return null;
    }
    public int editUser(User user, String password){
        try {
            String token = TokenManager.getInstance().getToken();
            LocalDate date = LocalDate.parse(user.getDob());
            UserRequest userRequest = new UserRequest(
                    user.getUsername(),
                    user.getName(),
                    (date.getDayOfMonth()),
                    (date.getMonth().getValue()),
                    (date.getYear()),
                    user.getGender(),
                    user.getEmail(),
                    user.getPhone(),
                    password
            );

            String requestBody = objectMapper.writeValueAsString(userRequest);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "users"))
                    .header("Authorization", "Bearer " + token)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() == 200){
                return USER_EDITED_SUCCESSFUL;
            }
            return ERROR;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}