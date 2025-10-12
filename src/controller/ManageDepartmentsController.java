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

        loadDepartments();
    }

    private void loadDepartments() {
        departmentList.clear();
        departmentList.addAll(departmentDAO.getAllDepartments());
        departmentTable.setItems(departmentList);
    }

    @FXML
    private void handleAddDepartment() {
        // TODO: Open a dialog to add department
        System.out.println("Add Department Clicked");

        // For demo, you can call DAO directly
        boolean added = departmentDAO.addDepartment("New Dept", 1); // Replace 1 with selected hotel ID
        if (added) {
            loadDepartments();
        }
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
