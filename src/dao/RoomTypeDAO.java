package dao;

import model.RoomType;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomTypeDAO {

    private static Connection getConnection() throws Exception {
        return DBConnection.getConnection(); // your DB connection class
    }

    public static List<RoomType> getAllRoomTypes() {
        List<RoomType> list = new ArrayList<>();
        String query = "SELECT RoomType_id, TypeName, Rent FROM RoomType";

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

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
}
