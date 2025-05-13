package com.example.demo2;

import javafx.scene.Scene;
import java.util.Objects;
import java.util.prefs.Preferences;
import java.net.URL;


public class ThemeManager {
    private static final Preferences prefs = Preferences.userNodeForPackage(ThemeManager.class);
    private static final String THEME_KEY = "theme";
    private static String currentTheme;

    static {
        // Default theme
        currentTheme = prefs.get(THEME_KEY, "/com/example/demo2/cssStyles/style.css");
    }

    public static void applyTheme(Scene scene, String themePath) {
        URL url = ThemeManager.class.getResource(themePath);
        if (url == null) {
            System.err.println("❌ Theme not found: " + themePath);
            return;
        }

        // Update theme
        currentTheme = themePath;
        prefs.put(THEME_KEY, currentTheme);

        // Apply to scene
        scene.getStylesheets().clear();
        scene.getStylesheets().add(url.toExternalForm());
    }

    public static void applySavedTheme(Scene scene) {
        applyTheme(scene, currentTheme);
    }

    public static String getTheme() {
        return currentTheme;
    }
}

