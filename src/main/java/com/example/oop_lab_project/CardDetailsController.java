package com.example.oop_lab_project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CardDetailsController {

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField expiryField;

    @FXML
    private TextField cvvField;

    private AppointmentPatientController parentController;

    public void setParentController(AppointmentPatientController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void handleConfirm(ActionEvent event) {
        String cardNumber = cardNumberField.getText();
        String expiry = expiryField.getText();
        String cvv = cvvField.getText();

        if (!isValidCardNumber(cardNumber)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Card number must be exactly 16 digits.");
            return;
        }

        if (!isValidExpiryDate(expiry)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Expiry date must be in the format MM/YY.");
            return;
        }

        if (!isValidCVV(cvv)) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "CVV must be exactly 3 digits.");
            return;
        }

        // Here you can add further validation for card details if needed

        parentController.saveAppointment();
        Stage stage = (Stage) cardNumberField.getScene().getWindow();
        stage.close();
    }

    private boolean isValidCardNumber(String cardNumber) {
        return cardNumber.matches("\\d{16}");
    }

    private boolean isValidExpiryDate(String expiry) {
        return expiry.matches("(0[1-9]|1[0-2])/\\d{2}");
    }

    private boolean isValidCVV(String cvv) {
        return cvv.matches("\\d{3}");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
