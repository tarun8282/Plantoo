package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import util.AuthUtil;

public class LoginController {

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    public void initialize() {
        roleComboBox.getItems().addAll("Admin", "Manager", "Guest");
    }

    @FXML
    private void handleLogin() {
        String role = roleComboBox.getValue();
        String password = passwordField.getText();

        if (AuthUtil.authenticate(role, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + role + "!");
            openDashboard(role);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials!");
        }
    }

    private void openDashboard(String role) {
        try {
            String fxmlFile = switch (role) {
                case "Admin" -> "/view/admin_dashboard.fxml";
                case "Manager" -> "/view/manager_dashboard.fxml";
                default -> "/view/guest_dashboard.fxml";
            };

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) roleComboBox.getScene().getWindow(); // close current window
            stage.setScene(new Scene(root));
            stage.setTitle(role + " Dashboard");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
