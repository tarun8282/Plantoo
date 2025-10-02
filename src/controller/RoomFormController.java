package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import model.Room;
import model.RoomType;
import model.Hotel;
import dao.RoomDAO;
import dao.RoomTypeDAO;
import dao.HotelDAO;

import java.util.List;

public class RoomFormController {

    @FXML private Label formTitle;
    @FXML private ComboBox<Hotel> comboHotel;
    @FXML private TextField txtRoomNum;
    @FXML private ComboBox<RoomType> comboRoomType;
    @FXML private TextField txtRent;
    @FXML private ComboBox<String> comboStatus;
    @FXML private Button btnSave, btnCancel;

    private String mode; // "add" or "edit"
    private Room editingRoom;
    

    @FXML
    public void initialize() {
        // Load hotels
        List<Hotel> hotels = HotelDAO.getAllHotels();
        comboHotel.setItems(FXCollections.observableArrayList(hotels));

        // Load room types
        List<RoomType> roomTypes = RoomTypeDAO.getAllRoomTypes();
        comboRoomType.setItems(FXCollections.observableArrayList(roomTypes));

        // Load status options
        comboStatus.setItems(FXCollections.observableArrayList("Available", "Occupied", "Maintenance"));

        // Auto-fill rent when RoomType is selected
        comboRoomType.setOnAction(e -> {
            RoomType selected = comboRoomType.getValue();
            if (selected != null) {
                txtRent.setText(String.valueOf(selected.getRent()));
            }
        });

        // Cancel button closes form
        btnCancel.setOnAction(e -> ((Stage) btnCancel.getScene().getWindow()).close());
        btnSave.setOnAction(e -> handleSave());

    }
    

    // Called by ManageRoomsController
    public void setMode(String mode) {
        this.mode = mode;
        formTitle.setText(mode.equals("add") ? "Add Room" : "Edit Room");

        if (mode.equals("add")) {
            txtRoomNum.setEditable(true);
            comboHotel.setDisable(false);
        } else {
            txtRoomNum.setEditable(false);
            comboHotel.setDisable(true);
        }
    }

    // Called for editing an existing room
    public void setRoom(Room room) {
        this.editingRoom = room;

        comboHotel.setValue(new Hotel(room.getHotelId(), room.getHotelName())); // minimal constructor
        txtRoomNum.setText(String.valueOf(room.getRoomNum()));

        // Select RoomType
        for (RoomType rt : comboRoomType.getItems()) {
            if (rt.getRoomTypeId() == room.getRoomTypeId()) {
                comboRoomType.setValue(rt);
                break;
            }
        }

        txtRent.setText(String.valueOf(room.getRent()));
        comboStatus.setValue(room.getStatus());
    }
    
    @FXML
    private void handleSave() {
        try {
            Hotel selectedHotel = comboHotel.getValue();
            RoomType selectedType = comboRoomType.getValue();
            String status = comboStatus.getValue();
            int roomNum = Integer.parseInt(txtRoomNum.getText());

            if (selectedHotel == null || selectedType == null || status == null) {
                showAlert("Please fill all fields.");
                return;
            }

            if (mode.equals("add")) {
                Room newRoom = new Room(
                        selectedHotel.getHotelId(),
                        roomNum,
                        selectedType.getRoomTypeId(),
                        selectedType.getTypeName(),
                        selectedType.getRent(),
                        status,
                        selectedHotel.getHotelName()
                );

                boolean success = RoomDAO.addRoom(newRoom);
                if (!success) {
                    showAlert("Failed to add room. It may already exist.");
                    return;
                }

            } else if (mode.equals("edit") && editingRoom != null) {
                editingRoom.setRoomTypeId(selectedType.getRoomTypeId());
                editingRoom.setRent(selectedType.getRent());
                editingRoom.setStatus(status);

                boolean success = RoomDAO.updateRoom(editingRoom);
                if (!success) {
                    showAlert("Failed to update room.");
                    return;
                }
            }

            // Close the form
            Stage stage = (Stage) btnSave.getScene().getWindow();
            stage.close();

        } catch (NumberFormatException e) {
            showAlert("Room number must be a valid integer.");
        }
    }
    

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
