package com.example.demo2;

import javafx.scene.Scene;

import java.util.Objects;
import java.util.prefs.Preferences;


import javafx.scene.Scene;

import java.net.URL;
import java.util.Objects;
import java.util.prefs.Preferences;

public class ThemeManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
    private static final String THEME_KEY = "theme";
    private static String currentTheme;

    static {
        // Default to the correct full path with leading slash
        currentTheme = prefs.get(THEME_KEY, "/com/example/demo2/cssStyles/style.css");
    }

    public static void applyTheme(Scene scene) {
        scene.getStylesheets().clear();
        URL url = ThemeManager.class.getResource(currentTheme);
        if (url == null) {
            System.err.println("❌ CSS not found at: " + currentTheme);
            return;
        }
        scene.getStylesheets().add(url.toExternalForm());
    }

    public static void setTheme(String themePath) {
        currentTheme = themePath;
        prefs.put(THEME_KEY, currentTheme);
    }

    public static String getTheme() {
        return currentTheme;
    }
}

