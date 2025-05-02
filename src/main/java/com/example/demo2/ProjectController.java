package com.example.demo2;

import com.example.demo2.componentmodel.*;
import com.example.demo2.componentnode.*;
import com.example.demo2.projectactions.AddComponent;
import com.example.demo2.projectactions.ProjectActions;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;

public class ProjectController {
    @FXML
    private ScrollPane canvasScrollPane;
    @FXML
    private Button redoButton;
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
    private Button homeButton;
    @FXML
    private Slider zoomSlider;
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

    private double zoomScale = 1.0;
    private Project currentProject;

    void setCurrentProject(Project project) {
        currentProject = project;
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
        //Project data from database will be passed to the project view screen
        setCurrentProject(new Project(101, "Test Project"));
        projectNameLabel.setText(currentProject.getProjectName());
        loadComponentPaneImages();
        allowDragAndDrop();
        undoButton.setDisable(true);
        redoButton.setDisable(true);
        zoomSlider.setDisable(true);
    }

    @FXML
    protected void logout() throws IOException {
        Scene scene = this.logoutButton.getScene();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SignIn.fxml"));
        scene.setRoot(fxmlLoader.load());
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

    void initializeScrollPosition() {
        Platform.runLater(() -> {
            canvasScrollPane.setHvalue(0.5);
            canvasScrollPane.setVvalue(0.5);
//            Will be utilized later
//            for (Node node : canvasPane.getChildren()) {
//                node.setOpacity(1.0);
//            }
        });
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

        componentNode.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                //Gets the coordinates of the cursor within the canvasPane
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

                component.setComponentX((cursorInPane.getX() / zoomScale) - componentNode.getLayoutX());
                component.setComponentY((cursorInPane.getY() / zoomScale) - componentNode.getLayoutY());
                canvasScrollPane.setPannable(false);
                componentNode.toFront();
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
                double newComponentX = Math.max(0, Math.min(potentialNewX, (canvasPane.getPrefWidth() - componentNode.getLayoutBounds().getWidth())));
                double newComponentY = Math.max(0, Math.min(potentialNewY, (canvasPane.getPrefHeight() - componentNode.getLayoutBounds().getHeight())));

                //Set the new position of the component
                componentNode.setLayoutX(newComponentX);
                componentNode.setLayoutY(newComponentY);
            }
        });

        componentNode.setOnMouseReleased(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                canvasScrollPane.setPannable(true);
                component.setComponentX(componentNode.getLayoutX());
                component.setComponentY(componentNode.getLayoutY());
            }
        });
    }

    public void makeWireDraggable(WireNode wire, Component wireComponent) {
        Node canvas = canvasScrollPane.getContent();
        WireTerminalNode leftTerminal = wire.getLeftTerminalNode();
        WireTerminalNode rightTerminal = wire.getRightTerminalNode();
        WireModel wireModel = wire.getWireModel();

        final Point2D[] cursorToStartOffset = new Point2D[1];
        final Point2D[] cursorToEndOffset = new Point2D[1];

        wire.setOnMousePressed(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                double cursorX = cursorInPane.getX() / zoomScale;
                double cursorY = cursorInPane.getY() / zoomScale;

                //Stores offset from cursor to both endpoints, coordinates within the wire
                cursorToStartOffset[0] = new Point2D(cursorX - wire.getStartX(), cursorY - wire.getStartY());
                cursorToEndOffset[0] = new Point2D(cursorX - wire.getEndX(), cursorY - wire.getEndY());

                canvasScrollPane.setPannable(false);
                wire.toFront();
                leftTerminal.toFront();
                rightTerminal.toFront();
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
                wireModel.setComponentX(wire.getStartX() - 12.5);
                wireModel.setComponentY(wire.getStartY() - 12.5);
                wireModel.setRightSideX(wire.getEndX() + 12.5);
                wireModel.setRightSideY(wire.getEndY() - 12.5);

                leftTerminal.toBack();
                rightTerminal.toBack();
                wire.toBack();
            }
        });
    }

    public void makeWireTerminalDraggable(WireTerminalNode leftTerminal, WireTerminalNode rightTerminal, WireNode wire) {
        Node canvas = canvasScrollPane.getContent();
        WireModel wireModel = wire.getWireModel();

        final Point2D[] cursorLeftTerminalOffset = new Point2D[1];
        final Point2D[] cursorRightTerminalOffset = new Point2D[1];

        leftTerminal.setOnMousePressed(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            //Stores offset from cursor to both endpoints, coordinates within the wire terminal
            cursorLeftTerminalOffset[0] = new Point2D(leftTerminal.getCenterX() - cursorX, leftTerminal.getCenterY() - cursorY);
            canvasScrollPane.setPannable(false);
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
                wireModel.setComponentX(wire.getStartX() - 12.5);
                wireModel.setComponentY(wire.getStartY() - 12.5);
                wireModel.setRightSideX(wire.getEndX() + 12.5);
                wireModel.setRightSideY(wire.getEndY() - 12.5);

                leftTerminal.toBack();
                rightTerminal.toBack();
                wire.toBack();
            }
        });

        rightTerminal.setOnMousePressed(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
            double cursorX = cursorInPane.getX() / zoomScale;
            double cursorY = cursorInPane.getY() / zoomScale;

            //Stores offset from cursor to both endpoints, coordinates within the wire terminal
            cursorRightTerminalOffset[0] = new Point2D(rightTerminal.getCenterX() - cursorX, rightTerminal.getCenterY() - cursorY);
            canvasScrollPane.setPannable(false);
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
                wireModel.setComponentX(wire.getStartX() - 12.5);
                wireModel.setComponentY(wire.getStartY() - 12.5);
                wireModel.setRightSideX(wire.getEndX() + 12.5);
                wireModel.setRightSideY(wire.getEndY() - 12.5);

                leftTerminal.toBack();
                rightTerminal.toBack();
                wire.toBack();
            }
        });
    }

    public void addWire(double leftX, double y) {
        double rightX = leftX + 100.0;
        double strokeOffsetLeftCorner = 12.5;
        double strokeOffsetRightCorner = -12.5;

        WireNode wire = new WireNode(leftX, y, rightX, y);
        WireModel wireModel = wire.getWireModel();

        WireTerminalNode leftTerminal = wire.getLeftTerminalNode();
        WireTerminalNode rightTerminal = wire.getRightTerminalNode();

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

        AddComponent add = new AddComponent(currentProject, canvasPane, wire, wireModel);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeWireDraggable(wire, wireModel);
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

        AddComponent add = new AddComponent(currentProject, canvasPane, battery, batteryModel);
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

        AddComponent add = new AddComponent(currentProject, canvasPane, resistor, resistorModel);
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

    public void setSwitchFunctionality(CircuitSwitchNode circuitSwitch) {
        circuitSwitch.setOnMouseClicked(mouseEvent -> {
            CircuitSwitchModel circuitSwitchModel = circuitSwitch.getSwitchModel();
            if (mouseEvent.isStillSincePress()) {
                circuitSwitchModel.setActive();
                circuitSwitch.setSwitchImageState();
            }
        });
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

        AddComponent add = new AddComponent(currentProject, canvasPane, circuitSwitch, circuitSwitchModel);
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        setSwitchFunctionality(circuitSwitch);

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

        AddComponent add = new AddComponent(currentProject, canvasPane, lightbulb, lightbulbModel);
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
}
