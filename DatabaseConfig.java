/*
 * DatabaseConfig.java
 * Description: Contains database connection configuration and utility methods
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    private static final String URL = "jdbc:mysql://localhost:3306/student_management";
    private static final String USER = "root";
    private static final String PASSWORD = "Weinachten01@";

    public static Connection getConnection() throws SQLException {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }
    }
}