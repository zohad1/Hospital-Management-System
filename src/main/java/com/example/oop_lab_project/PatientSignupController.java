package com.example.oop_lab_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatientSignupController {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField departmentField;

    @FXML
    private TextField genderField;

    @FXML
    private TextField contactField;

    @FXML
    private Button signUpButton;

    @FXML
    private Button logInButton;

    @FXML
    private Button backButton;

    private Connection connectDB() throws SQLException {
        return Database.getConnection();
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = departmentField.getText();
        String gender = genderField.getText();
        String contactNumber = contactField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || gender.isEmpty() || contactNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Passwords do not match");
            return;
        }

        if (password.length() < 7) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Password must be at least 7 characters long");
            return;
        }

        String insertQuery = "INSERT INTO patient_signup (username, password, gender, contact_number) VALUES (?, ?, ?, ?)";

        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, contactNumber);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "Patient registered successfully");
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", "Failed to register patient");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database");
        }
    }

    @FXML
    private void handleLogIn(ActionEvent event) {
        switchScene(event, "Patient_login.fxml", "Patient Login");
    }

    @FXML
    private void handleBack(ActionEvent event) {
        switchScene(event, "login_signup_all.fxml", "Login/Signup");
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
