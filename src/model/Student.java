package model;

import interfaces.Reportable;

public class Student implements Reportable {
    private int studentId;
    private String name;
    private String email;
    private String major;

    // Constructor with id (for existing students loaded from DB)
    public Student(int studentId, String name, String email, String major) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.major = major;
    }

    // Constructor without id (for new students before saving)
    public Student(String name, String email, String major) {
        this.name = name;
        this.email = email;
        this.major = major;
    }

    // Getters and setters
    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    @Override
    public String toReportString() {
        return studentId + "," + name;
    }

    @Override
    public String toString() {
        return name; // or use: return courseId + " - " + title;
    }

}
