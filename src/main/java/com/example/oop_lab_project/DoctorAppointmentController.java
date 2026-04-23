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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DoctorAppointmentController {

    @FXML
    private TableView<DoctorAppointment> appointmentTable;

    @FXML
    private TableColumn<DoctorAppointment, Integer> columnId;

    @FXML
    private TableColumn<DoctorAppointment, String> columnName;

    @FXML
    private TableColumn<DoctorAppointment, String> columnGender;

    @FXML
    private TableColumn<DoctorAppointment, String> columnIssue;

    @FXML
    private TableColumn<DoctorAppointment, String> columnMedicalHistory;

    @FXML
    private TableColumn<DoctorAppointment, String> columnMedications;

    @FXML
    private TableColumn<DoctorAppointment, String> columnRoom;

    @FXML
    private TableColumn<DoctorAppointment, String> columnTime;

    @FXML
    private TableColumn<DoctorAppointment, String> columnStatus;

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

        loadAppointments();
    }

    private void loadAppointments() {
        String query = "SELECT * FROM appointment";
        ObservableList<DoctorAppointment> appointmentList = FXCollections.observableArrayList();

        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                DoctorAppointment appointment = new DoctorAppointment(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("gender"),
                        resultSet.getString("issue"),
                        resultSet.getString("medical_history"),
                        resultSet.getString("medications"),
                        resultSet.getString("room"),
                        resultSet.getTimestamp("time").toString(),
                        resultSet.getString("status")
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
    private void handleBack(ActionEvent event) {
        switchScene(event, "Main-Doc.fxml", "Doctor Dashboard");
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

    private Connection connectDB() throws SQLException {
        return Database.getConnection();
    }
}
