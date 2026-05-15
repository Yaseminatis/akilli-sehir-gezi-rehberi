
/**
 * Akıllı Şehir Gezi Rehberi - Backend API Client
 * Frontend (Java) → Backend REST API bağlantısı için
 */
package org.example;


import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeziBacakendClient {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newHttpClient();

    /**
     * GET - Tüm şehirleri getir
     */
    public static String getAllCities() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cities"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        return response.body();
    }

    /**
     * GET - ID'ye göre şehir getir
     */
    public static String getCityById(Long cityId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cities/" + cityId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * POST - Yeni şehir oluştur
     */
    public static String createCity(String name, String country, String description, String imageUrl)
            throws Exception {
        String jsonBody = String.format("""
            {
                "name": "%s",
                "country": "%s",
                "description": "%s",
                "imageUrl": "%s"
            }
            """, name, country, description, imageUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cities"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        return response.body();
    }

    /**
     * PUT - Şehri güncelle
     */
    public static String updateCity(Long cityId, String name, String country,
                                    String description, String imageUrl) throws Exception {
        String jsonBody = String.format("""
            {
                "name": "%s",
                "country": "%s",
                "description": "%s",
                "imageUrl": "%s"
            }
            """, name, country, description, imageUrl);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cities/" + cityId))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * DELETE - Şehri sil
     */
    public static int deleteCity(Long cityId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/cities/" + cityId))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.statusCode();
    }

    /**
     * GET - Tüm mekanları getir (ApiResponse wrapper ile)
     */
    public static String getAllPlaces() throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places")).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String getPlacesByCity(Long cityId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places/city/" + cityId)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String getPlacesByCategory(String category) throws Exception {
        String url = BASE_URL + "/places/category?category=" + category;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String getPlaceById(Long id) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places/" + id)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String searchPlaces(String keyword) throws Exception {
        String encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8);
        String url = BASE_URL + "/places/search?keyword=" + encodedKeyword;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String createPlace(String name, String description, String category, Long cityId) throws Exception {
        String jsonBody = String.format("""
        {
            "name": "%s",
            "description": "%s",
            "category": "%s",
            "cityId": %d
        }
        """, name, description, category, cityId);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json").build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    // (Not: Güncelleme ve Silme işlemleri için bizim yazdığımız PUT ve DELETE metotları burada kalmalı)
    public static String updatePlace(Long placeId, String name, String description, String category, Long cityId) throws Exception {
        String jsonBody = String.format("""
        {
            "name": "%s",
            "description": "%s",
            "category": "%s",
            "cityId": %d
        }
        """, name, description, category, cityId);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places/" + placeId))
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json").build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static int deletePlace(Long placeId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/places/" + placeId)).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).statusCode();
    }

    /**
     * POST - Puan ekle (1-5 arası)
     */
    public static String createRating(Integer score, Long userId, Long placeId) throws Exception {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Puan 1-5 arası olmalı");
        }
        String jsonBody = String.format("""
        {
            "score": %d,
            "userId": %d,
            "placeId": %d
        }
        """, score, userId, placeId);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/ratings"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json").build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String getRatingsByPlace(Long placeId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + "/ratings/place/" + placeId)).GET().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }
    /**
     * POST - Kullanıcı oluştur
     */
    public static String createUser(String username, String email, String password, String role)
            throws Exception {
        String jsonBody = String.format("""
            {
                "username": "%s",
                "email": "%s",
                "password": "%s",
                "role": "%s"
            }
            """, username, email, password, role);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * GET - Kullanıcı detayı (ID)
     */
    public static String getUserById(Long userId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/users/" + userId))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * GET - Kullanıcı detayı (Email)
     */
    public static String getUserByEmail(String email) throws Exception {
        String encodedEmail = URLEncoder.encode(email, StandardCharsets.UTF_8);
        String url = BASE_URL + "/users/email?email=" + encodedEmail;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    /**
     * POST - Favori mekan ekle (MongoDB)
     */
    public static String addFavorite(Long userId, Long placeId) throws Exception {
        String url = BASE_URL + "/nosql/favorites?userId=" + userId + "&placeId=" + placeId;

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(url))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(""))
                .build();

        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    public static String getFavoritesByUser(Long userId) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/favorites/user/" + userId))
                .GET()
                .build();

        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    /**
     * POST - Gezi planı oluştur (MongoDB)
     */
// Belirli bir şehre ait mekanları çeker
    public static String getPlacesByCityId(Long cityId) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/places/city/" + cityId))
                .GET()
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    // Kullanıcının Gezi Planlarını Çeker
    public static String getTravelPlansByUser(Long userId) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/travel-plans/user/" + userId))
                .GET()
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    // Yeni Gezi Planı Oluşturma
    public static String createTravelPlan(Long userId, String title, java.util.List<Long> placeIds) throws Exception {
        com.google.gson.JsonArray placesArray = new com.google.gson.JsonArray();
        for (Long id : placeIds) { placesArray.add(id); }

        com.google.gson.JsonObject jsonBody = new com.google.gson.JsonObject();
        jsonBody.addProperty("userId", userId);
        jsonBody.addProperty("title", title);
        jsonBody.add("placeIds", placesArray);

        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/travel-plans"))
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody.toString()))
                .header("Content-Type", "application/json")
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    // ID'ye Göre Tek Bir Planı Getirme
    public static String getPlanById(String planId) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/travel-plans/" + planId))
                .GET()
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    // Planı Güncelleme
    public static String updatePlan(String planId, String jsonBody) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/travel-plans/" + planId))
                .PUT(java.net.http.HttpRequest.BodyPublishers.ofString(jsonBody))
                .header("Content-Type", "application/json")
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }
    public static String deletePlan(String planId) throws Exception {
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(BASE_URL + "/nosql/travel-plans/" + planId))
                .DELETE()
                .build();
        return client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString()).body();
    }

    // ================== MAIN - ÖRNEK KULLANIM ==================

    public static void main(String[] args) {
        try {
            String adminResponse = createUser("gizem", "gizem@akillisehir.com", "123456", "ADMIN");
            System.out.println(" ADMIN başarıyla oluşturuldu.");

            String userResponse = createUser("test", "test@akillisehir.com", "1234", "USER");
            System.out.println(" TEST KULLANICISI (USER) başarıyla oluşturuldu.");
            
            System.out.println("=== Tüm Şehirler ===");
            String cities = getAllCities();
            System.out.println(cities);

            System.out.println("\n=== Yeni Şehir Oluştur ===");
            String newCity = createCity("İzmir", "Türkiye", "Ege'nin güzel kenti", "https://...");
            System.out.println(newCity);

            System.out.println("\n=== Tüm Mekanlar ===");
            String places = getAllPlaces();
            System.out.println(places);

            System.out.println("\n=== Yeni Mekan Oluştur ===");

            // 1L: Şehir ID'si (Örn: İzmir), "HISTORICAL_SITE" ve "PARK" ise zorunlu kategori isimleri
            String place1 = createPlace("Tarihi Saat Kulesi", "Şehrin en bilinen tarihi simgesi.", "HISTORICAL_SITE", 1L);
            System.out.println("Eklenen Mekan 1: " + place1);

            String place2 = createPlace("Merkez Park", "Yeşillikler içinde huzurlu bir yürüyüş alanı.", "PARK", 1L);
            System.out.println("Eklenen Mekan 2: " + place2);

            System.out.println("\n=== Mekan Ara ===");
            String searchResult = searchPlaces("saray");
            System.out.println(searchResult);

            System.out.println("\n=== Puan Ekle ===");
            String rating = createRating(5, 1L, 1L);
            System.out.println(rating);

            System.out.println("\n=== Favori Ekle ===");
            String favorite = addFavorite(1L, 5L);
            System.out.println(favorite);

            System.out.println("\n=== Gezi Planı Oluştur ===");
            java.util.List<Long> placeIds = java.util.List.of(1L, 2L, 3L, 4L);
            String plan = createTravelPlan(1L, "İstanbul Turu", placeIds);
            System.out.println(plan);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
