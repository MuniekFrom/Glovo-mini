package com.example.main;

public class Session {
    private static boolean loggedIn = false;
    private static String loggedUser = null;

    public static boolean isLoggedIn() { return loggedIn; }
    public static String getLoggedUser() { return loggedUser; }

    public static void login(String user) {
        loggedIn = true;
        loggedUser = user;
    }

    public static void logout() {
        loggedIn = false;
        loggedUser = null;
    }
}
