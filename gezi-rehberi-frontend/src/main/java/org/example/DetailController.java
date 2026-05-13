package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.io.IOException;

public class DetailController {

    @FXML private Label cityNameLabel;
    @FXML private Label cityDescLabel;
    @FXML private VBox placesContainer;

    private City selectedCity;

    // MainController'dan seçilen şehri bu sayfaya aktarmak için kullanacağız
    public void initData(City city) {
        this.selectedCity = city;
        cityNameLabel.setText(city.getName() + " Rehberi");
        cityDescLabel.setText(city.getDescription());

        placesContainer.getChildren().clear();

        try {
            // Yasemin'in API'sinden bu şehre ait mekanları çek
            String jsonResponse = GeziBacakendClient.getPlacesByCity(city.getId());

            // 1. Yasemin'in Generic Response yapısını (O en dıştaki JSON objesini) okuyoruz
            com.google.gson.JsonObject rootObject = com.google.gson.JsonParser.parseString(jsonResponse).getAsJsonObject();

            // 2. İçindeki "data" isimli listeyi alıyoruz
            com.google.gson.JsonArray dataArray = rootObject.getAsJsonArray("data");

            // 3. Eğer "data" listesi boşsa
            if (dataArray == null || dataArray.isEmpty()) {
                Label emptyLabel = new Label("Bu şehir için henüz mekan kaydı bulunmuyor.");
                emptyLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: #6b7280; -fx-padding: 20;");
                placesContainer.getChildren().add(emptyLabel);
                return;
            }

            // 4. Liste doluysa, Place nesnelerine çevir ve ekrana bas
            com.google.gson.Gson gson = new com.google.gson.Gson();
            Place[] places = gson.fromJson(dataArray, Place[].class);

            for (Place place : places) {
                VBox placeCard = new VBox();
                placeCard.getStyleClass().add("place-card");

                Label name = new Label("📌 " + place.getName());
                name.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

                Label category = new Label("Kategori: " + place.getCategory());
                category.setStyle("-fx-text-fill: #d97706; -fx-font-size: 12px;");

                Label desc = new Label(place.getDescription());
                desc.setWrapText(true);
                desc.setStyle("-fx-text-fill: #4b5563;");

                javafx.scene.control.Button favBtn = new javafx.scene.control.Button("❤️ Favorilere Ekle");
                favBtn.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-cursor: hand; -fx-padding: 8 15; -fx-background-radius: 5;");

                favBtn.setOnAction(e -> {
                    try {
                        // Yasemin'in API'sine MongoDB kaydı atıyoruz
                        GeziBacakendClient.addFavorite(1L, place.getId());

                        // Butonu "Eklendi" olarak değiştir ve yeşil yap
                        favBtn.setText("✓ Eklendi");
                        favBtn.setStyle("-fx-background-color: #dcfce3; -fx-text-fill: #16a34a; -fx-padding: 8 15; -fx-background-radius: 5;");
                        favBtn.setDisable(true); // Tekrar tıklanmasın
                    } catch (Exception ex) {
                        System.out.println("Favori eklenirken hata: " + ex.getMessage());
                    }
                });

                placeCard.getChildren().addAll(name, category, desc, favBtn);
                placesContainer.getChildren().add(placeCard);
            }

        } catch (Exception e) {
            Label errorLabel = new Label("Mekanlar yüklenirken hata oluştu: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            placesContainer.getChildren().add(errorLabel);
        }
    }

    // Geri Dön butonuna basılınca çalışacak metot
    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}