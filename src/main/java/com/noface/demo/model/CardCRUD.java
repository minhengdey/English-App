package com.noface.demo.card;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.*;
import com.noface.demo.utils.TokenManager;
import javafx.beans.property.ListProperty;
import org.json.*;

public class CardCRUD
{
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private String token, apiUri = "http://localhost:8080/";

    public CardCRUD()
    {
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        token = TokenManager.getInstance().getToken();
    }

    public Card addCard(String frontSide, String backSide, String topic, String name)
    {
        Card card = new Card();
        try
        {
            CardRequest cardRequest = new CardRequest(frontSide, backSide, topic, LocalDateTime.now().toString(), name);
            String requestBody = objectMapper.writeValueAsString(cardRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                long id = jsonNode.get("id").asLong();
                String front_side = jsonNode.get("frontSide").asText();
                String back_side = jsonNode.get("backSide").asText();
                String Topic = jsonNode.get("topic").asText();
                String date = jsonNode.get("date").asText();
                String Name = jsonNode.get("name").asText();
                card = new Card(id, Name, front_side, back_side, Topic, date);
            }
            else
            {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                int code = jsonNode.get("code").asInt();
                if (code == 1009) System.out.println ("Đã tồn tại từ mới!");
                else System.out.println ("Có lỗi xảy ra");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return card;
    }

    public void deleteCard(Card card)
    {
        try
        {
            long id = Long.parseLong(card.getId());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/" + id))
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                // Xóa thành công
                System.out.println ("Thành công");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public Card editCard(Card card, String frontSide, String backSide, String topic, String name)
    {
        try
        {
            String date = LocalDateTime.now().toString();
            CardRequest cardRequest = new CardRequest(frontSide, backSide, topic, date, name);
            long id = Long.parseLong(card.getId());
            String requestBody = objectMapper.writeValueAsString(cardRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/" + id))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) card = new Card(id, frontSide, backSide, topic, name, date);
            else System.out.println ("Có lỗi xảy ra");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return card;
    }

    public ArrayList<String> getAllTopics()
    {
        ArrayList<String> topics = new ArrayList<>();
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "topic"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); ++i)
                {
                    topics.add(jsonArray.getString(i));
                }
            }
            else System.out.println ("Có lỗi xảy ra");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return topics;
    }

    public ArrayList<Card> getAllCardsByTopic(String topic)
    {
        ArrayList<Card> cards = new ArrayList<>();
        try
        {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/topic/" + topic))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                String jsonResponse = response.body();
                JsonNode arrayNode = objectMapper.readTree(jsonResponse);
                for (JsonNode node : arrayNode)
                {
                    long id = node.get("id").asLong();
                    String name = node.get("name").asText();
                    String frontSide = node.get("frontSide").asText();
                    String backSide = node.get("backSide").asText();
                    String date = node.get("date").asText();
                    cards.add(new Card(id, name, frontSide, backSide, topic, date));
                }
            }
            else System.out.println ("Có lỗi xảy ra");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return cards;
    }
}