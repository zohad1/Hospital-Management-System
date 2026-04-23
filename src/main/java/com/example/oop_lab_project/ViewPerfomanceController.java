package com.example.oop_lab_project;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPerfomanceController {

    @FXML
    private LineChart<String, Number> lineChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private void initialize() {
        loadChartData();
    }

    private void loadChartData() {
        String query = "SELECT DATE_FORMAT(time, '%Y-%m-%d') as appointment_date, COUNT(*) as patient_count FROM appointment GROUP BY appointment_date ORDER BY appointment_date";
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Number of Patients");

        try (Connection connection = connectDB(); PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String date = resultSet.getString("appointment_date");
                int count = resultSet.getInt("patient_count");
                series.getData().add(new XYChart.Data<>(date, count));
            }

            lineChart.getData().add(series);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "An error occurred while fetching appointment data.");
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
