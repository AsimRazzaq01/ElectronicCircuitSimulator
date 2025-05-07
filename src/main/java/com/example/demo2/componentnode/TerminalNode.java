package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.TerminalModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * TerminalNode class -> 1 var  initialized from terminal Model class -> 1 method , 1 get terminal method , 2 setter methods (x/y)
 * ^ extends Circle
 */
public class TerminalNode extends Circle {
    // Private variable - terminal class
    private TerminalModel terminalModel;
    private int componentId;

    // TerminalNode method
    public TerminalNode(Component c, double centerX, double centerY, String charge) {
        this.setRadius(9);
        this.setCenterY(centerY);
        if (charge.equals("Positive")) {
            this.setFill(Color.RED);
            this.setStroke(Color.BLACK);
            this.setCenterX(centerX);
            terminalModel = new TerminalModel(charge, c);
            componentId = terminalModel.getParent().getComponentID();
        }
        else if (charge.equals("Negative")) {
            this.setFill(Color.GRAY);
            this.setStroke(Color.BLACK);
            this.setCenterX(centerX);
            terminalModel = new TerminalModel(charge, c);
            componentId = terminalModel.getParent().getComponentID();
        }
    }

    // getter method
    public TerminalModel getTerminalModel() {
        return terminalModel;
    }

    // Set x methods
    public void setTerminalX(double centerX) {
        if (getTerminalModel().getCharge().equals("Positive")) {
            this.setCenterX(centerX);
        }
        else if (getTerminalModel().getCharge().equals("Negative")) {
            this.setCenterX(centerX);
        }
    }

    // Set Y methods
    public void setTerminalY(double centerY) {
        this.setCenterY(centerY);
    }
} // End TerminalModel class
