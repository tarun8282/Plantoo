package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthUtil {

    
    public static boolean authenticate(String role, String password) {
        try {
            if ("Admin".equalsIgnoreCase(role)) {
                return password.equals("1");
            } else if ("Manager".equalsIgnoreCase(role)) {
                return password.equals("2");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*Guest Login Authentatication */
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
