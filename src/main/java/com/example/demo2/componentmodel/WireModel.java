package com.example.demo2.componentmodel;

/**
 * WireModel -> class -> 3 Private variables , 2 Constructor methods , 2 getter & 1 setter methods
 * ^ extends Component
 */
public class WireModel extends Component {
    // Private variables
    private double rightSideX;
    private double rightSideY;
    private double current;

    // Constructor method 1
    public WireModel(double startX, double startY, double endX, double endY) {
        super(startX, startY, "Wire");
        rightSideX = endX;
        rightSideY = endY;
        current = 0;
    }

    // Constructor method 2
    public WireModel(double startX, double startY, double endX, double endY, double c) {
        super(startX, startY, "Wire");
        rightSideX = endX;
        rightSideY = endY;
        current = c;
    }

    // Getter Methods
    public double getRightSideX() {
        return rightSideX;
    }

    public double getRightSideY() {
        return rightSideY;
    }

    public double getCurrent() {
        return current;
    }


    // Setter Methods
    public void setRightSideX(double endX) {
        rightSideX = endX;
    }

    public void setRightSideY(double endY) {
        rightSideY = endY;
    }

    public void setCurrent(double current) {
        this.current = current;
    }

} // End WireModel class
