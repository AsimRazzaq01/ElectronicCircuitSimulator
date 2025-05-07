package com.example.demo2.componentmodel;

import com.example.demo2.componentnode.TerminalNode;

import java.util.ArrayList;
import java.util.Objects;

public abstract class Component {
    private int componentID;
    private double componentX;
    private double componentY;
    private final String COMPONENT_TYPE;
    public static double zoomScale;
    private int group;

    private final ArrayList<Component> negativeSide;
    private final ArrayList<Component> positiveSide;

    public Component(double x, double y, String type) {
        componentID = -1;
        COMPONENT_TYPE = type;
        componentX = x;
        componentY = y;
        negativeSide = new ArrayList<>();
        positiveSide = new ArrayList<>();
        group = -1;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }

    public double getComponentX() {
        return componentX;
    }

    public double getComponentY() {
        return componentY;
    }

    public void setComponentX(double x) {
        componentX = x;
    }

    public void setComponentY(double y) {
        componentY = y;
    }

    public void setComponentID(int id) {
        componentID = id;
    }

    public int getComponentID() {
        return componentID;
    }

    public void setGroup(int number) {
        group = number;
    }

    public int getGroup() {
        return group;
    }

    public boolean hasValidID() {
        return componentID > 0;
    }

    public void addToNegativeSide(Component c) {
        negativeSide.add(c);
    }

    public void addToPositiveSide(Component c) {
        positiveSide.add(c);
    }

    public ArrayList<Component> getNegativeSide() {
        return negativeSide;
    }

    public ArrayList<Component> getPositiveSide() {
        return positiveSide;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Component) {
            return componentID == ((Component) o).getComponentID() && COMPONENT_TYPE.equals(((Component) o).getComponentType());
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(componentID, COMPONENT_TYPE);
    }
}
