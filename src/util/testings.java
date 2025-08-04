package util;

import DAO.*;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;

public class testings {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration", "root", "")) {
            StudentDAO studentDAO = new StudentDAO(conn);
            CourseDAO courseDAO = new CourseDAO(conn);
            RegistrationDAO registrationDAO = new RegistrationDAO(conn);

            
            Student s1 = new Student("Test User", "testuser@example.com", "CS");
            studentDAO.addStudentIfNotExists(s1);

            
            System.out.println("All Students:");
            for (Student s : studentDAO.getAllStudents()) {
                System.out.println(s.getStudentId() + ": " + s.getName() + " - " + s.getEmail());
            }

            
            s1.setName("Updated User");
            studentDAO.updateStudent(s1);

            
            Student fetched = studentDAO.findById(s1.getStudentId());
            System.out.println("Fetched Student: " + fetched.getName());

            
            Course c1 = new Course(999, "Testing Java", "Prof. Tester", 5);
            courseDAO.addCourseIfNotExists(c1);

            
            Registration reg = new Registration(s1, c1, LocalDate.now());
            registrationDAO.addRegistrationIfNotExists(reg);

            
            System.out.println("All Registrations:");
            for (Registration r : registrationDAO.getAllRegistrations()) {
                System.out.println(r.getStudent().getName() + " -> " + r.getCourse().getTitle());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
