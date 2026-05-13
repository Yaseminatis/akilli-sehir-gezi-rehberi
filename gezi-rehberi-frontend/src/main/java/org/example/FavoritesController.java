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
            // Yasemin'in MongoDB API'sinden favorileri çekiyoruz
            String jsonResponse = GeziBacakendClient.getFavoritesByUser(1L);

            // JSON'ın en dışındaki kutuyu açıp "data" listesini alıyoruz
            com.google.gson.JsonObject rootObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();
            com.google.gson.JsonArray dataArray = rootObject.getAsJsonArray("data");

            // Eğer favori yoksa
            if (dataArray == null || dataArray.isEmpty()) {
                Label emptyLabel = new Label("Henüz favorilere eklenmiş bir mekan yok.");
                emptyLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #6b7280;");
                favoritesContainer.getChildren().add(emptyLabel);
                return;
            }

            // Gelen JSON verilerini parçalayıp şık kartlara çeviriyoruz
            for (com.google.gson.JsonElement element : dataArray) {
                com.google.gson.JsonObject favObj = element.getAsJsonObject();
                String placeId = favObj.get("placeId").getAsString(); // Mekanın ID'sini al

                VBox card = new VBox();
                card.getStyleClass().add("place-card");
                card.setStyle("-fx-padding: 15; -fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 4);");

                Label favLabel = new Label("⭐ Kaydedilmiş Mekan (Sistem ID: " + placeId + ")");
                favLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #f59e0b;");

                Label desc = new Label("Bu mekan NoSQL (MongoDB) veritabanından başarıyla çekildi!");
                desc.setStyle("-fx-text-fill: #4b5563; -fx-font-size: 13px;");

                card.getChildren().addAll(favLabel, desc);
                favoritesContainer.getChildren().add(card);
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