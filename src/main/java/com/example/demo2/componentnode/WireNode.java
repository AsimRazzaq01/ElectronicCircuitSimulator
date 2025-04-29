package com.example.demo2.componentnode;

import com.example.demo2.componentmodel.WireModel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class WireNode extends Line {
    private WireModel wireModel;
    double strokeOffsetLeftCorner = 9;
    double strokeOffsetRight = -9;

    public WireNode(double startX, double startY, double endX, double endY) {
        this.setStroke(Color.BLACK);
        this.setStrokeWidth(18);
        wireModel = new WireModel(startX, startY, endX, endY);
        this.setStartX(startX + strokeOffsetLeftCorner);
        this.setStartY(startY + strokeOffsetLeftCorner);
        this.setEndX(endX + strokeOffsetRight);
        this.setEndY(endY + strokeOffsetLeftCorner);
    }

    public WireModel getWireModel() {
        return wireModel;
    }
}
