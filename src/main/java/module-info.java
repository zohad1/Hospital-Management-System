module com.example.oop_lab_project {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;

    opens com.example.oop_lab_project to javafx.fxml;
    exports com.example.oop_lab_project;
}