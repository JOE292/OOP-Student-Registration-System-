package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found", e);
        }
    }

    public static Connection connect() throws SQLException {
        String url  = ConfigReader.get("db.url");
        String user = ConfigReader.get("db.user");
        String pass = ConfigReader.get("db.pass");
        return DriverManager.getConnection(url, user, pass);
    }
}
