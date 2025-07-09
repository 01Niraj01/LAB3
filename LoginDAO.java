import java.sql.*;

public class LoginDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/hrdb";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static boolean validateLogin(String email, String password) {
        String sql = "SELECT * FROM admin WHERE email=? AND password=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
            return false;
        }
    }
}
