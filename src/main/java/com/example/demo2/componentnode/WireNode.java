package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.WireModel;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class WireNode extends Line {
    private final WireModel wireModel;
    private final TerminalNode leftTerminalNode;
    private final TerminalNode rightTerminalNode;
    double strokeOffsetLeftCorner = 12.5;
    double strokeOffsetRight = -12.5;

    public WireNode(double startX, double startY, double endX, double endY) {
        this.setStroke(Paint.valueOf("#1D1542"));
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeWidth(18);
        wireModel = new WireModel(startX, startY, endX, endY);
        this.setStartX(startX + strokeOffsetLeftCorner);
        this.setStartY(startY + strokeOffsetLeftCorner);
        leftTerminalNode = new TerminalNode(getWireModel(), startX + strokeOffsetLeftCorner, startY + strokeOffsetLeftCorner, "Negative");
        rightTerminalNode = new TerminalNode(getWireModel(), endX + strokeOffsetRight, endY + strokeOffsetLeftCorner, "Positive");
        this.setEndX(endX + strokeOffsetRight);
        this.setEndY(endY + strokeOffsetLeftCorner);
    }

    public WireModel getWireModel() {
        return wireModel;
    }

    public TerminalNode getLeftTerminalNode() {
        return leftTerminalNode;
    }

    public TerminalNode getRightTerminalNode() {
        return rightTerminalNode;
    }
}
