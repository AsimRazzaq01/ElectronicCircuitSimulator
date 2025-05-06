package com.example.demo2.componentmodel;

/**
 * ResistorModel -> 1 private variable , 2 Constructor methods , getter & setter methods
 * ^ extends Component
 */
public class ResistorModel extends Component {
    //private variable
    private double resistance;

    // Constructor method 1
    public ResistorModel(double x, double y) {
        super(x,y,"Resistor");
        resistance = 10.0;
    }

    // Constructor method 2
    public ResistorModel(double x, double y, double r) {
        super(x,y,"Resistor");
        resistance = r;
    }

    // getter method -> getResistance
    public double getResistance() {
        return resistance;
    }

    // setter method -> setResistance
    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

} // End ResistorModel class
