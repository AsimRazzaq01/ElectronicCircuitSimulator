package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.LightbulbModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * LightbulbNode displays a lightbulb image and its terminals.
 * It swaps between unlit and lit images based on terminal voltages.
 */
public class LightbulbNode extends Group {
    private final LightbulbModel lightbulbModel;
    private final ImageView bulbView;
    private final Image unlitImg;
    private final Image litImg;
    private final TerminalNode negative;
    private final TerminalNode positive;

    /**
     * Constructs an unlit lightbulb at (x, y).
     */
    public LightbulbNode(double x, double y) {
        // Load images
        unlitImg = new Image(
                Project.class.getResource("component_sprites/lightbulb.png").toExternalForm(),
                70, 0, true, true
        );
        litImg = new Image(
                Project.class.getResource("component_sprites/litLightbulb.png").toExternalForm(),
                70, 0, true, true
        );

        // Create ImageView for the bulb
        bulbView = new ImageView(unlitImg);
        bulbView.setFitWidth(70);
        bulbView.setPreserveRatio(true);

        // Initialize model and terminals
        lightbulbModel = new LightbulbModel(x, y);
        negative = new TerminalNode(lightbulbModel, 62, 95, "Negative");
        positive = new TerminalNode(lightbulbModel, 35, 125, "Positive");

        // Add to group
        getChildren().addAll(bulbView, negative, positive);
        setLayoutX(x);
        setLayoutY(y);
    }

    /**
     * Constructs a lightbulb at (x, y) with custom resistance.
     */
    public LightbulbNode(double x, double y, double r) {
        this(x, y);
        lightbulbModel.setResistance(r);
    }

    /**
     * Update the bulb image based on terminal voltages. Call after simulation.
     */
    public void updateVisualState() {
        double vNeg = negative.getTerminalModel().getVoltage();
        double vPos = positive.getTerminalModel().getVoltage();
        boolean powered = (vNeg != vPos);
        bulbView.setImage(powered ? litImg : unlitImg);
    }

    // Getters
    public LightbulbModel getLightbulbModel() {
        return lightbulbModel;
    }

    public TerminalNode getNegative() {
        return negative;
    }

    public TerminalNode getPositive() {
        return positive;
    }
}
