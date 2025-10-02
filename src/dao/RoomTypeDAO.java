package dao;

import model.RoomType;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {

    private static Connection getConnection() throws Exception {
        return DBConnection.getConnection();
    }

    // Get all room types
    public static List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String sql = "SELECT * FROM RoomType";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                RoomType rt = new RoomType(
                        rs.getInt("RoomType_id"),
                        rs.getString("TypeName"),
                        rs.getDouble("Rent")
                );
                list.add(rt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Add a new room type
    public static boolean addRoomType(RoomType roomType) {
        String sql = "INSERT INTO RoomType (RoomType_id, TypeName, Rent) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomType.getRoomTypeId());
            ps.setString(2, roomType.getTypeName());
            ps.setDouble(3, roomType.getRent());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update room type
    public static boolean updateRoomType(RoomType roomType) {
        String sql = "UPDATE RoomType SET TypeName=?, Rent=? WHERE RoomType_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roomType.getTypeName());
            ps.setDouble(2, roomType.getRent());
            ps.setInt(3, roomType.getRoomTypeId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete room type
    public static boolean deleteRoomType(int roomTypeId) {
        String sql = "DELETE FROM RoomType WHERE RoomType_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Get a single room type by ID
    public static RoomType getRoomTypeById(int roomTypeId) {
        String sql = "SELECT * FROM RoomType WHERE RoomType_id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roomTypeId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new RoomType(
                        rs.getInt("RoomType_id"),
                        rs.getString("TypeName"),
                        rs.getDouble("Rent")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
