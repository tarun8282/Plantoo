package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Hotel;
import dao.HotelDAO;

public class HotelFormController {

    @FXML
    private Label formTitle;

    @FXML
    private TextField txtHotelId, txtHotelName, txtTotalRooms, txtLocation;

    @FXML
    private Button btnSave, btnCancel;

    private String mode; // "add" or "edit"
    private Hotel hotel; // hotel to edit

    public void setMode(String mode) {
        this.mode = mode;
        if ("edit".equals(mode)) {
            formTitle.setText("Edit Hotel");
            txtHotelId.setVisible(true);
            txtHotelId.setManaged(true);
            txtHotelId.setDisable(true); // ID cannot be changed
        } else {
            formTitle.setText("Add Hotel");
            txtHotelId.setVisible(false);
            txtHotelId.setManaged(false);
        }
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
        if (hotel != null) {
            txtHotelId.setText(String.valueOf(hotel.getHotelId()));
            txtHotelName.setText(hotel.getHotelName());
            txtTotalRooms.setText(String.valueOf(hotel.getTotalRooms()));
            txtLocation.setText(hotel.getLocation());
        }
    }

    @FXML
    public void initialize() {
        btnCancel.setOnAction(e -> closeWindow());
        btnSave.setOnAction(e -> saveHotel());
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void saveHotel() {
        try {
            String name = txtHotelName.getText();
            int totalRooms = Integer.parseInt(txtTotalRooms.getText());
            String location = txtLocation.getText();

            if ("add".equals(mode)) {
                Hotel newHotel = new Hotel();
                newHotel.setHotelName(name);
                newHotel.setTotalRooms(totalRooms);
                newHotel.setLocation(location);

                int generatedId = HotelDAO.addHotel(newHotel); // DAO returns auto-generated ID
                if (generatedId != -1) {
                    System.out.println("Hotel added with ID: " + generatedId);
                }
            } else if ("edit".equals(mode)) {
                hotel.setHotelName(name);
                hotel.setTotalRooms(totalRooms);
                hotel.setLocation(location);
                HotelDAO.updateHotel(hotel);
            }

            closeWindow();

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enter a valid number for Total Rooms.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving hotel: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}
