package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.WireModel;
import com.example.demo2.componentnode.TerminalNode;
import com.example.demo2.componentnode.WireNode;

public class MoveWire implements ProjectActions {
    private final WireNode WIRE_NODE;
    private final WireModel WIRE_MODEL;
    private final Project PROJECT;
    private final TerminalNode NEGATIVE;
    private final TerminalNode POSITIVE;

    double initialStartX;
    double initialStartY;
    double initialEndX;
    double initialEndY;
    double initialNegativeX;
    double initialNegativeY;
    double initialPositiveX;
    double initialPositiveY;

    double newStartX;
    double newStartY;
    double newEndX;
    double newEndY;
    double newNegativeX;
    double newNegativeY;
    double newPositiveX;
    double newPositiveY;

    public MoveWire(Project currentProject, WireNode componentNode, TerminalNode negative, TerminalNode positive) {
        PROJECT = currentProject;
        WIRE_NODE = componentNode;
        WIRE_MODEL = WIRE_NODE.getWireModel();
        NEGATIVE = negative;
        POSITIVE = positive;
    }

    @Override
    public void performAction() {
        //NOTE: Write to db first, if insert is successful, then perform action
        WIRE_NODE.setStartX(newStartX);
        WIRE_NODE.setStartY(newStartY);
        WIRE_NODE.setEndX(newEndX);
        WIRE_NODE.setEndY(newEndY);

        NEGATIVE.setTerminalX(newNegativeX);
        NEGATIVE.setTerminalY(newNegativeY);

        POSITIVE.setTerminalX(newPositiveX);
        POSITIVE.setTerminalY(newPositiveY);

        WIRE_MODEL.setComponentX(newStartX - 12.5);
        WIRE_MODEL.setComponentY(newStartY - 12.5);
        WIRE_MODEL.setRightSideX(newEndX + 12.5);
        WIRE_MODEL.setRightSideY(newEndY - 12.5);
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        WIRE_NODE.setStartX(initialStartX);
        WIRE_NODE.setStartY(initialStartY);
        WIRE_NODE.setEndX(initialEndX);
        WIRE_NODE.setEndY(initialEndY);

        NEGATIVE.setTerminalX(initialNegativeX);
        NEGATIVE.setTerminalY(initialNegativeY);

        POSITIVE.setTerminalX(initialPositiveX);
        POSITIVE.setTerminalY(initialPositiveY);

        WIRE_MODEL.setComponentX(initialStartX - 12.5);
        WIRE_MODEL.setComponentY(initialStartY - 12.5);
        WIRE_MODEL.setRightSideX(initialEndX + 12.5);
        WIRE_MODEL.setRightSideY(initialEndY - 12.5);

        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }

    public void setInitialStartX(double initialStartX) {
        this.initialStartX = initialStartX;
    }

    public void setInitialStartY(double initialStartY) {
        this.initialStartY = initialStartY;
    }

    public void setInitialEndX(double initialEndX) {
        this.initialEndX = initialEndX;
    }

    public void setInitialEndY(double initialEndY) {
        this.initialEndY = initialEndY;
    }

    public void setNewStartX(double newStartX) {
        this.newStartX = newStartX;
    }

    public void setNewStartY(double newStartY) {
        this.newStartY = newStartY;
    }

    public void setNewEndX(double newEndX) {
        this.newEndX = newEndX;
    }

    public void setNewEndY(double newEndY) {
        this.newEndY = newEndY;
    }

    public void setInitialNegativeX(double initialNegativeX) {
        this.initialNegativeX = initialNegativeX;
    }

    public void setInitialNegativeY(double initialNegativeY) {
        this.initialNegativeY = initialNegativeY;
    }

    public void setInitialPositiveX(double initialPositiveX) {
        this.initialPositiveX = initialPositiveX;
    }

    public void setInitialPositiveY(double initialPositiveY) {
        this.initialPositiveY = initialPositiveY;
    }

    public void setNewNegativeX(double newNegativeX) {
        this.newNegativeX = newNegativeX;
    }

    public void setNewNegativeY(double newNegativeY) {
        this.newNegativeY = newNegativeY;
    }

    public void setNewPositiveX(double newPositiveX) {
        this.newPositiveX = newPositiveX;
    }

    public void setNewPositiveY(double newPositiveY) {
        this.newPositiveY = newPositiveY;
    }
}
