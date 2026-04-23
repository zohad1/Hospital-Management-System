package com.example.oop_lab_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class AppointmentPatientController {

    @FXML
    private TextField nameField;

    @FXML
    private TextField genderField;

    @FXML
    private TextArea issueField;

    @FXML
    private TextArea medicalHistoryField;

    @FXML
    private TextArea medicationsField;

    @FXML
    private ComboBox<String> doctorComboBox;

    @FXML
    private ComboBox<String> departmentComboBox;

    private String name;
    private String gender;
    private String issue;
    private String medicalHistory;
    private String medications;
    private String selectedDoctor;

    @FXML
    private void initialize() {
        populateDepartmentComboBox();
    }

    @FXML
    private void handleRequest(ActionEvent event) {
        name = nameField.getText();
        gender = genderField.getText();
        issue = issueField.getText();
        medicalHistory = medicalHistoryField.getText();
        medications = medicationsField.getText();
        selectedDoctor = doctorComboBox.getValue();

        if (name.isEmpty() || gender.isEmpty() || issue.isEmpty() || selectedDoctor == null) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please fill all the required fields and select a doctor.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CardDetails.fxml"));
            Parent root = loader.load();
            CardDetailsController controller = loader.getController();
            controller.setParentController(this);

            Stage stage = new Stage();
            stage.setTitle("Enter Card Details");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while opening the card details window.");
            e.printStackTrace();
        }
    }

    public void saveAppointment() {
        String query = "INSERT INTO appointment (name, gender, issue, medical_history, medications, time, status) VALUES (?, ?, ?, ?, ?, ?, 'Pending')";
        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, gender);
            preparedStatement.setString(3, issue);
            preparedStatement.setString(4, medicalHistory);
            preparedStatement.setString(5, medications);
            preparedStatement.setTimestamp(6, Timestamp.valueOf("2024-01-01 00:00:00"));  // Set the default timestamp

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment request submitted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit appointment request. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database.");
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        switchScene(event, "Main-Patient.fxml", "Patient Dashboard");
    }

    private void populateDepartmentComboBox() {
        String query = "SELECT DISTINCT department FROM doctor_signup";
        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> departmentList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                departmentList.add(resultSet.getString("department"));
            }
            departmentComboBox.setItems(departmentList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching department data.");
        }
    }

    @FXML
    private void handleDepartmentSelection(ActionEvent event) {
        String selectedDepartment = departmentComboBox.getValue();
        if (selectedDepartment != null) {
            populateDoctorComboBox(selectedDepartment);
        }
    }

    private void populateDoctorComboBox(String department) {
        String query = "SELECT username FROM doctor_signup WHERE department = ?";
        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, department);
            ResultSet resultSet = preparedStatement.executeQuery();
            ObservableList<String> doctorList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                doctorList.add(resultSet.getString("username"));
            }
            doctorComboBox.setItems(doctorList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching doctor data.");
        }
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
