package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Guest;

import java.util.ArrayList;
import java.util.List;

public class GuestFormController {

    @FXML private Label lblTitle;
    @FXML private TextField txtName, txtAge, txtPhones;
    @FXML private ComboBox<String> comboGender;

    private Stage stage;
    private Guest guest;

    // -------------------- Setters --------------------
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
        if (guest != null) {
            lblTitle.setText("Edit Guest");
            txtName.setText(guest.getName());
            txtAge.setText(String.valueOf(guest.getAge()));
            comboGender.setValue(guest.getGender());
            txtPhones.setText(String.join(", ", guest.getPhoneNumbers()));
        } else {
            lblTitle.setText("Add Guest");
        }
    }

    public Guest getGuest() {
        return guest;
    }

    // -------------------- Button Handlers --------------------
    @FXML
    private void handleSave() {
        try {
            // Validate Name
            String name = txtName.getText().trim();
            if (name.isEmpty()) {
                showAlert("Name cannot be empty.");
                return;
            }

            // Validate Age
            int age = Integer.parseInt(txtAge.getText().trim());
            if (age > 99) {
                showAlert("Warning: Age is unusually high (>99).");
            }

            // Validate Gender
            String gender = comboGender.getValue();
            if (gender == null || gender.isEmpty()) {
                showAlert("Please select gender.");
                return;
            }

            // Validate Phone Numbers
            List<String> phones = new ArrayList<>();
            for (String p : txtPhones.getText().split(",")) {
                p = p.trim();
                if (!p.matches("\\d{10}")) {
                    showAlert("Phone must be exactly 10 digits: " + p);
                    return;
                }
                phones.add(p);
            }

            // Create or update Guest object
            if (guest == null) {
                guest = new Guest(0, name, age, gender, phones);
            } else {
                guest.setName(name);
                guest.setAge(age);
                guest.setGender(gender);
                guest.setPhoneNumbers(phones);
            }

            stage.close(); // Close the form

        } catch (NumberFormatException e) {
            showAlert("Age must be a valid number.");
        }
    }

    @FXML
    private void handleCancel() {
        guest = null;
        stage.close();
    }

    // -------------------- Helper --------------------
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
