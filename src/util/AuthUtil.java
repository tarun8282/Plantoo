package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthUtil {

    /**
     * Authenticate Admin or Manager using predefined credentials.
     * (You can later connect these to a Staff table if needed)
     */
    public static boolean authenticate(String role, String password) {
        try {
            if ("Admin".equalsIgnoreCase(role)) {
                return password.equals("admin@123");
            } else if ("Manager".equalsIgnoreCase(role)) {
                return password.equals("manager@123");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Authenticate Guest using the database table `Guest`.
     * The table should have columns: guest_id (INT), password (VARCHAR)
     */
    public static boolean authenticateGuest(int guestId, String password) {
        String query = "SELECT password FROM Guest WHERE guest_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                return password.equals(dbPassword);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
