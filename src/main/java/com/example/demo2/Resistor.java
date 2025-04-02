package com.example.demo2;

import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Resistor extends Group {
    private double resistorX;
    private double resistorY;
    private double resistance;

    Resistor(double x, double y) {
        Rectangle resistor = new Rectangle(70, 30);
        resistance = 10;
        resistor.setFill(Color.MAROON);
        this.getChildren().add(resistor);
        resistorX = x;
        resistorY = y;
        this.setLayoutX(resistorX);
        this.setLayoutY(resistorY);
    }

    void makeDraggable(ScrollPane pane) {
        this.setOnMousePressed(mouseEvent -> {
            resistorX = mouseEvent.getSceneX() - this.getLayoutX();
            resistorY = mouseEvent.getSceneY() - this.getLayoutY();
            pane.setPannable(false);
        });

        this.setOnMouseDragged(mouseEvent -> {
            double newResistorX = mouseEvent.getSceneX() - resistorX;
            double newResistorY = mouseEvent.getSceneY() - resistorY;
            this.setLayoutX(newResistorX);
            this.setLayoutY(newResistorY);
        });

        this.setOnMouseReleased(mouseEvent -> {
            pane.setPannable(true);
            resistorX = this.getLayoutX();
            resistorY = this.getLayoutY();
        });
    }
}
