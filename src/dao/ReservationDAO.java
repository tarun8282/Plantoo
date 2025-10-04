package dao;

import model.Reservation;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    // Insert new reservation
    public boolean insertReservation(Reservation reservation) {
        String sql = "INSERT INTO Reservation (Guest_id, Hotel_id, Room_num, Check_in, Check_out) " +
                     "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getHotelId());
            stmt.setInt(3, reservation.getRoomNum());
            stmt.setDate(4, Date.valueOf(reservation.getCheckIn()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOut()));

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update reservation
    public boolean updateReservation(Reservation reservation) {
        String sql = "UPDATE Reservation SET Guest_id=?, Hotel_id=?, Room_num=?, Check_in=?, Check_out=? " +
                     "WHERE Reservation_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reservation.getGuestId());
            stmt.setInt(2, reservation.getHotelId());
            stmt.setInt(3, reservation.getRoomNum());
            stmt.setDate(4, Date.valueOf(reservation.getCheckIn()));
            stmt.setDate(5, Date.valueOf(reservation.getCheckOut()));
            stmt.setInt(6, reservation.getReservationId());

            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete reservation
    public boolean deleteReservation(int reservationId) {
        String sql = "DELETE FROM Reservation WHERE Reservation_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, reservationId);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get all reservations
    public List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.Reservation_id, r.Guest_id, g.Name AS GuestName, r.Hotel_id, r.Room_num, " +
                     "r.Check_in, r.Check_out " +
                     "FROM Reservation r JOIN Guest g ON r.Guest_id = g.Guest_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("Reservation_id"));
                r.setGuestId(rs.getInt("Guest_id"));
                r.setGuestName(rs.getString("GuestName"));
                r.setHotelId(rs.getInt("Hotel_id"));
                r.setRoomNum(rs.getInt("Room_num"));
                r.setCheckIn(rs.getDate("Check_in").toLocalDate());
                r.setCheckOut(rs.getDate("Check_out").toLocalDate());

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // Search by Guest ID or Name
    public List<Reservation> searchReservation(String keyword) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.Reservation_id, r.Guest_id, g.Name AS GuestName, r.Hotel_id, r.Room_num, " +
                     "r.Check_in, r.Check_out " +
                     "FROM Reservation r JOIN Guest g ON r.Guest_id = g.Guest_id " +
                     "WHERE g.Name LIKE ? OR r.Guest_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");

            // If keyword is numeric, bind as Guest_id; else bind -1
            try {
                stmt.setInt(2, Integer.parseInt(keyword));
            } catch (NumberFormatException e) {
                stmt.setInt(2, -1);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setReservationId(rs.getInt("Reservation_id"));
                r.setGuestId(rs.getInt("Guest_id"));
                r.setGuestName(rs.getString("GuestName"));
                r.setHotelId(rs.getInt("Hotel_id"));
                r.setRoomNum(rs.getInt("Room_num"));
                r.setCheckIn(rs.getDate("Check_in").toLocalDate());
                r.setCheckOut(rs.getDate("Check_out").toLocalDate());

                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
