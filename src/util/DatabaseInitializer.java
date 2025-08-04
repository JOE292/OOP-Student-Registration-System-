package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void createDatabaseIfNotExists(String dbName, String user, String pass) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/", user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
        }
    }

    public static void createTables(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students (" +
                            "  studentId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "  name VARCHAR(255) NOT NULL, " +
                            "  email VARCHAR(255) NOT NULL, " +
                            "  major VARCHAR(100) NOT NULL" +
                            ");"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS courses (" +
                            "  courseId VARCHAR(50) PRIMARY KEY, " +
                            "  title VARCHAR(255) NOT NULL, " +
                            "  instructor VARCHAR(255) NOT NULL, " +
                            "  creditHours INT NOT NULL" +
                            ");"
            );

            stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS registrations (" +
                            "  registrationId INT AUTO_INCREMENT PRIMARY KEY, " +
                            "  studentId INT NOT NULL, " +
                            "  courseId VARCHAR(50) NOT NULL, " +
                            "  registrationDate DATETIME NOT NULL, " +
                            "  FOREIGN KEY (studentId) REFERENCES students(studentId), " +
                            "  FOREIGN KEY (courseId)  REFERENCES courses(courseId)" +
                            ");"
            );
        }
    }
}
