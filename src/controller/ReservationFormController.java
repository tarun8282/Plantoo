package controller;

import dao.ReservationDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Reservation;

public class ReservationFormController {

    @FXML private Label lblTitle;
    @FXML private TextField txtGuestId, txtHotelId, txtRoomNum;
    @FXML private DatePicker dpCheckIn, dpCheckOut;
    @FXML private Button btnSave;

    private ReservationDAO reservationDAO = new ReservationDAO();
    private Reservation currentReservation = null; // null = Add mode, not null = Edit mode

    // ✅ Called by parent to set Edit mode
    public void setReservation(Reservation reservation) {
        this.currentReservation = reservation;
        if (reservation != null) {
            lblTitle.setText("✏ Edit Reservation");
            btnSave.setText("💾 Update");
            txtGuestId.setText(String.valueOf(reservation.getGuestId()));
            txtHotelId.setText(String.valueOf(reservation.getHotelId()));
            txtRoomNum.setText(String.valueOf(reservation.getRoomNum()));
            dpCheckIn.setValue(reservation.getCheckIn());
            dpCheckOut.setValue(reservation.getCheckOut());
        }
    }

    // ✅ Save or Update logic
    @FXML
    private void handleSave() {
        try {
            if (currentReservation == null) {
                // Add mode
                Reservation newRes = new Reservation();
                newRes.setGuestId(Integer.parseInt(txtGuestId.getText()));
                newRes.setHotelId(Integer.parseInt(txtHotelId.getText()));
                newRes.setRoomNum(Integer.parseInt(txtRoomNum.getText()));
                newRes.setCheckIn(dpCheckIn.getValue());
                newRes.setCheckOut(dpCheckOut.getValue());

                if (reservationDAO.insertReservation(newRes)) {
                    showAlert("✅ Reservation added successfully!");
                    closeWindow();
                } else {
                    showAlert("❌ Failed to add reservation.");
                }
            } else {
                // Edit mode
                currentReservation.setGuestId(Integer.parseInt(txtGuestId.getText()));
                currentReservation.setHotelId(Integer.parseInt(txtHotelId.getText()));
                currentReservation.setRoomNum(Integer.parseInt(txtRoomNum.getText()));
                currentReservation.setCheckIn(dpCheckIn.getValue());
                currentReservation.setCheckOut(dpCheckOut.getValue());

                if (reservationDAO.updateReservation(currentReservation)) {
                    showAlert("✅ Reservation updated successfully!");
                    closeWindow();
                } else {
                    showAlert("❌ Failed to update reservation.");
                }
            }
        } catch (Exception e) {
            showAlert("⚠️ Please check your input values.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) txtGuestId.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
