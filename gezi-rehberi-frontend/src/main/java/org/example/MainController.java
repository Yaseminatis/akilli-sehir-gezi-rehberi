package org.example;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainController {
    @FXML private FlowPane cityContainer;
    @FXML private javafx.scene.control.Button addCityBtn;
    @FXML private Label welcomeLabel;

    @FXML
    public void initialize() {
        if (SessionManager.getCurrentUsername() != null) {
            welcomeLabel.setText("Merhaba, " + SessionManager.getCurrentUsername());
        }
        if ("USER".equals(SessionManager.getCurrentUserRole())) {
            addCityBtn.setVisible(false);
            addCityBtn.setManaged(false);
        }
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
                card.setOnMouseClicked(event -> {
                    System.out.println("🚨 ŞEHRE TIKLANDI: " + city.getName()); // 1. Tıklamayı algılıyor mu?
                    try {
                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/detail.fxml"));
                        javafx.scene.Parent root = loader.load();

                        DetailController controller = loader.getController();
                        if (controller != null) {
                            controller.initData(city);
                        } else {
                            System.out.println("🚨 HATA: DetailController dosyaya bağlanamamış!");
                        }

                        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new javafx.scene.Scene(root, 800, 600));

                    } catch (Exception e) {
                        System.out.println("🚨 SAYFA YÜKLENİRKEN ÇÖKTÜ! HATA SEBEBİ:");
                        e.printStackTrace(); // 2. Neden çöktüğünü konsola kırmızı kırmızı yazacak
                    }
                });

                cityContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            cityContainer.getChildren().add(new Label("Veri çekilemedi: " + e.getMessage()));
        }
    }
    @FXML
    private void handleLogout(javafx.event.ActionEvent event){
        try {
            SessionManager.clearSession();

            FXMLLoader loader= new FXMLLoader( getClass().getResource("/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root,800,600));
            stage.centerOnScreen();
        } catch (Exception e){
            e.printStackTrace();
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
    private void openPlansPage(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/plans.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root, 800, 600));
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