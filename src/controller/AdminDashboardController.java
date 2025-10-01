package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Hotel;
import javafx.scene.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.net.URL;

import dao.HotelDAO;

public class AdminDashboardController {

    @FXML
    private StackPane contentArea; // Center content area

    @FXML
    private VBox leftMenuContainer; // Left menu container

    // Backup of main menu buttons
    private ObservableList<Node> mainMenuBackup;

    @FXML
    public void initialize() {
        if (leftMenuContainer != null) {
            // Store a copy of original menu buttons
            mainMenuBackup = FXCollections.observableArrayList(leftMenuContainer.getChildren());
        } else {
            System.err.println("Warning: leftMenuContainer is null! Check fx:id in FXML.");
        }
    }

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

        // Create left menu for Manage Hotels options
        VBox hotelMenu = new VBox(15);
        hotelMenu.setStyle("-fx-background-color: #f0f2f5; -fx-padding: 20;");

        Button addHotel = new Button("Add Hotel");
        Button editHotel = new Button("Edit Hotel");
        Button deleteHotel = new Button("Delete Hotel");
        Button back = new Button("Back");

        addHotel.setPrefWidth(160);
        editHotel.setPrefWidth(160);
        deleteHotel.setPrefWidth(160);
        back.setPrefWidth(160);

        hotelMenu.getChildren().addAll(addHotel, editHotel, deleteHotel, back);

        // Replace left menu
        leftMenuContainer.getChildren().setAll(hotelMenu.getChildren());

        // -----------------------------
        // Add Hotel
        // -----------------------------
        addHotel.setOnAction(e -> {
            try {
                FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/view/hotel_form.fxml"));
                Parent formRoot = formLoader.load();
                HotelFormController formController = formLoader.getController();
                formController.setMode("add");

                Stage stage = new Stage();
                stage.setTitle("Add Hotel");
                stage.setScene(new Scene(formRoot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                // Refresh table
                tableController.loadHotels();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // -----------------------------
        // Edit Hotel
        // -----------------------------
        editHotel.setOnAction(e -> {
            Hotel selected = tableController.getSelectedHotel();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a hotel to edit.").showAndWait();
                return;
            }

            try {
                FXMLLoader formLoader = new FXMLLoader(getClass().getResource("/view/hotel_form.fxml"));
                Parent formRoot = formLoader.load();
                HotelFormController formController = formLoader.getController();
                formController.setMode("edit");
                formController.setHotel(selected);

                Stage stage = new Stage();
                stage.setTitle("Edit Hotel");
                stage.setScene(new Scene(formRoot));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

                tableController.loadHotels();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // -----------------------------
        // Delete Hotel
        // -----------------------------
        deleteHotel.setOnAction(e -> {
            Hotel selected = tableController.getSelectedHotel();
            if (selected == null) {
                new Alert(Alert.AlertType.WARNING, "Select a hotel to delete.").showAndWait();
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this hotel?");
            if (confirm.showAndWait().get() == ButtonType.OK) {
                HotelDAO.deleteHotel(selected.getHotelId());
                tableController.loadHotels();
            }
        });

        // -----------------------------
        // Back button restores main menu
        // -----------------------------
        back.setOnAction(e -> {
            if (mainMenuBackup != null) {
                leftMenuContainer.getChildren().setAll(mainMenuBackup);
            }
            // Optionally, reset content area
            contentArea.getChildren().clear();
            contentArea.getChildren().add(new Label("Welcome, Admin!"));
        });

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
