package com.example.demo2;

import com.example.demo2.componentmodel.BatteryModel;
import com.example.demo2.componentmodel.CircuitSwitchModel;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.ResistorModel;
import com.example.demo2.componentnode.BatteryNode;
import com.example.demo2.componentnode.TerminalNode;
import com.example.demo2.projectactions.ProjectActions;
import javafx.scene.Node;

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
        for (BatteryModel batteryModel : BATTERY_LIST.keySet()) {
            if (batteryModel.isStartingBattery() && !batteryModel.getNegativeSide().isEmpty() && !batteryModel.getPositiveSide().isEmpty()) {
                circuitGroups.put(count, createCircuits(batteryModel, count));
                count++;
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

            if (current instanceof ResistorModel) {
                totalResistance += ((ResistorModel) current).getResistance();
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
