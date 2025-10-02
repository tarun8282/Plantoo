package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Hotel;
import dao.HotelDAO;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ManageHotelsController {

    @FXML
    private TableView<Hotel> hotelTable;
    @FXML
    private TableColumn<Hotel, Number> SerialNO; // Use Number instead of Integer
    @FXML
    private TableColumn<Hotel, Integer> colId;
    @FXML
    private TableColumn<Hotel, String> colName;
    @FXML
    private TableColumn<Hotel, Integer> colRooms;
    @FXML
    private TableColumn<Hotel, String> colLocation;

    private ObservableList<Hotel> hotelList;

    @FXML
    public void initialize() {
        // Serial No column (always starts from 1, increments row by row)
        SerialNO.setCellValueFactory(cellData ->
            new ReadOnlyObjectWrapper<>(hotelTable.getItems().indexOf(cellData.getValue()) + 1)
        );

        colId.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        colRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadHotels();
    }

    public void loadHotels() {
        hotelList = FXCollections.observableArrayList(HotelDAO.getAllHotels());
        hotelTable.setItems(hotelList);
    }

    public Hotel getSelectedHotel() {
        return hotelTable.getSelectionModel().getSelectedItem();
    }
}
