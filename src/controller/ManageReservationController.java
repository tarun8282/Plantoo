package controller;

import dao.ReservationDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Reservation;

import java.time.LocalDate;
import java.util.List;

public class ManageReservationController {

    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, Integer> colGuestId;
    @FXML private TableColumn<Reservation, String> colGuestName;
    @FXML private TableColumn<Reservation, Integer> colHotelId;
    @FXML private TableColumn<Reservation, Integer> colRoomNum;
    @FXML private TableColumn<Reservation, LocalDate> colCheckIn;
    @FXML private TableColumn<Reservation, LocalDate> colCheckOut;


    @FXML private TextField txtGuestId, txtHotelId, txtRoomNum, txtSearch;
    @FXML private DatePicker dpCheckIn, dpCheckOut;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private ObservableList<Reservation> reservationList;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getReservationId()).asObject());
        colGuestId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getGuestId()).asObject());
        colGuestName.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGuestName()));
        colHotelId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getHotelId()).asObject());
        colRoomNum.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getRoomNum()).asObject());
        colCheckIn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCheckIn()));
        colCheckOut.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getCheckOut()));

        loadReservations();
    }

    private void loadReservations() {
        reservationList = FXCollections.observableArrayList(reservationDAO.getAllReservations());
        reservationTable.setItems(reservationList);
    }

    // ✅ Add Reservation
    @FXML
    private void handleAddReservation() {
        Reservation r = new Reservation();
        r.setGuestId(Integer.parseInt(txtGuestId.getText()));
        r.setHotelId(Integer.parseInt(txtHotelId.getText()));
        r.setRoomNum(Integer.parseInt(txtRoomNum.getText()));
        r.setCheckIn(dpCheckIn.getValue());
        r.setCheckOut(dpCheckOut.getValue());

        if (reservationDAO.insertReservation(r)) {
            showAlert("Reservation added successfully!");
            loadReservations();
        }
    }

    // ✅ Edit Reservation
    @FXML
    private void handleEditReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a reservation to update!");
            return;
        }

        selected.setGuestId(Integer.parseInt(txtGuestId.getText()));
        selected.setHotelId(Integer.parseInt(txtHotelId.getText()));
        selected.setRoomNum(Integer.parseInt(txtRoomNum.getText()));
        selected.setCheckIn(dpCheckIn.getValue());
        selected.setCheckOut(dpCheckOut.getValue());

        if (reservationDAO.updateReservation(selected)) {
            showAlert("Reservation updated successfully!");
            loadReservations();
        }
    }

    // ✅ Delete Reservation
    @FXML
    private void handleDeleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a reservation to delete!");
            return;
        }

        if (reservationDAO.deleteReservation(selected.getReservationId())) {
            showAlert("Reservation deleted successfully!");
            loadReservations();
        }
    }

    // ✅ Search by Guest ID or Name
    @FXML
    private void handleSearchReservation() {
        String keyword = txtSearch.getText().trim();
        if (keyword.isEmpty()) {
            showAlert("Please enter Guest ID or Guest Name to search.");
            return;
        }

        List<Reservation> results = reservationDAO.searchReservation(keyword);
        if (results.isEmpty()) {
            showAlert("No reservations found for: " + keyword);
        } else {
            reservationTable.getItems().setAll(results);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
