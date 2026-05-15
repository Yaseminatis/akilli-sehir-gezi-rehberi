package org.example;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class MainController {
    @FXML private FlowPane cityContainer;
    @FXML private Label welcomeLabel;
    @FXML private javafx.scene.control.Button adminBtn;

    @FXML
    public void initialize() {
        if (SessionManager.getCurrentUsername() != null) {
            welcomeLabel.setText("Merhaba, " + SessionManager.getCurrentUsername());
        }

        boolean isAdmin = "ADMIN".equals(SessionManager.getCurrentUserRole());
        if (adminBtn != null) {
            adminBtn.setVisible(isAdmin);
            adminBtn.setManaged(isAdmin);
        }

        try {
            String jsonResponse = GeziBackendClient.getAllCities();
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

                javafx.scene.layout.VBox card = new javafx.scene.layout.VBox(10);
                card.getStyleClass().add("city-card");
                card.setPrefSize(250, 220);

                javafx.scene.image.ImageView cityImageView = new javafx.scene.image.ImageView();
                cityImageView.setFitWidth(250);
                cityImageView.setFitHeight(140);
                cityImageView.setPreserveRatio(false);

                try {
                    String url = city.getImageUrl();
                    if (url != null && !url.isEmpty()) {
                        cityImageView.setImage(new javafx.scene.image.Image(url, true));
                    } else {
                        cityImageView.setImage(new javafx.scene.image.Image("https://via.placeholder.com/250x140?text=" + city.getName().replace(" ", "+")));
                    }
                } catch (Exception e) {
                    cityImageView.setImage(new javafx.scene.image.Image("https://via.placeholder.com/250x140?text=Gorsel+Bulunamadi"));
                }

                javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(250, 140);
                clip.setArcWidth(30); clip.setArcHeight(30);
                cityImageView.setClip(clip);

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