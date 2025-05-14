package com.example.demo2;

/**
 * Session -> Sets the session details for the currently logged-in user.
 */
public class Session {
    public static int loggedInUserId;
    public static String username;

    public static void setSession(int userId, String user) {
        loggedInUserId = userId;
        username = user;
    }

    public static int getUserId() {
        return loggedInUserId;
    }

} // End Session class

