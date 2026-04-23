package com.example.oop_lab_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUpFacultyController {

    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField departmentField;
    @FXML
    private TextField genderField;
    @FXML
    private TextField contactField;

    @FXML
    private void initialize() {
        roleComboBox.getItems().addAll("Admin", "Doctor", "Nurse");
    }

    @FXML
    private void handleSignUp() {
        String role = roleComboBox.getValue();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String department = departmentField.getText();
        String gender = genderField.getText();
        String contact = contactField.getText();

        if (username.isEmpty() || password.isEmpty() || department.isEmpty() || gender.isEmpty() || contact.isEmpty()) {
            showAlert(AlertType.ERROR, "Form Error!", "Please fill all the fields");
            return;
        }

        if (password.length() < 7) {
            showAlert(AlertType.ERROR, "Form Error!", "Password must be at least 7 characters long");
            return;
        }

        String tableName;
        if ("Admin".equalsIgnoreCase(role)) {
            tableName = "admin_signup";
        } else if ("Doctor".equalsIgnoreCase(role)) {
            tableName = "doctor_signup";
        } else if ("Nurse".equalsIgnoreCase(role)) {
            tableName = "nurse_signup";
        } else {
            showAlert(AlertType.ERROR, "Form Error!", "Please select a valid role");
            return;
        }

        saveToDatabase(tableName, username, password, department, gender, contact);
    }

    @FXML
    private void handleLogIn(ActionEvent event) {
        switchScene(event, "Log_in_Faculty.fxml", "Faculty Login");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        switchScene(event, "login_signup_all.fxml", "Login/Signup");
    }

    private void saveToDatabase(String tableName, String username, String password, String department, String gender, String contact) {
        String query = "INSERT INTO " + tableName + " (username, password, department, gender, contact_number) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, department);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, contact);

            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                showAlert(AlertType.INFORMATION, "Success", "Signup successful!");
            } else {
                showAlert(AlertType.ERROR, "Error", "Signup failed. Please try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(AlertType.ERROR, "Database Error", "Error occurred while saving to database.");
        }
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
            showAlert(AlertType.ERROR, "Scene Switch Error", "An error occurred while switching scenes: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
