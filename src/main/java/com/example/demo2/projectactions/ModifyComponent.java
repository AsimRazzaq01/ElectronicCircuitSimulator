package com.example.demo2.projectactions;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.BatteryModel;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentmodel.LightbulbModel;
import com.example.demo2.componentmodel.ResistorModel;

public class ModifyComponent implements ProjectActions {
    private final Project PROJECT;
    private final Component COMPONENT;
    private final double INITIAL_VALUE;
    private final double NEW_VALUE;

    public ModifyComponent(Project currentProject, Component component, double initialValue, double newValue) {
        PROJECT = currentProject;
        COMPONENT = component;
        INITIAL_VALUE = initialValue;
        NEW_VALUE = newValue;
    }

    @Override
    public void performAction() {
        switch (COMPONENT.getComponentType()) {
            case "Battery" -> {
                BatteryModel batteryModel = (BatteryModel) COMPONENT;
                batteryModel.setVoltage(NEW_VALUE);
            }
            case "Resistor" -> {
                ResistorModel resistorModel = (ResistorModel) COMPONENT;
                resistorModel.setResistance(NEW_VALUE);
            }
            case "Light bulb" -> {
                LightbulbModel lightbulbModel = (LightbulbModel) COMPONENT;
                lightbulbModel.setResistance(NEW_VALUE);
            }
        }
        PROJECT.addToUndoStack(this);
    }

    @Override
    public void undo() {
        switch (COMPONENT.getComponentType()) {
            case "Battery" -> {
                BatteryModel batteryModel = (BatteryModel) COMPONENT;
                batteryModel.setVoltage(INITIAL_VALUE);
            }
            case "Resistor" -> {
                ResistorModel resistorModel = (ResistorModel) COMPONENT;
                resistorModel.setResistance(INITIAL_VALUE);
            }
            case "Light bulb" -> {
                LightbulbModel lightbulbModel = (LightbulbModel) COMPONENT;
                lightbulbModel.setResistance(INITIAL_VALUE);
            }
        }
        PROJECT.addToRedoStack(this);
    }

    @Override
    public void redo() {
        this.performAction();
    }
}
