package com.example.demo2;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project project;
    private final Pane projectCanvas;
    private final Node component;


    AddComponent(Project currentProject, Pane currentCanvas, Node com) {
        project = currentProject;
        projectCanvas = currentCanvas;
        component = com;
    }

    @Override
    public void performAction() {
        projectCanvas.getChildren().add(component);
        project.addComponent(component);
        project.addToUndoStack(this);
    }

    @Override
    public void undo() {
        projectCanvas.getChildren().remove(component);
        project.removeComponent(component);
        project.addToRedoStack(this);
    }

    @Override
    public void redo() {

    }
}
