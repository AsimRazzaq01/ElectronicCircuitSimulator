package com.example.demo2.componentmodel;

public class ResistorModel extends Component {
    private double resistance;

    public ResistorModel(double x, double y) {
        super(x,y,"ResistorModel");
        resistance = 10.0;
    }

    public ResistorModel(double x, double y, double r) {
        super(x,y,"ResistorModel");
        resistance = r;
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

}
