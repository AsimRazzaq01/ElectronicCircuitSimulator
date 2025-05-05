package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.TerminalModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class WireTerminalNode extends Circle {
    private TerminalModel terminalModel;

    public WireTerminalNode(Component c, double centerX, double centerY, String charge) {
        this.setRadius(9);

        if (charge.equals("Positive")) {
            this.setFill(Color.RED);
        } else if (charge.equals("Negative")) {
            this.setFill(Color.GRAY);
        }

        this.setStroke(Color.BLACK);

        terminalModel = new TerminalModel(charge, c);
    }

    public TerminalModel getTerminalModel() {
        return terminalModel;
    }

    public void setTerminalX(double centerX) {
        // Safely update only if not bound
        if (!centerXProperty().isBound()) {
            setCenterX(centerX);
        }
    }

    public void setTerminalY(double centerY) {
        if (!centerYProperty().isBound()) {
            setCenterY(centerY);
        }
    }
}
