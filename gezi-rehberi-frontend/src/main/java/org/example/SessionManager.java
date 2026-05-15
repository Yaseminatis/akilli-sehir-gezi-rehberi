package org.example;

public class SessionManager {
    private static Long currentUserId = null;

    public static Long getCurrentUserId() {
        return currentUserId;
    }

    public static void setCurrentUserId(Long id) {
        currentUserId = id;
    }
}