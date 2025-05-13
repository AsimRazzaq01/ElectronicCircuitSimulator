package com.example.demo2;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.media.Media; // ✅ CORRECT for JavaFX video/audio
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class splashScreenController implements Initializable {
    @FXML
    private MediaView mediaView;
    @FXML
    private Text splash_header;

    private File file;
    private Media media;
    private MediaPlayer mediaPlayer;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        URL mediaUrl = getClass().getResource("Splash_Screen_vid.mp4");

        if (mediaUrl != null) {
            media = new Media(mediaUrl.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setRate(2.0);
            mediaView.setMediaPlayer(mediaPlayer);

            mediaPlayer.setOnReady(() -> {
                Duration duration = media.getDuration();
                Timeline fadeTrigger = new Timeline(
                        new KeyFrame(duration.subtract(Duration.seconds(2)), event -> {
                            // Start fading out 1 second before the video ends
                            FadeTransition fadeOut = new FadeTransition(Duration.seconds(3), mediaView);
                            FadeTransition fadeOut2 = new FadeTransition(Duration.seconds(3), splash_header);
                            fadeOut.setFromValue(1.0);
                            fadeOut.setToValue(0.0);
                            fadeOut.play();
                            fadeOut2.setFromValue(1.0);
                            fadeOut2.setToValue(0.0);
                            fadeOut2.play();
                        })
                );
                fadeTrigger.play();
            });

            mediaPlayer.setOnEndOfMedia(() -> switchToMainScreen());

            mediaPlayer.play();
        } else {
            System.out.println("Video file not found.");
        }
    }


    private void switchToMainScreen() {
        try {
            // Load the next screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginRegister.fxml"));
            Parent newRoot = loader.load();
            newRoot.setOpacity(0); // Start fully transparent

            // Get the current stage and scene
            Stage stage = (Stage) mediaView.getScene().getWindow();
            Scene currentScene = stage.getScene();
            Parent oldRoot = currentScene.getRoot();

            // Wrap both old and new roots in a StackPane to layer them
            StackPane stack = new StackPane(oldRoot, newRoot);
            Scene transitionScene = new Scene(stack, 1200, 720);

            stage.setScene(transitionScene);

            // Create fade transitions
            FadeTransition fadeOutOld = new FadeTransition(Duration.seconds(2), oldRoot);
            fadeOutOld.setFromValue(1.0);
            fadeOutOld.setToValue(0.0);

            FadeTransition fadeInNew = new FadeTransition(Duration.seconds(2), newRoot);
            fadeInNew.setFromValue(0.0);
            fadeInNew.setToValue(1.0);

            // Once transition is done, set final scene to newRoot only
//            fadeInNew.setOnFinished(event -> stage.setScene(new Scene(newRoot, 1200, 720)));


            // Play both animations in parallel
            new ParallelTransition(fadeOutOld, fadeInNew).play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
