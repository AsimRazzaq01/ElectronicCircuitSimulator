package com.example.demo2.components;

import com.example.demo2.Project;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class Battery extends Component {
    private Group battery;
    private double voltage;

    public Battery(double x , double y) {
        super(x,y);
        URL imagePath = Project.class.getResource("component_sprites/battery.png");
        if (imagePath != null) {
            battery = new Group();
            Image batteryImage = new Image(imagePath.toExternalForm(),70,0,true,false);
            ImageView batteryImageView = new ImageView(batteryImage);
            battery.getChildren().add(batteryImageView);
            voltage = 9.0;
            battery.setLayoutX(getComponentX());
            battery.setLayoutY(getComponentY());
        }
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    @Override
    public void makeDraggable(ScrollPane pane) {
        Node canvas = pane.getContent();

        battery.setOnMousePressed(mouseEvent -> {
            //Gets the coordinates of the cursor within the canvasPane
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            setComponentX((cursorInPane.getX() / zoomScale) - battery.getLayoutX());
            setComponentY((cursorInPane.getY() / zoomScale) - battery.getLayoutY());
            pane.setPannable(false);
            battery.toFront();
        });

        battery.setOnMouseDragged(mouseEvent -> {
            Point2D cursorInPane = canvas.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());

            double potentialNewX = (cursorInPane.getX() / zoomScale) - getComponentX();
            double potentialNewY = (cursorInPane.getY() / zoomScale) - getComponentY();

            //The position of the battery can only be in 0 and 1528 for x and 0 and 842.5 for y
            //The new coordinate values are calculated by getting the maximum value of 0 and the minimum value of the new calculated coordinates and the maximum possible value
            double newBatteryX = Math.max(0, Math.min(potentialNewX, 1528));
            double newBatteryY = Math.max(0, Math.min(potentialNewY, 842.5));

            //Set the new position of the battery
            battery.setLayoutX(newBatteryX);
            battery.setLayoutY(newBatteryY);
        });

        battery.setOnMouseReleased(_ -> {
            pane.setPannable(true);
            setComponentX(battery.getLayoutX());
            setComponentY(battery.getLayoutY());
        });
    }

    @Override
    public Group getComponentNode() {
        return battery;
    }

}
