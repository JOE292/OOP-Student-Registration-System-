package DAO;

import model.Course;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO {
    private final Connection connection;

    public CourseDAO(Connection connection) {
        this.connection = connection;
    }

    public void addCourse(Course course) throws SQLException {
        String sql = "INSERT INTO courses(courseId, title, instructor, creditHours) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, course.getCourseId());
            statement.setString(2, course.getTitle());
            statement.setString(3, course.getInstructor());
            statement.setInt(4, course.getCreditHours());
            statement.executeUpdate();
        }
    }

    public Course findById(int courseId) throws SQLException {
        String sql = "SELECT courseId, title, instructor, creditHours FROM courses WHERE courseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    return new Course(
                            resultSet.getInt("courseId"),
                            resultSet.getString("title"),
                            resultSet.getString("instructor"),
                            resultSet.getInt("creditHours")
                    );
                }
                return null;
            }
        }
    }


    public boolean courseExists(int courseId) throws SQLException {
        String sql = "SELECT 1 FROM courses WHERE courseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addCourseIfNotExists(Course course) throws SQLException {
        if (!courseExists(course.getCourseId())) {
            addCourse(course);
        }
    }

    public List<Course> getAllCourses() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT courseId, title, instructor, creditHours FROM courses";
        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {
            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("courseId"),
                        resultSet.getString("title"),
                        resultSet.getString("instructor"),
                        resultSet.getInt("creditHours")
                ));
            }
        }
        return courses;
    }

    public void updateCourse(Course course) throws SQLException {
        String sql = "UPDATE courses SET title = ?, instructor = ?, creditHours = ? WHERE courseId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, course.getTitle());
            statement.setString(2, course.getInstructor());
            statement.setInt(3, course.getCreditHours());
            statement.setInt(4, course.getCourseId());
            statement.executeUpdate();
        }
    }

    public void deleteById(int courseId) throws SQLException {
        String sql = "DELETE FROM courses WHERE courseId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, courseId);
            statement.executeUpdate();
        }
    }



}
