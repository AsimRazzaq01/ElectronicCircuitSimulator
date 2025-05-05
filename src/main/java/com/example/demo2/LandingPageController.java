package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextInputDialog;


import java.io.IOException;
import java.util.List;

public class LandingPageController {
    @FXML
    private Button settingsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private VBox projectListVBox;

    @FXML
    private Button newProjectButton;

    @FXML
    private Label welcomeLabel;


    private final ConnDbOps dbOps = new ConnDbOps();
    private int currentUserId = Session.getUserId(); // no getInstance needed

    @FXML
    public void initialize() {
        String username = new ConnDbOps().getUsernameById(Session.loggedInUserId);
        welcomeLabel.setText("Welcome Back, " + (username != null ? username : "User") + "!");
        loadProjects();
    }

    private void loadProjects() {
        List<String> projects = dbOps.getProjectsForUser(currentUserId);

        projectListVBox.getChildren().clear(); // Clear old content

        if (projects.isEmpty()) {
            Label noProjectsLabel = new Label("No projects yet. Click 'New Project' to get started!");
            projectListVBox.getChildren().add(noProjectsLabel);
        } else {
            for (String name : projects) {
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 5px; -fx-alignment: center-left;");

                Label projectLabel = new Label(name);
                projectLabel.setStyle("-fx-font-size: 16px; -fx-cursor: hand;");
                projectLabel.setOnMouseClicked(e -> openProject(name));

                Button deleteButton = new Button("Delete");
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(e -> {
                    dbOps.deleteProject(currentUserId, name);
                    loadProjects(); // Refresh UI after deletion
                });

                row.getChildren().addAll(projectLabel, deleteButton);
                projectListVBox.getChildren().add(row);

            }
        }
    }


    @FXML
    private void openNewProject() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Project");
        dialog.setHeaderText("Create a New Project");
        dialog.setContentText("Project Name:");

        dialog.showAndWait().ifPresent(projectName -> {
            String trimmedName = projectName.trim();
            if (!trimmedName.isEmpty()) {
                dbOps.insertProject(currentUserId, trimmedName);
                loadProjects(); // refresh UI
                openProject(trimmedName);
            } else {
                System.out.println("Project name was empty or invalid.");
            }
        });
    }

    private void openProject(String projectName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Project-view.fxml"));
            Parent root = loader.load();

            // Get the controller and set project name
            ProjectController projectController = loader.getController();
            projectController.setProjectName(projectName);

            Stage stage = (Stage) newProjectButton.getScene().getWindow();
            stage.setResizable(true);
            stage.setScene(new Scene(root, 1280, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
            Parent root = FXMLLoader.load(getClass().getResource("LoginRegister.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
