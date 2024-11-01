package com.example.englishapp.Controller;

import com.example.englishapp.Vocabulary;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.Optional;
import java.net.*;
import java.net.http.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VocabularyController
{
    private TableView<Vocabulary> table;
    private ObservableList<Vocabulary> list;
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public VocabularyController()
    {
        table = new TableView<>();
        list = FXCollections.observableArrayList();
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        table.setItems(list);
        display();
    }

    public TableCell<Vocabulary, Void> action()
    {
        return new TableCell<>()
        {
            private Button editButton = new Button("Sửa");
            private Button deleteButton = new Button("Xóa");
            {
                editButton.setOnAction(_ ->
                {
                    Vocabulary word = getTableView().getItems().get(getIndex());
                    editWord(word);
                });

                deleteButton.setOnAction(_ ->
                {
                    Vocabulary word = getTableView().getItems().get(getIndex());
                    deleteWord(word);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty)
            {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else setGraphic(new HBox(10, editButton, deleteButton));
            }
        };
    }

    public VBox getRoot()
    {
        TableColumn<Vocabulary, String> EnglishColumn = new TableColumn<>("Từ tiếng Anh");
        EnglishColumn.setCellValueFactory(cellData -> cellData.getValue().EnglishProperty());
        TableColumn<Vocabulary, String> VietnameseColumn = new TableColumn<>("Nghĩa tiếng Việt");
        VietnameseColumn.setCellValueFactory(cellData -> cellData.getValue().VietnameseProperty());
        TableColumn<Vocabulary, Void> Actions = new TableColumn<>("Thao tác");
        Actions.setCellFactory(_ -> action());
        table.getColumns().addAll(EnglishColumn, VietnameseColumn, Actions);

        Button addButton = new Button("Thêm từ vựng");
        addButton.setOnAction(_ -> addWord());

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.getChildren().addAll(table, addButton);
        return layout;
    }

    private void display()
    {
        try
        {
            HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/new_word")).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                String responseBody = response.body();
                //System.out.print (responseBody);
                List<Vocabulary> vocabularies = objectMapper.readValue(responseBody, new TypeReference<>(){});
                list.setAll(vocabularies);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addWord()
    {
        Dialog<Vocabulary> dialog = new Dialog<>();
        dialog.setTitle("Thêm từ mới");

        Label EnglishLabel = new Label("Từ tiếng Anh: ");
        TextField EnglishInput = new TextField();
        EnglishInput.setPromptText("Từ tiếng Anh");
        Label VietnameseLabel = new Label("Nghĩa tiếng Việt: ");
        TextField VietnameseInput = new TextField();
        VietnameseInput.setPromptText("Nghĩa tiếng Việt");

        VBox layout = new VBox(10);
        layout.getChildren().addAll(EnglishLabel, EnglishInput, VietnameseLabel, VietnameseInput);
        dialog.getDialogPane().setContent(layout);

        ButtonType buttonType = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE));

        Button button = (Button)dialog.getDialogPane().lookupButton(buttonType);
        button.setDisable(true);
        EnglishInput.textProperty().addListener((_, _, _) ->
        {
            button.setDisable(EnglishInput.getText().trim().isEmpty() || VietnameseInput.getText().trim().isEmpty());
        });
        VietnameseInput.textProperty().addListener((_, _, _) ->
        {
            button.setDisable(EnglishInput.getText().trim().isEmpty() || VietnameseInput.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == buttonType) return new Vocabulary(EnglishInput.getText(), VietnameseInput.getText());
            return null;
        });

        dialog.showAndWait().ifPresent(word ->
        {
            try
            {
                String requestBody = objectMapper.writeValueAsString(word);
                //System.out.println (requestBody);
                HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/new_word")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                //System.out.println (response.statusCode());
                if (response.statusCode() == 200)
                {
                    list.add(word);
                    String jsonResponse = response.body();
                    //System.out.println (jsonResponse);
                    JsonNode jsonNode = objectMapper.readTree(jsonResponse);
                    long ID = jsonNode.get("id").asInt();
                    //System.out.println (ID);
                    word.setId(ID);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    private void editWord(Vocabulary word)
    {
        Dialog<Vocabulary> dialog = new Dialog<>();
        dialog.setTitle("Sửa từ vựng");

        Label EnglishLabel = new Label("Từ tiếng Anh: ");
        TextField EnglishInput = new TextField(word.getEnglish());
        Label VietnameseLabel = new Label("Nghĩa tiếng Việt: ");
        TextField VietnameseInput = new TextField(word.getVietnamese());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(EnglishLabel, EnglishInput, VietnameseLabel, VietnameseInput);
        dialog.getDialogPane().setContent(layout);

        ButtonType buttonType = new ButtonType("Lưu", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, new ButtonType("Hủy", ButtonBar.ButtonData.CANCEL_CLOSE));

        Button button = (Button)dialog.getDialogPane().lookupButton(buttonType);
        button.setDisable(false);
        EnglishInput.textProperty().addListener((_, _, _) ->
        {
            button.setDisable(EnglishInput.getText().trim().isEmpty() || VietnameseInput.getText().trim().isEmpty());
        });
        VietnameseInput.textProperty().addListener((_, _, _) ->
        {
            button.setDisable(EnglishInput.getText().trim().isEmpty() || VietnameseInput.getText().trim().isEmpty());
        });

        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == buttonType) return new Vocabulary(EnglishInput.getText(), VietnameseInput.getText());
            return null;
        });

        dialog.showAndWait().ifPresent(w ->
        {
            try
            {
                word.setEnglish(w.getEnglish());
                word.setVietnamese(w.getVietnamese());
                String requestBody = objectMapper.writeValueAsString(word);
                //System.out.println (requestBody + "\n" + word.getId());
                HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/new_word/" + word.getId())).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(requestBody)).build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                //System.out.println (response.statusCode());
                if (response.statusCode() == 200) table.refresh();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    private void deleteWord(Vocabulary word)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận xóa");
        alert.setHeaderText("Bạn có chắc chắn muốn xóa từ vựng này?");
        alert.setContentText("Từ tiếng Anh: " + word.getEnglish() + "\nNghĩa tiếng Việt: " + word.getVietnamese());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK)
        {
            try
            {
                HttpRequest request = HttpRequest.newBuilder().uri(new URI("http://localhost:8080/new_word/" + word.getId())).DELETE().build();
                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) list.remove(word);
                //System.out.println (response.statusCode());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public ObservableList<Vocabulary> getList() {
        return list;
    }

    public void setList(ObservableList<Vocabulary> list) {
        this.list = list;
    }
}