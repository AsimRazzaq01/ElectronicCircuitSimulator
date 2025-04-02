package com.example.demo2;

import javafx.scene.Node;

import java.util.ArrayList;

public class Project {
    int projectID;
    String projectName;
    ArrayList<Node> projectElements;

    Project(int id, String name) {
        projectID = id;
        projectName = name;
        projectElements = new ArrayList<>();
    }

    String getProjectName() {
        return projectName;
    }
}
