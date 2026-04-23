package com.example.oop_lab_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;

public class MainAdminController {

    @FXML
    private void handleManageDoctor(ActionEvent event) {
        switchScene(event, "ManageDoctor.fxml", "Manage Doctor");
    }

    @FXML
    private void handleManageNurse(ActionEvent event) {
        switchScene(event, "ManageNurse.fxml", "Manage Nurse");
    }

    @FXML
    private void handleManagePatient(ActionEvent event) {
        switchScene(event, "ManagePatient.fxml", "Manage Patient");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        switchScene(event, "main.fxml", "main");
    }

    @FXML
    private void handleViewPerformance(ActionEvent event) {
        switchScene(event, "ViewPerfomance.fxml", "View Performance");
    }

    @FXML
    private void handleManageAppointmentTime(ActionEvent event) {
        switchScene(event, "ManageAppointmentTime.fxml", "Manage Appointment Time");
    }

    @FXML
    private void handleManageRooms(ActionEvent event) {
        switchScene(event, "ManageRoom.fxml", "Manage Rooms");
    }

    private void switchScene(ActionEvent event, String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Scene Switch Error", "An error occurred while switching scenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
