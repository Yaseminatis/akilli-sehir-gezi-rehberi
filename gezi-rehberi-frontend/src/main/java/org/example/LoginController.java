package org.example;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private PasswordField passwordField;
    @FXML private TextField emailField;
    @FXML private Label errorLabel;

    @FXML
    private void login(ActionEvent event) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // 1. Temel Kontrol
        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Lütfen tüm alanları doldurun.");
            return;
        }

        try {
            String response = GeziBacakendClient.getUserByEmail(email);
            System.out.println("🚨 BACKEND'DEN GELEN CEVAP: " + response);
            JsonObject rootJson = JsonParser.parseString(response).getAsJsonObject();

            JsonObject userJson = null;

            // 1. Yasemin'in ApiResponse kutusunu (data) kontrol et
            if (rootJson.has("data") && !rootJson.get("data").isJsonNull()) {
                userJson = rootJson.getAsJsonObject("data");
            } else {
                userJson = rootJson; // Kutu yoksa direkt objeyi al
            }

            // 2. Artık id'yi arayabiliriz
            if (userJson != null && userJson.has("id")) {
                String dbPassword = userJson.get("password").getAsString();

                if (password.equals(dbPassword)) {
                    // Şifre doğru! Sisteme gir.
                    SessionManager.setCurrentUserId(userJson.get("id").getAsLong());
                    SessionManager.setCurrentUserRole(userJson.get("role").getAsString());
                    SessionManager.setCurrentUsername(userJson.get("username").getAsString());

                    loadMainPage(event);
                } else {
                    errorLabel.setText("Hata: Şifre yanlış!");
                }
            } else {
                errorLabel.setText("Hata: Bu e-posta ile kayıtlı kullanıcı bulunamadı.");
            }
        } catch (Exception e) {
            errorLabel.setText("Sunucuya bağlanılamadı. Lütfen tekrar deneyin.");
            e.printStackTrace();
        }
    }

    private void loadMainPage(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Pencere boyutunu ana sayfa için uygun hale getir
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}