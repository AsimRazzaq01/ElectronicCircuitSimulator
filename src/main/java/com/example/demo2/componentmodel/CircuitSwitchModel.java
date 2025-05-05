package com.example.demo2.componentmodel;

public class CircuitSwitchModel extends Component {
    private boolean active;

    public CircuitSwitchModel(double x, double y) {
        super(x, y, "Switch");
        active = false;
    }

    public CircuitSwitchModel(double x, double y, boolean a) {
        super(x, y, "Switch");
        active = a;
    }

    public void setActive(boolean a) {
        active = a;
    }

    public boolean isActive() {
        return active;
    }
}
