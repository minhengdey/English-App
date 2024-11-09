package com.example.demo.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import com.example.demo.entity.EnglishToVietnamese;
import com.example.demo.service.EnglishToVietnameseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component  // Đánh dấu đây là một Spring Bean
public class DictionaryLookupView {

    @FXML
    private TextField searchField;
    @FXML
    private Label wordLabel;
    @FXML
    private Label definitionLabel;

    @Autowired
    private EnglishToVietnameseService englishToVietnameseService;

    // Constructor để khởi tạo giao diện
    public DictionaryLookupView() {
        // Không khởi tạo giao diện ở đây nữa
    }

    @FXML
    private void searchWord() {
        String word = searchField.getText();

        if (word != null && !word.isEmpty()) {
            try {
                // Gọi service tìm kiếm từ và lấy kết quả
                EnglishToVietnamese result = englishToVietnameseService.getEnglishToVietnameseByWord(word);

                // Hiển thị kết quả trên giao diện
                wordLabel.setText(result.getWord());
                definitionLabel.setText(result.getDescription());
            } catch (RuntimeException e) {
                wordLabel.setText("Không tìm thấy từ này");
                definitionLabel.setText("");
            }
        }
    }

    public Scene createScene() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DictionaryLookupView.fxml"));
        loader.setController(this);  // Đảm bảo controller đã được thiết lập từ Spring
        AnchorPane root = loader.load();
        return new Scene(root);
    }
}
