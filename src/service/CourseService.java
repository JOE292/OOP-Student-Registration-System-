package service;

import DAO.CourseDAO;
import model.Course;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CourseService {
    private final CourseDAO courseDAO;

    public CourseService(Connection conn) {
        this.courseDAO = new CourseDAO(conn);
    }

    public void addCourse(Course course) throws SQLException {
        if ((course.getCourseId() == 0) || (course.getTitle() == null) ||
                (course.getInstructor() == null) || (course.getCreditHours() <= 0)) {
            throw new IllegalArgumentException("Course fields are invalid or missing.");
        }
        courseDAO.addCourse(course);
    }

    public Course findCourseById(int courseId) throws SQLException {
       return courseDAO.findById(courseId);
    }

    public void deleteCourse(int courseId) throws SQLException {
        courseDAO.deleteById(courseId);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseDAO.getAllCourses();
    }
}
