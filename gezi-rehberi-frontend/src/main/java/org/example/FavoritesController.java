package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FavoritesController {

    @FXML private VBox favoritesContainer;

    @FXML
    public void initialize() {
        try {
            // 1. DÜZELTME: Artık sabit 1L değil, sisteme giriş yapan gerçek kullanıcının ID'sini alıyoruz!
            Long userId = SessionManager.getCurrentUserId();
            if (userId == null) {
                favoritesContainer.getChildren().add(new Label("Lütfen önce giriş yapın!"));
                return;
            }

            // Yasemin'in MongoDB API'sinden bu kullanıcının favorilerini çekiyoruz
            String jsonResponse = GeziBacakendClient.getFavoritesByUser(userId);

            com.google.gson.JsonObject rootObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();
            com.google.gson.JsonArray dataArray = rootObject.getAsJsonArray("data");

            if (dataArray == null || dataArray.isEmpty()) {
                Label emptyLabel = new Label("Henüz favorilere eklenmiş bir mekanınız yok.");
                emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6b7280;");
                favoritesContainer.getChildren().add(emptyLabel);
                return;
            }

            // Gelen her bir favori kaydı için dönüyoruz
            for (com.google.gson.JsonElement element : dataArray) {
                com.google.gson.JsonObject favObj = element.getAsJsonObject();
                Long placeId = favObj.get("placeId").getAsLong(); // Mekanın ID'sini al

                // 2. SİHİRLİ DOKUNUŞ: MongoDB'den gelen ID ile SQL'den mekanın gerçek bilgilerini çek!
                String placeResponse = GeziBacakendClient.getPlaceById(placeId);
                com.google.gson.JsonObject placeRoot = com.google.gson.JsonParser.parseString(placeResponse).getAsJsonObject();

                // Eğer mekan başarıyla bulunduysa verilerini al
                if (placeRoot.get("success").getAsBoolean()) {
                    com.google.gson.JsonObject placeData = placeRoot.getAsJsonObject("data");
                    String realName = placeData.get("name").getAsString();
                    String realCategory = placeData.get("category").getAsString();

                    // Kartı Tasarla
                    VBox card = new VBox();
                    card.getStyleClass().add("place-card");
                    card.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");

                    Label nameLabel = new Label("⭐ " + realName);
                    nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #f59e0b;");

                    Label catLabel = new Label("Kategori: " + realCategory);
                    catLabel.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");

                    card.getChildren().addAll(nameLabel, catLabel);
                    favoritesContainer.getChildren().add(card);
                }
            }

        } catch (Exception e) {
            favoritesContainer.getChildren().add(new Label("Favoriler yüklenemedi: " + e.getMessage()));
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