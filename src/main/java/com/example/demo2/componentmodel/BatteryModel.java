package com.example.demo2.componentmodel;

/**
 * BatteryModel class -> 2 Constructor methods , voltage variable, get/set methods
 *  ^ extends Component
 */
public class BatteryModel extends Component {
    //private variable
    private double voltage;

    // Constructor method 1
    public BatteryModel(double x , double y) {
        super(x,y,"Battery");
        voltage = 9.0;
    }

    // Constructor method 2
    public BatteryModel(double x , double y, double v) {
        super(x,y,"Battery");
        voltage = v;
    }

    // getter method
    public double getVoltage() {
        return voltage;
    }

    // setter method
    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

} // End BatteryModel class
