// src/service/RegistrationService.java
package service;

import DAO.CourseDAO;
import DAO.RegistrationDAO;
import DAO.StudentDAO;
import exceptions.InvalidRegistrationException;
import model.Course;
import model.Registration;
import model.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RegistrationService {
    private final RegistrationDAO registrationDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO  courseDAO;

    public RegistrationService(Connection conn) {
        this.registrationDAO = new RegistrationDAO(conn);
        this.studentDAO      = new StudentDAO(conn);
        this.courseDAO       = new CourseDAO(conn);
    }


    public int registerStudent(int studentId, int courseId)
            throws InvalidRegistrationException, SQLException {
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            throw new InvalidRegistrationException("Student not found: " + studentId);
        }

        Course course = courseDAO.findById(courseId);
        if (course == null) {
            throw new InvalidRegistrationException("Course not found: " + courseId);
        }

        if (isAlreadyRegistered(studentId, courseId)) {
            throw new InvalidRegistrationException("Student already registered for course.");
        }

        Registration reg = new Registration(0, student, course, LocalDate.now());
        return registrationDAO.addRegistration(reg);
    }

    private boolean isAlreadyRegistered(int studentId, int courseId) throws SQLException {
        return registrationDAO.getAllRegistrations().stream()
                .anyMatch(r ->
                        r.getStudent().getStudentId() == studentId &&
                                r.getCourse().getCourseId()==(courseId)
                );
    }


    public List<Registration> getAllRegistrations() throws SQLException {
        return registrationDAO.getAllRegistrations();
    }


    public Registration findById(int id) throws SQLException {
        return registrationDAO.findById(id);
    }


    public void deleteRegistration(int id) throws SQLException {
        registrationDAO.deleteById(id);
    }
}
