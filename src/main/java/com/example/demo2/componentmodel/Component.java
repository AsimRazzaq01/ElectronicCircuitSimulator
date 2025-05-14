package com.example.demo2.componentmodel;

import java.util.ArrayList;
import java.util.Objects;

/**
 *  * This class is meant to be extended by specific component types
 *  ex. battery hence it is a (abstract class)
 */
public abstract class Component {
    private int componentID;
    private double componentX;
    private double componentY;
    private final String COMPONENT_TYPE;
    public static double zoomScale;
    private int group;

    private final ArrayList<Component> negativeSide;
    private final ArrayList<Component> positiveSide;

    /**
     * Constructs a new Component with a position and type.
     * @param x    The x-coordinate of the component
     * @param y    The y-coordinate of the component
     * @param type The type of the component (e.g., "Resistor", "Capacitor")
     */
    public Component(double x, double y, String type) {
        componentID = -1;
        COMPONENT_TYPE = type;
        componentX = x;
        componentY = y;
        negativeSide = new ArrayList<>();
        positiveSide = new ArrayList<>();
        group = 0;
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

    /**
     * Two components are equal if their IDs and types match.
     * @param o The object to compare
     * @return True if equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Component) {
            return componentID == ((Component) o).getComponentID()
                    && COMPONENT_TYPE.equals(((Component) o).getComponentType());
        }
        else {
            return false;
        }
    }

    /**
     * @return Hash code -> gen a hash code for component using its ID and type.
     */
    @Override
    public int hashCode() {
        return Objects.hash(componentID, COMPONENT_TYPE);
    }
} // End Component class
