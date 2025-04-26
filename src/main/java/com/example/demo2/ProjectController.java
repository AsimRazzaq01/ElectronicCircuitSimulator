package com.example.demo2;

import com.example.demo2.componentmodel.CircuitSwitchModel;
import com.example.demo2.componentmodel.Component;
import com.example.demo2.componentnode.BatteryNode;
import com.example.demo2.componentnode.CircuitSwitchNode;
import com.example.demo2.componentnode.LightbulbNode;
import com.example.demo2.componentnode.ResistorNode;
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
    }

    private void allowDragAndDrop() {
        batteryImageView.setOnDragDetected(mouseEvent -> {
            Dragboard batteryDragboard = batteryImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("battery");
            batteryDragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        resistorImageView.setOnDragDetected(mouseEvent -> {
            Dragboard resistorDragboard = resistorImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.putString("resistor");
            resistorDragboard.setContent(clipboardContent);
            mouseEvent.consume();
        });

        switchImageView.setOnDragDetected(mouseEvent -> {
            Dragboard switchDragboard = switchImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent switchClipboardContent = new ClipboardContent();
            switchClipboardContent.putString("switch");
            switchDragboard.setContent(switchClipboardContent);
            mouseEvent.consume();
        });

        lightbulbImageView.setOnDragDetected(mouseEvent -> {
            Dragboard lightbulbDragboard = lightbulbImageView.startDragAndDrop(TransferMode.MOVE);
            ClipboardContent lightbulbClipboardContent = new ClipboardContent();
            lightbulbClipboardContent.putString("lightbulb");
            lightbulbDragboard.setContent(lightbulbClipboardContent);
            mouseEvent.consume();
        });

        canvasPane.setOnDragOver(mouseEvent -> {
            if (mouseEvent.getGestureSource() != canvasPane && mouseEvent.getDragboard().hasString()) {
                mouseEvent.acceptTransferModes(TransferMode.MOVE);
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
                }
                success = true;
            }

            mouseEvent.setDropCompleted(success);
            mouseEvent.consume();
            canvasScrollPane.requestFocus();
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

                //The position of the battery can only be in 0 and 1528 for x and 0 and 842.5 for y
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

    public void addBattery(double x, double y) {
        BatteryNode battery = new BatteryNode(x, y);
        AddComponent add = new AddComponent(currentProject, canvasPane, battery, battery.getBatteryModel());
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(battery, battery.getBatteryModel());
    }

    public void addResistor(double x, double y) {
        ResistorNode resistor = new ResistorNode(x, y);
        AddComponent add = new AddComponent(currentProject, canvasPane, resistor, resistor.getResistorModel());
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(resistor, resistor.getResistorModel());
    }

    public void addCircuitSwitch(double x, double y) {
        CircuitSwitchNode circuitSwitch = new CircuitSwitchNode(x, y);
        AddComponent add = new AddComponent(currentProject, canvasPane, circuitSwitch, circuitSwitch.getSwitchModel());
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        setSwitchFunctionality(circuitSwitch);

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(circuitSwitch, circuitSwitch.getSwitchModel());
    }

    public void addLightbulb(double x, double y) {
        LightbulbNode lightbulb = new LightbulbNode(x, y);
        AddComponent add = new AddComponent(currentProject, canvasPane, lightbulb, lightbulb.getLightbulbModel());
        add.performAction();

        //If a new action is performed when the redo stack contains actions, the redo stack will be cleared
        if (!currentProject.getRedoStack().isEmpty()) {
            currentProject.clearRedoStack();
            redoButton.setDisable(true);
        }

        undoButton.setDisable(false);
        adjustComponentZoomScale(zoomScale);
        makeDraggable(lightbulb, lightbulb.getLightbulbModel());
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
}
