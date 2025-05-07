package com.example.demo2;

import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.BatteryNode;
import com.example.demo2.componentnode.LightbulbNode;
import com.example.demo2.componentnode.TerminalNode;
import com.example.demo2.componentnode.WireNode;
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
    HashMap<Integer, Double> circuitGroups;

    Project(int id, String name) {
        PROJECT_ID = id;
        PROJECT_NAME = name;
        PROJECT_COMPONENTS = new HashMap<>();
        BATTERY_LIST = new HashMap<>();
        TERMINAL_LIST = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
        circuitGroups = new HashMap<>();
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

    public void calculateCircuitGroups() {
        int count = 1;
        circuitGroups.clear();
        for (Node componentNode : PROJECT_COMPONENTS.values()) {
            if (componentNode instanceof WireNode wireNode) {
                wireNode.setStroke(Paint.valueOf("#1D1542"));
            }
        }

        for (BatteryModel batteryModel : BATTERY_LIST.keySet()) {
            if (batteryModel.isStartingBattery() && !batteryModel.getNegativeSide().isEmpty() && !batteryModel.getPositiveSide().isEmpty()) {
                circuitGroups.put(count, createCircuits(batteryModel, count));
                count++;
            }
        }

        for (Node componentNode : PROJECT_COMPONENTS.values()) {
            if (componentNode instanceof WireNode wireNode) {
                if (wireNode.getWireModel().getGroup() > 0 && !wireNode.getWireModel().getNegativeSide().isEmpty() && !wireNode.getWireModel().getPositiveSide().isEmpty()) {
                    if (circuitGroups.get(wireNode.getWireModel().getGroup()) != null && circuitGroups.get(wireNode.getWireModel().getGroup()) > 0.0) {
                        wireNode.setStroke(Paint.valueOf("9444CC"));
                    }
                }
            }
        }
        System.out.println(circuitGroups);

        for (Node node : PROJECT_COMPONENTS.values()) {
            if (node instanceof LightbulbNode bulb) {
                // look up the loop voltage for this bulb’s circuit group
                int group = bulb.getLightbulbModel().getGroup();
                double loopVolt = circuitGroups.getOrDefault(group, 0.0);

                // set negative side to 0V, positive side to loopVolt
                bulb.getNegative().getTerminalModel().setVoltage(0.0);
                bulb.getPositive().getTerminalModel().setVoltage(loopVolt);

                // now swap its image if loopVolt > 0
                bulb.updateVisualState();
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
