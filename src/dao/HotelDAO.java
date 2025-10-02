package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import model.Hotel;
import util.DBConnection;  // Your DBConnection class

public class HotelDAO {

    // Fetch all hotels
    public static List<Hotel> getAllHotels() {
        List<Hotel> hotels = new ArrayList<>();
        String query = "SELECT * FROM Hotel";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("Hotel_id");
                String name = rs.getString("HotelName");
                int totalRooms = rs.getInt("TotalRooms");
                String location = rs.getString("Location");

                hotels.add(new Hotel(id, name, totalRooms, location));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return hotels;
    }

    // Add new hotel and return generated Hotel_id
    public static int addHotel(Hotel hotel) {
        String query = "INSERT INTO Hotel (HotelName, TotalRooms, Location) VALUES (?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, hotel.getHotelName());
            pstmt.setInt(2, hotel.getTotalRooms());
            pstmt.setString(3, hotel.getLocation());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);  // get generated Hotel_id
                    hotel.setHotelId(generatedId); // update model object
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return generatedId;
    }

    // Update existing hotel
    public static void updateHotel(Hotel hotel) {
        String query = "UPDATE Hotel SET HotelName = ?, TotalRooms = ?, Location = ? WHERE Hotel_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, hotel.getHotelName());
            pstmt.setInt(2, hotel.getTotalRooms());
            pstmt.setString(3, hotel.getLocation());
            pstmt.setInt(4, hotel.getHotelId());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete hotel (cascades to departments, staff, rooms, etc.)
    public static boolean deleteHotel(int hotelId) {
        String query = "DELETE FROM Hotel WHERE Hotel_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, hotelId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
