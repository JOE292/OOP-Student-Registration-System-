package service;

import DAO.StudentDAO;
import model.Student;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class StudentService {
    private final StudentDAO studentDAO;

    public StudentService(Connection conn) {
        this.studentDAO = new StudentDAO(conn);
    }

    public int addStudent(Student student) throws SQLException {
        if (student.getName() == null || student.getEmail() == null || student.getMajor() == null) {
            throw new IllegalArgumentException("Student fields cannot be null");
        }
        return studentDAO.addStudent(student);
    }

    public Student findStudentById(int id) throws SQLException {
        return studentDAO.findById(id);
    }

    public void deleteStudent(int id) throws SQLException {
        studentDAO.deleteById(id);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentDAO.getAllStudents();
    }
}
