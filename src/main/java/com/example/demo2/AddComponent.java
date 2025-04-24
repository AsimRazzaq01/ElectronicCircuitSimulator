package com.example.demo2;

import com.example.demo2.componentmodel.Component;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final Node COMPONENT_NODE;
    private final Component COMPONENT;


    AddComponent(Project currentProject, Pane currentCanvas, Node componentNode, Component com) {
        PROJECT = currentProject;
        PROJECT_CANVAS = currentCanvas;
        COMPONENT_NODE = componentNode;
        COMPONENT = com;
    }

    @Override
    public void performAction() {
        PROJECT_CANVAS.getChildren().add(COMPONENT_NODE);
        PROJECT.addComponent(COMPONENT, COMPONENT_NODE);
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        PROJECT_CANVAS.getChildren().remove(COMPONENT_NODE);
        PROJECT.removeComponent(COMPONENT);
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
