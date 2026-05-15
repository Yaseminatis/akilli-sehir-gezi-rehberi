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

public class PlansController {

    @FXML private VBox plansContainer;
    @FXML private VBox planDetailModal;
    @FXML private Label modalPlanTitle;
    @FXML private VBox modalPlacesContainer;

    @FXML
    public void initialize() {
        loadUserPlans();
    }

    private void loadUserPlans() {
        plansContainer.getChildren().clear();

        try {
            Long userId = SessionManager.getCurrentUserId();
            String response = GeziBacakendClient.getTravelPlansByUser(userId);

            JsonObject rootJson = JsonParser.parseString(response).getAsJsonObject();
            JsonArray dataArray = rootJson.getAsJsonArray("data");

            if (dataArray == null || dataArray.isEmpty()) {
                plansContainer.getChildren().add(new Label("Henüz bir gezi planınız yok. Detay sayfasından mekan ekleyerek başlayabilirsiniz!"));
                return;
            }

            for (JsonElement element : dataArray) {
                JsonObject planObj = element.getAsJsonObject();
                String id = planObj.get("id").getAsString();
                String title = planObj.get("title").getAsString();
                JsonArray placesArray = planObj.getAsJsonArray("placeIds");
                int placeCount = placesArray.size();

                HBox card = new HBox(15);
                card.setStyle("-fx-background-color: white; -fx-padding: 20; -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);");
                card.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                VBox textInfo = new VBox(5);
                Label titleLabel = new Label("⚲ " + title);
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #8b5cf6;");
                Label countLabel = new Label(placeCount + " mekan eklendi");
                countLabel.setStyle("-fx-text-fill: #6b7280;");
                textInfo.getChildren().addAll(titleLabel, countLabel);

                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                Button viewBtn = new Button("Görüntüle / Düzenle");
                viewBtn.setStyle("-fx-background-color: #10b981; -fx-text-fill: white; -fx-cursor: hand;");
                viewBtn.setOnAction(e -> openPlanDetails(planObj));

                Button deleteBtn = new Button("Planı Sil");
                deleteBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand;");
                deleteBtn.setOnAction(e -> deletePlanCompletely(id));

                card.getChildren().addAll(textInfo, spacer, viewBtn, deleteBtn);
                plansContainer.getChildren().add(card);
            }

        } catch (Exception e) {
            plansContainer.getChildren().add(new Label("Planlar yüklenemedi: " + e.getMessage()));
        }
    }



    private void openPlanDetails(JsonObject planObj) {
        String planId = planObj.get("id").getAsString();
        String title = planObj.get("title").getAsString();
        JsonArray placesArray = planObj.getAsJsonArray("placeIds");

        modalPlanTitle.setText("⚲ " + title);
        modalPlacesContainer.getChildren().clear();

        if (placesArray.size() == 0) {
            modalPlacesContainer.getChildren().add(new Label("Bu planda henüz mekan yok."));
        } else {
            for (int i = 0; i < placesArray.size(); i++) {
                Long placeId = placesArray.get(i).getAsLong();

                try {
                    String placeResponse = GeziBacakendClient.getPlaceById(placeId);
                    JsonObject placeData = JsonParser.parseString(placeResponse).getAsJsonObject().getAsJsonObject("data");
                    String placeName = placeData.get("name").getAsString();

                    HBox placeRow = new HBox(10);
                    placeRow.setStyle("-fx-padding: 10; -fx-background-color: #f3f4f6; -fx-background-radius: 5;");
                    placeRow.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

                    javafx.scene.control.Hyperlink nameLink = new javafx.scene.control.Hyperlink("" + placeName);
                    nameLink.setStyle("-fx-font-weight: bold; -fx-text-fill: #1f2937; -fx-underline: false;");


                    nameLink.setOnAction(e -> {
                        modalPlanTitle.setText("⚲ " + placeName);
                        modalPlacesContainer.getChildren().clear();
                        String category = placeData.has("category") ? placeData.get("category").getAsString() : "Kategori Yok";
                        String desc = placeData.has("description") && !placeData.get("description").isJsonNull()
                                ? placeData.get("description").getAsString()
                                : "Bu mekan için detaylı açıklama bulunmuyor.";

                        Label catLabel = new Label("Kategori: " + category);
                        catLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #8b5cf6; -fx-font-size: 14px;");

                        Label descLabel = new Label(desc);
                        descLabel.setWrapText(true);
                        descLabel.setStyle("-fx-text-fill: #4b5563; -fx-font-size: 14px;");

                        modalPlacesContainer.getChildren().addAll(catLabel, descLabel);
                    });

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    Button removeBtn = new Button("✕ Çıkar");
                    removeBtn.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-cursor: hand; -fx-font-size: 11px;");

                    removeBtn.setOnAction(e -> removePlaceFromPlan(planObj, placeId));
                    placeRow.getChildren().addAll(nameLink, spacer, removeBtn);
                    modalPlacesContainer.getChildren().add(placeRow);

                } catch (Exception e) {
                    System.out.println("Mekan getirilemedi ID: " + placeId);
                }
            }
        }
        planDetailModal.setVisible(true);
    }

    private void removePlaceFromPlan(JsonObject planObj, Long placeIdToRemove) {
        try {
            String planId = planObj.get("id").getAsString();
            JsonArray placesArray = planObj.getAsJsonArray("placeIds");

            JsonArray newPlacesArray = new JsonArray();
            for (JsonElement p : placesArray) {
                if (p.getAsLong() != placeIdToRemove) {
                    newPlacesArray.add(p);
                }
            }
            planObj.add("placeIds", newPlacesArray);

            GeziBacakendClient.updatePlan(planId, planObj.toString());
            openPlanDetails(planObj);
            loadUserPlans();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePlanCompletely(String planId) {
        try {
            GeziBacakendClient.deletePlan(planId);
            loadUserPlans();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void closeModal() {
        planDetailModal.setVisible(false);
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