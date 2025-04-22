package com.example.demo2.components;

import com.example.demo2.Project;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Resistor extends Component {
    private Group resistor;
    private double resistance;

    public Resistor(double x, double y) {
        super(x,y);
        URL imagePath = Project.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            resistor = new Group();
            Image resistorImage = new Image(imagePath.toExternalForm(),70,0,true,false);
            ImageView resistorImageView = new ImageView(resistorImage);
            resistor.getChildren().add(resistorImageView);
            resistance = 10.0;
            resistor.setLayoutX(getComponentX());
            resistor.setLayoutY(getComponentY());
        }
    }

    public double getResistance() {
        return resistance;
    }

    public void setResistance(double resistance) {
        this.resistance = resistance;
    }

    @Override
    public void makeDraggable(ScrollPane pane) {
        Node canvas = pane.getContent();

        resistor.setOnMousePressed(mouseEvent -> {
            //Gets the coordinates of the cursor within the canvasPane
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            //Maintains the position of the cursor in resistor when it is being dragged
            //Taking into account of the current zoom in order to prevent it
            setComponentX((cursorInPane.getX() / zoomScale) - resistor.getLayoutX());
            setComponentY((cursorInPane.getY() / zoomScale) - resistor.getLayoutY());

            pane.setPannable(false);
            resistor.toFront();
        });

        resistor.setOnMouseDragged(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            double potentialNewX = (cursorInPane.getX() / zoomScale) - getComponentX();
            double potentialNewY = (cursorInPane.getY() / zoomScale) - getComponentY();

            //The position of the resistor can only be in 0 and 1528 for x and 0 and 842.5 for y
            //The new coordinate values are calculated by getting the maximum value of 0 and the minimum value of the new calculated coordinates and the maximum possible value
            double newResistorX = Math.max(0, Math.min(potentialNewX, 1528));
            double newResistorY = Math.max(0, Math.min(potentialNewY, 842.5));

            //Set the new position of the resistor
            resistor.setLayoutX(newResistorX);
            resistor.setLayoutY(newResistorY);
        });

        resistor.setOnMouseReleased(_ -> {
            pane.setPannable(true);
            setComponentX(resistor.getLayoutX());
            setComponentY(resistor.getLayoutY());
        });
    }

    @Override
    public Group getComponentNode() {
        return resistor;
    }
}
