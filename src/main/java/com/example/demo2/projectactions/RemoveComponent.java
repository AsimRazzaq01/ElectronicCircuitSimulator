package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.WireModel;
import com.example.demo2.componentnode.*;
import com.example.demo2.db.ConnDbOps;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

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
        if (COMPONENT_NODE instanceof WireNode wireNode) {
            TerminalNode negativeTerminal = wireNode.getLeftTerminalNode();
            TerminalNode positiveTerminal = wireNode.getRightTerminalNode();
            PROJECT_CANVAS.getChildren().remove(negativeTerminal);
            PROJECT_CANVAS.getChildren().remove(positiveTerminal);
            PROJECT.removeFromTerminalList(negativeTerminal);
            PROJECT.removeFromTerminalList(positiveTerminal);

            WireModel wireModel = wireNode.getWireModel();

            ArrayList<Component> wireLeftRemove = new ArrayList<>(wireModel.getNegativeSide());
            ArrayList<Component> wireRightRemove = new ArrayList<>(wireModel.getPositiveSide());

            Bounds leftTerminal = negativeTerminal.localToScene(negativeTerminal.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == negativeTerminal) {
                    continue;
                }

                if (terminalBounds.intersects(leftTerminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Positive") && !wireLeftRemove.contains(connected)) {
                        connected.addToPositiveSide(wireModel);
                        wireModel.addToNegativeSide(connected);
                    }

                    wireLeftRemove.remove(connected);
                }
            }

            for (Component positive : wireLeftRemove) {
                wireModel.getNegativeSide().remove(positive);
                positive.getPositiveSide().remove(wireModel);
                wireModel.setGroup(0);
            }

            Bounds rightTerminal = positiveTerminal.localToScene(positiveTerminal.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == positiveTerminal) {
                    continue;
                }

                if (terminalBounds.intersects(rightTerminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Negative") && !wireRightRemove.contains(connected)) {
                        connected.addToNegativeSide(wireModel);
                        wireModel.addToPositiveSide(connected);
                    }

                    wireRightRemove.remove(connected);
                }
            }

            for (Component negative : wireRightRemove) {
                wireModel.getPositiveSide().remove(negative);
                negative.getNegativeSide().remove(wireModel);
                wireModel.setGroup(0);
            }

            PROJECT.calculateCircuitGroups();
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

        ConnDbOps.deleteComponent(COMPONENT);
        PROJECT.removeComponent(COMPONENT);
        COMPONENT.setComponentID(-1);

        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        PROJECT_CANVAS.getChildren().add(COMPONENT_NODE);
        if (COMPONENT_NODE instanceof WireNode wireNode) {
            TerminalNode negativeTerminal = wireNode.getLeftTerminalNode();
            TerminalNode positiveTerminal = wireNode.getRightTerminalNode();
            PROJECT_CANVAS.getChildren().add(negativeTerminal);
            PROJECT_CANVAS.getChildren().add(positiveTerminal);
            PROJECT.addToTerminalList(negativeTerminal);
            PROJECT.addToTerminalList(positiveTerminal);

            WireModel wireModel = wireNode.getWireModel();

            ArrayList<Component> wireLeftRemove = new ArrayList<>(wireModel.getNegativeSide());
            ArrayList<Component> wireRightRemove = new ArrayList<>(wireModel.getPositiveSide());

            Bounds leftTerminal = negativeTerminal.localToScene(negativeTerminal.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == negativeTerminal) {
                    continue;
                }

                if (terminalBounds.intersects(leftTerminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Positive") && !wireLeftRemove.contains(connected)) {
                        connected.addToPositiveSide(wireModel);
                        wireModel.addToNegativeSide(connected);
                    }

                    wireLeftRemove.remove(connected);
                }
            }

            for (Component positive : wireLeftRemove) {
                wireModel.getNegativeSide().remove(positive);
                positive.getPositiveSide().remove(wireModel);
                wireModel.setGroup(0);
            }

            Bounds rightTerminal = positiveTerminal.localToScene(positiveTerminal.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == positiveTerminal) {
                    continue;
                }

                if (terminalBounds.intersects(rightTerminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Negative") && !wireRightRemove.contains(connected)) {
                        connected.addToNegativeSide(wireModel);
                        wireModel.addToPositiveSide(connected);
                    }

                    wireRightRemove.remove(connected);
                }
            }

            for (Component negative : wireRightRemove) {
                wireModel.getPositiveSide().remove(negative);
                negative.getNegativeSide().remove(wireModel);
                wireModel.setGroup(0);
            }

            ConnDbOps.saveComponent(PROJECT, COMPONENT);
            PROJECT.addComponent(COMPONENT, COMPONENT_NODE);

            PROJECT.addToRedoStack(this);

            PROJECT.calculateCircuitGroups();
            return;
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
