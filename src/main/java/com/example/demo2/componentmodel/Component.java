package com.example.demo2.componentmodel;

import java.util.Objects;

public abstract class Component {
    private int componentID;
    private double componentX;
    private double componentY;
    private final String COMPONENT_TYPE;
    public static double zoomScale;

    public Component(double x, double y, String type) {
        componentID = -1;
        COMPONENT_TYPE = type;
        componentX = x;
        componentY = y;
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

    public boolean hasValidID() {
        return componentID > 0;
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
