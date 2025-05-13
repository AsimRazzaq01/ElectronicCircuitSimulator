package com.example.demo2;

import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.*;
import com.example.demo2.db.ConnDbOps;
import com.example.demo2.projectactions.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProjectController {
    @FXML
    private ScrollPane canvasScrollPane;
    @FXML
    private Pane canvasPane;
    @FXML
    private Button zoomOutButton;
    @FXML
    private Label projectNameLabel;
    @FXML
    private Label currentZoomLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button zoomInButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button homeButton;
    @FXML
    private ImageView wireImageView;
    @FXML
    private ImageView batteryImageView;
    @FXML
    private ImageView resistorImageView;
    @FXML
    private ImageView switchImageView;
    @FXML
    private ImageView lightbulbImageView;
    @FXML
    private TextField currentField;
    @FXML
    private TextField resistanceField;
    @FXML
    private TextField voltageField;
    @FXML
    private Button startButton;
    @FXML
    private HBox topButtonBar;

    private double zoomScale = 1.0;
    private Project currentProject;

    void setCurrentProject(Project project) {
        currentProject = project;
        HashMap<Component, Node> components = ConnDbOps.loadComponentsForProject(project.getProjectID());

        for (Map.Entry<Component, Node> entry : components.entrySet()) {
            Component component = entry.getKey();
            Node componentNode = entry.getValue();
            AddComponent addComponent;
            switch (component.getComponentType()) {
                case "Battery", "Resistor", "Light bulb", "Switch" -> {
                    addComponent = new AddComponent(currentProject, canvasPane, componentNode, component, true);
                    addComponent.performAction();

                    adjustComponentZoomScale(zoomScale);
                    makeDraggable(componentNode, component);
                }
                case "Wire" -> {
                    addComponent = new AddComponent(currentProject, canvasPane, componentNode, component, true);
                    addComponent.performAction();

                    WireNode wireNode = (WireNode) componentNode;

                    adjustComponentZoomScale(zoomScale);
                    makeWireDraggable(wireNode);
                    makeWireTerminalDraggable(wireNode.getLeftTerminalNode(), wireNode.getRightTerminalNode(), wireNode);
                }
            }
        }

        projectNameLabel.setText(project.getProjectName());
    }

    @FXML
    protected void returnHome() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPage.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root, 1280, 720); // width: 680, height: 400
        stage.setScene(scene);
        ThemeManager.applySavedTheme(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void loadComponentPaneImages() {
        URL batteryImagePath = this.getClass().getResource("component_sprites/battery.png");
        URL resistorImagePath = this.getClass().getResource("component_sprites/resistor_default.png");
        URL switchImagePath = this.getClass().getResource("component_sprites/switch_closed.png");
        URL lightbulbImagePath = Project.class.getResource("component_sprites/lightbulb.png");
        URL wireImagePath = Project.class.getResource("component_sprites/wire.png");

        if (batteryImagePath != null) {
            Image batteryImage = new Image(batteryImagePath.toExternalForm(), 500, 0, true, false);
            batteryImageView.setImage(batteryImage);
        }

        if (resistorImagePath != null) {
            Image resistorImage = new Image(resistorImagePath.toExternalForm(), 500, 0, true, false);
            resistorImageView.setImage(resistorImage);
        }

        if (switchImagePath != null) {
            Image switchImage = new Image(switchImagePath.toExternalForm(), 500, 0, true, false);
            switchImageView.setImage(switchImage);
            //switchImageView.setSmooth(false);
        }

        if (lightbulbImagePath != null) {
            Image lightbulbImage = new Image(lightbulbImagePath.toExternalForm(), 500, 0, true, false);
            lightbulbImageView.setImage(lightbulbImage);
        }

        if (wireImagePath != null) {
            Image wireImage = new Image(wireImagePath.toExternalForm(), 500, 0, true, false);
            wireImageView.setImage(wireImage);
        }
    }

    private void allowDragAndDrop() {
        wireImageView.setOnDragDetected(mouseEvent -> {
            Dragboard wireDragboard = wireImageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("wire");
            wireDragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        batteryImageView.setOnDragDetected(mouseEvent -> {
            Dragboard batteryDragboard = batteryImageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("battery");
            batteryDragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        resistorImageView.setOnDragDetected(mouseEvent -> {
            Dragboard resistorDragboard = resistorImageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("resistor");
            resistorDragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        switchImageView.setOnDragDetected(mouseEvent -> {
            Dragboard switchDragboard = switchImageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent switchClipboardContent = new ClipboardContent();
            switchClipboardContent.putString("switch");
            switchDragboard.setContent(switchClipboardContent);
            mouseEvent.consume();
        });

        lightbulbImageView.setOnDragDetected(mouseEvent -> {
            Dragboard lightbulbDragboard = lightbulbImageView.startDragAndDrop(TransferMode.COPY);
            ClipboardContent lightbulbClipboardContent = new ClipboardContent();
            lightbulbClipboardContent.putString("lightbulb");
            lightbulbDragboard.setContent(lightbulbClipboardContent);
            mouseEvent.consume();
        });

        canvasPane.setOnDragOver(mouseEvent -> {
            if (mouseEvent.getGestureSource() != canvasPane && mouseEvent.getDragboard().hasString()) {
                mouseEvent.acceptTransferModes(TransferMode.COPY);
            }

            mouseEvent.consume();
        });

        canvasPane.setOnDragDropped(mouseEvent -> {
            Dragboard dragboard = mouseEvent.getDragboard();
            boolean success = false;
            if (dragboard.hasString()) {
                String dragData = dragboard.getString();
                switch (dragData) {
                    case "battery" -> addBattery(mouseEvent.getX(), mouseEvent.getY());
                    case "resistor" -> addResistor(mouseEvent.getX(), mouseEvent.getY());
                    case "switch" -> addCircuitSwitch(mouseEvent.getX(), mouseEvent.getY());
                    case "lightbulb" -> addLightbulb(mouseEvent.getX(), mouseEvent.getY());
                    case "wire" -> addWire(mouseEvent.getX(), mouseEvent.getY());
                }
                success = true;
            }

            mouseEvent.setDropCompleted(success);
            mouseEvent.consume();
            canvasScrollPane.requestFocus();
            canvasScrollPane.setPannable(true);
        });
    }

    @FXML
    public void initialize() {
        ThemeManager.getTheme();

        //Project data from database will be passed to the project view screen
        loadComponentPaneImages();
        allowDragAndDrop();
        undoButton.setDisable(true);
        redoButton.setDisable(true);
    }

    @FXML
    protected void logout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginRegister.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        Scene scene = new Scene(root, 1200, 720); // width: 680, height: 400
        stage.setScene(scene);
        ThemeManager.applySavedTheme(scene); // 👈 Restore the saved theme
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    protected void handleSaveButton() {
        if (currentProject == null) {
            System.out.println("Project is null!");
            return;
        }

        int count = 0;

        for (Map.Entry<Component, Node> entry : currentProject.getProjectComponents().entrySet()) {
            Component model = entry.getKey();
            Node view = entry.getValue();

            if (model instanceof WireModel wireModel && view instanceof WireNode wireNode) {
                // Update start and end points for wire
                wireModel.setComponentX(wireNode.getStartX() - 12.5);
                wireModel.setComponentY(wireNode.getStartY() - 12.5);
                wireModel.setRightSideX(wireNode.getEndX() + 12.5);
                wireModel.setRightSideY(wireNode.getEndY() - 12.5);

            } else {
                // Use layoutX/Y for normal components
                model.setComponentX(view.getLayoutX());
                model.setComponentY(view.getLayoutY());
            }

            // Save after model update
            ConnDbOps.saveComponent(currentProject, model);
            count++;
        }

        System.out.println("Success: " + count + " components attempted to save.");

        showAlert("Success", count + " components were saved successfully!");
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void adjustComponentZoomScale(double zoomScale) {
        Component.zoomScale = zoomScale;
    }

    @FXML
    void zoomIn() {
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / zoomScale;
        double oldCenterY = getViewportCenterY() / zoomScale;

        //Zoom in the pane
        zoomScale += 0.1;
        applyZoom (viewportWidth, viewportHeight, canvasWidth, canvasHeight, oldCenterX, oldCenterY);

        if (zoomScale >= 1.5) {
            zoomInButton.setDisable(true);
        }
        else {
            zoomOutButton.setDisable(false);
        }
    }

    @FXML
    void zoomOut() {
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();
        double canvasWidth = canvasPane.getLayoutBounds().getWidth();
        double canvasHeight = canvasPane.getLayoutBounds().getHeight();

        double oldCenterX = getViewportCenterX() / zoomScale;
        double oldCenterY = getViewportCenterY() / zoomScale;

        //Zoom out of the pane
        zoomScale -= 0.1;
        applyZoom (viewportWidth, viewportHeight, canvasWidth, canvasHeight, oldCenterX, oldCenterY);

        if (zoomScale < 0.6) {
            zoomOutButton.setDisable(true);
        }
        else {
            zoomInButton.setDisable(false);
        }
    }

    private void applyZoom (double viewportWidth, double viewportHeight, double canvasWidth, double canvasHeight, double oldCenterX, double oldCenterY) {
        canvasPane.setScaleX(zoomScale);
        canvasPane.setScaleY(zoomScale);
        adjustComponentZoomScale(zoomScale);

        Platform.runLater(() -> {
            double scaledCenterX = oldCenterX * zoomScale;
            double scaledCenterY = oldCenterY * zoomScale;

            //Calculates the original center of viewport when zoom scale is 1.0
            double originalCenterX = scaledCenterX / zoomScale;
            double originalCenterY = scaledCenterY / zoomScale;

            //The new center is the original center coordinates multiplied by the new scale
            double newCenterX = originalCenterX * zoomScale;
            double newCenterY = originalCenterY * zoomScale;

            double hValue = (newCenterX - viewportWidth / 2.0) / ((canvasWidth * zoomScale) - viewportWidth);
            double vValue = (newCenterY - viewportHeight / 2.0) / ((canvasHeight * zoomScale) - viewportHeight);

            //Force a layout update
            canvasScrollPane.layout();

            canvasScrollPane.setHvalue(hValue);
            canvasScrollPane.setVvalue(vValue);

            currentZoomLabel.setText(((int)(100* zoomScale)) + "%");
        });
    }

    private double getViewportCenterX() {
        double contentWidth = canvasScrollPane.getContent().getBoundsInLocal().getWidth();
        double viewportWidth = canvasScrollPane.getViewportBounds().getWidth();

        double hValue = canvasScrollPane.getHvalue();

        double leftEdge = hValue * (contentWidth - viewportWidth);
        double rightEdge = leftEdge + viewportWidth;

        return (rightEdge + leftEdge) / 2.0;
    }

    private double getViewportCenterY() {
        double contentHeight = canvasScrollPane.getContent().getBoundsInLocal().getHeight();
        double viewportHeight = canvasScrollPane.getViewportBounds().getHeight();

        double vValue = canvasScrollPane.getVvalue();
        double topEdge = vValue * (contentHeight - viewportHeight);
        double bottomEdge = topEdge + viewportHeight;

        return (bottomEdge + topEdge) / 2.0;
    }

    @FXML
    public void undo() {
        if (!currentProject.getUndoStack().isEmpty()) {
            undoButton.setDisable(false);
            ProjectActions action = currentProject.performUndo();
            action.undo();
        }

        if (!currentProject.getRedoStack().isEmpty()) {
            redoButton.setDisable(false);
        }

        if (currentProject.getUndoStack().isEmpty()) {
            undoButton.setDisable(true);
        }
    }

    @FXML
    public void redo() {
        if (!currentProject.getRedoStack().isEmpty()) {
            redoButton.setDisable(false);
            ProjectActions action = currentProject.performRedo();
            action.redo();
        }

        if (!currentProject.getUndoStack().isEmpty()) {
            undoButton.setDisable(false);
        }

        if (currentProject.getRedoStack().isEmpty()) {
            redoButton.setDisable(true);
        }
    }

    public void makeDraggable(Node componentNode, Component component) {
        Node canvas = canvasScrollPane.getContent();
        //Needs to be a different memory address for every time component is dragged
        final MoveComponent[] moveComponent = new MoveComponent[1];

        componentNode.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                //Gets the coordinates of the cursor within the canvasPane
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                moveComponent[0] = new MoveComponent(currentProject, componentNode, component);

                moveComponent[0].setInitialX(component.getComponentX());
                moveComponent[0].setInitialY(component.getComponentY());

                component.setComponentX((cursorInPane.getX() / zoomScale) - componentNode.getLayoutX());
                component.setComponentY((cursorInPane.getY() / zoomScale) - componentNode.getLayoutY());
                canvasScrollPane.setPannable(false);
                componentNode.toFront();

                switch(component.getComponentType()) {
                    case "Battery" -> {
                        BatteryModel b = (BatteryModel) component;
                        voltageField.setText(String.valueOf(b.getVoltage()));
                        HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                        currentField.setText(String.valueOf(circuitGroups.get(b.getGroup())));
                        resistanceField.setText("null");
                    }
                    case "Resistor" -> {
                        ResistorModel r = (ResistorModel) component;
                        resistanceField.setText(String.valueOf(r.getResistance()));
                        HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                        currentField.setText(String.valueOf(circuitGroups.get(r.getGroup())));
                        voltageField.setText("null");
                    }
                    case "Light bulb" -> {
                        LightbulbModel l = (LightbulbModel) component;
                        resistanceField.setText(String.valueOf(l.getResistance()));
                        HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                        currentField.setText(String.valueOf(circuitGroups.get(l.getGroup())));
                        voltageField.setText("null");
                    }
                    case "Switch" -> {
                        voltageField.setText("null");
                        resistanceField.setText("null");
                        currentField.setText("null");
                    }
                }

            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                openComponentMenu(componentNode, component);
            }
        });

        componentNode.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                double potentialNewX = (cursorInPane.getX() / zoomScale) - component.getComponentX();
                double potentialNewY = (cursorInPane.getY() / zoomScale) - component.getComponentY();

                //The position of the component can only be in 0 and 1528 for x and 0 and 842.5 for y
                //The new coordinate values are calculated by getting the maximum value of 0 and the minimum value of the new calculated coordinates and the maximum possible value
                //Maximum value is determined by the size of the canvas - the size of the component
                double newComponentX = Math.max(0, Math.min(potentialNewX, (canvasPane.getPrefWidth() - componentNode.getLayoutBounds().getWidth() - 1)));
                double newComponentY = Math.max(0, Math.min(potentialNewY, (canvasPane.getPrefHeight() - componentNode.getLayoutBounds().getHeight())));

                //Set the new position of the component
                componentNode.setLayoutX(newComponentX);
                componentNode.setLayoutY(newComponentY);
            }
        });

        componentNode.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                canvasScrollPane.setPannable(true);

                moveComponent[0].setNewX(componentNode.getLayoutX());
                moveComponent[0].setNewY(componentNode.getLayoutY());

                moveComponent[0].performAction();

                //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
                if (!currentProject.getRedoStack().isEmpty()) {
                    currentProject.clearRedoStack();
                    redoButton.setDisable(true);
                }

                undoButton.setDisable(false);
            }
        });
    }

    public void makeWireDraggable(WireNode wire) {
        Node canvas = canvasScrollPane.getContent();
        TerminalNode leftTerminal = wire.getLeftTerminalNode();
        TerminalNode rightTerminal = wire.getRightTerminalNode();

        final Point2D[] cursorToStartOffset = new Point2D[1];
        final Point2D[] cursorToEndOffset = new Point2D[1];

        final MoveWire[] moveWire = new MoveWire[1];

        wire.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                moveWire[0] = new MoveWire(currentProject, wire, leftTerminal, rightTerminal);

                moveWire[0].setInitialStartX(wire.getStartX());
                moveWire[0].setInitialStartY(wire.getStartY());
                moveWire[0].setInitialEndX(wire.getEndX());
                moveWire[0].setInitialEndY(wire.getEndY());

                moveWire[0].setInitialNegativeX(leftTerminal.getCenterX());
                moveWire[0].setInitialNegativeY(leftTerminal.getCenterY());

                moveWire[0].setInitialPositiveX(rightTerminal.getCenterX());
                moveWire[0].setInitialPositiveY(rightTerminal.getCenterY());

                double cursorX = cursorInPane.getX() / zoomScale;
                double cursorY = cursorInPane.getY() / zoomScale;

                //Stores offset from cursor to both endpoints, coordinates within the wire
                cursorToStartOffset[0] = new Point2D(cursorX - wire.getStartX(), cursorY - wire.getStartY());
                cursorToEndOffset[0] = new Point2D(cursorX - wire.getEndX(), cursorY - wire.getEndY());

                canvasScrollPane.setPannable(false);
                wire.toFront();
                leftTerminal.toFront();
                rightTerminal.toFront();

                HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                voltageField.setText("null");
                currentField.setText(String.valueOf(circuitGroups.get(wire.getWireModel().getGroup())));
                resistanceField.setText("null");
            }
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                openComponentMenu(wire, wire.getWireModel());
            }
        });

        wire.setOnMouseDragged(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && cursorToStartOffset[0] != null && cursorToEndOffset[0] != null) {
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                double cursorX = cursorInPane.getX() / zoomScale;
                double cursorY = cursorInPane.getY() / zoomScale;

                //Calculates the potential position of the wire based of the new location of the cursor as the wire is being dragged
                double potentialStartX = cursorX - cursorToStartOffset[0].getX();
                double potentialStartY = cursorY - cursorToStartOffset[0].getY();
                double potentialEndX = cursorX - cursorToEndOffset[0].getX();
                double potentialEndY = cursorY - cursorToEndOffset[0].getY();

                double minX = Math.min(potentialStartX, potentialEndX);
                double minY = Math.min(potentialStartY, potentialEndY);
                double maxX = Math.max(potentialStartX, potentialEndX);
                double maxY = Math.max(potentialStartY, potentialEndY);

                double offsetX = 0;
                double offsetY = 0;

                //12.5 is the minimum value that makes it so the line is still in the canvas
                if (minX < 12.5) {
                    offsetX = 12.5 - minX;
                }
                if (maxX > canvasPane.getPrefWidth() - 12.5) {
                    offsetX = (canvasPane.getPrefWidth() - 12.5) - maxX;
                }
                if (minY < 12.5) {
                    offsetY = 12.5 - minY;
                }
                if (maxY > canvasPane.getPrefHeight() - 12.5) {
                    offsetY = (canvasPane.getPrefHeight() - 12.5) - maxY;
                }

                //The new position of the wire is the potential coordinate value + the calculated offset
                wire.setStartX(potentialStartX + offsetX);
                leftTerminal.setTerminalX(potentialStartX + offsetX);
                wire.setStartY(potentialStartY + offsetY);
                leftTerminal.setTerminalY(potentialStartY + offsetY);
                wire.setEndX(potentialEndX + offsetX);
                rightTerminal.setTerminalX(potentialEndX + offsetX);
                wire.setEndY(potentialEndY + offsetY);
                rightTerminal.setTerminalY(potentialEndY + offsetY);
            }
        });

        wire.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                canvasScrollPane.setPannable(true);

                moveWire[0].setNewStartX(wire.getStartX());
                moveWire[0].setNewStartY(wire.getStartY());
                moveWire[0].setNewEndX(wire.getEndX());
                moveWire[0].setNewEndY(wire.getEndY());

                moveWire[0].setNewNegativeX(leftTerminal.getCenterX());
                moveWire[0].setNewNegativeY(leftTerminal.getCenterY());

                moveWire[0].setNewPositiveX(rightTerminal.getCenterX());
                moveWire[0].setNewPositiveY(rightTerminal.getCenterY());

                moveWire[0].performAction();

                //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
                if (!currentProject.getRedoStack().isEmpty()) {
                    currentProject.clearRedoStack();
                    redoButton.setDisable(true);
                }

                undoButton.setDisable(false);
            }
        });
    }

    public void makeWireTerminalDraggable(TerminalNode leftTerminal, TerminalNode rightTerminal, WireNode wire) {
        Node canvas = canvasScrollPane.getContent();

        final MoveWireTerminal[] leftSide = new MoveWireTerminal[1];
        final MoveWireTerminal[] rightSide = new MoveWireTerminal[1];

        final Point2D[] cursorLeftTerminalOffset = new Point2D[1];
        final Point2D[] cursorRightTerminalOffset = new Point2D[1];

        leftTerminal.setOnMousePressed(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            leftSide[0] = new MoveWireTerminal(currentProject, canvasPane, wire, leftTerminal);

            leftSide[0].setInitialTerminalX(leftTerminal.getCenterX());
            leftSide[0].setInitialTerminalY(leftTerminal.getCenterY());

            leftSide[0].setInitialWireSideX(wire.getStartX());
            leftSide[0].setInitialWireSideY(wire.getStartY());

            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            //Stores offset from cursor to both endpoints, coordinates within the wire terminal
            cursorLeftTerminalOffset[0] = new Point2D(leftTerminal.getCenterX() - cursorX, leftTerminal.getCenterY() - cursorY);
            canvasScrollPane.setPannable(false);

            wire.toFront();
            leftTerminal.toFront();
            rightTerminal.toFront();

            HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
            voltageField.setText("null");
            currentField.setText(String.valueOf(circuitGroups.get(wire.getWireModel().getGroup())));
            resistanceField.setText("null");
        });

        leftTerminal.setOnMouseDragged(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            double potentialCenterX = cursorX + cursorLeftTerminalOffset[0].getX();
            double potentialCenterY = cursorY + cursorLeftTerminalOffset[0].getY();

            double offsetX = 0;
            double offsetY = 0;

            //12.5 is the minimum value that makes it so the line is still in the canvas
            if (potentialCenterX < 12.5) {
                offsetX = 12.5 - potentialCenterX;
            }
            if (potentialCenterX > canvasPane.getPrefWidth() - 12.5) {
                offsetX = (canvasPane.getPrefWidth() - 12.5) - potentialCenterX;
            }
            if (potentialCenterY < 12.5) {
                offsetY = 12.5 - potentialCenterY;
            }
            if (potentialCenterY > canvasPane.getPrefHeight() - 12.5) {
                offsetY = (canvasPane.getPrefHeight() - 12.5) - potentialCenterY;
            }

            leftTerminal.setTerminalX(potentialCenterX + offsetX);
            leftTerminal.setTerminalY(potentialCenterY + offsetY);
            wire.setStartX(potentialCenterX + offsetX);
            wire.setStartY(potentialCenterY + offsetY);
        });

        leftTerminal.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                canvasScrollPane.setPannable(true);

                leftSide[0].setNewTerminalX(leftTerminal.getCenterX());
                leftSide[0].setNewTerminalY(leftTerminal.getCenterY());

                leftSide[0].setNewWireSideX(wire.getStartX());
                leftSide[0].setNewWireSideY(wire.getStartY());

                leftSide[0].performAction();

                //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
                if (!currentProject.getRedoStack().isEmpty()) {
                    currentProject.clearRedoStack();
                    redoButton.setDisable(true);
                }

                undoButton.setDisable(false);

                HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                voltageField.setText("null");
                currentField.setText(String.valueOf(circuitGroups.get(wire.getWireModel().getGroup())));
                resistanceField.setText("null");
            }
        });

        rightTerminal.setOnMousePressed(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            rightSide[0] = new MoveWireTerminal(currentProject, canvasPane, wire, rightTerminal);

            rightSide[0].setInitialTerminalX(rightTerminal.getCenterX());
            rightSide[0].setInitialTerminalY(rightTerminal.getCenterY());

            rightSide[0].setInitialWireSideX(wire.getEndX());
            rightSide[0].setInitialWireSideY(wire.getEndY());

            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            //Stores offset from cursor to both endpoints, coordinates within the wire terminal
            cursorRightTerminalOffset[0] = new Point2D(rightTerminal.getCenterX() - cursorX, rightTerminal.getCenterY() - cursorY);
            canvasScrollPane.setPannable(false);

            wire.toFront();
            leftTerminal.toFront();
            rightTerminal.toFront();

            HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
            voltageField.setText("null");
            currentField.setText(String.valueOf(circuitGroups.get(wire.getWireModel().getGroup())));
            resistanceField.setText("null");
        });

        rightTerminal.setOnMouseDragged(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            double potentialCenterX = cursorX + cursorRightTerminalOffset[0].getX();
            double potentialCenterY = cursorY + cursorRightTerminalOffset[0].getY();

            double offsetX = 0;
            double offsetY = 0;

            //12.5 is the minimum value that makes it so the line is still in the canvas
            if (potentialCenterX < 12.5) {
                offsetX = 12.5 - potentialCenterX;
            }
            if (potentialCenterX > canvasPane.getPrefWidth() - 12.5) {
                offsetX = (canvasPane.getPrefWidth() - 12.5) - potentialCenterX;
            }
            if (potentialCenterY < 12.5) {
                offsetY = 12.5 - potentialCenterY;
            }
            if (potentialCenterY > canvasPane.getPrefHeight() - 12.5) {
                offsetY = (canvasPane.getPrefHeight() - 12.5) - potentialCenterY;
            }

            rightTerminal.setTerminalX(potentialCenterX + offsetX);
            rightTerminal.setTerminalY(potentialCenterY + offsetY);
            wire.setEndX(potentialCenterX + offsetX);
            wire.setEndY(potentialCenterY + offsetY);
        });

        rightTerminal.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                canvasScrollPane.setPannable(true);

                rightSide[0].setNewTerminalX(rightTerminal.getCenterX());
                rightSide[0].setNewTerminalY(rightTerminal.getCenterY());

                rightSide[0].setNewWireSideX(wire.getEndX());
                rightSide[0].setNewWireSideY(wire.getEndY());

                rightSide[0].performAction();

                //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
                if (!currentProject.getRedoStack().isEmpty()) {
                    currentProject.clearRedoStack();
                    redoButton.setDisable(true);
                }

                undoButton.setDisable(false);

                HashMap<Integer, Double> circuitGroups = currentProject.getCircuitGroups();
                voltageField.setText("null");
                currentField.setText(String.valueOf(circuitGroups.get(wire.getWireModel().getGroup())));
                resistanceField.setText("null");
            }
        });
    }

    private void openComponentMenu(Node componentNode, Component component) {
        Dialog<Void> componentDialog = new Dialog<>();
        componentDialog.setTitle("Edit " + component.getComponentType());

        componentDialog.getDialogPane().setStyle("-fx-background-color: #6abce2");

        ButtonType okType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        componentDialog.getDialogPane().getButtonTypes().add(okType);

        VBox menuVbox = new VBox();

        VBox headerVbox = new VBox();
        headerVbox.setStyle(("-fx-background-color: #E0E0E0;"));
        headerVbox.setPrefHeight(45);
        headerVbox.setAlignment(Pos.CENTER_LEFT);
        headerVbox.setPadding(new Insets(0,10,0,10));
        Label headerLabel = new Label();
        headerLabel.setStyle("-fx-font-family: System; -fx-font-weight: bold; -fx-font-size: 18px;");
        headerVbox.getChildren().add(headerLabel);

        Button updateValueButton;

        if (!Objects.equals(component.getComponentType(), "Switch") && !Objects.equals(component.getComponentType(), "Wire")) {
            menuVbox.setPrefWidth(300);
            menuVbox.setPrefHeight(180);
            menuVbox.setAlignment(Pos.TOP_CENTER);
            menuVbox.setPadding(new Insets(0,0,0,0));

            headerLabel.setText("Enter new value");

            VBox subheaderVbox = new VBox();
            subheaderVbox.setStyle(("-fx-background-color: #F6F6F6;"));
            subheaderVbox.setPrefHeight(30);
            subheaderVbox.setAlignment(Pos.CENTER_LEFT);
            subheaderVbox.setPadding(new Insets(0,10,0,10));
            Label subheaderLabel = new Label();
            subheaderLabel.setStyle("-fx-font-family: System; -fx-font-size: 15px;");
            subheaderVbox.getChildren().add(subheaderLabel);

            HBox valueHbox = new HBox();
            valueHbox.setStyle(("-fx-background-color: #F6F6F6;"));
            VBox.setVgrow(valueHbox, Priority.ALWAYS);
            valueHbox.setPrefHeight(45);
            valueHbox.setSpacing(10);
            valueHbox.setAlignment(Pos.CENTER_LEFT);
            valueHbox.setPadding(new Insets(0,10,0,10));
            TextField valueTextField = new TextField();
            valueTextField.setPromptText("Value must be between 0 and 120");
            valueTextField.setPrefWidth(235);
            updateValueButton = new Button("Update");
            updateValueButton.setPrefWidth(80);

            valueHbox.getChildren().addAll(valueTextField, updateValueButton);

            switch(component.getComponentType()) {
                case "Battery" -> {
                    BatteryModel batteryModel = (BatteryModel) component;
                    subheaderLabel.setText("Battery Voltage (0 - 120 Volts)");
                    valueTextField.setText(String.valueOf(batteryModel.getVoltage()));
                    updateValueButton.setOnAction(_ -> {
                        Pattern p = Pattern.compile("^\\d*\\.?\\d*$");

                        double voltage;
                        try {
                            voltage = Double.parseDouble(valueTextField.getText());
                        } catch (NumberFormatException e) {
                            valueTextField.setText(String.valueOf(batteryModel.getVoltage()));
                            return;
                        }

                        Matcher matcher = p.matcher(String.valueOf(voltage));
                        if (voltage >= 0 && voltage <= 120.0 && matcher.find()) {
                            ModifyComponent modifyBattery = new ModifyComponent(currentProject, batteryModel, batteryModel.getVoltage(), voltage);
                            modifyBattery.performAction();

                            if (!currentProject.getRedoStack().isEmpty()) {
                                currentProject.clearRedoStack();
                                redoButton.setDisable(true);
                            }

                            valueTextField.setText(batteryModel.getVoltage() + "");
                        }
                        else {
                            valueTextField.setText(String.valueOf(batteryModel.getVoltage()));
                        }
                    });
                }
                case "Resistor" -> {
                    ResistorModel resistorModel = (ResistorModel) component;
                    subheaderLabel.setText("Resistor Resistance (0 - 120 Ohms)");
                    valueTextField.setText(String.valueOf(resistorModel.getResistance()));
                    updateValueButton.setOnAction(_ -> {
                        Pattern p = Pattern.compile("^\\d*\\.?\\d*$");

                        double resistance;
                        try {
                            resistance = Double.parseDouble(valueTextField.getText());
                        } catch (NumberFormatException e) {
                            valueTextField.setText(String.valueOf(resistorModel.getResistance()));
                            return;
                        }

                        Matcher matcher = p.matcher(String.valueOf(resistance));
                        if (resistance >= 0 && resistance <= 120.0 && matcher.find()) {
                            ModifyComponent modifyResistor = new ModifyComponent(currentProject, resistorModel, resistorModel.getResistance(), resistance);
                            modifyResistor.performAction();

                            if (!currentProject.getRedoStack().isEmpty()) {
                                currentProject.clearRedoStack();
                                redoButton.setDisable(true);
                            }

                            valueTextField.setText(resistorModel.getResistance() + "");
                        }
                        else {
                            valueTextField.setText(String.valueOf(resistorModel.getResistance()));
                        }
                    });
                }
                case "Light bulb" -> {
                    LightbulbModel lightbulbModel = (LightbulbModel) component;
                    subheaderLabel.setText("Light Bulb Resistance (0 - 120 Ohms)");
                    valueTextField.setText(String.valueOf(lightbulbModel.getResistance()));
                    updateValueButton.setOnAction(_ -> {
                        Pattern p = Pattern.compile("^\\d*\\.?\\d*$");

                        double resistance;
                        try {
                            resistance = Double.parseDouble(valueTextField.getText());
                        } catch (NumberFormatException e) {
                            valueTextField.setText(String.valueOf(lightbulbModel.getResistance()));
                            return;
                        }

                        Matcher matcher = p.matcher(String.valueOf(resistance));
                        if (resistance >= 0 && resistance <= 120.0 && matcher.find()) {
                            ModifyComponent modifyLightbulb = new ModifyComponent(currentProject, lightbulbModel, lightbulbModel.getResistance(), resistance);
                            modifyLightbulb.performAction();

                            if (!currentProject.getRedoStack().isEmpty()) {
                                currentProject.clearRedoStack();
                                redoButton.setDisable(true);
                            }

                            valueTextField.setText(lightbulbModel.getResistance() + "");
                        }
                        else {
                            valueTextField.setText(String.valueOf(lightbulbModel.getResistance()));
                        }
                    });
                }
            }

            menuVbox.getChildren().addAll(headerVbox, subheaderVbox, valueHbox);

        }
        else if (Objects.equals(component.getComponentType(), "Switch")) {
            headerLabel.setText("Edit " + component.getComponentType());

            menuVbox.setPrefWidth(300);
            menuVbox.setPrefHeight(100);
            menuVbox.setAlignment(Pos.TOP_CENTER);
            menuVbox.setPadding(new Insets(0,0,0,0));

            HBox stateHbox = new HBox();
            stateHbox.setStyle(("-fx-background-color: #F6F6F6;"));
            stateHbox.setPrefHeight(35);
            stateHbox.setAlignment(Pos.CENTER);
            stateHbox.setSpacing(20);
            Button stateButton = new Button("");
            CircuitSwitchModel switchModel = (CircuitSwitchModel) component;
            CircuitSwitchNode switchNode = (CircuitSwitchNode) componentNode;

            if (switchModel.isActive()) {
                stateButton.setText("Close");
            }
            else {
                stateButton.setText("Open");
            }

            stateHbox.getChildren().add(stateButton);

            stateButton.setOnAction(_ -> {
                ModifySwitchState switchState = new ModifySwitchState(currentProject, switchNode, switchModel, switchModel.isActive(), !switchModel.isActive());
                switchState.performAction();

                if (!currentProject.getRedoStack().isEmpty()) {
                    currentProject.clearRedoStack();
                    redoButton.setDisable(true);
                }

                if (switchModel.isActive()) {
                    stateButton.setText("Close");
                }
                else {
                    stateButton.setText("Open");
                }
            });

            menuVbox.getChildren().addAll(headerVbox, stateHbox);
        }
        else {
            menuVbox.setPrefWidth(300);
            menuVbox.setPrefHeight(80);
            menuVbox.setAlignment(Pos.TOP_CENTER);
            menuVbox.setPadding(new Insets(0,0,0,0));

            headerLabel.setText("Delete " + component.getComponentType());

            menuVbox.getChildren().add(headerVbox);
        }

        HBox optionsHbox = new HBox();
        optionsHbox.setStyle(("-fx-background-color: #F6F6F6;"));
        optionsHbox.setPrefHeight(35);
        optionsHbox.setAlignment(Pos.CENTER);
        optionsHbox.setSpacing(20);
        Button deleteButton = new Button("Delete");

        deleteButton.setOnAction(_ -> {
            RemoveComponent removeComponent = new RemoveComponent(currentProject, canvasPane, componentNode, component);
            removeComponent.performAction();
            componentDialog.close();
        });

        optionsHbox.getChildren().add(deleteButton);

        menuVbox.getChildren().add(optionsHbox);

        componentDialog.getDialogPane().setContent(menuVbox);

        componentDialog.showAndWait();
    }

    public void addWire(double leftX, double y) {
        double rightX = leftX + 100.0;
        double strokeOffsetLeftCorner = 12.5;
        double strokeOffsetRightCorner = -12.5;

        WireNode wire = new WireNode(leftX, y, rightX, y);
        WireModel wireModel = wire.getWireModel();

        TerminalNode leftTerminal = wire.getLeftTerminalNode();
        TerminalNode rightTerminal = wire.getRightTerminalNode();

        if (rightX > canvasPane.getPrefWidth()) {
            double correctedLeftX = canvasPane.getPrefWidth() - 100.0;
            double correctedRightX = canvasPane.getPrefWidth();
            wire.setStartX(correctedLeftX + strokeOffsetLeftCorner);
            wire.setEndX(correctedRightX + strokeOffsetRightCorner);
            wireModel.setComponentX(correctedLeftX);
            wireModel.setRightSideX(correctedRightX);
            leftTerminal.setTerminalX(wire.getStartX());
            rightTerminal.setTerminalX(wire.getEndX());
        }

        if (y + strokeOffsetLeftCorner > canvasPane.getPrefHeight()) {
            double correctedY = (canvasPane.getPrefHeight() + strokeOffsetRightCorner);
            wire.setStartY(correctedY);
            wire.setEndY(correctedY);
            wireModel.setComponentY(correctedY);
            wireModel.setRightSideY(correctedY);
            leftTerminal.setTerminalY(wire.getStartY());
            rightTerminal.setTerminalY(wire.getEndY());
        }

        AddComponent add = new AddComponent(currentProject, canvasPane, wire, wireModel, false);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeWireDraggable(wire);
        makeWireTerminalDraggable(leftTerminal, rightTerminal, wire);
    }

    public void addBattery(double x, double y) {
        BatteryNode battery = new BatteryNode(x, y);
        BatteryModel batteryModel = battery.getBatteryModel();

        double batteryRight = x + battery.getLayoutBounds().getWidth();
        double batteryBottom = y + battery.getLayoutBounds().getHeight();

        //Checks if the component would be out of bounds before adding it to the canvas, then calculates a position
        //so that it is in bounds
        if (batteryRight > canvasPane.getPrefWidth()) {
            double correctedX = canvasPane.getPrefWidth() - battery.getLayoutBounds().getWidth();
            battery.setLayoutX(correctedX);
            batteryModel.setComponentX(correctedX);
        }

        if (batteryBottom > canvasPane.getPrefHeight()) {
            double correctedY = canvasPane.getPrefHeight() - battery.getLayoutBounds().getHeight();
            battery.setLayoutY(correctedY);
            batteryModel.setComponentY(correctedY);
        }

        AddComponent add = new AddComponent(currentProject, canvasPane, battery, batteryModel, false);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(battery, batteryModel);
    }

    public void addResistor(double x, double y) {
        ResistorNode resistor = new ResistorNode(x, y);
        ResistorModel resistorModel = resistor.getResistorModel();

        double resistorRight = x + resistor.getLayoutBounds().getWidth();
        double resistorBottom = y + resistor.getLayoutBounds().getHeight();

        //Checks if the component would be out of bounds before adding it to the canvas, then calculates a position
        //so that it is in bounds
        if (resistorRight > canvasPane.getPrefWidth()) {
            double correctedX = canvasPane.getPrefWidth() - resistor.getLayoutBounds().getWidth();
            resistor.setLayoutX(correctedX);
            resistorModel.setComponentX(correctedX);
        }

        if (resistorBottom > canvasPane.getPrefHeight()) {
            double correctedY = canvasPane.getPrefHeight() - resistor.getLayoutBounds().getHeight();
            resistor.setLayoutY(correctedY);
            resistorModel.setComponentY(correctedY);
        }

        AddComponent add = new AddComponent(currentProject, canvasPane, resistor, resistorModel, false);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(resistor, resistorModel);
    }

    public void addCircuitSwitch(double x, double y) {
        CircuitSwitchNode circuitSwitch = new CircuitSwitchNode(x, y);
        CircuitSwitchModel circuitSwitchModel = circuitSwitch.getSwitchModel();

        double circuitSwitchRight = x + circuitSwitch.getLayoutBounds().getWidth();
        double circuitSwitchBottom = y + circuitSwitch.getLayoutBounds().getHeight();

        //Checks if the component would be out of bounds before adding it to the canvas, then calculates a position
        //so that it is in bounds
        if (circuitSwitchRight > canvasPane.getPrefWidth()) {
            double correctedX = canvasPane.getPrefWidth() - circuitSwitch.getLayoutBounds().getWidth();
            circuitSwitch.setLayoutX(correctedX);
            circuitSwitchModel.setComponentX(correctedX);
        }

        if (circuitSwitchBottom > canvasPane.getPrefHeight()) {
            double correctedY = canvasPane.getPrefHeight() - circuitSwitch.getLayoutBounds().getHeight();
            circuitSwitch.setLayoutY(correctedY);
            circuitSwitchModel.setComponentY(correctedY);
        }

        AddComponent add = new AddComponent(currentProject, canvasPane, circuitSwitch, circuitSwitchModel, false);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(circuitSwitch, circuitSwitchModel);
    }

    public void addLightbulb(double x, double y) {
        LightbulbNode lightbulb = new LightbulbNode(x, y);
        LightbulbModel lightbulbModel = lightbulb.getLightbulbModel();

        double lightbulbRight = x + lightbulb.getLayoutBounds().getWidth();
        double lightbulbBottom = y + lightbulb.getLayoutBounds().getHeight();

        //Checks if the component would be out of bounds before adding it to the canvas, then calculates a position
        //so that it is in bounds
        if (lightbulbRight > canvasPane.getPrefWidth()) {
            double correctedX = canvasPane.getPrefWidth() - lightbulb.getLayoutBounds().getWidth();
            lightbulb.setLayoutX(correctedX);
            lightbulbModel.setComponentX(correctedX);
        }

        if (lightbulbBottom > canvasPane.getPrefHeight()) {
            double correctedY = canvasPane.getPrefHeight() - lightbulb.getLayoutBounds().getHeight();
            lightbulb.setLayoutY(correctedY);
            lightbulbModel.setComponentY(correctedY);
        }

        AddComponent add = new AddComponent(currentProject, canvasPane, lightbulb, lightbulbModel, false);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(lightbulb, lightbulbModel);
    }

    public void getDisplayCalculations(WireNode wireNode) {
        WireModel wireModel = wireNode.getWireModel();

        TerminalNode negativeTerminal = wireNode.getLeftTerminalNode();
        TerminalNode positiveTerminal = wireNode.getRightTerminalNode();

        ArrayList<Component> wireLeftRemove = new ArrayList<>(wireModel.getNegativeSide());
        ArrayList<Component> wireRightRemove = new ArrayList<>(wireModel.getPositiveSide());

        Bounds leftTerminal = negativeTerminal.localToScene(negativeTerminal.getBoundsInLocal());
        for (TerminalNode t : currentProject.getTerminalList()) {
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
        for (TerminalNode t : currentProject.getTerminalList()) {
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

        currentProject.calculateCircuitGroups();
    }


    @FXML
    void startSimulation() {
        HashMap<Component, Node> projectComponents = currentProject.getProjectComponents();
        for (Node componentNode : projectComponents.values()) {
            if (componentNode instanceof WireNode) {
                getDisplayCalculations((WireNode) componentNode);
            }
        }
        topButtonBar.getChildren().remove(startButton);
    }

}
