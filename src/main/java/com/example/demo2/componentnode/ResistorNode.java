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
            resistorImageView.setLayoutX(20);
            resistorImageView.setPreserveRatio(true);
            resistorModel = new ResistorModel(x, y);
            TerminalNode negative = new TerminalNode(resistorModel, 10, 16.4, "Negative");
            TerminalNode positive = new TerminalNode(resistorModel, 100, 16.4, "Positive");
            this.getChildren().add(resistorImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
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
            resistorImageView.setLayoutX(20);
            resistorModel = new ResistorModel(x, y, r);
            TerminalNode negative = new TerminalNode(resistorModel, 10, 16.4, "Negative");
            TerminalNode positive = new TerminalNode(resistorModel, 100, 16.4, "Positive");
            this.getChildren().add(resistorImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            this.setLayoutX(resistorModel.getComponentX());
            this.setLayoutY(resistorModel.getComponentY());
        }
    }

    public ResistorModel getResistorModel() {
        return resistorModel;
    }
}
