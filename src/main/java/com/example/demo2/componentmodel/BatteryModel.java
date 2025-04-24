package com.example.demo2.componentmodel;

public class BatteryModel extends Component {
    private double voltage;

    public BatteryModel(double x , double y) {
        super(x,y,"Battery");
        voltage = 9.0;
    }

    public BatteryModel(double x , double y, double v) {
        super(x,y,"Battery");
        voltage = v;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }
}
