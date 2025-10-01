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

    // Add new hotel
    public static void addHotel(Hotel hotel) {
        String query = "INSERT INTO Hotel (Hotel_id, HotelName, TotalRooms, Location) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, hotel.getHotelId());
            pstmt.setString(2, hotel.getHotelName());
            pstmt.setInt(3, hotel.getTotalRooms());
            pstmt.setString(4, hotel.getLocation());
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Delete hotel
    public static void deleteHotel(int hotelId) {
        String query = "DELETE FROM Hotel WHERE Hotel_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, hotelId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
