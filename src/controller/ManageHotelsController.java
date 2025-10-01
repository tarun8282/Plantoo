package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Hotel;
import dao.HotelDAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ManageHotelsController {

    @FXML
    private TableView<Hotel> hotelTable;

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
