package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import javafx.scene.Node;

public class MoveComponent implements ProjectActions {
    double initialX;
    double initialY;

    double newX;
    double newY;

    private final Node COMPONENT_NODE;
    private final Component COMPONENT;
    private final Project PROJECT;

    public MoveComponent(Project currentProject, Node componentNode, Component component) {
        PROJECT = currentProject;
        COMPONENT_NODE = componentNode;
        COMPONENT = component;
    }

    @Override
    public void performAction() {
        //NOTE: Write to db first, if insert is successful, then perform action
        COMPONENT_NODE.setLayoutX(newX);
        COMPONENT_NODE.setLayoutY(newY);
        COMPONENT.setComponentX(newX);
        COMPONENT.setComponentY(newY);
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        COMPONENT_NODE.setLayoutX(initialX);
        COMPONENT_NODE.setLayoutY(initialY);
        COMPONENT.setComponentX(initialX);
        COMPONENT.setComponentY(initialY);
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }

    public void setInitialX(double initialX) {
        this.initialX = initialX;
    }

    public void setInitialY(double initialY) {
        this.initialY = initialY;
    }

    public void setNewX(double newX) {
        this.newX = newX;
    }

    public void setNewY(double newY) {
        this.newY = newY;
    }
}
