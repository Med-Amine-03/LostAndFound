package com.lostandfound.utils;

import com.lostandfound.model.User;

public class SessionManager {
    private static User currentUser;

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    public static String getCurrentUserRole() {
        return (currentUser != null) ? currentUser.getRole() : null;
    }
}
