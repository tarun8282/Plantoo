package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Department;
import model.Hotel;
import util.DBConnection;

import java.sql.*;

public class ManageDepartmentsController {

    @FXML
    private TableView<Department> departmentTable;
    @FXML
    private TableColumn<Department, Integer> colDeptId;
    @FXML
    private TableColumn<Department, String> colDeptName;
    @FXML
    private TableColumn<Department, String> colHotel;
    @FXML
    private TableColumn<Department, Void> colActions;

    private ObservableList<Department> departmentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up table columns
        colDeptId.setCellValueFactory(new PropertyValueFactory<>("deptId"));
        colDeptName.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        colHotel.setCellValueFactory(new PropertyValueFactory<>("hotelName"));

        loadDepartments();
    }

    private void loadDepartments() {
        departmentList.clear();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(
                     "SELECT d.Dept_id, d.Dept_name, h.HotelName FROM Department d " +
                             "JOIN Hotel h ON d.Hotel_id = h.Hotel_id")) {

            while (rs.next()) {
                int id = rs.getInt("Dept_id");
                String name = rs.getString("Dept_name");
                String hotel = rs.getString("HotelName");

                departmentList.add(new Department(id, name, hotel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        departmentTable.setItems(departmentList);
    }

    @FXML
    private void handleAddDepartment() {
        // TODO: Open a dialog to add department
        System.out.println("Add Department Clicked");
    }
}
