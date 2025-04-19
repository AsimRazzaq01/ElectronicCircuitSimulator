package com.example.demo2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ProjectController {
    @FXML
    private ScrollPane canvasScrollPane;
    @FXML
    private Button redoButton;
    @FXML
    private Pane canvasPane;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label currentZoomLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button homeButton;
    @FXML
    private Slider zoomSlider;

    @FXML
    private Button wireButton;
    @FXML
    private Button batteryButton;
    @FXML
    private Button resistorButton;
    @FXML
    private Button switchButton;
    @FXML
    private Button lightBulbButton;
    @FXML
    private Button fuseButton;

    private double zoomScale = 1.0;

    @FXML
    public void initialize() {
        //canvasScrollPane.setPannable(true);
        zoomSlider.setDisable(true);
    }

    @FXML
    void zoomIn() {
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / zoomScale;
        double oldCenterY = getViewportCenterY() / zoomScale;

        //Zoom in the pane
        zoomScale += 0.1;
        canvasPane.setScaleX(zoomScale);
        canvasPane.setScaleY(zoomScale);
        adjustElementZoomScale(zoomScale);

        Platform.runLater(() -> {
            double scaledCenterX = oldCenterX * zoomScale;
            double scaledCenterY = oldCenterY * zoomScale;

            //Calculates the original center of viewport when zoom scale is 1.0
            double originalCenterX = scaledCenterX / zoomScale;
            double originalCenterY = scaledCenterY / zoomScale;

            //The new center is the original center coordinates multiplied by the new scale
            double newCenterX = originalCenterX * zoomScale;
            double newCenterY = originalCenterY * zoomScale;

            double hValue = (newCenterX - viewportWidth / 2.0) / ((canvasWidth * zoomScale) - viewportWidth);
            double vValue = (newCenterY - viewportHeight / 2.0) / ((canvasHeight * zoomScale) - viewportHeight);

            //Force a layout update
            canvasScrollPane.layout();

            canvasScrollPane.setHvalue(hValue);
            canvasScrollPane.setVvalue(vValue);

            currentZoomLabel.setText(((int)(100* zoomScale)) + "%");
        });

        if (zoomScale >= 1.5) {
            zoomInButton.setDisable(true);
        }
        else {
            zoomOutButton.setDisable(false);
        }
    }

    @FXML
    void zoomOut() {

        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / zoomScale;
        double oldCenterY = getViewportCenterY() / zoomScale;

        //Zoom out of the pane
        zoomScale -= 0.1;
        canvasPane.setScaleX(zoomScale);
        canvasPane.setScaleY(zoomScale);
        adjustElementZoomScale(zoomScale);

        Platform.runLater(() -> {
            double scaledCenterX = oldCenterX * zoomScale;
            double scaledCenterY = oldCenterY * zoomScale;

            //Calculates the original center of viewport when zoom scale is 1.0
            double originalCenterX = scaledCenterX / zoomScale;
            double originalCenterY = scaledCenterY / zoomScale;

            //The new center is the original center coordinates multiplied by the new scale
            double newCenterX = originalCenterX * zoomScale;
            double newCenterY = originalCenterY * zoomScale;

            double hValue = (newCenterX - viewportWidth / 2.0) / ((canvasWidth * zoomScale) - viewportWidth);
            double vValue = (newCenterY - viewportHeight / 2.0) / ((canvasHeight * zoomScale) - viewportHeight);

            //Force a layout update
            canvasScrollPane.layout();

            canvasScrollPane.setHvalue(hValue);
            canvasScrollPane.setVvalue(vValue);

            currentZoomLabel.setText(((int)(100* zoomScale)) + "%");
        });

        if (zoomScale < 0.6) {
            zoomOutButton.setDisable(true);
        }
        else {
            zoomInButton.setDisable(false);
        }
    }

    private double getViewportCenterX() {
        double contentWidth = canvasScrollPane.getContent().getBoundsInLocal().getWidth();
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();

        double hValue = canvasScrollPane.getHvalue();

        double leftEdge = hValue * (contentWidth - viewportWidth);
        double rightEdge = leftEdge + viewportWidth;

        return (rightEdge + leftEdge) / 2.0;
    }

    private double getViewportCenterY() {
        double contentHeight = canvasScrollPane.getContent().getBoundsInLocal().getHeight();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();

        double vValue = canvasScrollPane.getVvalue();
        double topEdge = vValue * (contentHeight - viewportHeight);
        double bottomEdge = topEdge + viewportHeight;

        return (bottomEdge + topEdge) / 2.0;
    }

    void initializeScrollPosition() {
        Platform.runLater(() -> {
            canvasScrollPane.setHvalue(0.5);
            canvasScrollPane.setVvalue(0.5);
//            Will be utilized later
//            for (Node node : canvasPane.getChildren()) {
//                node.setOpacity(1.0);
//            }
        });
    }

    private void adjustElementZoomScale(double zoomScale) {
        Battery.zoomScale = zoomScale;
    }

    @FXML
    public void addBattery() {
        Battery b = new Battery(getViewportCenterX() / zoomScale, getViewportCenterY() / zoomScale);
        canvasPane.getChildren().add(b);
        adjustElementZoomScale(zoomScale);
        b.makeDraggable(canvasScrollPane);
    }

    @FXML
    public void addResistor() {
        Resistor r = new Resistor(getViewportCenterX() / zoomScale, getViewportCenterY() / zoomScale);
        canvasPane.getChildren().add(r);
        r.makeDraggable(canvasScrollPane);
    }

    @FXML
    protected void logout() throws IOException {
        Scene scene = this.logoutButton.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        scene.setRoot(fxmlLoader.load());
    }
}
