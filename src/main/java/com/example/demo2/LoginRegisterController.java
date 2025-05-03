package com.example.demo2;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginRegisterController implements Initializable {

    @FXML
    private Pane pane_box;

    @FXML
    private VBox vBox_existing_fields, vBox_new_fields;

    @FXML
    private VBox vBox_existing_box, vBox_new_box;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        showExisting();
    }

    public void showExisting() {
        FadeTransition fadeInFields = createFade(vBox_existing_fields, 0.0, 1.0);
        FadeTransition fadeInBox = createFade(vBox_existing_box, 0.0, 1.0);
        FadeTransition fadeOutFields = createFade(vBox_new_fields, 1.0, 0.0);
        FadeTransition fadeOutBox = createFade(vBox_new_box, 1.0, 0.0);
        PathTransition slideLeft = createSlideTransition(510, 200, 170, 200);
        ParallelTransition transition = new ParallelTransition(
                slideLeft, fadeInFields, fadeInBox, fadeOutFields, fadeOutBox
        );
        vBox_existing_fields.setVisible(true);
        vBox_existing_box.setVisible(true);
        vBox_new_fields.setVisible(false);
        vBox_new_box.setVisible(false);
        transition.play();
    }

    public void showNew() {
        FadeTransition fadeInFields = createFade(vBox_new_fields, 0.0, 1.0);
        FadeTransition fadeInBox = createFade(vBox_new_box, 0.0, 1.0);
        FadeTransition fadeOutFields = createFade(vBox_existing_fields, 1.0, 0.0);
        FadeTransition fadeOutBox = createFade(vBox_existing_box, 1.0, 0.0);
        PathTransition slideRight = createSlideTransition(170, 200, 510, 200);
        ParallelTransition transition = new ParallelTransition(
                slideRight, fadeInFields, fadeInBox, fadeOutFields, fadeOutBox
        );
        vBox_new_fields.setVisible(true);
        vBox_new_box.setVisible(true);
        vBox_existing_fields.setVisible(false);
        vBox_existing_box.setVisible(false);
        transition.play();
    }

    private FadeTransition createFade(VBox node, double from, double to) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), node);
        fade.setFromValue(from);
        fade.setToValue(to);
        return fade;
    }

    private PathTransition createSlideTransition(double fromX, double fromY, double toX, double toY) {
        Path path = new Path(new MoveTo(fromX, fromY), new LineTo(toX, toY));
        PathTransition transition = new PathTransition(Duration.seconds(1.25), path, pane_box);
        return transition;
    }

    @FXML
    public void handleButton_login() throws IOException {
        // add DB code here
        Scene scene = this.vBox_existing_box.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("project-view.fxml"));
        Stage stage = (Stage) vBox_existing_box.getScene().getWindow();
        stage.setResizable(true);
        scene.setRoot(fxmlLoader.load());
    }

    @FXML
    public void handleButton_register() {
        // add DB code here
    }
}