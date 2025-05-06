package com.example.demo2.componentmodel;

/**
 * LightbulbModel -> 1 private variable , 2 Constructor methods , getter & setter methods
 * ^ extends Component
 */
public class LightbulbModel extends Component {
    // private variable
    private double resistance;

    // Constructor method 1
    public LightbulbModel(double x, double y) {
        super(x, y, "Light bulb");
        resistance = 10.0;
    }

    // Constructor method 2
    public LightbulbModel(double x, double y, double r) {
        super(x, y, "Light bulb");
        resistance = r;
    }

    // getter method -> getResistance
    public double getResistance() {
        return resistance;
    }

    // getter method -> setResistance
    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

} // End LightbulbModel Class