package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.util.Objects;

/**
 * SettingsController -> class for setting page
 */
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
            Stage stage = (Stage) changeUserNameField.getScene().getWindow();
            Scene scene = new Scene(root, 1200, 720); // width: 680, height: 400
            stage.setScene(scene);
//            ThemeManager.applyTheme(scene);
            ThemeManager.applySavedTheme(scene); // 👈 Restore the saved theme

            stage.setResizable(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * SetLightTheme -> BUTTON ON CLICK
     * @param event -> Change theme
     */
    @FXML
    void SetLightTheme(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        // Use the correct path for your light theme CSS file
        ThemeManager.applyTheme(scene, "/com/example/demo2/cssStyles/style.css");
        showConfirmation("Light theme applied.");
    }

    /**
     * SetDarkTheme -> BUTTON ON CLICK
     * @param event -> Change theme
     */
    @FXML
    void SetDarkTheme(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        // Use the correct path for your dark theme CSS file
        // Remove the redundant scene.getStylesheets().clear(); line
        ThemeManager.applyTheme(scene, "/com/example/demo2/cssStyles/darkStyle.css");
        showConfirmation("Dark theme applied.");
    }

    // Debugging
//    @FXML
//    void SetDarkTheme(ActionEvent event) {
//        Scene scene = ((Node) event.getSource()).getScene();
//        System.out.println("Stylesheets BEFORE applying dark theme: " + scene.getStylesheets());
//        ThemeManager.applyTheme(scene, "/com/example/demo2/cssStyles/darkStyle.css");
//        System.out.println("Stylesheets AFTER applying dark theme: " + scene.getStylesheets());
//        showConfirmation("Dark theme applied.");
//    }


    private void showConfirmation(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    } // End showConfirmation

} // End SettingsController
