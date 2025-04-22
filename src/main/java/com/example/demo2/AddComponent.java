package com.example.demo2;

import com.example.demo2.components.Component;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project project;
    private final Pane projectCanvas;
    private final Node componentNode;
    private final Component component;


    AddComponent(Project currentProject, Pane currentCanvas, Component com) {
        project = currentProject;
        projectCanvas = currentCanvas;
        component = com;
        componentNode = com.getComponentNode();
    }

    @Override
    public void performAction() {
        projectCanvas.getChildren().add(componentNode);
        project.addComponent(component);
        project.addToUndoStack(this);
    }

    @Override
    public void undo() {
        projectCanvas.getChildren().remove(componentNode);
        project.removeComponent(component);
        project.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
