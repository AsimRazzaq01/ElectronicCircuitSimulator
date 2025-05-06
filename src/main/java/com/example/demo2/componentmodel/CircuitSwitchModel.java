package com.example.demo2.componentmodel;

/**
 * CircuitSwitchModel -> 2 Constructor methods , active variable, is/set active methods
 *  ^ extends Component
 */
public class CircuitSwitchModel extends Component {
    //private variable
    private boolean active;

    // Constructor method 1
    public CircuitSwitchModel(double x, double y) {
        super(x, y, "Switch");
        active = false;
    }

    // Constructor method 2
    public CircuitSwitchModel(double x, double y, boolean a) {
        super(x, y, "Switch");
        active = a;
    }

    // set active method
    public void setActive(boolean a) {
        active = a;
    }

    // is active method
    public boolean isActive() {
        return active;
    }

} // End CircuitSwitchModel class
