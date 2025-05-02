package com.example.demo2.componentmodel;

public class TerminalModel {
    private String charge;
    private final Component parent;

    public TerminalModel(String c, Component p) {
        charge = c;
        parent = p;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public Component getParent() {
        return parent;
    }
}
