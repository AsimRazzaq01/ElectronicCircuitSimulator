package com.example.demo2;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Battery extends Group {
    private double batteryX;
    private double batteryY;
    private double voltage;
    static double zoomScale;

    Battery(double x , double y) {
        URL imagePath = Battery.class.getResource("component_sprites/battery.png");
        if (imagePath != null) {
            Image batteryImage = new Image(imagePath.toExternalForm(),70,0,true,false);
            ImageView batteryImageView = new ImageView(batteryImage);
            this.getChildren().add(batteryImageView);
            voltage = 9.0;
            batteryX = x;
            batteryY = y;
            this.setLayoutX(batteryX);
            this.setLayoutY(batteryY);
        }
    }

    double getBatteryX() {
        return batteryX;
    }

    double getBatteryY() {
        return batteryY;
    }

    void makeDraggable(ScrollPane pane) {
        this.setOnMousePressed(mouseEvent -> {
            batteryX = (mouseEvent.getSceneX() / zoomScale) - this.getLayoutX();
            batteryY = (mouseEvent.getSceneY() / zoomScale) - this.getLayoutY();
            pane.setPannable(false);
            this.toFront();
        });

        this.setOnMouseDragged(mouseEvent -> {
            double newBatteryX = (mouseEvent.getSceneX() / zoomScale) - batteryX;
            double newBatteryY = (mouseEvent.getSceneY() / zoomScale) - batteryY;

            if (newBatteryX >= 0 && newBatteryX <= 1528) {
                this.setLayoutX(newBatteryX);
            }
            if (newBatteryY >= 0 && newBatteryY <= 842.5) {
                this.setLayoutY(newBatteryY);
            }
        });

        this.setOnMouseReleased(_ -> {
            pane.setPannable(true);
            batteryX = this.getLayoutX();
            batteryY = this.getLayoutY();
        });
    }
}
