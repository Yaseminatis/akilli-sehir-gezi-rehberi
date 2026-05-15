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
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AdminController {

    @FXML private ListView<String> cityListView, placeListView;
    @FXML private TextField cityNameField, cityCountryField, cityImageField, placeNameField;
    @FXML private TextArea cityDescArea, placeDescArea;
    @FXML private ComboBox<String> placeCategoryBox, placeCityBox;
    @FXML private Label cityMessageLabel, placeMessageLabel;
    @FXML private Button btnSaveCity, btnUpdateCity, btnDeleteCity, btnSavePlace, btnUpdatePlace, btnDeletePlace;

    // Şehir ve Mekan verilerini tam obje olarak saklıyoruz (ID'lere erişmek için)
    private Map<String, JsonObject> cityDataMap = new HashMap<>();
    private Map<String, JsonObject> placeDataMap = new HashMap<>();

    private Long selectedCityId = null;
    private Long selectedPlaceId = null;

    @FXML
    public void initialize() {
        // Sayfa açılırken tüm verileri tek seferde çekiyoruz
        loadAllData();

        // Şehir listesi tıklama dinleyicisi
        cityListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillCityForm(newVal);
        });

        // Mekan listesi tıklama dinleyicisi
        placeListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) fillPlaceForm(newVal);
        });
    }

    private void loadAllData() {
        try {
            // ŞEHİRLERİ ÇEK (Hem liste hem ComboBox için)
            String cityRes = GeziBacakendClient.getAllCities();
            JsonArray cities = JsonParser.parseString(cityRes).isJsonArray()
                    ? JsonParser.parseString(cityRes).getAsJsonArray()
                    : JsonParser.parseString(cityRes).getAsJsonObject().getAsJsonArray("data");

            cityListView.getItems().clear();
            placeCityBox.getItems().clear();
            cityDataMap.clear();

            for (JsonElement e : cities) {
                JsonObject obj = e.getAsJsonObject();
                String name = obj.get("name").getAsString();
                cityDataMap.put(name, obj);
                cityListView.getItems().add(name);
                placeCityBox.getItems().add(name); // Mekan ekleme kısmındaki şehirlere ekle
            }

            // MEKANLARI ÇEK
            String placeRes = GeziBacakendClient.getAllPlaces();
            JsonArray places = JsonParser.parseString(placeRes).isJsonArray()
                    ? JsonParser.parseString(placeRes).getAsJsonArray()
                    : JsonParser.parseString(placeRes).getAsJsonObject().getAsJsonArray("data");

            placeListView.getItems().clear();
            placeDataMap.clear();

            for (JsonElement e : places) {
                JsonObject obj = e.getAsJsonObject();
                String name = obj.get("name").getAsString();
                placeDataMap.put(name, obj);
                placeListView.getItems().add(name);
            }

        } catch (Exception e) {
            System.out.println("Veri yükleme hatası: " + e.getMessage());
        }
    }

    private void fillCityForm(String cityName) {
        JsonObject obj = cityDataMap.get(cityName);
        selectedCityId = obj.get("id").getAsLong();
        cityNameField.setText(obj.get("name").getAsString());
        cityCountryField.setText(obj.has("country") && !obj.get("country").isJsonNull() ? obj.get("country").getAsString() : "");
        cityImageField.setText(obj.has("imageUrl") && !obj.get("imageUrl").isJsonNull() ? obj.get("imageUrl").getAsString() : "");
        cityDescArea.setText(obj.has("description") && !obj.get("description").isJsonNull() ? obj.get("description").getAsString() : "");

        btnSaveCity.setDisable(true);
        btnUpdateCity.setDisable(false);
        btnDeleteCity.setDisable(false);
    }

    private void fillPlaceForm(String placeName) {
        JsonObject obj = placeDataMap.get(placeName);
        selectedPlaceId = obj.get("id").getAsLong();
        placeNameField.setText(obj.get("name").getAsString());
        placeCategoryBox.setValue(obj.has("category") ? obj.get("category").getAsString() : null);
        placeDescArea.setText(obj.has("description") && !obj.get("description").isJsonNull() ? obj.get("description").getAsString() : "");

        // Mekanın içindeki cityId'yi kullanarak ComboBox'ta doğru şehri seçiyoruz
        Long cId = obj.get("cityId").getAsLong();
        for (Map.Entry<String, JsonObject> entry : cityDataMap.entrySet()) {
            if (entry.getValue().get("id").getAsLong() == cId) {
                placeCityBox.setValue(entry.getKey());
                break;
            }
        }

        btnSavePlace.setDisable(true);
        btnUpdatePlace.setDisable(false);
        btnDeletePlace.setDisable(false);
    }

    @FXML private void clearCityForm() {
        selectedCityId = null;
        cityNameField.clear(); cityCountryField.clear(); cityImageField.clear(); cityDescArea.clear();
        cityListView.getSelectionModel().clearSelection();
        btnSaveCity.setDisable(false); btnUpdateCity.setDisable(true); btnDeleteCity.setDisable(true);
    }

    @FXML private void clearPlaceForm() {
        selectedPlaceId = null;
        placeNameField.clear(); placeCategoryBox.getSelectionModel().clearSelection();
        placeCityBox.getSelectionModel().clearSelection(); placeDescArea.clear();
        placeListView.getSelectionModel().clearSelection();
        btnSavePlace.setDisable(false); btnUpdatePlace.setDisable(true); btnDeletePlace.setDisable(true);
    }

    @FXML private void saveCity() {
        try {
            GeziBacakendClient.createCity(cityNameField.getText(), cityCountryField.getText(), cityDescArea.getText(), cityImageField.getText());
            cityMessageLabel.setText("✅ Başarıyla Kaydedildi!");
            loadAllData();
            clearCityForm();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void updateCity() {
        if (selectedCityId == null) return;
        try {
            GeziBacakendClient.updateCity(selectedCityId, cityNameField.getText(), cityCountryField.getText(), cityDescArea.getText(), cityImageField.getText());
            cityMessageLabel.setText("✅ Şehir Güncellendi!");
            loadAllData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void deleteCity() {
        if (selectedCityId == null) return;
        try {
            int status = GeziBacakendClient.deleteCity(selectedCityId);
            if (status == 200 || status == 204) {
                cityMessageLabel.setText("🗑️ Şehir Silindi!");
                loadAllData();
                clearCityForm();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void savePlace() {
        try {
            String selectedCity = placeCityBox.getValue();
            if (selectedCity == null) return;
            Long cId = cityDataMap.get(selectedCity).get("id").getAsLong();

            GeziBacakendClient.createPlace(placeNameField.getText(), placeDescArea.getText(), placeCategoryBox.getValue(), cId);
            placeMessageLabel.setText("✅ Mekan Kaydedildi!");
            loadAllData();
            clearPlaceForm();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void updatePlace() {
        if (selectedPlaceId == null) return;
        try {
            String selectedCity = placeCityBox.getValue();
            Long cId = cityDataMap.get(selectedCity).get("id").getAsLong();

            GeziBacakendClient.updatePlace(selectedPlaceId, placeNameField.getText(), placeDescArea.getText(), placeCategoryBox.getValue(), cId);
            placeMessageLabel.setText("✅ Mekan Güncellendi!");
            loadAllData();
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void deletePlace() {
        if (selectedPlaceId == null) return;
        try {
            int status = GeziBacakendClient.deletePlace(selectedPlaceId);
            if (status == 200 || status == 204) {
                placeMessageLabel.setText("🗑️ Mekan Silindi!");
                loadAllData();
                clearPlaceForm();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    @FXML private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
        } catch (Exception e) { e.printStackTrace(); }
    }
}