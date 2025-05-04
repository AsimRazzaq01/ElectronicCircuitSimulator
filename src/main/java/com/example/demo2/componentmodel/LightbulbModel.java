package com.example.demo2.componentmodel;

public class LightbulbModel extends Component {
    private double resistance;

    public LightbulbModel(double x, double y) {
        super(x, y, "Light bulb");
        resistance = 10.0;
    }

    public LightbulbModel(double x, double y, double r) {
        super(x, y, "Light bulb");
        resistance = r;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }
}
