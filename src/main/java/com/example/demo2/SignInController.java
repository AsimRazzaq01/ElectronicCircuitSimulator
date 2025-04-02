package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

public class SignInController {


    @FXML
    private TextField EmailTextField;

    @FXML
    private TextField PasswordTextField;

    @FXML
    private Text SignInMessage;

    @FXML
    void RegisterClick(ActionEvent event) throws IOException {
        Scene scene = this.SignInMessage.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Register.fxml"));
        scene.setRoot(fxmlLoader.load());
    }

    @FXML
    protected void SignInClick() throws IOException {
        Scene scene = this.SignInMessage.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("project-view.fxml"));
        scene.setRoot(fxmlLoader.load());
    }


}