package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentnode.TerminalNode;
import com.example.demo2.componentnode.WireNode;
import com.example.demo2.db.ConnDbOps;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class RemoveComponent implements ProjectActions{
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final Node COMPONENT_NODE;
    private final Component COMPONENT;


    public RemoveComponent(Project currentProject, Pane currentCanvas, Node componentNode, Component com) {
        PROJECT = currentProject;
        PROJECT_CANVAS = currentCanvas;
        COMPONENT_NODE = componentNode;
        COMPONENT = com;
    }

    @Override
    public void performAction() {
        PROJECT_CANVAS.getChildren().remove(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            TerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            TerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().remove(leftTerminal);
            PROJECT_CANVAS.getChildren().remove(rightTerminal);
        }
        ConnDbOps.deleteComponent(COMPONENT);
        PROJECT.removeComponent(COMPONENT);
        COMPONENT.setComponentID(-1);

        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        PROJECT_CANVAS.getChildren().add(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            TerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            TerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().add(leftTerminal);
            PROJECT_CANVAS.getChildren().add(rightTerminal);
        }

        ConnDbOps.saveComponent(PROJECT, COMPONENT);
        PROJECT.addComponent(COMPONENT, COMPONENT_NODE);

        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
