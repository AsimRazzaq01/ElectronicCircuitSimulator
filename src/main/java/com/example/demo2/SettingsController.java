package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class SettingsController {

    private final ConnDbOps db = new ConnDbOps();

    @FXML
    private Button backButton;

    @FXML
    private TextField changeUserNameField;

    public void start() {
        String username = db.getUsernameById(Session.loggedInUserId);
        if (username != null) {
            changeUserNameField.setText(username);
        }
    }

    @FXML
    private void handleUsernameChange() {
        String newUsername = changeUserNameField.getText().trim();
        if (!newUsername.isEmpty()) {
            db.updateUsernameById(Session.loggedInUserId, newUsername);
            showConfirmation("Username updated successfully.");
        } else {
            showConfirmation("Username cannot be empty.");
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
            Parent root = loader.load();

            LandingPageController controller = loader.getController();
            controller.initialize();

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showConfirmation(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
