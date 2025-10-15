package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import util.AuthUtil;

public class LoginController {

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField guestIdField;

    @FXML
    private HBox guestIdBox;

    @FXML
    public void initialize() {
        // Add roles
        roleComboBox.getItems().addAll("Admin", "Manager", "Guest");
        guestIdBox.setVisible(false); // Hide Guest ID field by default
    }

    @FXML
    private void handleRoleSelection() {
        String role = roleComboBox.getValue();

        // Show Guest ID field only for Guest role
        if ("Guest".equals(role)) {
            guestIdBox.setVisible(true);
            passwordField.setPromptText("Enter Guest Password");
        } else {
            guestIdBox.setVisible(false);
            passwordField.setPromptText("Enter " + role + " Password");
        }
    }

    @FXML
    private void handleLogin() {
        String role = roleComboBox.getValue();
        String password = passwordField.getText();

        if (role == null || role.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Missing Role", "Please select a role before logging in.");
            return;
        }

        // Guest login (needs both ID + password)
        if ("Guest".equals(role)) {
            String guestIdText = guestIdField.getText();

            if (guestIdText.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Incomplete Fields", "Please enter both Guest ID and Password.");
                return;
            }

            try {
                int guestId = Integer.parseInt(guestIdText);

                if (AuthUtil.authenticateGuest(guestId, password)) {
                    showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome Guest " + guestId + "!");
                    openGuestDashboard(guestId);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid Guest ID or Password!");
                }

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Guest ID must be a number.");
            }

        } else {
            // Admin or Manager login
            if (password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Missing Password", "Please enter your password.");
                return;
            }

            if (AuthUtil.authenticate(role, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + role + "!");
                openDashboard(role);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials!");
            }
        }
    }

    // ----------- OPEN DASHBOARD METHODS ------------

    private void openDashboard(String role) {
        try {
            String fxmlFile = switch (role) {
                case "Admin" -> "/view/admin_dashboard.fxml";
                case "Manager" -> "/view/manager_dashboard.fxml";
                default -> "/view/guest_dashboard.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) roleComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(role + " Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openGuestDashboard(int guestId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/guest_dashboard.fxml"));
            Parent root = loader.load();

            // Get controller and pass guestId
            GuestPanelController guestController = loader.getController();
            guestController.setGuestId(guestId);

            Stage stage = (Stage) roleComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Guest Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------- ALERT METHOD ------------

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
