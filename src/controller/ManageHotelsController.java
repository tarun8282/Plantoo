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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ManageHotelsController {

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

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnDelete;

    private ObservableList<Hotel> hotelList;

    private void showAlert(String message) {
    new Alert(Alert.AlertType.WARNING, message).showAndWait();
}


    private void openHotelForm(String mode, Hotel hotel) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Forms/hotel_form.fxml"));
        Parent root = loader.load();

        HotelFormController controller = loader.getController();
        controller.setMode(mode);     // "add" or "edit"
        if (hotel != null) {
            controller.setHotel(hotel); // pre-fill fields for edit
        }

        Stage stage = new Stage();
        stage.setTitle(mode.equals("add") ? "Add Hotel" : "Edit Hotel");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        loadHotels(); // refresh table after closing form
    } catch (Exception ex) {
        ex.printStackTrace();
    }
}


    @FXML
    public void initialize() {
        // Serial No column
        SerialNO.setCellValueFactory(
                cellData -> new ReadOnlyObjectWrapper<>(hotelTable.getItems().indexOf(cellData.getValue()) + 1));

        colId.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        colName.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        colRooms.setCellValueFactory(new PropertyValueFactory<>("totalRooms"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));

        loadHotels();

        // Optional: Button actions (fill later)

        btnAdd.setOnAction(e -> openHotelForm("add", null));

        // -----------------------------
        // Edit Hotel
        // -----------------------------
        btnEdit.setOnAction(e -> {
            Hotel selected = getSelectedHotel();
            if (selected == null) {
                showAlert("Select a hotel to edit.");
                return;
            }
            openHotelForm("edit", selected);
        });

        // -----------------------------
        // Delete Hotel
        // -----------------------------
        btnDelete.setOnAction(e -> {
            Hotel selected = getSelectedHotel();
            if (selected == null) {
                showAlert("Select a hotel to delete.");
                return;
            }

            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this hotel?");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    HotelDAO.deleteHotel(selected.getHotelId());
                    loadHotels(); // refresh table
                }
            });
        });

    }

    public void loadHotels() {
        hotelList = FXCollections.observableArrayList(HotelDAO.getAllHotels());
        hotelTable.setItems(hotelList);
    }

    public Hotel getSelectedHotel() {
        return hotelTable.getSelectionModel().getSelectedItem();
    }
}
