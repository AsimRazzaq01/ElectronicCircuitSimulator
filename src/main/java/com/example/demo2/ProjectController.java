package com.example.demo2;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

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

    private double scale = 1.0;

    @FXML
    public void initialize() {
        canvasScrollPane.setPannable(true);
        zoomSlider.setDisable(true);
    }

    @FXML
    void zoomIn(ActionEvent actionEvent) {
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / scale;
        double oldCenterY = getViewportCenterY() / scale;

        //Zoom in the pane
        scale += 0.1;
        canvasPane.setScaleX(scale);
        canvasPane.setScaleY(scale);

        Platform.runLater(() -> {
            double scaledCenterX = oldCenterX * scale;
            double scaledCenterY = oldCenterY * scale;

            //Calculates the original center of viewport when zoom scale is 1.0
            double originalCenterX = scaledCenterX / scale;
            double originalCenterY = scaledCenterY / scale;

            //The new center is the original center coordinates multiplied by the new scale
            double newCenterX = originalCenterX * scale;
            double newCenterY = originalCenterY * scale;

            double hValue = (newCenterX - viewportWidth / 2.0) / ((canvasWidth * scale) - viewportWidth);
            double vValue = (newCenterY - viewportHeight / 2.0) / ((canvasHeight * scale) - viewportHeight);

            //Force a layout update
            canvasScrollPane.layout();

            canvasScrollPane.setHvalue(hValue);
            canvasScrollPane.setVvalue(vValue);

            currentZoomLabel.setText(((int)(100*scale)) + "%");
        });

        if (scale >= 1.5) {
            zoomInButton.setDisable(true);
        }
        else {
            zoomOutButton.setDisable(false);
        }
    }

    @FXML
    void zoomOut(ActionEvent actionEvent) {

        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / scale;
        double oldCenterY = getViewportCenterY() / scale;

        //Zoom out of the pane
        scale -= 0.1;
        canvasPane.setScaleX(scale);
        canvasPane.setScaleY(scale);

        Platform.runLater(() -> {
            double scaledCenterX = oldCenterX * scale;
            double scaledCenterY = oldCenterY * scale;

            //Calculates the original center of viewport when zoom scale is 1.0
            double originalCenterX = scaledCenterX / scale;
            double originalCenterY = scaledCenterY / scale;

            //The new center is the original center coordinates multiplied by the new scale
            double newCenterX = originalCenterX * scale;
            double newCenterY = originalCenterY * scale;

            double hValue = (newCenterX - viewportWidth / 2.0) / ((canvasWidth * scale) - viewportWidth);
            double vValue = (newCenterY - viewportHeight / 2.0) / ((canvasHeight * scale) - viewportHeight);

            //Force a layout update
            canvasScrollPane.layout();

            canvasScrollPane.setHvalue(hValue);
            canvasScrollPane.setVvalue(vValue);

            currentZoomLabel.setText(((int)(100*scale)) + "%");
        });

        if (scale < 0.6) {
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
}
