package com.example.demo2.componentmodel;

public class WireModel extends Component {
    private double rightSideX;
    private double rightSideY;
    private double current;

    public WireModel(double startX, double startY, double endX, double endY) {
        super(startX, startY, "Wire");
        rightSideX = endX;
        rightSideY = endY;
        current = 0;

    }

    public WireModel(double startX, double startY, double endX, double endY, double c) {
        super(startX, startY, "Wire");
        rightSideX = endX;
        rightSideY = endY;
        current = c;
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

    public double getCurrent() {
        return current;
    }

    public void setCurrent(double current) {
        this.current = current;
    }
}
