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

    public WireNode(WireModel model) {
        this.setStroke(Paint.valueOf("#1D1542")); // Blue
        this.setStrokeLineCap(StrokeLineCap.ROUND);
        this.setStrokeWidth(18);
        this.wireModel = model;

        this.setStartX(model.getComponentX());
        this.setStartY(model.getComponentY());
        this.setEndX(model.getRightSideX());
        this.setEndY(model.getRightSideY());

        this.leftWireTerminalNode = new WireTerminalNode(model, getStartX(), getStartY(), "Negative");
        this.rightWireTerminalNode = new WireTerminalNode(model, getEndX(), getEndY(), "Positive");

        leftWireTerminalNode.centerXProperty().bind(startXProperty());
        leftWireTerminalNode.centerYProperty().bind(startYProperty());
        rightWireTerminalNode.centerXProperty().bind(endXProperty());
        rightWireTerminalNode.centerYProperty().bind(endYProperty());
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
