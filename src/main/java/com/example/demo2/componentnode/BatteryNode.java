package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.BatteryModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;

/**
 * BatteryNode class -> 1 private variable initialized from battery Model class -> 2 methods , 1 get battery method
 */
public class BatteryNode extends Group {
    // Private variable
    private BatteryModel batteryModel;
    TerminalNode negative;
    TerminalNode positive;

    // BatteryNode method 1
    public BatteryNode(double x, double y) {
        URL imagePath = Project.class.getResource("component_sprites/battery.png");
        if (imagePath != null) {
            Image batteryImage = new Image(imagePath.toExternalForm(), 500, 0, true, false);
            ImageView batteryImageView = new ImageView(batteryImage);
            batteryImageView.setFitWidth(70);
            batteryImageView.setPreserveRatio(true);
            batteryImageView.setLayoutX(20);
            batteryModel = new BatteryModel(x, y);
            negative = new TerminalNode(batteryModel, 10, 16.4, "Negative");
            positive = new TerminalNode(batteryModel, 100, 16.4, "Positive");
            this.getChildren().add(batteryImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            this.setLayoutX(batteryModel.getComponentX());
            this.setLayoutY(batteryModel.getComponentY());
        }
    }

    // BatteryNode method 2
    public BatteryNode(double x, double y, double v) {
        URL IMAGE_PATH = Project.class.getResource("component_sprites/battery.png");
        if (IMAGE_PATH != null) {
            Image batteryImage = new Image(IMAGE_PATH.toExternalForm(), 500, 0, true, false);
            ImageView batteryImageView = new ImageView(batteryImage);
            batteryImageView.setFitWidth(70);
            batteryImageView.setPreserveRatio(true);
            batteryImageView.setLayoutX(20);
            batteryModel = new BatteryModel(x, y, v);
            negative = new TerminalNode(batteryModel, 10, 16.4, "Negative");
            positive = new TerminalNode(batteryModel, 100, 16.4, "Positive");
            this.getChildren().add(batteryImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            this.setLayoutX(batteryModel.getComponentX());
            this.setLayoutY(batteryModel.getComponentY());
        }
    }

    // getter method
    public BatteryModel getBatteryModel() {
        return batteryModel;
    }

    public TerminalNode getNegative() {
        return negative;
    }

    public TerminalNode getPositive() {
        return positive;
    }
} // End BatteryNode class
