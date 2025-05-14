package com.example.demo2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application class -> start method to start project
 */
public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("LoginRegister.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Splash_Screen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 720);  //680, 400
        ThemeManager.applySavedTheme(scene); // 👈 Restore the saved theme
        stage.setTitle("Electronic Circuit Simulator!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();

    } // start method

    public static void main(String[] args) {
        launch();
    }

} // End Application class