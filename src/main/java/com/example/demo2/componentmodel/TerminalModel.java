package com.example.demo2.componentmodel;

/**
 * TerminalModel class -> 2 Private variables , 1 Constructor methods , 2 getter & 1 setter methods
 */
public class TerminalModel {
    // Private variables
    private String charge;
    private final Component parent;

    // Constructor method 1
    public TerminalModel(String c, Component p) {
        charge = c;
        parent = p;
    }

    // getter method -> getCharge
    public String getCharge() {
        return charge;
    }

    // setter method -> setCharge
    public void setCharge(String charge) {
        this.charge = charge;
    }

    // getter method -> getParent
    public Component getParent() {
        return parent;
    }
} // End TerminalModel class
