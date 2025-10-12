package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.DBConnection;

import java.sql.*;

public class StaffFormController {

    @FXML private TextField txtName;
    @FXML private ComboBox<String> cmbGender;
    @FXML private TextField txtSalary;
    @FXML private ComboBox<String> cmbHotel;
    @FXML private ComboBox<String> cmbDept;
    @FXML private TextField txtPhone;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private ObservableList<String> hotelList = FXCollections.observableArrayList();
    private ObservableList<String> deptList = FXCollections.observableArrayList();

    private int selectedHotelId = -1;
    private int selectedDeptId = -1;
    

    @FXML
    public void initialize() {
        cmbGender.getItems().addAll("Male", "Female", "Other");

        loadHotels();

        // When hotel is selected, load departments for that hotel
        cmbHotel.setOnAction(e -> {
            String selectedHotel = cmbHotel.getSelectionModel().getSelectedItem();
            if (selectedHotel != null) {
                try (Connection con = DBConnection.getConnection()) {
                    String query = "SELECT Dept_id, Dept_name FROM Department WHERE Hotel_id = ?";
                    PreparedStatement pst = con.prepareStatement(query);
                    pst.setInt(1, getHotelIdByName(selectedHotel));
                    ResultSet rs = pst.executeQuery();

                    deptList.clear();
                    while (rs.next()) {
                        deptList.add(rs.getString("Dept_name"));
                    }
                    cmbDept.setItems(deptList);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnSave.setOnAction(e -> saveStaff());
        btnCancel.setOnAction(e -> ((Stage) btnCancel.getScene().getWindow()).close());
    }

    private void loadHotels() {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Hotel_id, HotelName FROM Hotel";
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();
            hotelList.clear();
            while (rs.next()) {
                hotelList.add(rs.getString("HotelName"));
            }
            cmbHotel.setItems(hotelList);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private int getHotelIdByName(String hotelName) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Hotel_id FROM Hotel WHERE HotelName = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, hotelName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("Hotel_id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private int getDeptIdByNameAndHotel(String deptName, int hotelId) {
        try (Connection con = DBConnection.getConnection()) {
            String query = "SELECT Dept_id FROM Department WHERE Dept_name=? AND Hotel_id=?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, deptName);
            pst.setInt(2, hotelId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) return rs.getInt("Dept_id");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private void saveStaff() {
        String name = txtName.getText().trim();
        String gender = cmbGender.getSelectionModel().getSelectedItem();
        double salary;
        try { salary = Double.parseDouble(txtSalary.getText().trim()); }
        catch (NumberFormatException ex) { showAlert("Salary must be a number!"); return; }

        String hotelName = cmbHotel.getSelectionModel().getSelectedItem();
        String deptName = cmbDept.getSelectionModel().getSelectedItem();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || gender == null || hotelName == null || deptName == null || phone.isEmpty()) {
            showAlert("Please fill all fields.");
            return;
        }

        int hotelId = getHotelIdByName(hotelName);
        int deptId = getDeptIdByNameAndHotel(deptName, hotelId);

        // Insert into Staff and Staff_Phone
        try (Connection con = DBConnection.getConnection()) {
            String insertStaff = "INSERT INTO Staff (Name, Gender, Salary, Dept_id, Hotel_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = con.prepareStatement(insertStaff, Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, name);
            pst.setString(2, gender);
            pst.setDouble(3, salary);
            pst.setInt(4, deptId);
            pst.setInt(5, hotelId);
            pst.executeUpdate();

            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int staffId = generatedKeys.getInt(1);
                String insertPhone = "INSERT INTO Staff_Phone (Staff_id, Phone) VALUES (?, ?)";
                PreparedStatement pstPhone = con.prepareStatement(insertPhone);
                pstPhone.setInt(1, staffId);
                pstPhone.setString(2, phone);
                pstPhone.executeUpdate();
            }

            showAlert("Staff added successfully!");
            ((Stage) btnSave.getScene().getWindow()).close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.showAndWait();
    }
}
