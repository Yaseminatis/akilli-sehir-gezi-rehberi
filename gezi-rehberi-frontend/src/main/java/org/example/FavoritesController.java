package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class FavoritesController {

    @FXML private VBox favoritesContainer;

    @FXML
    public void initialize() {
        loadFavorites();
    }

    private void loadFavorites() {
        try {
            Long userId = SessionManager.getCurrentUserId();
            String response = GeziBacakendClient.getFavoritesByUser(userId);

            JsonElement parsed = JsonParser.parseString(response);
            JsonArray favArray = parsed.isJsonArray() ? parsed.getAsJsonArray() :
                    (parsed.isJsonObject() && parsed.getAsJsonObject().has("data") ? parsed.getAsJsonObject().getAsJsonArray("data") : new JsonArray());

            if (favoritesContainer != null) {
                favoritesContainer.getChildren().clear();
                favoritesContainer.setSpacing(15);
            }

            if (favArray.size() == 0) {
                Label emptyLbl = new Label("Henüz favori mekanınız bulunmuyor.");
                emptyLbl.setStyle("-fx-font-size: 16px; -fx-text-fill: #6b7280; -fx-padding: 20;");
                favoritesContainer.getChildren().add(emptyLbl);
                return;
            }

            for (JsonElement element : favArray) {
                JsonObject favObj = element.getAsJsonObject();
                Long placeId = favObj.get("placeId").getAsLong(); // NoSQL'den sadece ID'yi aldık

                // --- SQL'DEN MEKAN BİLGİLERİNİ ÇEKELİM ---
                String placeName = "Yükleniyor...";
                String category = "Bilinmiyor";
                try {
                    String placeRes = GeziBacakendClient.getPlaceById(placeId); // API'den mekanı soruyoruz
                    JsonObject pObj = JsonParser.parseString(placeRes).getAsJsonObject();
                    if (pObj.has("data")) pObj = pObj.getAsJsonObject("data"); // Data formatındaysa içini al

                    placeName = pObj.has("name") ? pObj.get("name").getAsString() : "İsimsiz";
                    category = pObj.has("category") ? pObj.get("category").getAsString() : "Kategori Yok";
                } catch (Exception e) {
                    placeName = "Mekan bulunamadı (ID: " + placeId + ")";
                }

                // --- KART TASARIMI ---
                HBox card = new HBox(15);
                card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 12; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 10, 0, 0, 5);");
                card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                VBox infoBox = new VBox(5);
                Label nameLbl = new Label("❤ " + placeName);
                nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #111827;");

                Label catLbl = new Label("Kategori: " + category);
                catLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 13px;");

                infoBox.getChildren().addAll(nameLbl, catLbl);

                // Kartın sağa doğru düzgün uzaması için boşluk bırakıyoruz
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // BUTONU TAMAMEN KALDIRDIK. Sadece bilgi kutusunu ekliyoruz.
                card.getChildren().addAll(infoBox, spacer);

                favoritesContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            e.printStackTrace();
            if (favoritesContainer != null) {
                favoritesContainer.getChildren().add(new Label("Favoriler yüklenirken bir hata oluştu."));
            }
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