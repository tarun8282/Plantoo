package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Alert.AlertType;

import model.Guest;
import dao.GuestDAO;
import util.DBConnection;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ManageGuestController {

    @FXML private TableView<Guest> tblGuests;
    @FXML private TableColumn<Guest, Integer> colGuestId;
    @FXML private TableColumn<Guest, String> colGuestName;
    @FXML private TableColumn<Guest, Integer> colGuestAge;
    @FXML private TableColumn<Guest, String> colGuestGender;
    @FXML private TableColumn<Guest, String> colGuestPhone;

    @FXML private TextField txtSearchGuest;

    private GuestDAO guestDAO;
    private ObservableList<Guest> guestList;

    @FXML
    public void initialize() {
        // Map columns
        colGuestId.setCellValueFactory(new PropertyValueFactory<>("guestId"));
        colGuestName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colGuestAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colGuestGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colGuestPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumbersAsString"));

        try {
            Connection conn = DBConnection.getConnection();
            guestDAO = new GuestDAO(conn);
            loadGuests();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadGuests() {
        try {
            List<Guest> guests = guestDAO.getAllGuests();
            guestList = FXCollections.observableArrayList(guests);
            tblGuests.setItems(guestList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ----------------- Button Handlers -----------------

    @FXML
    private void handleAddGuest() {
        Guest newGuest = showGuestDialog(null);
        if (newGuest != null) {
            try {
                guestDAO.addGuest(newGuest);
                loadGuests();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to add guest", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleEditGuest() {
        Guest selectedGuest = tblGuests.getSelectionModel().getSelectedItem();
        if (selectedGuest == null) {
            showAlert("Warning", "Please select a guest to edit", AlertType.WARNING);
            return;
        }

        Guest updatedGuest = showGuestDialog(selectedGuest);
        if (updatedGuest != null) {
            try {
                guestDAO.updateGuest(updatedGuest);
                loadGuests();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to update guest", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleDeleteGuest() {
        Guest selectedGuest = tblGuests.getSelectionModel().getSelectedItem();
        if (selectedGuest == null) {
            showAlert("Warning", "Please select a guest to delete", AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Delete");
        confirm.setHeaderText("Are you sure you want to delete this guest?");
        confirm.setContentText(selectedGuest.getName());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                guestDAO.deleteGuest(selectedGuest.getGuestId());
                loadGuests();
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Failed to delete guest", AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleSearchGuest() {
        String keyword = txtSearchGuest.getText().toLowerCase();
        if (keyword.isEmpty()) {
            tblGuests.setItems(guestList);
            return;
        }

        ObservableList<Guest> filtered = FXCollections.observableArrayList();
        for (Guest g : guestList) {
            if (g.getName().toLowerCase().contains(keyword) ||
                String.valueOf(g.getGuestId()).contains(keyword)) {
                filtered.add(g);
            }
        }

        tblGuests.setItems(filtered);
    }

    // ----------------- Guest Form Dialog -----------------

    private Guest showGuestDialog(Guest guest) {
        Dialog<Guest> dialog = new Dialog<>();
        dialog.setTitle(guest == null ? "Add Guest" : "Edit Guest");

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField txtName = new TextField();
        txtName.setPromptText("Name");

        TextField txtAge = new TextField();
        txtAge.setPromptText("Age");

        ComboBox<String> comboGender = new ComboBox<>();
        comboGender.getItems().addAll("Male", "Female", "Other");
        comboGender.setPromptText("Select Gender");

        TextField txtPhones = new TextField();
        txtPhones.setPromptText("Phone numbers (comma separated)");

        if (guest != null) {
            txtName.setText(guest.getName());
            txtAge.setText(String.valueOf(guest.getAge()));
            comboGender.setValue(guest.getGender());
            txtPhones.setText(String.join(", ", guest.getPhoneNumbers()));
        }

        grid.add(new Label("Name:"), 0, 0);
        grid.add(txtName, 1, 0);
        grid.add(new Label("Age:"), 0, 1);
        grid.add(txtAge, 1, 1);
        grid.add(new Label("Gender:"), 0, 2);
        grid.add(comboGender, 1, 2);
        grid.add(new Label("Phones:"), 0, 3);
        grid.add(txtPhones, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                try {
                    String name = txtName.getText().trim();
                    if (name.isEmpty()) {
                        showAlert("Error", "Name cannot be empty.", AlertType.ERROR);
                        return null;
                    }

                    int age = Integer.parseInt(txtAge.getText().trim());
                    if (age > 99) {
                        showAlert("Warning", "Age is unusually high (>99).", AlertType.WARNING);
                    }

                    String gender = comboGender.getValue();
                    if (gender == null || gender.isEmpty()) {
                        showAlert("Error", "Please select a gender.", AlertType.ERROR);
                        return null;
                    }

                    List<String> phones = new ArrayList<>();
                    for (String p : txtPhones.getText().split(",")) {
                        p = p.trim();
                        if (!p.matches("\\d{10}")) {
                            showAlert("Error", "Phone numbers must be exactly 10 digits: " + p, AlertType.ERROR);
                            return null;
                        }
                        phones.add(p);
                    }

                    if (guest == null) {
                        return new Guest(0, name, age, gender, phones);
                    } else {
                        guest.setName(name);
                        guest.setAge(age);
                        guest.setGender(gender);
                        guest.setPhoneNumbers(phones);
                        return guest;
                    }

                } catch (NumberFormatException e) {
                    showAlert("Error", "Age must be a valid number.", AlertType.ERROR);
                    return null;
                }
            }
            return null;
        });

        Optional<Guest> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showAlert(String title, String message, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
