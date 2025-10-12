package dao;

import model.Department;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // Fetch all departments
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT d.Dept_id, d.Dept_name, h.HotelName " +
                     "FROM Department d JOIN Hotel h ON d.Hotel_id = h.Hotel_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("Dept_id");
                String name = rs.getString("Dept_name");
                String hotel = rs.getString("HotelName");
                departments.add(new Department(id, name, hotel));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return departments;
    }

    // Add new department
    public boolean addDepartment(String deptName, int hotelId) {
        String sql = "INSERT INTO Department (Dept_name, Hotel_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptName);
            ps.setInt(2, hotelId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update existing department
    public boolean updateDepartment(int deptId, String deptName, int hotelId) {
        String sql = "UPDATE Department SET Dept_name = ?, Hotel_id = ? WHERE Dept_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, deptName);
            ps.setInt(2, hotelId);
            ps.setInt(3, deptId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete department
    public boolean deleteDepartment(int deptId) {
        String sql = "DELETE FROM Department WHERE Dept_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get department by ID
    public Department getDepartmentById(int deptId) {
        String sql = "SELECT d.Dept_id, d.Dept_name, h.HotelName " +
                     "FROM Department d JOIN Hotel h ON d.Hotel_id = h.Hotel_id WHERE d.Dept_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, deptId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Department(
                        rs.getInt("Dept_id"),
                        rs.getString("Dept_name"),
                        rs.getString("HotelName")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
