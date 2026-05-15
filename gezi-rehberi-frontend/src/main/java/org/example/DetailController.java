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
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailController {

    @FXML private Label cityNameLabel;
    @FXML private Label cityDescLabel;
    @FXML private FlowPane placesContainer;

    @FXML private VBox planModal;
    @FXML private Label selectedPlaceLabel;
    @FXML private ComboBox<String> planComboBox;

    @FXML private VBox placeDetailModal;
    @FXML private Label modalPlaceName;
    @FXML private Label modalPlaceCategory;
    @FXML private Label modalPlaceLongDesc;

    @FXML private Button saveToPlanBtn;
    @FXML private Button submitRatingBtn;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryFilterCombo;
    @FXML private ComboBox<Integer> ratingCombo;
    @FXML private VBox newPlanModal;
    @FXML private TextField newPlanNameField;

    private Long selectedPlaceIdForModal;
    private City currentCity;
    private Long selectedPlaceId;
    private Map<String, String> planMap = new HashMap<>();

    @FXML
    public void initialize() {
        planComboBox.setOnAction(e -> checkExistingPlaceInPlan());
    }

    public void initData(City city) {
        this.currentCity = city;
        cityNameLabel.setText(city.getName());
        cityDescLabel.setText(city.getDescription());
        loadPlaces();
    }


    private void loadPlaces() {
        try {
            String response = GeziBackendClient.getPlacesByCity(currentCity.getId());
            processAndLoadPlaces(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSearch() {
        String keyword = searchField.getText() != null ? searchField.getText().trim() : "";
        String category = categoryFilterCombo.getValue();

        try {
            String response;
            if (!keyword.isEmpty()) {
                response = GeziBackendClient.searchPlaces(keyword);
            } else if (category != null && !category.equals("TÜMÜ") && !category.isEmpty()) {
                response = GeziBackendClient.getPlacesByCategory(category);
            } else {
                response = GeziBackendClient.getPlacesByCity(currentCity.getId());
            }

            processAndLoadPlaces(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
        // ANASAYFA İÇİN FİLTRELİ ARAMA BİRLEŞİK VERİ ÇEKİMİ
    private void processAndLoadPlaces(String jsonResponse) {
        placesContainer.getChildren().clear();

        List<Long> favoriMekanIdleri = new ArrayList<>();
        try {
            Long userId = SessionManager.getCurrentUserId();
            String favResponse = GeziBackendClient.getFavoritesByUser(userId);
            JsonElement parsed = JsonParser.parseString(favResponse);
            JsonArray favArray = parsed.isJsonArray() ? parsed.getAsJsonArray() :
                    (parsed.isJsonObject() && parsed.getAsJsonObject().has("data") ? parsed.getAsJsonObject().getAsJsonArray("data") : new JsonArray());

            for (JsonElement el : favArray) {
                if (el.getAsJsonObject().has("placeId")) {
                    favoriMekanIdleri.add(el.getAsJsonObject().get("placeId").getAsLong());
                }
            }
        } catch (Exception e) {
            System.out.println("Favoriler çekilirken hata: " + e.getMessage());
        }

        try {
            JsonElement rootElement = JsonParser.parseString(jsonResponse);
            JsonArray placesArray;

            if (rootElement.isJsonObject() && rootElement.getAsJsonObject().has("data")) {
                placesArray = rootElement.getAsJsonObject().getAsJsonArray("data");
            } else if (rootElement.isJsonArray()) {
                placesArray = rootElement.getAsJsonArray();
            } else {
                placesArray = new JsonArray();
            }

            for (JsonElement element : placesArray) {
                JsonObject placeObj = element.getAsJsonObject();
                if (placeObj.has("cityId") && placeObj.get("cityId").getAsLong() != currentCity.getId()) {
                    continue;
                }
                Long placeId = placeObj.get("id").getAsLong();

                String name = placeObj.has("name") && !placeObj.get("name").isJsonNull() ? placeObj.get("name").getAsString() : "İsimsiz Mekan";
                String desc = placeObj.has("description") && !placeObj.get("description").isJsonNull() ? placeObj.get("description").getAsString() : "Açıklama bulunmuyor.";
                String category = placeObj.has("category") && !placeObj.get("category").isJsonNull() ? placeObj.get("category").getAsString() : "KATEGORİ YOK";

                VBox card = new VBox(10);
                card.getStyleClass().add("city-card");
                card.setStyle("-fx-padding: 18; -fx-background-color: white; -fx-background-radius: 12;");


                card.setPrefSize(280, 180);
                card.setMinSize(280, 180);
                card.setMaxSize(280, 180);
                card.setAlignment(javafx.geometry.Pos.TOP_LEFT);

                HBox topBox = new HBox(10);
                topBox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                Label categoryBadge = new Label(category);
                categoryBadge.setStyle("-fx-background-color: #f3e8ff; -fx-text-fill: #7e22ce; -fx-padding: 4 10; -fx-background-radius: 15; -fx-font-size: 11px; -fx-font-weight: bold;");

                Region spacerHeader = new Region();
                HBox.setHgrow(spacerHeader, Priority.ALWAYS); // Kalbi en sağa iter

                Button favBtn = new Button(favoriMekanIdleri.contains(placeId) ? "❤" : "♡");
                favBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #ef4444; -fx-font-size: 22px; -fx-cursor: hand; -fx-padding: 0;");
                favBtn.setOnAction(e -> toggleFavorite(placeId, favBtn));

                topBox.getChildren().addAll(categoryBadge, spacerHeader, favBtn);


                Label nameLbl = new Label(name);
                nameLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #111827;");
                nameLbl.setWrapText(true);
                nameLbl.setMaxHeight(25);

                Label descLbl = new Label(desc);
                descLbl.setWrapText(true);
                descLbl.setMaxHeight(40);
                descLbl.setStyle("-fx-text-fill: #6b7280; -fx-font-size: 12px;");


                Region spacerMid = new Region();
                VBox.setVgrow(spacerMid, Priority.ALWAYS);


                HBox footer = new HBox(10);
                footer.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
                String ratingText = "★ Yeni";
                try {
                    String ratingsRes = GeziBackendClient.getRatingsByPlace(placeId);
                    JsonElement parsedRatings = JsonParser.parseString(ratingsRes);
                    JsonArray rArray = parsedRatings.isJsonArray() ? parsedRatings.getAsJsonArray() :
                            (parsedRatings.isJsonObject() && parsedRatings.getAsJsonObject().has("data") ? parsedRatings.getAsJsonObject().getAsJsonArray("data") : new JsonArray());

                    if (rArray.size() > 0) {
                        double totalScore = 0;
                        for (JsonElement r : rArray) {
                            totalScore += r.getAsJsonObject().get("score").getAsDouble();
                        }
                        double average = totalScore / rArray.size();
                        ratingText = String.format("★ %.1f", average).replace(",", ".");
                    }
                } catch (Exception e) {
                    System.out.println("Puanlar çekilemedi: " + e.getMessage());
                }

                Label ratingLbl = new Label(ratingText);
                ratingLbl.setStyle("-fx-text-fill: #f59e0b; -fx-font-weight: bold; -fx-font-size: 15px;");

                Region spacerFooter = new Region();
                HBox.setHgrow(spacerFooter, Priority.ALWAYS);


                Button addBtn = new Button("+ Plana Ekle");
                addBtn.setStyle("-fx-background-color: #8b5cf6; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand; -fx-font-weight: bold;");
                addBtn.setOnAction(e -> openPlanModal(placeId, name));

                footer.getChildren().addAll(ratingLbl, spacerFooter, addBtn);

                card.getChildren().addAll(topBox, nameLbl, descLbl, spacerMid, footer);

                card.setOnMouseClicked(e -> {
                    if (e.getTarget() instanceof Button) return;
                    showPlaceFullDetail(placeObj);
                });

                placesContainer.getChildren().add(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showPlaceFullDetail(JsonObject placeObj) {
        this.selectedPlaceIdForModal = placeObj.get("id").getAsLong();

        try {
            Long currentUserId = SessionManager.getCurrentUserId();
            String ratingsRes = GeziBackendClient.getRatingsByPlace(selectedPlaceIdForModal);

            JsonElement parsed = JsonParser.parseString(ratingsRes);
            JsonArray ratingsArray = parsed.isJsonArray() ? parsed.getAsJsonArray() :
                    (parsed.isJsonObject() && parsed.getAsJsonObject().has("data") ? parsed.getAsJsonObject().getAsJsonArray("data") : new JsonArray());

            boolean alreadyRated = false;
            for (JsonElement re : ratingsArray) {
                if (re.getAsJsonObject().get("userId").getAsLong() == currentUserId) {
                    alreadyRated = true; break;
                }
            }

            if (alreadyRated) {
                if (ratingCombo != null) ratingCombo.setDisable(true);
                if (submitRatingBtn != null) {
                    submitRatingBtn.setDisable(true);
                    submitRatingBtn.setText("Puanınız Kayıtlı ✔");
                    submitRatingBtn.setStyle("-fx-background-color: #9ca3af; -fx-text-fill: white; -fx-font-weight: bold;");
                }
            } else {
                if (ratingCombo != null) {
                    ratingCombo.setDisable(false);
                    ratingCombo.getSelectionModel().clearSelection();
                }
                if (submitRatingBtn != null) {
                    submitRatingBtn.setDisable(false);
                    submitRatingBtn.setText("★ Puanı Gönder");
                    submitRatingBtn.setStyle("-fx-background-color: #f59e0b; -fx-text-fill: white; -fx-font-weight: bold;");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        modalPlaceName.setText(placeObj.get("name").getAsString());
        modalPlaceCategory.setText(placeObj.has("category") ? placeObj.get("category").getAsString() : "");
        modalPlaceLongDesc.setText(placeObj.has("description") ? placeObj.get("description").getAsString() : "");
        placeDetailModal.setVisible(true);
    }

    @FXML private void closePlaceDetailModal() { placeDetailModal.setVisible(false); }

    @FXML
    private void submitRating(ActionEvent event) {
        if (ratingCombo == null || ratingCombo.getValue() == null) return;
        Integer score = ratingCombo.getValue();

        try {
            Long userId = SessionManager.getCurrentUserId();
            GeziBackendClient.createRating(score, userId, selectedPlaceIdForModal);
            ratingCombo.setDisable(true);
            if (submitRatingBtn != null) {
                submitRatingBtn.setDisable(true);
                submitRatingBtn.setText("✅ Puan Verildi");
                submitRatingBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-font-weight: bold;");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void toggleFavorite(Long placeId, Button btn) {
        try {
            Long userId = SessionManager.getCurrentUserId();

            if (btn.getText().equals("♡")) {
                GeziBackendClient.addFavorite(userId, placeId);
                btn.setText("❤");
            } else {
                GeziBackendClient.removeFavorite(userId, placeId);
                btn.setText("♡");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Favori işlemi sırasında hata: " + e.getMessage());
        }
    }

    private void checkExistingPlaceInPlan() {
        String selectedPlan = planComboBox.getValue();
        if (selectedPlan == null || selectedPlan.equals("+ Yeni Liste Oluştur...")) {
            if (saveToPlanBtn != null) {
                saveToPlanBtn.setDisable(false);
                saveToPlanBtn.setText("Planıma Kaydet");
            }
            return;
        }

        try {
            String planId = planMap.get(selectedPlan);
            String planResponse = GeziBackendClient.getPlanById(planId);
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

    private void openPlanModal(Long placeId, String placeName) {
        this.selectedPlaceId = placeId;
        selectedPlaceLabel.setText(placeName + " mekanını ekliyorsunuz.");
        selectedPlaceLabel.setStyle("-fx-text-fill: #6b7280;");

        planComboBox.getItems().clear();
        planMap.clear();

        try {
            Long userId = SessionManager.getCurrentUserId();
            String response = GeziBackendClient.getTravelPlansByUser(userId);
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
        } catch (Exception e) { System.out.println("Planlar çekilemedi."); }

        planModal.setVisible(true);
    }

    @FXML private void closeModal() { planModal.setVisible(false); }

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
                planModal.setVisible(false);
                newPlanNameField.clear();
                newPlanModal.setVisible(true);
            } else {
                String planId = planMap.get(selectedPlan);
                String planResponse = GeziBackendClient.getPlanById(planId);
                JsonObject planJson = JsonParser.parseString(planResponse).getAsJsonObject().getAsJsonObject("data");

                JsonArray placesArray = planJson.getAsJsonArray("placeIds");
                placesArray.add(selectedPlaceId);
                GeziBackendClient.updatePlan(planId, planJson.toString());
                closeModal();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
    @FXML
    private void closeNewPlanModal() {
        newPlanModal.setVisible(false);
        planModal.setVisible(true);
    }

    @FXML
    private void createNewPlanAndSave() {
        String title = newPlanNameField.getText().trim();
        if (title.isEmpty()) return;

        try {
            Long userId = SessionManager.getCurrentUserId();
            List<Long> initialPlaces = new ArrayList<>();
            initialPlaces.add(selectedPlaceId);

            GeziBackendClient.createTravelPlan(userId, title, initialPlaces);

            newPlanModal.setVisible(false);
            System.out.println("Yeni plan oluşturuldu ve mekan eklendi: " + title);
        } catch (Exception e) {
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
        } catch (Exception e) { e.printStackTrace(); }
    }
}