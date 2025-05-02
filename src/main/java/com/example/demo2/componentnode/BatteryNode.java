package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.BatteryModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class BatteryNode extends Group {
    private BatteryModel batteryModel;

    public BatteryNode(double x, double y) {
        URL imagePath = Project.class.getResource("component_sprites/battery.png");
        if (imagePath != null) {
            Image batteryImage = new Image(imagePath.toExternalForm(), 500, 0, true, false);
            ImageView batteryImageView = new ImageView(batteryImage);
            batteryImageView.setFitWidth(70);
            batteryImageView.setPreserveRatio(true);
            batteryModel = new BatteryModel(x, y);
            this.getChildren().add(batteryImageView);
            this.setLayoutX(batteryModel.getComponentX());
            this.setLayoutY(batteryModel.getComponentY());
        }
    }

    public BatteryNode(double x, double y, double v) {
        URL IMAGE_PATH = Project.class.getResource("component_sprites/battery.png");
        if (IMAGE_PATH != null) {
            Image batteryImage = new Image(IMAGE_PATH.toExternalForm(), 500, 0, true, false);
            ImageView batteryImageView = new ImageView(batteryImage);
            batteryImageView.setFitWidth(70);
            batteryImageView.setPreserveRatio(true);
            batteryModel = new BatteryModel(x, y, v);
            this.getChildren().add(batteryImageView);
            this.setLayoutX(batteryModel.getComponentX());
            this.setLayoutY(batteryModel.getComponentY());
        }
    }

    public BatteryModel getBatteryModel() {
        return batteryModel;
    }
}
