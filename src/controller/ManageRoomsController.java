package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Room;
import dao.RoomDAO;
import dao.HotelDAO;
import model.Hotel;

public class ManageRoomsController {

    @FXML private TableView<Room> roomTable;
    @FXML private TableColumn<Room, Number> colSerial;
    @FXML private TableColumn<Room, String> colHotel;
    @FXML private TableColumn<Room, Integer> colRoomNum;
    @FXML private TableColumn<Room, String> colRoomType;
    @FXML private TableColumn<Room, Double> colRent;
    @FXML private TableColumn<Room, String> colStatus;
    @FXML private TableColumn<Room, Integer> colHotelId;
    @FXML private ComboBox<Hotel> hotelCombo;
    @FXML private Button btnAddRoom, btnEditRoom, btnDeleteRoom, btnRefresh;

    private ObservableList<Room> roomList;

    @FXML
    public void initialize() {
        // Serial number column
        colSerial.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(roomTable.getItems().indexOf(cellData.getValue()) + 1)
        );
        
        colHotelId.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHotelId()));
        colHotel.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getHotelName()));
        colRoomNum.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRoomNum()));
        colRoomType.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRoomTypeName()));
        colRent.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getRent()));
        colStatus.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getStatus()));

        // Load hotels in ComboBox
        hotelCombo.setItems(FXCollections.observableArrayList(HotelDAO.getAllHotels()));
        hotelCombo.getItems().add(0, new Hotel(0, "All Hotels", 0, ""));
        hotelCombo.getSelectionModel().selectFirst();

        // Load rooms initially
        loadRooms(0);

        // Filter by hotel selection
        hotelCombo.setOnAction(e -> {
            Hotel selected = hotelCombo.getSelectionModel().getSelectedItem();
            loadRooms(selected.getHotelId());
        });

        // Button actions
        btnRefresh.setOnAction(e -> loadRooms(hotelCombo.getSelectionModel().getSelectedItem().getHotelId()));

        btnAddRoom.setOnAction(e -> openRoomForm("add", null));

        btnEditRoom.setOnAction(e -> {
            Room selected = getSelectedRoom();
            if (selected != null) {
                openRoomForm("edit", selected);
            } else {
                showAlert("Please select a room to edit.");
            }
        });

        btnDeleteRoom.setOnAction(e -> {
            Room selected = getSelectedRoom();
            if (selected != null) {
                boolean deleted = RoomDAO.deleteRoom(selected.getHotelId(), selected.getRoomNum());
                if (deleted) loadRooms(hotelCombo.getSelectionModel().getSelectedItem().getHotelId());
            } else {
                showAlert("Please select a room to delete.");
            }
        });
    }

    private void loadRooms(int hotelId) {
        roomList = FXCollections.observableArrayList(RoomDAO.getRoomsByHotel(hotelId));
        roomTable.setItems(roomList);
    }

    public Room getSelectedRoom() {
        return roomTable.getSelectionModel().getSelectedItem();
    }

    private void openRoomForm(String mode, Room room) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Forms/RoomForm.fxml"));
            Parent root = loader.load();

            RoomFormController controller = loader.getController();
            controller.setMode(mode); // "add" or "edit"
            if (room != null) controller.setRoom(room); // prefill fields for edit

            Stage stage = new Stage();
            stage.setTitle(mode.equals("add") ? "Add Room" : "Edit Room");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh table after closing form
            loadRooms(hotelCombo.getSelectionModel().getSelectedItem().getHotelId());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
