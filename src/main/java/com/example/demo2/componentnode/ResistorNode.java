package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.ResistorModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class ResistorNode extends Group {
    private ResistorModel resistorModel;

    public ResistorNode(double x, double y) {
        URL imagePath = Project.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            Image resistorImage = new Image(imagePath.toExternalForm(),500, 0, true, false);
            ImageView resistorImageView = new ImageView(resistorImage);
            resistorImageView.setFitWidth(70);
            resistorImageView.setPreserveRatio(true);
            resistorModel = new ResistorModel(x, y);
            this.getChildren().add(resistorImageView);
            this.setLayoutX(resistorModel.getComponentX());
            this.setLayoutY(resistorModel.getComponentY());
        }
    }

    public ResistorNode(double x, double y, double r) {
        URL imagePath = Project.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            Image resistorImage = new Image(imagePath.toExternalForm(),500, 0, true, false);
            ImageView resistorImageView = new ImageView(resistorImage);
            resistorImageView.setFitWidth(70);
            resistorImageView.setPreserveRatio(true);
            resistorModel = new ResistorModel(x, y, r);
            this.getChildren().add(resistorImageView);
            this.setLayoutX(resistorModel.getComponentX());
            this.setLayoutY(resistorModel.getComponentY());
        }
    }

    public ResistorModel getResistorModel() {
        return resistorModel;
    }
}
