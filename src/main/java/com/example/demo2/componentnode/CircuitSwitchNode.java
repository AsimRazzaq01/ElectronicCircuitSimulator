package com.example.demo2.componentnode;

import com.example.demo2.Project;
import com.example.demo2.componentmodel.CircuitSwitchModel;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;

public class CircuitSwitchNode extends Group {
    private CircuitSwitchModel switchModel;

    private Image CLOSED_SWITCH_IMAGE;
    private Image OPEN_SWITCH_IMAGE;
    private ImageView switchImageView;

    public CircuitSwitchNode(double x, double y) {
        URL OPEN_SWITCH_PATH = Project.class.getResource("component_sprites/switch_opened.png");
        URL CLOSED_SWITCH_PATH = Project.class.getResource("component_sprites/switch_closed.png");
        if (CLOSED_SWITCH_PATH != null && OPEN_SWITCH_PATH != null) {
            CLOSED_SWITCH_IMAGE = new Image(CLOSED_SWITCH_PATH.toExternalForm(),500, 0,true,false);
            OPEN_SWITCH_IMAGE = new Image(OPEN_SWITCH_PATH.toExternalForm(),500,0,true,false);
            switchModel = new CircuitSwitchModel(x, y);
            switchImageView = new ImageView(CLOSED_SWITCH_IMAGE);
            this.getChildren().add(switchImageView);
            switchImageView.setFitWidth(70);
            switchImageView.setPreserveRatio(true);
            switchImageView.setPickOnBounds(true);
            this.setLayoutX(switchModel.getComponentX());
            this.setLayoutY(switchModel.getComponentY());
        }
    }

    public CircuitSwitchNode(double x, double y, boolean active) {
        URL OPEN_SWITCH_PATH = Project.class.getResource("component_sprites/switch_opened.png");
        URL CLOSED_SWITCH_PATH = Project.class.getResource("component_sprites/switch_closed.png");
        if (CLOSED_SWITCH_PATH != null && OPEN_SWITCH_PATH != null) {
            CLOSED_SWITCH_IMAGE = new Image(CLOSED_SWITCH_PATH.toExternalForm(),500, 0,true,false);
            OPEN_SWITCH_IMAGE = new Image(OPEN_SWITCH_PATH.toExternalForm(),500,0,true,false);
            switchModel = new CircuitSwitchModel(x, y, active);
            switchImageView = new ImageView();
            switchImageView.setFitWidth(70);
            switchImageView.setPreserveRatio(true);
            setSwitchImageState();
            this.getChildren().add(switchImageView);
            switchImageView.setPickOnBounds(true);
            this.setLayoutX(switchModel.getComponentX());
            this.setLayoutY(switchModel.getComponentY());
        }
    }

    public CircuitSwitchModel getSwitchModel() {
        return switchModel;
    }

    public void setSwitchImageState() {
        if (switchModel.isActive()) {
            switchImageView.setImage(OPEN_SWITCH_IMAGE);
        }
        else {
            switchImageView.setImage(CLOSED_SWITCH_IMAGE);
        }
    }
}
