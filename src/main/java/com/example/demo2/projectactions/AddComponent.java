package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentnode.WireTerminalNode;
import com.example.demo2.componentnode.WireNode;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final Node COMPONENT_NODE;
    private final Component COMPONENT;


    public AddComponent(Project currentProject, Pane currentCanvas, Node componentNode, Component com) {
        PROJECT = currentProject;
        PROJECT_CANVAS = currentCanvas;
        COMPONENT_NODE = componentNode;
        COMPONENT = com;
    }

    @Override
    public void performAction() {
        PROJECT_CANVAS.getChildren().add(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            WireTerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            WireTerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().add(leftTerminal);
            PROJECT_CANVAS.getChildren().add(rightTerminal);
        }

        PROJECT.addComponent(COMPONENT, COMPONENT_NODE);
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        PROJECT_CANVAS.getChildren().remove(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            WireTerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            WireTerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().remove(leftTerminal);
            PROJECT_CANVAS.getChildren().remove(rightTerminal);
        }
        PROJECT.removeComponent(COMPONENT);
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
