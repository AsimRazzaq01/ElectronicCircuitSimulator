package com.example.demo2.componentmodel;

public class WireModel extends Component {
    private double rightSideX;
    private double rightSideY;
    private double current;

    public WireModel(double startX, double startY, double endX, double endY) {
        super(startX, startY, "Wire");
        rightSideX = endX;
        rightSideY = endY;
    }

    public double getRightSideX() {
        return rightSideX;
    }

    public double getRightSideY() {
        return rightSideY;
    }

    public void setRightSideX(double endX) {
        rightSideX = endX;
    }

    public void setRightSideY(double endY) {
        rightSideY = endY;
    }
}
