package com.example.demo2;

import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.*;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class AddComponent implements ProjectActions {
    private final Project PROJECT;
    private final Pane PROJECT_CANVAS;
    private final Node COMPONENT_NODE;
    private final Component COMPONENT;


    AddComponent(Project currentProject, Pane currentCanvas, Node componentNode, Component com) {
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

    public static AddComponent fromComponent(Project project, Pane canvas, Component component) {
        Node node = null;

        if (component instanceof BatteryModel b) {
            node = new BatteryNode(b.getComponentX(), b.getComponentY(), b.getVoltage());
        }
        else if (component instanceof ResistorModel r) {
            node = new ResistorNode(r.getComponentX(), r.getComponentY(), r.getResistance());
        }
        else if (component instanceof LightbulbModel l) {
            node = new LightbulbNode(l.getComponentX(), l.getComponentY(), l.getResistance());
        }
        else if (component instanceof CircuitSwitchModel s) {
            node = new CircuitSwitchNode(s.getComponentX(), s.getComponentY(), s.isActive());
        }
        else if (component instanceof WireModel w) {
            node = new WireNode(w.getComponentX(), w.getComponentY(), w.getRightSideX(), w.getRightSideY());
        }

        if (node == null) {
            System.out.println("❌ Unknown component type: " + component.getComponentType());
            return null;
        }

        node.setLayoutX(component.getComponentX());
        node.setLayoutY(component.getComponentY());

        return new AddComponent(project, canvas, node, component);
    }

    public Node getNode() {
        return COMPONENT_NODE;
    }


}
