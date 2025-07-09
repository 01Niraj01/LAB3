import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/hrdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Fetch all admins
    public static List<AdminData> getAllAdmins() {
        List<AdminData> list = new ArrayList<>();
        String sql = "SELECT * FROM admin";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AdminData(
                        rs.getInt("admin_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Admin Load Error: " + e.getMessage());
        }
        return list;
    }
}
