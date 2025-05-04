package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class SettingsController {

    @FXML
    private Button backButton;

    @FXML
    private TextField changeUserNameField;

    @FXML
    private void handleUsernameChange() {
        String newUsername = changeUserNameField.getText().trim();
        if (!newUsername.isEmpty()) {
            System.out.println("Username would be changed to: " + newUsername);
            // Later: update the database here
        }
    }


    @FXML
    private void goBack() {
        try {
            Stage stage = (Stage) backButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LandingPage.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
