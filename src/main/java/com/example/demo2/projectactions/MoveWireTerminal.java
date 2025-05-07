package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.TerminalModel;
import com.example.demo2.componentmodel.WireModel;
import com.example.demo2.componentnode.TerminalNode;
import com.example.demo2.componentnode.WireNode;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class MoveWireTerminal implements ProjectActions {
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final WireNode WIRE_NODE;
    private final WireModel WIRE_MODEL;
    private final TerminalNode TERMINAL_NODE;
    private final TerminalModel TERMINAL_MODEL;

    private double initialTerminalX;
    private double initialTerminalY;
    private double initialWireSideX;
    private double initialWireSideY;

    private double newTerminalX;
    private double newTerminalY;
    private double newWireSideX;
    private double newWireSideY;

    public MoveWireTerminal(Project currentProject, Pane currentCanvas, WireNode componentNode, TerminalNode terminalNode) {
        PROJECT = currentProject;
        PROJECT_CANVAS = currentCanvas;
        WIRE_NODE = componentNode;
        WIRE_MODEL = WIRE_NODE.getWireModel();
        TERMINAL_NODE = terminalNode;
        TERMINAL_MODEL = TERMINAL_NODE.getTerminalModel();
    }

    @Override
    public void performAction() {
        //NOTE: Write to db first, if insert is successful, then perform action
        if (TERMINAL_MODEL.getCharge().equals("Negative")){
            TERMINAL_NODE.setTerminalX(newTerminalX);
            TERMINAL_NODE.setTerminalY(newTerminalY);
            WIRE_NODE.setStartX(newWireSideX);
            WIRE_NODE.setStartY(newWireSideY);
            WIRE_MODEL.setComponentX(newWireSideX - 12.5);
            WIRE_MODEL.setComponentY(newWireSideY - 12.5);

            ArrayList<Component> wireLeftRemove = new ArrayList<>(WIRE_MODEL.getNegativeSide());

            Bounds terminal = TERMINAL_NODE.localToScene(TERMINAL_NODE.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == TERMINAL_NODE) {
                    continue;
                }

                if (terminalBounds.intersects(terminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Positive") && !wireLeftRemove.contains(connected)) {
                        connected.addToPositiveSide(WIRE_MODEL);
                        WIRE_MODEL.addToNegativeSide(connected);
                    }

                    wireLeftRemove.remove(connected);
                }
            }

            for (Component positive : wireLeftRemove) {
                WIRE_MODEL.getNegativeSide().remove(positive);
                positive.getPositiveSide().remove(WIRE_MODEL);
                WIRE_MODEL.setGroup(0);
            }

            PROJECT.calculateCircuitGroups();
        }
        else {
            TERMINAL_NODE.setTerminalX(newTerminalX);
            TERMINAL_NODE.setTerminalY(newTerminalY);
            WIRE_NODE.setEndX(newWireSideX);
            WIRE_NODE.setEndY(newWireSideY);
            WIRE_MODEL.setRightSideX(newWireSideX + 12.5);
            WIRE_MODEL.setRightSideY(newWireSideY - 12.5);

            ArrayList<Component> wireRightRemove = new ArrayList<>(WIRE_MODEL.getPositiveSide());

            Bounds terminal = TERMINAL_NODE.localToScene(TERMINAL_NODE.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == TERMINAL_NODE) {
                    continue;
                }

                if (terminalBounds.intersects(terminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Negative") && !wireRightRemove.contains(connected)) {
                        connected.addToNegativeSide(WIRE_MODEL);
                        WIRE_MODEL.addToPositiveSide(connected);
                    }

                    wireRightRemove.remove(connected);
                }
            }

            for (Component negative : wireRightRemove) {
                WIRE_MODEL.getPositiveSide().remove(negative);
                negative.getNegativeSide().remove(WIRE_MODEL);
                WIRE_MODEL.setGroup(0);
            }

            PROJECT.calculateCircuitGroups();
        }

        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        if (TERMINAL_MODEL.getCharge().equals("Negative")){
            TERMINAL_NODE.setTerminalX(initialTerminalX);
            TERMINAL_NODE.setTerminalY(initialTerminalY);
            WIRE_NODE.setStartX(initialWireSideX);
            WIRE_NODE.setStartY(initialWireSideY);
            WIRE_MODEL.setComponentX(initialWireSideX - 12.5);
            WIRE_MODEL.setComponentY(initialWireSideY - 12.5);

            ArrayList<Component> wireLeftRemove = new ArrayList<>(WIRE_MODEL.getNegativeSide());

            Bounds terminal = TERMINAL_NODE.localToScene(TERMINAL_NODE.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == TERMINAL_NODE) {
                    continue;
                }

                if (terminalBounds.intersects(terminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Positive") && !wireLeftRemove.contains(connected)) {
                        connected.addToPositiveSide(WIRE_MODEL);
                        WIRE_MODEL.addToNegativeSide(connected);
                    }

                    wireLeftRemove.remove(connected);
                }
            }

            for (Component positive : wireLeftRemove) {
                WIRE_MODEL.getNegativeSide().remove(positive);
                positive.getPositiveSide().remove(WIRE_MODEL);
                WIRE_MODEL.setGroup(0);
            }

            PROJECT.calculateCircuitGroups();
        }
        else {
            TERMINAL_NODE.setTerminalX(initialTerminalX);
            TERMINAL_NODE.setTerminalY(initialTerminalY);
            WIRE_NODE.setEndX(initialWireSideX);
            WIRE_NODE.setEndY(initialWireSideY);
            WIRE_MODEL.setRightSideX(initialWireSideX + 12.5);
            WIRE_MODEL.setRightSideY(initialWireSideY - 12.5);

            ArrayList<Component> wireRightRemove = new ArrayList<>(WIRE_MODEL.getPositiveSide());

            Bounds terminal = TERMINAL_NODE.localToScene(TERMINAL_NODE.getBoundsInLocal());
            for (TerminalNode t : PROJECT.getTerminalList()) {
                Bounds terminalBounds = t.localToScene(t.getBoundsInLocal());
                if (t == TERMINAL_NODE) {
                    continue;
                }

                if (terminalBounds.intersects(terminal)) {
                    Component connected = t.getTerminalModel().getParent();
                    String charge = t.getTerminalModel().getCharge();
                    if (charge.equals("Negative") && !wireRightRemove.contains(connected)) {
                        connected.addToNegativeSide(WIRE_MODEL);
                        WIRE_MODEL.addToPositiveSide(connected);
                    }

                    wireRightRemove.remove(connected);
                }
            }

            for (Component negative : wireRightRemove) {
                WIRE_MODEL.getPositiveSide().remove(negative);
                negative.getNegativeSide().remove(WIRE_MODEL);
                WIRE_MODEL.setGroup(0);
            }

            PROJECT.calculateCircuitGroups();
        }

        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }

    public void setInitialTerminalX(double initialTerminalX) {
        this.initialTerminalX = initialTerminalX;
    }

    public void setInitialTerminalY(double initialTerminalY) {
        this.initialTerminalY = initialTerminalY;
    }

    public void setInitialWireSideX(double initialWireSideX) {
        this.initialWireSideX = initialWireSideX;
    }

    public void setInitialWireSideY(double initialWireSideY) {
        this.initialWireSideY = initialWireSideY;
    }

    public void setNewTerminalX(double newTerminalX) {
        this.newTerminalX = newTerminalX;
    }

    public void setNewTerminalY(double newTerminalY) {
        this.newTerminalY = newTerminalY;
    }

    public void setNewWireSideX(double newWireSideX) {
        this.newWireSideX = newWireSideX;
    }

    public void setNewWireSideY(double newWireSideY) {
        this.newWireSideY = newWireSideY;
    }
}
