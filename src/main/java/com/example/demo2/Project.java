package com.example.demo2;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Stack;

public class Project {
    private final int projectID;
    private final String projectName;
    private ArrayList<Node> projectComponents;
    Stack<ProjectActions> undoStack;
    Stack<ProjectActions> redoStack;

    Project(int id, String name) {
        projectID = id;
        projectName = name;
        projectComponents = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    String getProjectName() {
        return projectName;
    }

    int getProjectID() {
        return projectID;
    }

    ArrayList<Node> getProjectComponents() {
        return projectComponents;
    }

    Stack<ProjectActions> getUndoStack() {
        return undoStack;
    }

    Stack<ProjectActions> getRedoStack() {
        return redoStack;
    }

    void addComponent(Node component) {
        projectComponents.add(component);
    }

    void removeComponent(Node component) {
        projectComponents.remove(component);
    }

    void addToUndoStack(ProjectActions action) {
        undoStack.push(action);
    }

    void addToRedoStack(ProjectActions action) {
        redoStack.push(action);
    }

    ProjectActions performUndo() {
        System.out.println(undoStack.toString());
        return undoStack.pop();
    }

    ProjectActions performRedo() {
        return redoStack.pop();
    }
}
