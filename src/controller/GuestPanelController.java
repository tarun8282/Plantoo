package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Guest;
import model.Reservation;
import util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GuestPanelController {

    // ---------------- UI Elements ----------------
    @FXML private Label lblGuestId, lblName, lblAge, lblGender, lblPhones, lblNoReservation;
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> colReservationId, colHotelId, colRoomNum;
    @FXML private TableColumn<Reservation, LocalDate> colCheckIn, colCheckOut;
    @FXML private Button btnLogout;

    // ---------------- Data ----------------
    private int guestId; // set from login

    // ---------------- Public Methods ----------------
    /** Called from Login Controller to set the guest ID */
    public void setGuestId(int guestId) {
        this.guestId = guestId;
        loadGuestInfo();
        loadGuestReservations();
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) btnLogout.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.setTitle("Plantoo Login");
            loginStage.setResizable(false);
            loginStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Private Methods ----------------
    /** Load guest info from DB and set labels */
    private void loadGuestInfo() {
        try (Connection conn = DBConnection.getConnection()) {
            String guestQuery = "SELECT guest_id, name, age, gender FROM Guest WHERE guest_id = ?";
            PreparedStatement ps = conn.prepareStatement(guestQuery);
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("guest_id");
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String gender = rs.getString("gender");

               String phoneQuery = "SELECT Phone FROM Guest_Phone WHERE Guest_id = ?";
                PreparedStatement psPhone = conn.prepareStatement(phoneQuery);
                psPhone.setInt(1, guestId);
                ResultSet phoneRs = psPhone.executeQuery();

                List<String> phones = new ArrayList<>();
                while (phoneRs.next()) {
                    phones.add(phoneRs.getString("Phone")); // exact column name from DB
                }

                Guest guest = new Guest(id, name, age, gender, phones);

                // Set UI
                lblGuestId.setText(String.valueOf(guest.getGuestId()));
                lblName.setText(guest.getName());
                lblAge.setText(String.valueOf(guest.getAge()));
                lblGender.setText(guest.getGender());
                lblPhones.setText(guest.getPhoneNumbersAsString());

            } else {
                lblName.setText("Guest not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Load guest reservations into TableView */
    private void loadGuestReservations() {
        ObservableList<Reservation> list = FXCollections.observableArrayList();

        try (Connection conn = DBConnection.getConnection()) {
            String query = """
                    SELECT reservation_id, guest_id, hotel_id, room_num, check_in, check_out
                    FROM Reservation
                    WHERE guest_id = ?
                    """;
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Reservation(
                        rs.getInt("reservation_id"),
                        rs.getInt("guest_id"),
                        "", // guestName not needed in table
                        rs.getInt("hotel_id"),
                        rs.getInt("room_num"),
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        null
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list.isEmpty()) {
            lblNoReservation.setVisible(true);
            reservationTable.setVisible(false);
        } else {
            lblNoReservation.setVisible(false);
            reservationTable.setVisible(true);
            reservationTable.setItems(list);
            setupReservationTable();
        }
    }

    /** Setup table columns for Reservation TableView */
    private void setupReservationTable() {
        colReservationId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("reservationId"));
        colHotelId.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("hotelId"));
        colRoomNum.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("roomNum"));
        colCheckIn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("checkIn"));
        colCheckOut.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("checkOut"));
    }
}
