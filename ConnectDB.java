package Project_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/studentinfodb";
        String username = "root";
        String password = "123456789"; // Replace with your actual MySQL password

        try {
            // Optional: Load MySQL JDBC Driver (not required in latest Java, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connection successful!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("⚠️ JDBC Driver not found. Did you add the MySQL JAR?");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Connection failed!");
            e.printStackTrace();
        }
    }
}
