package com.example.oop_lab_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.Node;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManageAppointmentTimeController {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> columnId;

    @FXML
    private TableColumn<Appointment, String> columnName;

    @FXML
    private TableColumn<Appointment, String> columnIssue;

    @FXML
    private TableColumn<Appointment, String> columnMedicalHistory;

    @FXML
    private TableColumn<Appointment, String> columnMedication;

    @FXML
    private TableColumn<Appointment, String> columnGender;

    @FXML
    private TableColumn<Appointment, String> columnStatus;

    @FXML
    private TableColumn<Appointment, String> columnWard;

    @FXML
    private TableColumn<Appointment, Integer> columnBedNo;

    @FXML
    private TableColumn<Appointment, String> columnTime;

    @FXML
    private TextField timeField;

    @FXML
    private void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnIssue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        columnMedicalHistory.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        columnMedication.setCellValueFactory(new PropertyValueFactory<>("medications"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnWard.setCellValueFactory(new PropertyValueFactory<>("ward"));
        columnBedNo.setCellValueFactory(new PropertyValueFactory<>("bedNumber"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        loadAppointments();
    }

    private void loadAppointments() {
        String query = "SELECT * FROM appointment";
        ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();

        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Appointment appointment = new Appointment(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getString("issue"),
                        resultSet.getString("medical_history"),
                        resultSet.getString("medications"),
                        resultSet.getString("room"),
                        resultSet.getString("time"),
                        resultSet.getString("status"),
                        resultSet.getString("ward"),
                        resultSet.getInt("bed_number")
                );
                appointmentList.add(appointment);
            }

            appointmentTable.setItems(appointmentList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching appointment data.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an appointment to update.");
            return;
        }

        String newTime = timeField.getText().trim();

        if (newTime.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error", "Please enter a time.");
            return;
        }

        String updateQuery = "UPDATE appointment SET time = ? WHERE id = ?";

        try (Connection connection = connectDB();
             PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            updateStmt.setString(1, newTime);
            updateStmt.setInt(2, selectedAppointment.getId());

            int result = updateStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Time updated successfully.");
                loadAppointments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update time.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the time.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        switchScene(event, "Main-Admin.fxml", "Admin Main");
    }

    private Connection connectDB() throws SQLException {
        return Database.getConnection();
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
            showAlert(Alert.AlertType.ERROR, "Scene Switch Error", "An error occurred while switching scenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
