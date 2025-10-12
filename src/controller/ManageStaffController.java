package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Staff;
import util.DBConnection;

import java.io.IOException;
import java.sql.*;

public class ManageStaffController {

    @FXML
    private TableView<Staff> staffTable;
    @FXML private TableColumn<Staff, Integer> colStaffId;
    @FXML private TableColumn<Staff, String> colName;
    @FXML private TableColumn<Staff, String> colGender;
    @FXML private TableColumn<Staff, Double> colSalary;
    @FXML private TableColumn<Staff, String> colDept;
    @FXML private TableColumn<Staff, String> colHotel;
    @FXML private TableColumn<Staff, String> colPhone;

    @FXML private Button btnAdd;
    @FXML private Button btnEdit;
    @FXML private Button btnDelete;
    @FXML private Button btnRefresh;

    private ObservableList<Staff> staffList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup columns
        colStaffId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStaffId()).asObject());
        colName.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getName()));
        colGender.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getGender()));
        colSalary.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getSalary()).asObject());
        colDept.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartment()));
        colHotel.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getHotel()));
        colPhone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPhone()));

        loadStaff();

        // Button actions
        btnRefresh.setOnAction(e -> loadStaff());
        btnAdd.setOnAction(e -> openStaffForm("Add"));
        btnEdit.setOnAction(e -> openStaffForm("Edit"));
        btnDelete.setOnAction(e -> deleteStaff());
    }

    private void loadStaff() {
        staffList.clear();
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT s.Staff_id, s.Name, s.Gender, s.Salary, d.Dept_name, h.HotelName, sp.Phone " +
                           "FROM Staff s " +
                           "JOIN Department d ON s.Dept_id = d.Dept_id " +
                           "JOIN Hotel h ON s.Hotel_id = h.Hotel_id " +
                           "LEFT JOIN Staff_Phone sp ON s.Staff_id = sp.Staff_id";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                staffList.add(new Staff(
                        rs.getInt("Staff_id"),
                        rs.getString("Name"),
                        rs.getString("Gender"),
                        rs.getDouble("Salary"),
                        rs.getString("Dept_name"),
                        rs.getString("HotelName"),
                        rs.getString("Phone")
                ));
            }
            staffTable.setItems(staffList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void openStaffForm(String action) {
    try {
        // Load the FXML for the form
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Forms/StaffForm.fxml"));
        Parent root = loader.load();

        // Optionally, pass data to the form controller for Edit
        StaffFormController controller = loader.getController();
        // If action is "Edit", you can call a method in StaffFormController to pre-fill fields
        // e.g., controller.setStaffData(selectedStaff);

        // Create a new stage (popup window)
        Stage stage = new Stage();
        stage.setTitle(action + " Staff");
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL); // Blocks interaction with other windows
        stage.setResizable(false);

        stage.showAndWait(); // Wait until the form is closed

        // Refresh the staff table after adding/editing
        loadStaff();

    } catch (IOException e) {
        e.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to open the Staff Form.");
        alert.showAndWait();
    }
}


    private void deleteStaff() {
        Staff selected = staffTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try (Connection con = DBConnection.getConnection()) {
                String deletePhone = "DELETE FROM Staff_Phone WHERE Staff_id=?";
                PreparedStatement pstPhone = con.prepareStatement(deletePhone);
                pstPhone.setInt(1, selected.getStaffId());
                pstPhone.executeUpdate();

                String deleteStaff = "DELETE FROM Staff WHERE Staff_id=?";
                PreparedStatement pstStaff = con.prepareStatement(deleteStaff);
                pstStaff.setInt(1, selected.getStaffId());
                pstStaff.executeUpdate();

                staffList.remove(selected);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Staff deleted successfully!");
                alert.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a staff member to delete.");
            alert.show();
        }
    }
}
