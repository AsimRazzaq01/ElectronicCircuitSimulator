package com.example.demo2;

import javafx.scene.Node;

import java.util.ArrayList;
import java.util.Stack;

public class Project {
    private final int projectID;
    private String projectName;
    private ArrayList<Node> projectElements;
    Stack<ProjectActions> undoStack;
    Stack<ProjectActions> redoStack;

    Project(int id, String name) {
        projectID = id;
        projectName = name;
        projectElements = new ArrayList<>();
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    String getProjectName() {
        return projectName;
    }

    void addElement(Node element) {
        projectElements.add(element);
    }

    void removeElement(Node element) {
        projectElements.remove(element);
    }

    void addToUndoStack(ProjectActions action) {
        undoStack.push(action);
    }

    void addToRedoStack(ProjectActions action) {
        redoStack.push(action);
    }

    ProjectActions performUndo() {
        return undoStack.pop();
    }

    ProjectActions performRedo() {
        return redoStack.pop();
    }
}
