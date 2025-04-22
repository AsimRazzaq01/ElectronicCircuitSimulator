package com.example.demo2.components;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;

public abstract class Component {
    private double componentX;
    private double componentY;
    public static double zoomScale;

    public Component(double x, double y) {
        componentX = x;
        componentY = y;
    }

    public double getComponentX() {
        return componentX;
    }

    public double getComponentY() {
        return componentY;
    }

    public void setComponentX(double x) {
        componentX = x;
    }

    public void setComponentY(double y) {
        componentY = y;
    }

    public abstract void makeDraggable(ScrollPane pane);

    public abstract Node getComponentNode();
}
