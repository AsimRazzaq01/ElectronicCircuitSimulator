package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.ResistorModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * ResistorNode class -> 1 var  initialized from resistor Model class -> 2 methods , 1 get resistor method
 */
public class ResistorNode extends Group {
    // Private variable - resistor class
    private ResistorModel resistorModel;
    TerminalNode negative;
    TerminalNode positive;

    // ResistorNode method 1
    public ResistorNode(double x, double y) {
        URL imagePath = Project.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            Image resistorImage = new Image(imagePath.toExternalForm(),500, 0, true, false);
            ImageView resistorImageView = new ImageView(resistorImage);
            resistorImageView.setFitWidth(70);
            resistorImageView.setLayoutX(20);
            resistorImageView.setPreserveRatio(true);
            resistorModel = new ResistorModel(x, y);
            negative = new TerminalNode(resistorModel, 10, 16.4, "Negative");
            positive = new TerminalNode(resistorModel, 100, 16.4, "Positive");
            this.getChildren().add(resistorImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            this.setLayoutX(resistorModel.getComponentX());
            this.setLayoutY(resistorModel.getComponentY());
        }
    }

    // ResistorNode method 2
    public ResistorNode(double x, double y, double r) {
        URL imagePath = Project.class.getResource("component_sprites/resistor_default.png");
        if (imagePath != null) {
            Image resistorImage = new Image(imagePath.toExternalForm(),500, 0, true, false);
            ImageView resistorImageView = new ImageView(resistorImage);
            resistorImageView.setFitWidth(70);
            resistorImageView.setPreserveRatio(true);
            resistorImageView.setLayoutX(20);
            resistorModel = new ResistorModel(x, y, r);
            negative = new TerminalNode(resistorModel, 10, 16.4, "Negative");
            positive = new TerminalNode(resistorModel, 100, 16.4, "Positive");
            this.getChildren().add(resistorImageView);
            this.getChildren().add(negative);
            this.getChildren().add(positive);
            this.setLayoutX(resistorModel.getComponentX());
            this.setLayoutY(resistorModel.getComponentY());
        }
    }

    // getter method
    public ResistorModel getResistorModel() {
        return resistorModel;
    }

    public TerminalNode getNegative() {
        return negative;
    }

    public TerminalNode getPositive() {
        return positive;
    }
} // End ResistorNode class
