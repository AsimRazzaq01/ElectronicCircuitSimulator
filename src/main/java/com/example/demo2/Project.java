package com.example.demo2;

import com.example.demo2.componentmodel.BatteryModel;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.projectactions.ProjectActions;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Project {
    private final int PROJECT_ID;
    private final String PROJECT_NAME;
    private final HashMap<Component, Node> PROJECT_COMPONENTS;
    private final ArrayList<BatteryModel> BATTERY_LIST;
    Stack<ProjectActions> undoStack;
    Stack<ProjectActions> redoStack;

    Project(int id, String name) {
        PROJECT_ID = id;
        PROJECT_NAME = name;
        PROJECT_COMPONENTS = new HashMap<>();
        BATTERY_LIST = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    String getProjectName() {
        return PROJECT_NAME;
    }

    int getProjectID() {
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
    }

    public void removeComponent(Component component) {
        PROJECT_COMPONENTS.remove(component);
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
}
