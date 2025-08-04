package util;

import model.Course;
import model.Student;
import model.Registration;

import java.util.List;

public class RegisteredStudentReport {
    public static String formatStudentReport(Student student, List<Course> courses) {
        StringBuilder sb = new StringBuilder();
        sb.append("Student ID: ").append(student.getStudentId()).append("\n");
        sb.append("Name: ").append(student.getName()).append("\n");
        sb.append("Email: ").append(student.getEmail()).append("\n");
        sb.append("Major: ").append(student.getMajor()).append("\n");
        sb.append("\nRegistered Courses:\n");

        if (courses.isEmpty()) {
            sb.append("  No registered courses.\n");
        } else {
            for (Course c : courses) {
                sb.append("  - ").append(c.getTitle())
                        .append(" (").append(c.getCourseId())
                        .append(", ").append(c.getInstructor())
                        .append(", ").append(c.getCreditHours()).append(" Credit Hrs)\n");
            }
        }

        return sb.toString();
    }
}
