package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.TerminalModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class TerminalNode extends Circle {
    private TerminalModel terminalModel;

    public TerminalNode(Component c, double centerX, double centerY, String charge) {
        this.setRadius(9);
        this.setCenterY(centerY);
        if (charge.equals("Positive")) {
            this.setFill(Color.RED);
            this.setStroke(Color.BLACK);
            this.setCenterX(centerX);
            terminalModel = new TerminalModel(charge, c);
        }
        else if (charge.equals("Negative")) {
            this.setFill(Color.GRAY);
            this.setStroke(Color.BLACK);
            this.setCenterX(centerX);
            terminalModel = new TerminalModel(charge, c);
        }
    }

    public TerminalModel getTerminalModel() {
        return terminalModel;
    }

    public void setTerminalX(double centerX) {
        if (getTerminalModel().getCharge().equals("Positive")) {
            this.setCenterX(centerX);
        }
        else if (getTerminalModel().getCharge().equals("Negative")) {
            this.setCenterX(centerX);
        }
    }
    public void setTerminalY(double centerY) {
        this.setCenterY(centerY);
    }
}
