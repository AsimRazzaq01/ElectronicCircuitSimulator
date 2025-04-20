package com.example.demo2;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Resistor extends Group {
    private double resistorX;
    private double resistorY;
    private double resistance;
    static double zoomScale;

    Resistor(double x, double y) {
        URL imagePath = Resistor.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            Image resistorImage = new Image(imagePath.toExternalForm(),70,0,true,false);
            ImageView resistorImageView = new ImageView(resistorImage);
            this.getChildren().add(resistorImageView);
            resistance = 10;
            resistorX = x;
            resistorY = y;
            this.setLayoutX(resistorX);
            this.setLayoutY(resistorY);
        }
    }

    void makeDraggable(ScrollPane pane) {
        this.setOnMousePressed(mouseEvent -> {
            resistorX = (mouseEvent.getSceneX() / zoomScale) - this.getLayoutX();
            resistorY = (mouseEvent.getSceneY() / zoomScale) - this.getLayoutY();
            pane.setPannable(false);
            this.toFront();
        });

        this.setOnMouseDragged(mouseEvent -> {
            double newResistorX = (mouseEvent.getSceneX() / zoomScale) - resistorX;
            double newResistorY = (mouseEvent.getSceneY() / zoomScale) - resistorY;

            if (newResistorX >= 0 && newResistorX <= 1528) {
                this.setLayoutX(newResistorX);
            }
            if (newResistorY >= 0 && newResistorY <= 842.5) {
                this.setLayoutY(newResistorY);
            }
        });

        this.setOnMouseReleased(_ -> {
            pane.setPannable(true);
            resistorX = this.getLayoutX();
            resistorY = this.getLayoutY();
        });
    }
}
