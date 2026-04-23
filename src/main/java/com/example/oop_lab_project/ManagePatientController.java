package com.example.oop_lab_project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagePatientController {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Integer> columnId;

    @FXML
    private TableColumn<Appointment, String> columnName;

    @FXML
    private TableColumn<Appointment, String> columnGender;

    @FXML
    private TableColumn<Appointment, String> columnIssue;

    @FXML
    private TableColumn<Appointment, String> columnMedicalHistory;

    @FXML
    private TableColumn<Appointment, String> columnMedications;

    @FXML
    private TableColumn<Appointment, String> columnRoom;

    @FXML
    private TableColumn<Appointment, String> columnTime;

    @FXML
    private TableColumn<Appointment, String> columnStatus;

    @FXML
    private TableColumn<Appointment, String> columnWard;

    @FXML
    private TableColumn<Appointment, Integer> columnBedNo;

    @FXML
    private TextField idField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField issueField;
    @FXML
    private TextField medicalHistoryField;
    @FXML
    private TextField medicationsField;
    @FXML
    private TextField roomField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TextField timeField;
    @FXML
    private TextField statusField;
    @FXML
    private TextField wardField;
    @FXML
    private TextField bedNoField;

    @FXML
    private void initialize() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnIssue.setCellValueFactory(new PropertyValueFactory<>("issue"));
        columnMedicalHistory.setCellValueFactory(new PropertyValueFactory<>("medicalHistory"));
        columnMedications.setCellValueFactory(new PropertyValueFactory<>("medications"));
        columnRoom.setCellValueFactory(new PropertyValueFactory<>("room"));
        columnTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        columnWard.setCellValueFactory(new PropertyValueFactory<>("ward"));
        columnBedNo.setCellValueFactory(new PropertyValueFactory<>("bedNumber"));

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
    private void handleInsert(ActionEvent event) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String gender = genderField.getText().trim();
        String issue = issueField.getText().trim();
        String medicalHistory = medicalHistoryField.getText().trim();
        String medications = medicationsField.getText().trim();
        String room = roomField.getText().trim();
        LocalDate date = datePicker.getValue();
        String time = timeField.getText().trim();
        String status = statusField.getText().trim();
        String ward = wardField.getText().trim();
        String bedNumber = bedNoField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID and Name fields are mandatory.");
            return;
        }

        String dateTime = date != null ? date.toString() + " " + time : "";

        if (!isValidDateTimeFormat(dateTime)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid date and time.");
            return;
        }

        String insertQuery = "INSERT INTO appointment (id, name, gender, issue, medical_history, medications, room, time, status, ward, bed_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = connectDB(); PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setInt(1, Integer.parseInt(id));
            insertStmt.setString(2, name);
            insertStmt.setString(3, gender);
            insertStmt.setString(4, issue);
            insertStmt.setString(5, medicalHistory);
            insertStmt.setString(6, medications);
            insertStmt.setString(7, room);
            insertStmt.setString(8, dateTime);
            insertStmt.setString(9, status);
            insertStmt.setString(10, ward);
            insertStmt.setInt(11, bedNumber.isEmpty() ? 0 : Integer.parseInt(bedNumber));

            int result = insertStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment inserted successfully.");
                loadAppointments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Insert Error", "Failed to insert appointment.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while inserting the appointment: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values for ID and Bed Number.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an appointment to update.");
            return;
        }

        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String gender = genderField.getText().trim();
        String issue = issueField.getText().trim();
        String medicalHistory = medicalHistoryField.getText().trim();
        String medications = medicationsField.getText().trim();
        String room = roomField.getText().trim();
        LocalDate date = datePicker.getValue();
        String time = timeField.getText().trim();
        String status = statusField.getText().trim();
        String ward = wardField.getText().trim();
        String bedNumber = bedNoField.getText().trim();

        if (id.isEmpty() || name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "ID and Name fields are mandatory.");
            return;
        }

        String dateTime = date != null ? date.toString() + " " + time : "";

        if (!isValidDateTimeFormat(dateTime)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid date and time.");
            return;
        }

        String updateQuery = "UPDATE appointment SET name = ?, gender = ?, issue = ?, medical_history = ?, medications = ?, room = ?, time = ?, status = ?, ward = ?, bed_number = ? WHERE id = ?";

        try (Connection connection = connectDB(); PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, name);
            updateStmt.setString(2, gender);
            updateStmt.setString(3, issue);
            updateStmt.setString(4, medicalHistory);
            updateStmt.setString(5, medications);
            updateStmt.setString(6, room);
            updateStmt.setString(7, dateTime);
            updateStmt.setString(8, status);
            updateStmt.setString(9, ward);
            updateStmt.setInt(10, bedNumber.isEmpty() ? 0 : Integer.parseInt(bedNumber));
            updateStmt.setInt(11, Integer.parseInt(id));

            int result = updateStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment updated successfully.");
                loadAppointments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update appointment.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the appointment: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter valid numeric values for ID and Bed Number.");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Appointment selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select an appointment to delete.");
            return;
        }

        String deleteQuery = "DELETE FROM appointment WHERE id = ?";

        try (Connection connection = connectDB();
             PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {

            deleteStmt.setInt(1, selectedAppointment.getId());

            int result = deleteStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Appointment deleted successfully.");
                loadAppointments();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete appointment.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the appointment: " + e.getMessage());
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

    private boolean isValidDateTimeFormat(String dateTime) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime.parse(dateTime, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
