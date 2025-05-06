package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.CircuitSwitchModel;
import com.example.demo2.componentnode.CircuitSwitchNode;
import com.example.demo2.db.ConnDbOps;

public class ModifySwitchState implements ProjectActions {
    private final Project PROJECT;
    private final CircuitSwitchModel CIRCUIT_SWITCH;
    private final CircuitSwitchNode CIRCUIT_SWITCH_NODE;
    private final boolean INITIAL_STATE;
    private final boolean NEW_STATE;

    public ModifySwitchState(Project currentProject, CircuitSwitchNode node, CircuitSwitchModel component, boolean initialState, boolean newState) {
        PROJECT = currentProject;
        CIRCUIT_SWITCH_NODE = node;
        CIRCUIT_SWITCH = component;
        INITIAL_STATE = initialState;
        NEW_STATE = newState;
    }

    @Override
    public void performAction() {
        ConnDbOps.updateSwitches(CIRCUIT_SWITCH, NEW_STATE);
        CIRCUIT_SWITCH.setActive(NEW_STATE);
        CIRCUIT_SWITCH_NODE.setSwitchImageState(NEW_STATE);
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        ConnDbOps.updateSwitches(CIRCUIT_SWITCH, INITIAL_STATE);
        CIRCUIT_SWITCH.setActive(INITIAL_STATE);
        CIRCUIT_SWITCH_NODE.setSwitchImageState(INITIAL_STATE);
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
