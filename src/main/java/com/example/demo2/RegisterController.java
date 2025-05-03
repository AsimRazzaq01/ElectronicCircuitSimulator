package com.example.demo2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML
    private Button backButton, registerButton;

    @FXML
    private TextField emailField, firstNameField, lastNameField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    protected void back() throws IOException {
        Scene scene = this.backButton.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        scene.setRoot(fxmlLoader.load());
    }

}
