package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class LoginRegisterController implements Initializable {

    @FXML private Button button_existingAccount, button_existing_login, button_newAccount, button_new_register;
    @FXML private ImageView logoImageView, logoImageViewNew;
    @FXML private Pane pane_box;
    @FXML private Rectangle rectangle;
    @FXML private TextField textField_existing_email, textField_new_email1, textField_new_username;
    @FXML private PasswordField textField_existing_pass, textField_new_pass1;
    @FXML private VBox vBox_existing_box, vBox_existing_fields, vBox_new_box, vBox_new_fields;

    private final ConnDbOps db = new ConnDbOps();


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ThemeManager.getTheme();
//        ThemeManager.applySavedTheme(mainScene);  // Pass your scene here
        showExisting();
    }

    public void showExisting() {
        fadeTransition(vBox_existing_fields, vBox_existing_box, vBox_new_fields, vBox_new_box, 900, 300);  //510, 170
    }

    public void showNew() {
        fadeTransition(vBox_new_fields, vBox_new_box, vBox_existing_fields, vBox_existing_box, 300, 900);  //170, 510
    }

    private void fadeTransition(VBox fadeInFields, VBox fadeInBox, VBox fadeOutFields, VBox fadeOutBox, double fromX, double toX) {
        FadeTransition inFields = new FadeTransition(Duration.seconds(1.5), fadeInFields);
        FadeTransition inBox = new FadeTransition(Duration.seconds(1.5), fadeInBox);
        FadeTransition outFields = new FadeTransition(Duration.seconds(1.5), fadeOutFields);
        FadeTransition outBox = new FadeTransition(Duration.seconds(1.5), fadeOutBox);

        inFields.setFromValue(0.0); inFields.setToValue(1.0);
        inBox.setFromValue(0.0); inBox.setToValue(1.0);
        outFields.setFromValue(1.0); outFields.setToValue(0.0);
        outBox.setFromValue(1.0); outBox.setToValue(0.0);

        Path path = new Path(new MoveTo(fromX, 360), new LineTo(toX, 360)); //200
        PathTransition slide = new PathTransition(Duration.seconds(1.25), path, pane_box);

        fadeInFields.setVisible(true);
        fadeInBox.setVisible(true);
        fadeOutFields.setVisible(false);
        fadeOutBox.setVisible(false);

        new ParallelTransition(slide, inFields, inBox, outFields, outBox).play();
    }

    @FXML
    void handleButton_login() {
        String email = textField_existing_email.getText();
        String password = textField_existing_pass.getText();

        Stage popupStage = new Stage();
        popupStage.setTitle("Loading");
        popupStage.setWidth(300);
        popupStage.setHeight(200);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        VBox popupVbox = new VBox();
        popupVbox.setAlignment(Pos.CENTER);
        Label headerLabel = new Label("Please wait");
        headerLabel.setStyle("-fx-font-family: System; -fx-font-size: 25px; -fx-font-weight: bold;");
        popupVbox.getChildren().add(headerLabel);
        Scene popup = new Scene(popupVbox);
        popupStage.setScene(popup);

        //Perform login operation on task
        Task<Integer> login = new Task<>() {

            @Override
            protected Integer call() throws Exception {
                int userId = db.validateLoginAndGetUserId(email, password);

                if (userId == -1) {
                    throw new NullPointerException();
                }
                if (userId == -2) {
                    throw new SQLException();
                }
                return userId;
            }
        };

        login.setOnSucceeded(_ -> {
            Session.loggedInUserId = login.getValue(); // Set user ID globally
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) button_existing_login.getScene().getWindow();
                Scene scene = new Scene(root, 1200, 720); // width: 680, height: 400
                stage.setScene(scene);
//                ThemeManager.applyTheme(scene); // Apply the saved theme to the scene
                ThemeManager.applySavedTheme(scene); // 👈 Restore the saved theme
                stage.setResizable(false);
                popupStage.close();
            } catch (IOException _) {
            }
        });

        login.setOnFailed(_ -> {
            popupStage.close();
            if (login.getException() instanceof NullPointerException) {
                showAlert("Login Failed", "Invalid email or password.");
            }
            if (login.getException() instanceof SQLException) {
                showAlert("Login Failed", "Could not connect to server. Please try again later.");
            }
        });

        Thread thread = new Thread(login);
        thread.setDaemon(true);
        thread.start();

        popupStage.show();
    }

    @FXML
    void handleButton_register(ActionEvent event) {
        String username = textField_new_username.getText().trim();
        String email = textField_new_email1.getText().trim();
        String password = textField_new_pass1.getText().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert("Registration Failed", "All fields are required.");
            return;
        }

        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showAlert("Registration Failed", "Please enter a valid email address.");
            return;
        }


        Stage popupStage = new Stage();
        popupStage.setTitle("Loading");
        popupStage.setWidth(300);
        popupStage.setHeight(200);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        VBox popupVbox = new VBox();
        popupVbox.setAlignment(Pos.CENTER);
        Label headerLabel = new Label("Please wait");
        headerLabel.setStyle("-fx-font-family: System; -fx-font-size: 25px; -fx-font-weight: bold;");
        popupVbox.getChildren().add(headerLabel);
        Scene popup = new Scene(popupVbox);
        popupStage.setScene(popup);

        Task<Integer> createAccount = new Task<>() {

            @Override
            protected Integer call() throws Exception {
                int status = db.insertUser(username, email, password);

                if (status == -1) {
                    throw new SQLException();
                }
                return status;
            }
        };

        createAccount.setOnSucceeded(_ -> {
            showAlert("Account Created", "Registration successful! You can now log in.");
            showExisting();
        });

        createAccount.setOnFailed(_ -> {
            popupStage.close();
            if (createAccount.getException() instanceof SQLException) {
                showAlert("Account Creation Failed", "Could not connect to server. Please try again later.");
            }
        });

        Thread thread = new Thread(createAccount);
        thread.setDaemon(true);
        thread.start();

        popupStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
