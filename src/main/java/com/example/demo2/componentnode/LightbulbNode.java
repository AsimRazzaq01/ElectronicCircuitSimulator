package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.LightbulbModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class LightbulbNode extends Group {
    private LightbulbModel lightbulbModel;

    public LightbulbNode(double x, double y) {
        URL imagePath = Project.class.getResource("component_sprites/lightbulb.png");
        if (imagePath != null) {
            Image lightbulbImage = new Image(imagePath.toExternalForm(), 500, 0, true, false);
            ImageView lightbulbImageView = new ImageView(lightbulbImage);
            lightbulbImageView.setFitWidth(70);
            lightbulbImageView.setPreserveRatio(true);
            lightbulbImageView.setPickOnBounds(true);
            lightbulbModel = new LightbulbModel(x, y);
            this.getChildren().add(lightbulbImageView);
            this.setLayoutX(lightbulbModel.getComponentX());
            this.setLayoutY(lightbulbModel.getComponentY());
        }
    }

    public LightbulbNode(double x, double y, double r) {
        URL imagePath = Project.class.getResource("component_sprites/lightbulb.png");
        if (imagePath != null) {
            Image lightbulbImage = new Image(imagePath.toExternalForm(), 500, 0, true, false);
            ImageView lightbulbImageView = new ImageView(lightbulbImage);
            lightbulbImageView.setFitWidth(70);
            lightbulbImageView.setPreserveRatio(true);
            lightbulbImageView.setPickOnBounds(true);
            lightbulbModel = new LightbulbModel(x, y, r);
            this.getChildren().add(lightbulbImageView);
            this.setLayoutX(lightbulbModel.getComponentX());
            this.setLayoutY(lightbulbModel.getComponentY());
        }
    }

    public LightbulbModel getLightbulbModel() {
        return lightbulbModel;
    }
}
