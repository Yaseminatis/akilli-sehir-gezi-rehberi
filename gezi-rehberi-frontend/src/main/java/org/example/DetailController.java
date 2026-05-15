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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DetailController {

    @FXML private Label cityNameLabel;
    @FXML private Label cityDescLabel;
    @FXML private FlowPane placesContainer;

    @FXML private VBox planModal;
    @FXML private Label selectedPlaceLabel;
    @FXML private ComboBox<String> planComboBox;

    @FXML private VBox placeDetailModal;
    @FXML private ImageView modalPlaceImage;
    @FXML private Label modalPlaceName;
    @FXML private Label modalPlaceCategory;
    @FXML private Label modalPlaceLongDesc;
    @FXML private Button saveToPlanBtn;

    private City currentCity;
    private Long selectedPlaceId;
    private Map<String, String> planMap = new HashMap<>(); // Plan İsmi -> Plan ID eşleştirmesi

    @FXML
    public void initialize() {
        // ComboBox değiştiğinde kontrol yap
        planComboBox.setOnAction(e -> checkExistingPlaceInPlan());
    }
    private void checkExistingPlaceInPlan() {
        String selectedPlan = planComboBox.getValue();
        if (selectedPlan == null || selectedPlan.equals("+ Yeni Liste Oluştur...")) {
            saveToPlanBtn.setDisable(false);
            saveToPlanBtn.setText("Planıma Kaydet");
            return;
        }

        try {
            String planId = planMap.get(selectedPlan);
            String planResponse = GeziBacakendClient.getPlanById(planId);
            JsonObject planJson = JsonParser.parseString(planResponse).getAsJsonObject().getAsJsonObject("data");
            JsonArray placesArray = planJson.getAsJsonArray("placeIds");

            boolean exists = false;
            for (JsonElement p : placesArray) {
                if (p.getAsLong() == selectedPlaceId) { exists = true; break; }
            }

            if (exists) {
                saveToPlanBtn.setDisable(true);
                saveToPlanBtn.setText("Zaten Planınızda ✔");
                saveToPlanBtn.setStyle("-fx-background-color: #9ca3af; -fx-text-fill: white;");
            } else {
                saveToPlanBtn.setDisable(false);
                saveToPlanBtn.setText("Planıma Kaydet");
                saveToPlanBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white;");
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // Mekan Detaylarını Açan Metot
    private void showPlaceFullDetail(JsonObject placeObj) {
        modalPlaceName.setText(placeObj.get("name").getAsString());
        modalPlaceCategory.setText(placeObj.get("category").getAsString());
        modalPlaceLongDesc.setText(placeObj.get("description").getAsString());

        String imgUrl = placeObj.has("imageUrl") && !placeObj.get("imageUrl").isJsonNull() ? placeObj.get("imageUrl").getAsString() : null;
        if (imgUrl != null) {
            modalPlaceImage.setImage(new javafx.scene.image.Image(imgUrl));
        }

        placeDetailModal.setVisible(true);
    }

    @FXML private void closePlaceDetailModal() { placeDetailModal.setVisible(false); }
    public void initData(City city) {
        this.currentCity = city;
        cityNameLabel.setText(city.getName());
        cityDescLabel.setText(city.getDescription());
        loadPlaces();
    }

    private void loadPlaces() {
        try {
            String response = GeziBacakendClient.getPlacesByCityId(currentCity.getId());
            JsonObject rootJson = JsonParser.parseString(response).getAsJsonObject();
            JsonArray placesArray = rootJson.getAsJsonArray("data");

            placesContainer.getChildren().clear();

            for (JsonElement element : placesArray) {
                JsonObject placeObj = element.getAsJsonObject();
                Long placeId = placeObj.get("id").getAsLong();
                String name = placeObj.get("name").getAsString();
                String desc = placeObj.has("description") ? placeObj.get("description").getAsString() : "Açıklama bulunmuyor.";
                String imageUrl = placeObj.has("imageUrl") && !placeObj.get("imageUrl").isJsonNull() ? placeObj.get("imageUrl").getAsString() : null;

                // --- ŞIK KART TASARIMI ---
                VBox card = new VBox(10);
                card.setStyle("-fx-background-color: white; -fx-padding: 0; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5); -fx-pref-width: 250; -fx-overflow: hidden;");

                // 1. MEKAN GÖRSELİ
                javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView();
                imageView.setFitWidth(250);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(false);

                if (imageUrl != null && !imageUrl.isEmpty()) {
                    imageView.setImage(new javafx.scene.image.Image(imageUrl, true));
                } else {
                    // Resim yoksa varsayılan bir görsel koyalım
                    imageView.setImage(new javafx.scene.image.Image("https://via.placeholder.com/250x150?text=Gorsel+Yok"));
                }

                // 2. İÇERİK ALANI (Padding ekliyoruz)
                VBox content = new VBox(8);
                content.setStyle("-fx-padding: 15;");

                Label nameLbl = new Label(name);
                nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #1f2937;");

                Label descLbl = new Label(desc);
                descLbl.setWrapText(true);
                descLbl.setMaxHeight(60);
                descLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");

                // 3. PUANLAMA (Yıldız Efekti)
                Label ratingLbl = new Label("⭐ 4.5 / 5"); // Şimdilik statik, Yasemin'den çekebiliriz
                ratingLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #f59e0b;");

                // 4. AKSİYON BUTONU
                javafx.scene.control.Button addPlanBtn = new javafx.scene.control.Button("+ Planıma Ekle");
                addPlanBtn.setMaxWidth(Double.MAX_VALUE);
                addPlanBtn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand; -fx-background-radius: 8; -fx-padding: 8;");
                addPlanBtn.setOnAction(e -> openPlanModal(placeId, name));

                content.getChildren().addAll(nameLbl, ratingLbl, descLbl, addPlanBtn);
                card.getChildren().addAll(imageView, content);

                placesContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPlanModal(Long placeId, String placeName) {
        this.selectedPlaceId = placeId;
        selectedPlaceLabel.setText(placeName + " mekanını ekliyorsunuz.");
        selectedPlaceLabel.setStyle("-fx-text-fill: #6b7280;"); // Rengi sıfırla

        planComboBox.getItems().clear();
        planMap.clear();

        try {
            Long userId = SessionManager.getCurrentUserId();
            String response = GeziBacakendClient.getTravelPlansByUser(userId);
            JsonObject rootJson = JsonParser.parseString(response).getAsJsonObject();
            JsonArray plansArray = rootJson.getAsJsonArray("data");

            if (plansArray != null) {
                for (JsonElement element : plansArray) {
                    JsonObject planObj = element.getAsJsonObject();
                    String title = planObj.get("title").getAsString();
                    String id = planObj.get("id").getAsString();

                    planMap.put(title, id);
                    planComboBox.getItems().add(title);
                }
            }
            planComboBox.getItems().add("+ Yeni Liste Oluştur...");
        } catch (Exception e) {
            System.out.println("Planlar çekilemedi: " + e.getMessage());
        }

        planModal.setVisible(true);
    }

    @FXML
    private void closeModal() {
        planModal.setVisible(false);
    }

    @FXML
    private void saveToPlan() {
        String selectedPlan = planComboBox.getValue();

        if (selectedPlan == null) {
            selectedPlaceLabel.setText("Lütfen bir plan seçin!");
            selectedPlaceLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        try {
            Long userId = SessionManager.getCurrentUserId();

            if (selectedPlan.equals("+ Yeni Liste Oluştur...")) {
                TextInputDialog dialog = new TextInputDialog("Yeni Gezi");
                dialog.setTitle("Yeni Plan Oluştur");
                dialog.setHeaderText("Yeni gezi planınızın adını girin:");
                dialog.setContentText("Plan Adı:");

                Optional<String> result = dialog.showAndWait();
                if (result.isPresent() && !result.get().trim().isEmpty()) {
                    String title = result.get().trim();
                    List<Long> initialPlaces = new ArrayList<>();
                    initialPlaces.add(selectedPlaceId);

                    GeziBacakendClient.createTravelPlan(userId, title, initialPlaces);
                    closeModal();
                }
            } else {
                String planId = planMap.get(selectedPlan);
                String planResponse = GeziBacakendClient.getPlanById(planId);
                JsonObject planJson = JsonParser.parseString(planResponse).getAsJsonObject().getAsJsonObject("data");

                JsonArray placesArray = planJson.getAsJsonArray("placeIds");
                boolean alreadyExists = false;
                for (JsonElement p : placesArray) {
                    if (p.getAsLong() == selectedPlaceId) { alreadyExists = true; break; }
                }

                if (!alreadyExists) {
                    placesArray.add(selectedPlaceId);
                    GeziBacakendClient.updatePlan(planId, planJson.toString());
                    closeModal();
                } else {
                    selectedPlaceLabel.setText("Bu mekan zaten planda var!");
                    selectedPlaceLabel.setStyle("-fx-text-fill: orange;");
                }
            }
        } catch (Exception e) {
            System.out.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
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