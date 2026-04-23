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

public class ManageNurseController {

    @FXML
    private TableView<Nurse> nurseTable;

    @FXML
    private TableColumn<Nurse, String> columnName;

    @FXML
    private TableColumn<Nurse, String> columnDepartment;

    @FXML
    private TableColumn<Nurse, String> columnGender;

    @FXML
    private TableColumn<Nurse, String> columnContact;

    @FXML
    private TextField nameField;

    @FXML
    private TextField departmentField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField contactField;

    @FXML
    private void initialize() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnDepartment.setCellValueFactory(new PropertyValueFactory<>("department"));
        columnGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        columnContact.setCellValueFactory(new PropertyValueFactory<>("contactNumber"));

        loadNurses();
    }

    private void loadNurses() {
        String query = "SELECT username, department, gender, contact_number FROM nurse_signup";
        ObservableList<Nurse> nurseList = FXCollections.observableArrayList();

        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Nurse nurse = new Nurse(
                        resultSet.getString("username"),
                        resultSet.getString("department"),
                        resultSet.getString("gender"),
                        resultSet.getString("contact_number")
                );
                nurseList.add(nurse);
            }

            nurseTable.setItems(nurseList);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while fetching nurse data.");
        }
    }

    @FXML
    private void handleInsert(ActionEvent event) {
        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String gender = genderField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Name and Contact fields are mandatory.");
            return;
        }

        String insertQuery = "INSERT INTO nurse_signup (username, department, gender, contact_number) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectDB(); PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, department);
            insertStmt.setString(3, gender);
            insertStmt.setString(4, contact);

            int result = insertStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Nurse inserted successfully.");
                loadNurses();
            } else {
                showAlert(Alert.AlertType.ERROR, "Insert Error", "Failed to insert nurse.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while inserting the nurse.");
        }
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        Nurse selectedNurse = nurseTable.getSelectionModel().getSelectedItem();

        if (selectedNurse == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a nurse to update.");
            return;
        }

        String name = nameField.getText().trim();
        String department = departmentField.getText().trim();
        String gender = genderField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || contact.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Name and Contact fields are mandatory.");
            return;
        }

        String updateQuery = "UPDATE nurse_signup SET username = ?, department = ?, gender = ?, contact_number = ? WHERE username = ?";

        try (Connection connection = connectDB(); PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {
            updateStmt.setString(1, name);
            updateStmt.setString(2, department);
            updateStmt.setString(3, gender);
            updateStmt.setString(4, contact);
            updateStmt.setString(5, selectedNurse.getUsername());

            int result = updateStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Nurse updated successfully.");
                loadNurses();
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update nurse.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while updating the nurse.");
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        Nurse selectedNurse = nurseTable.getSelectionModel().getSelectedItem();

        if (selectedNurse == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a nurse to delete.");
            return;
        }

        String deleteQuery = "DELETE FROM nurse_signup WHERE username = ?";

        try (Connection connection = connectDB(); PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
            deleteStmt.setString(1, selectedNurse.getUsername());

            int result = deleteStmt.executeUpdate();
            if (result > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Nurse deleted successfully.");
                loadNurses();
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete nurse.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while deleting the nurse.");
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
