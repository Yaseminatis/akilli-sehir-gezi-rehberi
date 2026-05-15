package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML private TextField userIdField;
    @FXML private Label errorLabel;

    @FXML
    private void login(ActionEvent event) {
        String idText = userIdField.getText();

        if (idText.isEmpty()) {
            errorLabel.setText("Lütfen bir Kullanıcı ID girin (Örn: 1)");
            return;
        }

        try {
            Long userId = Long.parseLong(idText);

            // Kullanıcı ID'sini sisteme kaydediyoruz! Artık her yerde bu ID geçerli olacak.
            SessionManager.setCurrentUserId(userId);

            // Ana sayfaya (main.fxml) geçiş yap
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));

        } catch (NumberFormatException e) {
            errorLabel.setText("Lütfen sadece rakam girin!");
        } catch (Exception e) {
            errorLabel.setText("Sisteme girilemedi: " + e.getMessage());
        }
    }
}