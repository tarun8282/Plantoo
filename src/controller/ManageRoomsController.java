package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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
        
        colHotelId.setCellValueFactory(cellData ->new ReadOnlyObjectWrapper<>(cellData.getValue().getHotelId()));
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

        // Button actions (can link to popups/forms later)
        btnRefresh.setOnAction(e -> loadRooms(hotelCombo.getSelectionModel().getSelectedItem().getHotelId()));
        btnAddRoom.setOnAction(e -> {/* open add room form */});
        btnEditRoom.setOnAction(e -> {/* open edit form for selected room */});
        btnDeleteRoom.setOnAction(e -> {
            Room selected = getSelectedRoom();
            if(selected != null) RoomDAO.deleteRoom(selected.getHotelId(), selected.getRoomNum());
            loadRooms(hotelCombo.getSelectionModel().getSelectedItem().getHotelId());
        });
    }

    private void loadRooms(int hotelId) {
        roomList = FXCollections.observableArrayList(RoomDAO.getRoomsByHotel(hotelId));
        roomTable.setItems(roomList);
    }

    public Room getSelectedRoom() {
        return roomTable.getSelectionModel().getSelectedItem();
    }
}
