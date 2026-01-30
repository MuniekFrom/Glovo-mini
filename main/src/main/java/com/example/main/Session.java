package com.example.main;

public class Session {
    private static boolean loggedIn = false;
    private static Integer userId = null;
    private static String loggedUser = null;

    public static boolean isLoggedIn() { return loggedIn; }
    public static Integer getUserId() { return userId; }
    public static String getLoggedUser() { return loggedUser; }

    public static void login(Integer id, String user) {
        loggedIn = true;
        userId = id;
        loggedUser = user;
    }

    public static void logout() {
        loggedIn = false;
        userId = null;
        loggedUser = null;
    }
}


