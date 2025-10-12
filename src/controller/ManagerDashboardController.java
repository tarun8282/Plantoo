package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.net.URL;

public class ManagerDashboardController {

    @FXML
    private StackPane contentArea; // Center content area

    @FXML
    private VBox leftMenuContainer; // Left menu container (if you have a sidebar)

    // -----------------------------------
    // Utility: Load FXML into content area
    // -----------------------------------
    private void loadContent(String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("FXML not found: " + fxmlPath);
                return;
            }

            Parent pane = FXMLLoader.load(resource);

            if (contentArea != null) {
                contentArea.getChildren().clear();
                contentArea.getChildren().add(pane);
            } else {
                System.err.println("contentArea is null! Check fx:id in FXML.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------
    // Menu button actions
    // -----------------------------------

    @FXML
    private void openViewHotels() {
        System.out.println("Opening view_hotels.fxml...");
        loadContent("/view/view_hotels.fxml");
    }

    @FXML
    private void openManageRooms() {
        System.out.println("Opening manage_rooms.fxml...");
        loadContent("/view/manage_rooms.fxml");
    }

    @FXML
    private void openManageGuests() {
        System.out.println("Opening manage_guests.fxml...");
        loadContent("/view/manage_guests.fxml");
    }

    @FXML
    private void openManageReservations() {
        System.out.println("Opening manage_reservations.fxml...");
        loadContent("/view/manage_reservations.fxml");
    }

    @FXML
    private void openManageDepartments() {
        System.out.println("Opening manage_departments.fxml...");
        loadContent("/view/manage_departments.fxml");
    }

    @FXML
    private void openManageStaff() {
        System.out.println("Opening manage_staff.fxml...");
        loadContent("/view/manage_staff.fxml");
    }

    // -----------------------------------
    // Logout
    // -----------------------------------
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            URL resource = getClass().getResource("/view/login.fxml");
            if (resource == null) {
                System.err.println("login.fxml not found!");
                return;
            }
            Parent root = FXMLLoader.load(resource);
            Node source = (Node) event.getSource();
            source.getScene().setRoot(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

