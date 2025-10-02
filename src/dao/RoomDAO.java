package dao;

import model.Room;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {

    private static Connection getConnection() throws Exception {
        return DBConnection.getConnection(); // your DB connection class
    }

    public static List<Room> getAllRooms() {
        return getRoomsByHotel(0); // 0 = all hotels
    }

        public static List<Room> getRoomsByHotel(int hotelId) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT r.Hotel_id, r.Room_num, r.RoomType_id, t.TypeName, t.Rent, r.Status, h.HotelName " +
                    "FROM Room r " +
                    "JOIN RoomType t ON r.RoomType_id = t.RoomType_id " +
                    "JOIN Hotel h ON r.Hotel_id = h.Hotel_id";

        if (hotelId != 0) {
            sql += " WHERE r.Hotel_id = ?";
        }

        try (Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {

            if (hotelId != 0) ps.setInt(1, hotelId);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("Hotel_id"),
                        rs.getInt("Room_num"),
                        rs.getInt("RoomType_id"),
                        rs.getString("TypeName"),
                        rs.getDouble("Rent"),
                        rs.getString("Status"),
                        rs.getString("HotelName")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }


    public static boolean addRoom(Room room) {
        String sql = "INSERT INTO Room (Hotel_id, Room_num, RoomType_id, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, room.getHotelId());
            ps.setInt(2, room.getRoomNum());
            ps.setInt(3, room.getRoomTypeId());
            ps.setString(4, room.getStatus());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateRoom(Room room) {
        String sql = "UPDATE Room SET RoomType_id=?, Status=? WHERE Hotel_id=? AND Room_num=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, room.getRoomTypeId());
            ps.setString(2, room.getStatus());
            ps.setInt(3, room.getHotelId());
            ps.setInt(4, room.getRoomNum());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteRoom(int hotelId, int roomNum) {
        String sql = "DELETE FROM Room WHERE Hotel_id=? AND Room_num=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hotelId);
            ps.setInt(2, roomNum);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
