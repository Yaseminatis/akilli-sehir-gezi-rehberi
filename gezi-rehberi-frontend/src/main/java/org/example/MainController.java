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
    @FXML private javafx.scene.control.Button adminBtn;

    @FXML
    public void initialize() {
        if (SessionManager.getCurrentUsername() != null) {
            welcomeLabel.setText("Merhaba, " + SessionManager.getCurrentUsername());
        }

        // Sadece Adminler butonları görsün
        boolean isAdmin = "ADMIN".equals(SessionManager.getCurrentUserRole());
        if (addCityBtn != null) {
            addCityBtn.setVisible(isAdmin);
            addCityBtn.setManaged(isAdmin);
        }
        if (adminBtn != null) {
            adminBtn.setVisible(isAdmin);
            adminBtn.setManaged(isAdmin);
        }

        try {
            // Backend'den şehirleri al ve GÜVENLİ bir şekilde dönüştür
            String jsonResponse = GeziBacakendClient.getAllCities();
            com.google.gson.JsonElement element = com.google.gson.JsonParser.parseString(jsonResponse);

            com.google.gson.Gson gson = new com.google.gson.Gson();
            City[] cities;
            if (element.isJsonObject() && element.getAsJsonObject().has("data")) {
                cities = gson.fromJson(element.getAsJsonObject().get("data"), City[].class);
            } else {
                cities = gson.fromJson(element, City[].class);
            }

            cityContainer.getChildren().clear();

            for (City city : cities) {
                // 1. KARTIN KENDİSİ
                javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(10);
                card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5); " +
                        "-fx-cursor: hand; -fx-padding: 0;");
                card.setPrefSize(250, 220);

                // 2. ŞEHİR GÖRSELİ (Hata alsa bile sistemi çökertmez!)
                javafx.scene.image.ImageView cityImageView = new javafx.scene.image.ImageView();
                cityImageView.setFitWidth(250);
                cityImageView.setFitHeight(140);
                cityImageView.setPreserveRatio(false);

                try {
                    String url = city.getImageUrl();
                    if (url != null && !url.isEmpty()) {
                        cityImageView.setImage(new javafx.scene.image.Image(url, true));
                    } else {
                        // URL yoksa şehrin adını yazan sahte bir resim koy
                        cityImageView.setImage(new javafx.scene.image.Image("https://via.placeholder.com/250x140?text=" + city.getName().replace(" ", "+")));
                    }
                } catch (Exception e) {
                    cityImageView.setImage(new javafx.scene.image.Image("https://via.placeholder.com/250x140?text=Gorsel+Bulunamadi"));
                }

                // Resmin üst köşelerini kartla uyumlu (oval) yap
                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(250, 140);
                clip.setArcWidth(30); clip.setArcHeight(30);
                cityImageView.setClip(clip);

                // 3. METİN ALANI (İsim ve Açıklama)
                javafx.scene.layout.VBox textInfo = new javafx.scene.layout.VBox(5);
                textInfo.setPadding(new javafx.geometry.Insets(0, 15, 10, 15));

                javafx.scene.control.Label name = new javafx.scene.control.Label(city.getName());
                name.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #1f2937;");

                javafx.scene.control.Label desc = new javafx.scene.control.Label(city.getDescription());
                desc.setWrapText(true);
                desc.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");
                desc.setMaxHeight(40);

                textInfo.getChildren().addAll(name, desc);
                card.getChildren().addAll(cityImageView, textInfo);

                // 4. TIKLAMA ÖZELLİĞİ (Görsel yüklense de yüklenmese de çalışacak)
                card.setOnMouseClicked(event -> {
                    try {
                        javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/detail.fxml"));
                        javafx.scene.Parent root = loader.load();
                        DetailController controller = loader.getController();
                        controller.initData(city);

                        javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                        stage.setScene(new javafx.scene.Scene(root, 800, 600));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                cityContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
            cityContainer.getChildren().add(new javafx.scene.control.Label("Veri çekilemedi: " + e.getMessage()));
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
    @FXML
    private void openAdminPage(javafx.event.ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/admin.fxml"));
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new javafx.scene.Scene(root, 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}