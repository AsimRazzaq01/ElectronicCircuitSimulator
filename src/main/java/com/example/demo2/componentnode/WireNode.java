package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.WireModel;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class WireNode extends Line {
    private WireModel wireModel;
    private WireTerminalNode leftWireTerminalNode;
    private WireTerminalNode rightWireTerminalNode;
    double strokeOffsetLeftCorner = 12.5;
    double strokeOffsetRight = -12.5;

    public WireNode(double startX, double startY, double endX, double endY) {
        this.setStroke(Paint.valueOf("#1D1542"));
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeWidth(18);
        wireModel = new WireModel(startX, startY, endX, endY);
        this.setStartX(startX + strokeOffsetLeftCorner);
        this.setStartY(startY + strokeOffsetLeftCorner);
        leftWireTerminalNode = new WireTerminalNode(getWireModel(), startX + strokeOffsetLeftCorner, startY + strokeOffsetLeftCorner, "Negative");
        rightWireTerminalNode = new WireTerminalNode(getWireModel(), endX + strokeOffsetRight, endY + strokeOffsetLeftCorner, "Positive");
        this.setEndX(endX + strokeOffsetRight);
        this.setEndY(endY + strokeOffsetLeftCorner);
    }

    public WireModel getWireModel() {
        return wireModel;
    }

    public WireTerminalNode getLeftTerminalNode() {
        return leftWireTerminalNode;
    }

    public WireTerminalNode getRightTerminalNode() {
        return rightWireTerminalNode;
    }
}
