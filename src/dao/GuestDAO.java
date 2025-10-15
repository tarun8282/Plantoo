package dao;

import model.Guest;

import java.sql.*;
import java.util.List;

public class GuestDAO {

    private Connection conn;

    public GuestDAO(Connection conn) {
        this.conn = conn;
    }

    // ---------------- Add Guest ----------------
    public void addGuest(Guest guest) throws SQLException {
        // Insert into Guest table
        String insertGuestSQL = "INSERT INTO Guest (Name, Age, Gender) VALUES (?, ?, ?)";
        PreparedStatement pst = conn.prepareStatement(insertGuestSQL, Statement.RETURN_GENERATED_KEYS);
        pst.setString(1, guest.getName());
        pst.setInt(2, guest.getAge());
        pst.setString(3, guest.getGender());
        pst.executeUpdate();

        // Get generated Guest_id
        ResultSet rs = pst.getGeneratedKeys();
        if (rs.next()) {
            int guestId = rs.getInt(1);

            // Insert phone numbers
            if (guest.getPhoneNumbers() != null) {
                String insertPhoneSQL = "INSERT INTO Guest_Phone (Guest_id, Phone) VALUES (?, ?)";
                PreparedStatement pstPhone = conn.prepareStatement(insertPhoneSQL);
                for (String phone : guest.getPhoneNumbers()) {
                    pstPhone.setInt(1, guestId);
                    pstPhone.setString(2, phone);
                    pstPhone.addBatch();
                }
                pstPhone.executeBatch();
            }

            // Insert password into Guest_Login table
            if (guest.getPassword() != null && !guest.getPassword().isEmpty()) {
                String insertLoginSQL = "INSERT INTO Guest_Login (Guest_id, Password) VALUES (?, ?)";
                PreparedStatement pstLogin = conn.prepareStatement(insertLoginSQL);
                pstLogin.setInt(1, guestId);
                pstLogin.setString(2, guest.getPassword());
                pstLogin.executeUpdate();
            }
        }
    }

    // ---------------- Update Guest ----------------
    public void updateGuest(Guest guest) throws SQLException {
        // Update Guest table
        String updateGuestSQL = "UPDATE Guest SET Name = ?, Age = ?, Gender = ? WHERE Guest_id = ?";
        PreparedStatement pst = conn.prepareStatement(updateGuestSQL);
        pst.setString(1, guest.getName());
        pst.setInt(2, guest.getAge());
        pst.setString(3, guest.getGender());
        pst.setInt(4, guest.getGuestId());
        pst.executeUpdate();

        // Delete old phone numbers
        String deletePhonesSQL = "DELETE FROM Guest_Phone WHERE Guest_id = ?";
        PreparedStatement pstDelete = conn.prepareStatement(deletePhonesSQL);
        pstDelete.setInt(1, guest.getGuestId());
        pstDelete.executeUpdate();

        // Insert updated phone numbers
        if (guest.getPhoneNumbers() != null) {
            String insertPhoneSQL = "INSERT INTO Guest_Phone (Guest_id, Phone) VALUES (?, ?)";
            PreparedStatement pstPhone = conn.prepareStatement(insertPhoneSQL);
            for (String phone : guest.getPhoneNumbers()) {
                pstPhone.setInt(1, guest.getGuestId());
                pstPhone.setString(2, phone);
                pstPhone.addBatch();
            }
            pstPhone.executeBatch();
        }

        // Update password in Guest_Login table
        if (guest.getPassword() != null && !guest.getPassword().isEmpty()) {
            String updateLoginSQL = "UPDATE Guest_Login SET Password = ? WHERE Guest_id = ?";
            PreparedStatement pstLogin = conn.prepareStatement(updateLoginSQL);
            pstLogin.setString(1, guest.getPassword());
            pstLogin.setInt(2, guest.getGuestId());
            pstLogin.executeUpdate();
        }
    }

    // ---------------- Delete Guest ----------------
    public void deleteGuest(int guestId) throws SQLException {
        // Delete from Guest_Login
        String deleteLoginSQL = "DELETE FROM Guest_Login WHERE Guest_id = ?";
        PreparedStatement pstLogin = conn.prepareStatement(deleteLoginSQL);
        pstLogin.setInt(1, guestId);
        pstLogin.executeUpdate();

        // Delete phone numbers first
        String deletePhonesSQL = "DELETE FROM Guest_Phone WHERE Guest_id = ?";
        PreparedStatement pstDelete = conn.prepareStatement(deletePhonesSQL);
        pstDelete.setInt(1, guestId);
        pstDelete.executeUpdate();

        // Delete guest
        String deleteGuestSQL = "DELETE FROM Guest WHERE Guest_id = ?";
        PreparedStatement pstGuest = conn.prepareStatement(deleteGuestSQL);
        pstGuest.setInt(1, guestId);
        pstGuest.executeUpdate();
    }

    // ---------------- Fetch all Guests ----------------
    public List<Guest> getAllGuests() throws SQLException {
        List<Guest> guests = new java.util.ArrayList<>();
        String sql = "SELECT g.Guest_id, g.Name, g.Age, g.Gender, p.Phone " +
                     "FROM Guest g LEFT JOIN Guest_Phone p ON g.Guest_id = p.Guest_id " +
                     "ORDER BY g.Guest_id";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        java.util.Map<Integer, Guest> guestMap = new java.util.HashMap<>();

        while (rs.next()) {
            int id = rs.getInt("Guest_id");
            Guest guest = guestMap.get(id);
            if (guest == null) {
                guest = new Guest(id, rs.getString("Name"), rs.getInt("Age"),
                        rs.getString("Gender"), new java.util.ArrayList<>());
                guestMap.put(id, guest);
            }
            String phone = rs.getString("Phone");
            if (phone != null) guest.getPhoneNumbers().add(phone);
        }

        guests.addAll(guestMap.values());
        return guests;
    }
}
