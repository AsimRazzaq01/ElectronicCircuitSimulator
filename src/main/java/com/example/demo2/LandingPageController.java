package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextInputDialog;

import java.io.IOException;

public class LandingPageController {

    @FXML
    private VBox projectListVBox;

    @FXML
    private Button newProjectButton;

    @FXML
    private Label welcomeLabel;


    public void start() {
        String username = new ConnDbOps().getUsernameById(Session.loggedInUserId);
        welcomeLabel.setText("Welcome Back, " + (username != null ? username : "User") + "!");
        loadProjects();
    }

    @FXML
    public void initialize() {
        loadProjects();
    }

    private void loadProjects() {
        // Placeholder projects
        for (int i = 1; i <= 3; i++) {
            final int projectNumber = i;

            Label projectLabel = new Label("Project " + projectNumber);
            projectLabel.setStyle("-fx-font-size: 18px;");
            projectLabel.setOnMouseClicked(event -> openProject("Project " + projectNumber));
            projectListVBox.getChildren().add(projectLabel);
        }
    }


    @FXML
    private void openNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Create a New Project");
        dialog.setContentText("Project Name:");

        dialog.showAndWait().ifPresent(projectName -> {
            openProject(projectName);
        });
    }

    private void openProject(String projectName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("project-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set project name
            ProjectController projectController = loader.getController();
            projectController.setProjectName(projectName);

            Stage stage = (Stage) newProjectButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Settings.fxml"));
            Parent root = loader.load();

            SettingsController controller = loader.getController();
            controller.start();  // SettingsController will also read from Session

            Stage stage = (Stage) settingsButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void logout() {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LoginRegister.fxml")); // assuming SignIn.fxml is your login screen
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
