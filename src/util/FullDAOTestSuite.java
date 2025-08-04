package util;

import DAO.*;
import model.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.LocalDate;
import java.util.List;

public class FullDAOTestSuite {
    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration", "root", "")) {
            StudentDAO studentDAO = new StudentDAO(conn);
            CourseDAO courseDAO = new CourseDAO(conn);
            RegistrationDAO registrationDAO = new RegistrationDAO(conn);

            System.out.println("🚀 Starting full DAO test suite...");

        
            Student student = new Student("Test User", "test@example.com", "CS");
            studentDAO.addStudentIfNotExists(student);


            Student fetched = studentDAO.findById(student.getStudentId());
            System.out.println("✅ Fetch by ID: " + fetched.getName());
            
            student.setName("Updated User");
            studentDAO.updateStudent(student);
            
            List<Student> allStudents = studentDAO.getAllStudents();
            System.out.println("✅ All Students:");
            allStudents.forEach(s -> System.out.println(s.getStudentId() + ": " + s.getName()));

            Course course = new Course(101, "Testing Course", "Dr. Developer", 4);
            courseDAO.addCourseIfNotExists(course);

            Course fetchedCourse = courseDAO.findById(course.getCourseId());
            System.out.println("✅ Fetched Course: " + fetchedCourse.getTitle());

            
            Registration registration = new Registration(student, course, LocalDate.now());
            registrationDAO.addRegistrationIfNotExists(registration);

            
            List<Registration> registrations = registrationDAO.getAllRegistrations();
            System.out.println("✅ All Registrations:");
            registrations.forEach(r -> System.out.println(r.getStudent().getName() + " -> " + r.getCourse().getTitle()));

            courseDAO.deleteById(course.getCourseId());
            studentDAO.deleteById(student.getStudentId());

            System.out.println("✅ All tests passed and data cleaned up.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
