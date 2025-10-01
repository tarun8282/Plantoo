package util;

import java.util.HashMap;
import java.util.Map;

public class AuthUtil {

    private static Map<String, String> credentials = new HashMap<>();

    static {
        credentials.put("Admin", "admin123");
        credentials.put("Manager", "manager123");
        credentials.put("Guest", "guest123");
    }

    public static boolean authenticate(String role, String password) {
        return credentials.containsKey(role) && credentials.get(role).equals(password);
    }
}
