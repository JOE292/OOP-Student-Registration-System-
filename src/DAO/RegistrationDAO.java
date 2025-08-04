package DAO;

import model.Course;
import model.Registration;
import model.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RegistrationDAO {
    private final Connection connection;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;

    public RegistrationDAO(Connection conn) {
        this.connection   = conn;
        this.studentDAO   = new StudentDAO(conn);
        this.courseDAO    = new CourseDAO(conn);
    }

    public int addRegistration(Registration reg) throws SQLException {
        String sql = "INSERT INTO registrations(studentId, courseId, registrationDate) " +
                "VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, reg.getStudent().getStudentId());
            statement.setInt(2, reg.getCourse().getCourseId());
            statement.setString(3, reg.getRegistrationDate().toString());

            int rows = statement.executeUpdate();
            if (rows == 0) {
                throw new SQLException("Creating registration failed, no rows affected.");
            }

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    reg.setRegistrationId(id);
                    return id;
                } else {
                    throw new SQLException("Creating registration failed, no ID obtained.");
                }
            }
        }
    }

    public boolean registrationExists(int studentId,int courseId, LocalDate date) throws SQLException {
        String sql = "SELECT 1 FROM registrations WHERE studentId = ? AND courseId = ? AND registrationDate = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.setDate(3, Date.valueOf(date));
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void addRegistrationIfNotExists(Registration reg) throws SQLException {
        if (!registrationExists(reg.getStudent().getStudentId(), reg.getCourse().getCourseId(), reg.getRegistrationDate())) {
            addRegistration(reg);
        }
    }

    public Registration findById(int registrationId) throws SQLException {
        String sql =
                "SELECT r.registrationDate, r.studentId, r.courseId " +
                        "FROM registrations r " +
                        "WHERE r.registrationId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, registrationId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return null;

                Student student = studentDAO.findById(rs.getInt("studentId"));
                Course  course  = courseDAO.findById(rs.getInt("courseId"));
                LocalDate date = rs.getTimestamp("registrationDate").toLocalDateTime().toLocalDate();


                return new Registration(registrationId, student, course, date);
            }
        }
    }

    public List<Registration> getAllRegistrations() throws SQLException {
        List<Registration> regs = new ArrayList<>();
        String sql =
                "SELECT r.registrationId, r.registrationDate, " +
                        "       s.studentId AS studentId, s.name, s.email, s.major, " +
                        "       c.courseId, c.title, c.instructor, c.creditHours " +
                        "FROM registrations r " +
                        "JOIN students s ON r.studentId = s.studentId " +
                        "JOIN courses  c ON r.courseId  = c.courseId";

        try (Statement stmt = connection.createStatement();
             ResultSet resultSet = stmt.executeQuery(sql)) {

            while (resultSet.next()) {
                Student student = new Student(
                        resultSet.getInt("studentId"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("major")
                );
                Course course = new Course(
                        resultSet.getInt("courseId"),
                        resultSet.getString("title"),
                        resultSet.getString("instructor"),
                        resultSet.getInt("creditHours")
                );
                int id = resultSet.getInt("registrationId");
                LocalDate date = resultSet.getTimestamp("registrationDate").toLocalDateTime().toLocalDate();


                regs.add(new Registration(id, student, course, date));
            }
        }
        return regs;
    }


    public List<Course> getCoursesByStudentId(int studentId) throws SQLException {
        List<Course> courses = new ArrayList<>();

        String sql =
                "SELECT c.courseId, c.title, c.instructor, c.creditHours " +
                        "FROM registrations r " +
                        "JOIN courses c ON r.courseId = c.courseId " +
                        "WHERE r.studentId = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Course course = new Course(
                            resultSet.getInt("courseId"),
                            resultSet.getString("title"),
                            resultSet.getString("instructor"),
                            resultSet.getInt("creditHours")
                    );
                    courses.add(course);
                }
            }
        }

        return courses;
    }

    public void deleteById(int registrationId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE registrationId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, registrationId);
            statement.executeUpdate();
        }
    }

    public void deleteRegistration(int studentId, int courseId) throws SQLException {
        String sql = "DELETE FROM registrations WHERE studentId = ? AND courseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, courseId);
            stmt.executeUpdate();
        }
    }


    public int deleteByStudentId(int studentId) {
        String sql = "DELETE FROM registrations WHERE studentId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            return stmt.executeUpdate(); // returns number of rows deleted
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteByCourseId(int courseId) {
        String sql = "DELETE FROM registrations WHERE courseId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, courseId);
            return stmt.executeUpdate(); // returns number of rows deleted
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


}
