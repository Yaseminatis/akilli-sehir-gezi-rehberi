package org.example;

public class SessionManager {
    private static Long currentUserId = null;
    private static String currentUserRole = null;
    private static String currentUsername = null;

    public static Long getCurrentUserId() { return currentUserId; }
    public static void setCurrentUserId(Long id) { currentUserId = id; }

    public static String getCurrentUserRole() { return currentUserRole; }
    public static void setCurrentUserRole(String role) { currentUserRole = role; }

    public static String getCurrentUsername() { return currentUsername; }
    public static void setCurrentUsername(String username) { currentUsername = username; }

    public static void clearSession() {
        currentUserId = null;
        currentUserRole = null;
        currentUsername = null;
    }
}