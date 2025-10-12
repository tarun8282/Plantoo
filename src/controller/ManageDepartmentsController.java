package controller;

import dao.DepartmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Department;

public class ManageDepartmentsController {

    @FXML
    private ComboBox<String> cmbHotel; // Hotel selector

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
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    @FXML
    public void initialize() {
        // Set up table columns
        colDeptId.setCellValueFactory(new PropertyValueFactory<>("deptId"));
        colDeptName.setCellValueFactory(new PropertyValueFactory<>("deptName"));
        colHotel.setCellValueFactory(new PropertyValueFactory<>("hotelName"));

        // Load hotels into ComboBox
        loadHotels();

        // When hotel changes, reload departments
        cmbHotel.setOnAction(e -> loadDepartments());
    }

    private void loadHotels() {
        ObservableList<String> hotels = FXCollections.observableArrayList(departmentDAO.getAllHotelNames());
        cmbHotel.setItems(hotels);
    }

    private void loadDepartments() {
        departmentList.clear();
        String selectedHotel = cmbHotel.getSelectionModel().getSelectedItem();
        if (selectedHotel != null) {
            departmentList.addAll(departmentDAO.getDepartmentsByHotel(selectedHotel));
        }
        departmentTable.setItems(departmentList);
    }

    @FXML
    private void handleAddDepartment() {
        String selectedHotel = cmbHotel.getSelectionModel().getSelectedItem();
        if (selectedHotel == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a hotel first.");
            alert.showAndWait();
            return;
        }

        // Prompt for department name
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Department");
        dialog.setHeaderText("Add Department to Hotel: " + selectedHotel);
        dialog.setContentText("Enter Department Name:");
        dialog.showAndWait().ifPresent(deptName -> {
            if (!deptName.trim().isEmpty()) {
                // Get hotel ID from name
                int hotelId = departmentDAO.getHotelIdByName(selectedHotel);
                boolean added = departmentDAO.addDepartment(deptName.trim(), hotelId);
                if (added) {
                    loadDepartments();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR, "Failed to add department.");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleDeleteDepartment() {
        Department selected = departmentTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select a department to delete.");
            alert.showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete department: " + selected.getDeptName() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            boolean deleted = departmentDAO.deleteDepartment(selected.getDeptId());
            if (deleted) {
                loadDepartments();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to delete department.");
                error.showAndWait();
            }
        }
    }
}
