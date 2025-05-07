package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentnode.*;
import com.example.demo2.db.ConnDbOps;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final Node COMPONENT_NODE;
    private final Component COMPONENT;
    private final boolean loadedFromDB;


    public AddComponent(Project currentProject, Pane currentCanvas, Node componentNode, Component com, boolean db) {
        PROJECT = currentProject;
        PROJECT_CANVAS = currentCanvas;
        COMPONENT_NODE = componentNode;
        COMPONENT = com;
        loadedFromDB = db;
    }

    @Override
    public void performAction() {
        //NOTE: Write to db first, if insert is successful, then perform action
        PROJECT_CANVAS.getChildren().add(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            TerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            TerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().add(leftTerminal);
            PROJECT_CANVAS.getChildren().add(rightTerminal);
            PROJECT.addToTerminalList(leftTerminal);
            PROJECT.addToTerminalList(rightTerminal);
        }

        if (COMPONENT_NODE instanceof BatteryNode) {
            TerminalNode negative =((BatteryNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((BatteryNode) COMPONENT_NODE).getPositive();
            PROJECT.addToTerminalList(negative);
            PROJECT.addToTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof CircuitSwitchNode) {
            TerminalNode negative =((CircuitSwitchNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((CircuitSwitchNode) COMPONENT_NODE).getPositive();
            PROJECT.addToTerminalList(negative);
            PROJECT.addToTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof LightbulbNode) {
            TerminalNode negative =((LightbulbNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((LightbulbNode) COMPONENT_NODE).getPositive();
            PROJECT.addToTerminalList(negative);
            PROJECT.addToTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof ResistorNode) {
            TerminalNode negative =((ResistorNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((ResistorNode) COMPONENT_NODE).getPositive();
            PROJECT.addToTerminalList(negative);
            PROJECT.addToTerminalList(positive);
        }

        ConnDbOps.saveComponent(PROJECT, COMPONENT);
        PROJECT.addComponent(COMPONENT, COMPONENT_NODE);

        if (!loadedFromDB) {
            PROJECT.addToUndoStack(this);
        }
    }

    @Override
    public void undo() {
        PROJECT_CANVAS.getChildren().remove(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode) {
            TerminalNode leftTerminal = ((WireNode) COMPONENT_NODE).getLeftTerminalNode();
            TerminalNode rightTerminal = ((WireNode) COMPONENT_NODE).getRightTerminalNode();
            PROJECT_CANVAS.getChildren().remove(leftTerminal);
            PROJECT_CANVAS.getChildren().remove(rightTerminal);
            PROJECT.removeFromTerminalList(leftTerminal);
            PROJECT.removeFromTerminalList(rightTerminal);
        }

        if (COMPONENT_NODE instanceof BatteryNode) {
            TerminalNode negative =((BatteryNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((BatteryNode) COMPONENT_NODE).getPositive();
            PROJECT.removeFromTerminalList(negative);
            PROJECT.removeFromTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof CircuitSwitchNode) {
            TerminalNode negative =((CircuitSwitchNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((CircuitSwitchNode) COMPONENT_NODE).getPositive();
            PROJECT.removeFromTerminalList(negative);
            PROJECT.removeFromTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof LightbulbNode) {
            TerminalNode negative =((LightbulbNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((LightbulbNode) COMPONENT_NODE).getPositive();
            PROJECT.removeFromTerminalList(negative);
            PROJECT.removeFromTerminalList(positive);
        }

        if (COMPONENT_NODE instanceof ResistorNode) {
            TerminalNode negative =((ResistorNode) COMPONENT_NODE).getNegative();
            TerminalNode positive = ((ResistorNode) COMPONENT_NODE).getPositive();
            PROJECT.removeFromTerminalList(negative);
            PROJECT.removeFromTerminalList(positive);
        }

        ConnDbOps.deleteComponent(COMPONENT);
        PROJECT.removeComponent(COMPONENT);
        COMPONENT.setComponentID(-1);
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
