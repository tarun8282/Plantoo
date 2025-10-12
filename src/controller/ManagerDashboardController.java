package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class ManagerDashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void handleLogout() {
        // Implement logout logic
        System.out.println("Logout Clicked");
    }

    @FXML
    private void openViewHotels() {
        // Open view-only hotels UI
        System.out.println("View Hotels Clicked");
        // Example: load a FXML for viewing hotels
    }

    @FXML
    private void openManageRooms() {
        System.out.println("Manage Rooms Clicked");
        // Load Manage Rooms FXML
    }

    @FXML
    private void openManageGuests() {
        System.out.println("Manage Guests Clicked");
        // Load Manage Guests FXML
    }

    @FXML
    private void openManageReservations() {
        System.out.println("Manage Reservations Clicked");
        // Load Manage Reservations FXML
    }

    @FXML
    private void openManageDepartments() {
        System.out.println("Manage Departments Clicked");
        // Load Manage Departments FXML
    }

    @FXML
    private void openManageStaff() {
        System.out.println("Manage Staff Clicked");
        // Load Manage Staff FXML
    }
}
