package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import model.Hotel;
import dao.HotelDAO;

public class viewHotelsController {

    @FXML
    private TableView<Hotel> hotelTable;

    @FXML
    private TableColumn<Hotel, Number> SerialNO;

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
        // 🔢 Serial number column
        SerialNO.setCellValueFactory(
                cellData -> new ReadOnlyObjectWrapper<>(hotelTable.getItems().indexOf(cellData.getValue()) + 1)
        );

        // 🔗 Map table columns to Hotel model properties
        colId.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        colRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        // 🏨 Load hotel data
        loadHotels();
    }

    private void loadHotels() {
        hotelList = FXCollections.observableArrayList(HotelDAO.getAllHotels());
        hotelTable.setItems(hotelList);
    }
}
