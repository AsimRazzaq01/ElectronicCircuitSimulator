package com.example.demo2;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Battery extends Group {
    private double batteryX;
    private double batteryY;
    private double voltage;

    Battery(double x , double y) {
        Rectangle battery = new Rectangle(70, 30);
        voltage = 9;
        battery.setFill(Color.BLACK);
        this.getChildren().add(battery);
        batteryX = x;
        batteryY = y;
        this.setLayoutX(batteryX);
        this.setLayoutY(batteryY);
    }

    double getBatteryX() {
        return batteryX;
    }

    double getBatteryY() {
        return batteryY;
    }

    void makeDraggable(ScrollPane pane) {
        this.setOnMousePressed(mouseEvent -> {
            batteryX = mouseEvent.getSceneX() - this.getLayoutX();
            batteryY = mouseEvent.getSceneY() - this.getLayoutY();
            pane.setPannable(false);
        });

        this.setOnMouseDragged(mouseEvent -> {
            double newBatteryX = mouseEvent.getSceneX() - batteryX;
            double newBatteryY = mouseEvent.getSceneY() - batteryY;
            this.setLayoutX(newBatteryX);
            this.setLayoutY(newBatteryY);
        });

        this.setOnMouseReleased(mouseEvent -> {
            pane.setPannable(true);
            batteryX = this.getLayoutX();
            batteryY = this.getLayoutY();
        });
    }
}
