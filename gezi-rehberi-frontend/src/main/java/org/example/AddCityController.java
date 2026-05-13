package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCityController {

    @FXML private TextField nameField;
    @FXML private TextField countryField;
    @FXML private TextArea descArea;
    @FXML private TextField imageField;
    @FXML private Label statusLabel;

    @FXML
    private void saveCity(ActionEvent event) {
        String name = nameField.getText();
        String country = countryField.getText();
        String desc = descArea.getText();
        String imageUrl = imageField.getText();

        // Basit bir validasyon (Boş bırakılamaz kuralları)
        if (name.isEmpty() || country.isEmpty()) {
            statusLabel.setText("Lütfen Şehir Adı ve Ülke alanlarını doldurun!");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            // Yasemin'in API'sine POST isteği atarak şehri veritabanına ekliyoruz
            String response = GeziBacakendClient.createCity(name, country, desc, imageUrl);

            if (response != null) {
                System.out.println("Şehir Eklendi: " + response);
                // Başarılı olursa ana sayfaya geri dön
                goBack(event);
            }
        } catch (Exception e) {
            statusLabel.setText("Hata oluştu: Backend açık mı? Detay: " + e.getMessage());
            statusLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}