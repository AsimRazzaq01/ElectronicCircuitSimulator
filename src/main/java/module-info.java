module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.prefs;

    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.example.demo2.componentmodel;
    exports com.example.demo2.componentnode;
    opens com.example.demo2.componentmodel to javafx.fxml;
    exports com.example.demo2.projectactions;
    opens com.example.demo2.projectactions to javafx.fxml;
}