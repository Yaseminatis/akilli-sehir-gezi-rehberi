package org.example;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML private FlowPane cityContainer;

    @FXML
    public void initialize() {
        try {
            // Backend'den verileri al
            String jsonResponse = GeziBacakendClient.getAllCities();
            Gson gson = new Gson();
            City[] cities = gson.fromJson(jsonResponse, City[].class);

            // Her şehir için kart oluştur
            for (City city : cities) {
                VBox card = new VBox();
                card.getStyleClass().add("city-card");
                card.setPrefSize(200, 150);

                Label name = new Label(city.getName());
                name.getStyleClass().add("card-title");

                Label desc = new Label(city.getDescription());
                desc.setWrapText(true);

                card.getChildren().addAll(name, desc);

                // İŞTE BURASI: Karta tıklama özelliği ekliyoruz
                card.setOnMouseClicked(e -> {
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/detail.fxml"));
                        Parent root = loader.load();

                        // Detay sayfasına seçilen şehri gönderiyoruz
                        DetailController controller = loader.getController();
                        controller.initData(city);

                        // Ekranı değiştiriyoruz
                        Stage stage = (Stage) card.getScene().getWindow();
                        stage.setScene(new Scene(root, 800, 600));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

                cityContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            cityContainer.getChildren().add(new Label("Veri çekilemedi: " + e.getMessage()));
        }
    }
    @FXML
    private void openAddCityPage(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/add_city.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void openFavoritesPage(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/favorites.fxml"));
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}