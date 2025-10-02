package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.net.URL;

public class AdminDashboardController {

    @FXML
    private StackPane contentArea; // Center content area

    @FXML
    private VBox leftMenuContainer; // Left menu container
    // -----------------------------------
    // Load FXML into center content area
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
    // Main menu button actions
    // -----------------------------------
    @FXML
private void openManageHotels() {
    try {
        // Load manage_hotels.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/manage_hotels.fxml"));
        Parent root = loader.load();

        // Get controller to access table data
        ManageHotelsController tableController = loader.getController();

        // Set content area
        contentArea.getChildren().clear();
        contentArea.getChildren().add(root);

        

       

    } catch (Exception ex) {
        ex.printStackTrace();
    }
}

    @FXML
    private void openManageRooms() {
        loadContent("/view/manage_rooms.fxml");
    }

    @FXML
    private void openManageStaff() {
        loadContent("/view/manage_staff.fxml");
    }

    @FXML
    private void openReports() {
        loadContent("/view/reports.fxml");
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
