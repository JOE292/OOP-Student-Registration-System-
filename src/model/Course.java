package model;
import interfaces.Reportable;
public class Course implements Reportable {
    private int courseId;
    private String title;
    private String instructor;
    private int creditHours;


    public Course(int courseId, String title, String instructor, int creditHours) {
        this.courseId = courseId;
        this.title = title;
        this.instructor = instructor;
        this.creditHours = creditHours;
    }

    public Course(String title, String instructor, int creditHours) {
        this.title = title;
        this.instructor = instructor;
        this.creditHours = creditHours;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public int getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(int creditHours) {
        this.creditHours = creditHours;
    }

    @Override
    public String toString() {
        return title; // or use: return id + " - " + name;
    }



    @Override
    public String toReportString() {
       return courseId + "," + title;
    }

}


