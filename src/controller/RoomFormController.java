package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Room;
import model.RoomType;
import dao.RoomDAO;
import dao.RoomTypeDAO;

import java.util.List;

public class RoomFormController {

    @FXML
    private Label formTitle;

    @FXML
    private TextField txtHotelId, txtRoomNum;

    @FXML
    private ComboBox<RoomType> comboRoomType;

    @FXML
    private ComboBox<String> comboStatus;

    @FXML
    private Button btnSave, btnCancel;

    private String mode; // "add" or "edit"
    private Room room;

    public void setMode(String mode) {
        this.mode = mode;
        if ("edit".equals(mode)) {
            formTitle.setText("Edit Room");
            txtHotelId.setDisable(true); // Hotel ID cannot be changed
            txtRoomNum.setDisable(true); // Room number cannot be changed
        } else {
            formTitle.setText("Add Room");
        }
    }

    public void setRoom(Room room) {
        this.room = room;
        if (room != null) {
            txtHotelId.setText(String.valueOf(room.getHotelId()));
            txtRoomNum.setText(String.valueOf(room.getRoomNum()));
            comboRoomType.setValue(room.getRoomType());
            comboStatus.setValue(room.getStatus());
        }
    }

    @FXML
    public void initialize() {
        // Load Room Types
        List<RoomType> types = RoomTypeDAO.getAllRoomTypes();
        comboRoomType.setItems(FXCollections.observableArrayList(types));

        btnCancel.setOnAction(e -> closeWindow());
        btnSave.setOnAction(e -> saveRoom());
    }

    private void closeWindow() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void saveRoom() {
        try {
            int hotelId = Integer.parseInt(txtHotelId.getText());
            int roomNum = Integer.parseInt(txtRoomNum.getText());
            RoomType selectedType = comboRoomType.getSelectionModel().getSelectedItem();
            String status = comboStatus.getValue();

            if (selectedType == null || status == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Select Room Type and Status.");
                alert.showAndWait();
                return;
            }

            if ("add".equals(mode)) {
                Room newRoom = new Room(hotelId, roomNum, selectedType.getRoomTypeId(), selectedType, status, null);
                RoomDAO.addRoom(newRoom);
            } else if ("edit".equals(mode)) {
                room.setRoomTypeId(selectedType.getRoomTypeId());
                room.setRoomType(selectedType);
                room.setStatus(status);
                RoomDAO.updateRoom(room);
            }

            closeWindow();

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Enter valid numbers for Hotel ID and Room Number.");
            alert.showAndWait();
        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error saving room: " + ex.getMessage());
            alert.showAndWait();
        }
    }
}
