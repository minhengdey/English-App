package com.noface.englishapp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import com.noface.englishapp.model.Card;
import javafx.scene.control.Alert;
import org.json.*;

public class CardCRUD {
    public static final int CARD_IS_AVAILABLED = 1;
    public static final int ERROR = 2;
    public static final int CARD_ADDED_SUCCESS = 3;
    public static final int CARD_DELETED_SUCCESS = 4;
    public static final int CARD_EDITED_SUCCESS = 5;
    public static final int TOPIC_DELETED_SUCCESS = 6;
    public static final int TOPIC_RENAME_SUCCESS = 7;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;
    private String apiUri = "http://localhost:8080/";

    public static String normalize_name(String s) {
        String[] k = s.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String i : k) {
            sb.append(i.substring(0, 1).toUpperCase()).append(i.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    public CardCRUD() {
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
    }

    public int addCard(String frontSide, String backSide, String topic, String name) {
        Card card = new Card();
        try {
            String token = TokenManager.getInstance().getToken();
            CardRequest cardRequest = new CardRequest(frontSide, backSide, topic, LocalDateTime.now().toString(), name);
            String requestBody = objectMapper.writeValueAsString(cardRequest);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word"))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                long id = jsonNode.get("id").asLong();
                String date = jsonNode.get("date").asText();
                card = new Card(id, name, frontSide, backSide, topic, date);
            } else {
                String jsonResponse = response.body();
                JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                int code = jsonNode.get("code").asInt();
                if (code == 1009) {
                    Utilities.getInstance().showAlert("Card already exists, cannot add card.", Alert.AlertType.WARNING);
                    return CardCRUD.CARD_IS_AVAILABLED;
                } else {
                    Utilities.getInstance().showAlert("An error occurred, cannot add card.", Alert.AlertType.WARNING);
                    return CardCRUD.ERROR;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CardCRUD.CARD_ADDED_SUCCESS;
    }

    public int deleteCard(Card card) {
        try {
            String token = TokenManager.getInstance().getToken();
            long id = Long.parseLong(card.getId());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/" + id))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Delete status code " + response.statusCode());

            if (response.statusCode() == 200) {
                // Successfully deleted
                return CARD_DELETED_SUCCESS;
            }
            System.out.println("Delete status code " + response.statusCode());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERROR;
    }

    public int editCard(Card card, String frontSide, String backSide, String topic, String name, String date) {
        try {
            String token = TokenManager.getInstance().getToken();
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
            if (response.statusCode() == 200) {
                card = new Card(id, frontSide, backSide, topic, name, date);
                return CARD_EDITED_SUCCESS;
            } else System.out.println("An error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ERROR;
    }

    public ArrayList<String> getAllTopics() {
        ArrayList<String> topics = new ArrayList<>();
        try {
            String token = TokenManager.getInstance().getToken();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "topic"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Get all topics");
            System.out.println(response);
            System.out.println(token);
            if (response.statusCode() == 200) {
                JSONArray jsonArray = new JSONArray(response.body());
                for (int i = 0; i < jsonArray.length(); ++i) {
                    topics.add(jsonArray.getString(i));
                }
            } else System.out.println("An error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topics;
    }

    public List<Card> getAllCardsByTopic(String topic) {
        List<Card> cards = new ArrayList<>();
        try {
            String token = TokenManager.getInstance().getToken();
            String encodedTopic = URLEncoder.encode(normalize_name(topic), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/topic/" + encodedTopic))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                String jsonResponse = response.body();
                JsonNode arrayNode = objectMapper.readTree(jsonResponse);
                for (JsonNode node : arrayNode) {
                    long id = node.get("id").asLong();
                    String name = node.get("name").asText();
                    String frontSide = node.get("frontSide").asText();
                    String backSide = node.get("backSide").asText();
                    String date = node.get("date").asText();
                    cards.add(new Card(id, name, frontSide, backSide, topic, date));
                }
            } else System.out.println("An error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    public int deleteCardByTopic(String topic) {
        String token = TokenManager.getInstance().getToken();
        try {
            String encodedTopic = URLEncoder.encode(normalize_name(topic), StandardCharsets.UTF_8);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/topic/" + encodedTopic))
                    .header("Authorization", "Bearer " + token)
                    .DELETE()
                    .build();
            HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            System.out.println("Delete topic response code: " + response.statusCode());
            System.out.println(response.uri());
            if (response.statusCode() == 200) {
                System.out.println("Deleted successfully.");
                return TOPIC_DELETED_SUCCESS;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ERROR;
    }

    public int renameTopic(String oldTopic, String newTopic) {
        oldTopic = URLEncoder.encode(oldTopic, StandardCharsets.UTF_8);
        newTopic = URLEncoder.encode(normalize_name(newTopic), StandardCharsets.UTF_8);
        String token = TokenManager.getInstance().getToken();
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(apiUri + "new_word/topic/" + oldTopic + "/" + newTopic))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + token)
                    .POST(HttpRequest.BodyPublishers.noBody()).build();
            HttpResponse response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return TOPIC_RENAME_SUCCESS;
            }
            return ERROR;
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
