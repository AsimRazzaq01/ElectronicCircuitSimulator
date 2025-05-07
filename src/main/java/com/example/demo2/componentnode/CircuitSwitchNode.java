package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.CircuitSwitchModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * CircuitSwitchNode class -> 3 private variable -> 1 var  initialized from switch Model class -> 2 methods , get/set switch method
 */
public class CircuitSwitchNode extends Group {
    // Private variable - model class
    private CircuitSwitchModel switchModel;

    // Private variables
    private Image CLOSED_SWITCH_IMAGE;
    private Image OPEN_SWITCH_IMAGE;
    private ImageView switchImageView;
    TerminalNode negative;
    TerminalNode positive;


    // CircuitSwitchNode method 1
    public CircuitSwitchNode(double x, double y) {
        URL openSwitchPath = Project.class.getResource("component_sprites/switch_opened.png");
        URL closedSwitchPath = Project.class.getResource("component_sprites/switch_closed.png");
        if (closedSwitchPath != null && openSwitchPath != null) {
            CLOSED_SWITCH_IMAGE = new Image(closedSwitchPath.toExternalForm(),500, 0,true,false);
            OPEN_SWITCH_IMAGE = new Image(openSwitchPath.toExternalForm(),500,0,true,false);
            switchModel = new CircuitSwitchModel(x, y);
            switchImageView = new ImageView(CLOSED_SWITCH_IMAGE);
            switchImageView.setLayoutX(20);
            negative = new TerminalNode(switchModel, 10, 32.0, "Negative");
            positive = new TerminalNode(switchModel, 100, 32.0, "Positive");
            this.getChildren().add(switchImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            switchImageView.setFitWidth(70);
            switchImageView.setPreserveRatio(true);
            switchImageView.setPickOnBounds(true);
            this.setLayoutX(switchModel.getComponentX());
            this.setLayoutY(switchModel.getComponentY());
        }
    }

    // CircuitSwitchNode method 2
    public CircuitSwitchNode(double x, double y, boolean active) {
        URL openSwitchPath = Project.class.getResource("component_sprites/switch_opened.png");
        URL closedSwitchPath = Project.class.getResource("component_sprites/switch_closed.png");
        if (closedSwitchPath != null && openSwitchPath != null) {
            CLOSED_SWITCH_IMAGE = new Image(closedSwitchPath.toExternalForm(),500, 0,true,false);
            OPEN_SWITCH_IMAGE = new Image(openSwitchPath.toExternalForm(),500,0,true,false);
            switchModel = new CircuitSwitchModel(x, y, active);
            switchImageView = new ImageView();
            switchImageView.setFitWidth(70);
            switchImageView.setPreserveRatio(true);
            setSwitchImageState(active);
            switchImageView.setLayoutX(20);
            negative = new TerminalNode(switchModel, 10, 32.0, "Negative");
            positive = new TerminalNode(switchModel, 100, 32.0, "Positive");
            this.getChildren().add(switchImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            switchImageView.setPickOnBounds(true);
            this.setLayoutX(switchModel.getComponentX());
            this.setLayoutY(switchModel.getComponentY());
        }
    }

    // getter method
    public CircuitSwitchModel getSwitchModel() {
        return switchModel;
    }

    // setter method
    public void setSwitchImageState(boolean active) {
        if (active) {
            switchImageView.setImage(OPEN_SWITCH_IMAGE);
        }
        else {
            switchImageView.setImage(CLOSED_SWITCH_IMAGE);
        }
    }

    public TerminalNode getNegative() {
        return negative;
    }

    public TerminalNode getPositive() {
        return positive;
    }
} // End CircuitSwitchNode class
