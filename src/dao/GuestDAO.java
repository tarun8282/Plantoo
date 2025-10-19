package dao;

import model.Guest;
import java.sql.*;
import java.util.*;

public class GuestDAO {

    private Connection conn;

    public GuestDAO(Connection conn) {
        this.conn = conn;
    }

    // ---------------- Add Guest ----------------
    public void addGuest(Guest guest) throws SQLException {
        String insertGuestSQL = "INSERT INTO Guest (Name, Age, Gender, Password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(insertGuestSQL, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, guest.getName());
            pst.setInt(2, guest.getAge());
            pst.setString(3, guest.getGender());
            pst.setString(4, guest.getPassword() != null ? guest.getPassword() : "");
            pst.executeUpdate();

            try (ResultSet rs = pst.getGeneratedKeys()) {
                if (rs.next()) {
                    int guestId = rs.getInt(1);
                    guest.setGuestId(guestId);

                    // Insert phone numbers
                    insertGuestPhones(guestId, guest.getPhoneNumbers());
                }
            }
        }
    }

    // ---------------- Update Guest ----------------
    public void updateGuest(Guest guest) throws SQLException {
        // Update Guest info including password
        String updateGuestSQL = "UPDATE Guest SET Name = ?, Age = ?, Gender = ?, Password = ? WHERE Guest_id = ?";
        try (PreparedStatement pst = conn.prepareStatement(updateGuestSQL)) {
            pst.setString(1, guest.getName());
            pst.setInt(2, guest.getAge());
            pst.setString(3, guest.getGender());
            pst.setString(4, guest.getPassword() != null ? guest.getPassword() : "");
            pst.setInt(5, guest.getGuestId());
            pst.executeUpdate();
        }

        // Refresh phone numbers
        deleteGuestPhones(guest.getGuestId());
        insertGuestPhones(guest.getGuestId(), guest.getPhoneNumbers());
    }

    // ---------------- Delete Guest ----------------
    public void deleteGuest(int guestId) throws SQLException {
        try {
            conn.setAutoCommit(false);

            // Delete phones
            deleteGuestPhones(guestId);

            // Delete guest record
            try (PreparedStatement pstGuest = conn.prepareStatement("DELETE FROM Guest WHERE Guest_id = ?")) {
                pstGuest.setInt(1, guestId);
                pstGuest.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ---------------- Fetch all Guests ----------------
    public List<Guest> getAllGuests() throws SQLException {
        String sql = """
            SELECT g.Guest_id, g.Name, g.Age, g.Gender, g.Password, p.Phone
            FROM Guest g
            LEFT JOIN Guest_Phone p ON g.Guest_id = p.Guest_id
            ORDER BY g.Guest_id
        """;

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            Map<Integer, Guest> guestMap = new HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("Guest_id");
                Guest guest = guestMap.get(id);

                if (guest == null) {
                    guest = new Guest(
                            id,
                            rs.getString("Name"),
                            rs.getInt("Age"),
                            rs.getString("Gender"),
                            new ArrayList<>()
                    );
                    guest.setPassword(rs.getString("Password"));
                    guestMap.put(id, guest);
                }

                String phone = rs.getString("Phone");
                if (phone != null && !phone.isEmpty()) {
                    guest.getPhoneNumbers().add(phone);
                }
            }

            return new ArrayList<>(guestMap.values());
        }
    }

    // ---------------- Helper Methods ----------------
    private void insertGuestPhones(int guestId, List<String> phones) throws SQLException {
        if (phones == null || phones.isEmpty()) return;
        String insertPhoneSQL = "INSERT INTO Guest_Phone (Guest_id, Phone) VALUES (?, ?)";
        try (PreparedStatement pstPhone = conn.prepareStatement(insertPhoneSQL)) {
            for (String phone : phones) {
                pstPhone.setInt(1, guestId);
                pstPhone.setString(2, phone);
                pstPhone.addBatch();
            }
            pstPhone.executeBatch();
        }
    }

    private void deleteGuestPhones(int guestId) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM Guest_Phone WHERE Guest_id = ?")) {
            pst.setInt(1, guestId);
            pst.executeUpdate();
        }
    }
}
