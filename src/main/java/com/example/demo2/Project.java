package com.example.demo2;

import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.*;
import com.example.demo2.projectactions.ProjectActions;
import javafx.scene.Node;
import javafx.scene.paint.Paint;

import java.util.*;

public class Project {
    private final int PROJECT_ID;
    private final String PROJECT_NAME;
    private final HashMap<Component, Node> PROJECT_COMPONENTS;
    private final HashMap<BatteryModel, BatteryNode> BATTERY_LIST;
    private final ArrayList<TerminalNode> TERMINAL_LIST;
    Stack<ProjectActions> undoStack;
    Stack<ProjectActions> redoStack;
    private final HashMap<Integer, Double> CIRCUIT_GROUPS;

    Project(int id, String name) {
        PROJECT_ID = id;
        PROJECT_NAME = name;
        PROJECT_COMPONENTS = new HashMap<>();
        BATTERY_LIST = new HashMap<>();
        TERMINAL_LIST = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        CIRCUIT_GROUPS = new HashMap<>();
    }

    String getProjectName() {
        return PROJECT_NAME;
    }

    public int getProjectID() {
        return PROJECT_ID;
    }

    HashMap<Component, Node> getProjectComponents() {
        return PROJECT_COMPONENTS;
    }

    Stack<ProjectActions> getUndoStack() {
        return undoStack;
    }

    Stack<ProjectActions> getRedoStack() {
        return redoStack;
    }

    public void addComponent(Component component, Node node) {
        PROJECT_COMPONENTS.put(component, node);
        if (node instanceof BatteryNode batteryNode) {
            BATTERY_LIST.put(batteryNode.getBatteryModel(), batteryNode);
        }
    }

    public void removeComponent(Component component) {
        PROJECT_COMPONENTS.remove(component);
        if (component instanceof BatteryModel) {
            BATTERY_LIST.remove(component);
        }
    }

    public void addToTerminalList(TerminalNode node) {
        TERMINAL_LIST.add(node);
    }

    public HashMap<Integer, Double> getCircuitGroups() {
        return CIRCUIT_GROUPS;
    }

    public void removeFromTerminalList(TerminalNode node) {
        TERMINAL_LIST.remove(node);
    }

    public ArrayList<TerminalNode> getTerminalList() {
        return TERMINAL_LIST;
    }

    public void addToUndoStack(ProjectActions action) {
        undoStack.push(action);
    }

    public void addToRedoStack(ProjectActions action) {
        redoStack.push(action);
    }

    ProjectActions performUndo() {
        return undoStack.pop();
    }

    ProjectActions performRedo() {
        return redoStack.pop();
    }

    void clearRedoStack() {
        redoStack.clear();
    }

    private boolean broken_bulb = false;


    public void calculateCircuitGroups() {
        int count = 1;
        CIRCUIT_GROUPS.clear();
        for (Node componentNode : PROJECT_COMPONENTS.values()) {
            if (componentNode instanceof WireNode wireNode) {
                wireNode.setStroke(Paint.valueOf("#1D1542"));

                // bulb broken = true
                if (broken_bulb) {
                    wireNode.setStroke(Paint.valueOf("#D2042D"));
                }
            }
        }


        for (BatteryModel batteryModel : BATTERY_LIST.keySet()) {
            if (batteryModel.isStartingBattery() && !batteryModel.getNegativeSide().isEmpty() && !batteryModel.getPositiveSide().isEmpty()) {
                CIRCUIT_GROUPS.put(count, createCircuits(batteryModel, count));
                count++;
            }
        }
        for (Node componentNode : PROJECT_COMPONENTS.values()) {
            if (componentNode instanceof WireNode wireNode) {
                if (wireNode.getWireModel().getGroup() > 0 && !wireNode.getWireModel().getNegativeSide().isEmpty() && !wireNode.getWireModel().getPositiveSide().isEmpty()) {
                    if (CIRCUIT_GROUPS.get(wireNode.getWireModel().getGroup()) != null && CIRCUIT_GROUPS.get(wireNode.getWireModel().getGroup()) > 0.0) {
                        wireNode.setStroke(Paint.valueOf("9444CC"));
                    }
                }
            }
        }
        System.out.println(CIRCUIT_GROUPS);




        for (Node node : PROJECT_COMPONENTS.values()) {
            if (node instanceof LightbulbNode bulb ) {
                // look up the loop voltage for this bulb’s circuit group
                int group = bulb.getLightbulbModel().getGroup();
                double loopVolt = CIRCUIT_GROUPS.getOrDefault(group, 0.0);


                // set negative side to 0V, positive side to loopVolt
                bulb.getNegative().getTerminalModel().setVoltage(0.0);
                bulb.getPositive().getTerminalModel().setVoltage(loopVolt);

                System.out.println("bulb loopVolt = " +loopVolt);

                // now swap its image if loopVolt > 0
                bulb.updateVisualState();


                // 0.7 is the average capacity of current required to overload a 60 watt bulb
                if (loopVolt > 0.7) {
                    //wait(1000);
                    bulb.updateBrokenVisualState();
                    System.out.println("High current in system = " + loopVolt);
                    System.out.println("Bulb broken");
                    return;
                }
            }
        }
    }


    public double createCircuits(BatteryModel startingBattery, int group) {
        double totalVoltage = 0;
        double totalResistance = 0;


        Stack<Component> componentStack = new Stack<>();
        Set<Component> visited = new HashSet<>();


        componentStack.push(startingBattery);
        totalVoltage += startingBattery.getVoltage();


        while (!componentStack.empty()) {
            Component current = componentStack.pop();
            current.setGroup(group);


            if (current != startingBattery && current instanceof BatteryModel) {
                totalVoltage += ((BatteryModel) current).getVoltage();
                ((BatteryModel) current).setStartingBattery(false);
            }


            // count resistor resistance
            if (current instanceof ResistorModel) {
                totalResistance += ((ResistorModel) current).getResistance();
            }
            // ← NEW: count bulb resistance too
            else if (current instanceof LightbulbModel) {
                totalResistance += ((LightbulbModel) current).getResistance();
            }


            if (current instanceof CircuitSwitchModel) {
                if (((CircuitSwitchModel) current).isActive()) {
                    return 0;
                }
            }


            visited.add(current);


            for (Component positive : current.getPositiveSide()) {
                if (positive == startingBattery && visited.contains(startingBattery)) {
                    if (totalResistance == 0) {
                        return -1;
                    }
                    return totalVoltage / totalResistance;
                }
                if (!visited.contains(positive)) {
                    componentStack.push(positive);
                }
            }


        }
        return 0;
    }
}

