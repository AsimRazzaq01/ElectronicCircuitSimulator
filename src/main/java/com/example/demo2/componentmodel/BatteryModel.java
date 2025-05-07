package com.example.demo2.componentmodel;

/**
 * BatteryModel class -> 2 Constructor methods , voltage variable, get/set methods
 *  ^ extends Component
 */
public class BatteryModel extends Component {
    //private variable
    private double voltage;
    private boolean startingBattery;

    // Constructor method 1
    public BatteryModel(double x , double y) {
        super(x,y,"Battery");
        voltage = 9.0;
        startingBattery = true;
    }

    // Constructor method 2
    public BatteryModel(double x , double y, double v) {
        super(x,y,"Battery");
        voltage = v;
        startingBattery = true;
    }

    // getter method
    public double getVoltage() {
        return voltage;
    }

    // setter method
    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public boolean isStartingBattery() {
        return startingBattery;
    }

    public void setStartingBattery(boolean startingBattery) {
        this.startingBattery = startingBattery;
    }

} // End BatteryModel class
