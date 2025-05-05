package com.example.demo2;

import com.example.demo2.db.ConnDbOps;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        showExisting();
    }

    public void showExisting() {
        fadeTransition(vBox_existing_fields, vBox_existing_box, vBox_new_fields, vBox_new_box, 510, 170);
    }

    public void showNew() {
        fadeTransition(vBox_new_fields, vBox_new_box, vBox_existing_fields, vBox_existing_box, 170, 510);
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

        Path path = new Path(new MoveTo(fromX, 200), new LineTo(toX, 200));
        PathTransition slide = new PathTransition(Duration.seconds(1.25), path, pane_box);

        fadeInFields.setVisible(true);
        fadeInBox.setVisible(true);
        fadeOutFields.setVisible(false);
        fadeOutBox.setVisible(false);

        new ParallelTransition(slide, inFields, inBox, outFields, outBox).play();
    }

    @FXML
    void handleButton_login(ActionEvent event) {
        String email = textField_existing_email.getText();
        String password = textField_existing_pass.getText();

        int userId = db.validateLoginAndGetUserId(email, password);
        if (userId != -1) {
            Session.loggedInUserId = userId; // Set user ID globally
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) button_existing_login.getScene().getWindow();
                Scene scene = new Scene(root, 1200, 720); // width: 680, height: 400
                stage.setScene(scene);
                stage.setResizable(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Login Failed", "Invalid email or password.");
        }
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

        db.insertUser(username, email, password);
        showAlert("Account Created", "Registration successful! You can now log in.");
        showExisting();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
