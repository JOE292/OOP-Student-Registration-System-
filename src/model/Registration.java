package model;

import interfaces.Reportable;
import java.time.LocalDate;

public class Registration implements Reportable {
    private int  registrationId;
    private Student student;
    private Course course;
    private LocalDate registrationDate;

    public  Registration(int registrationId, Student student, Course course, LocalDate registrationDate){
        this.registrationId=registrationId;
        this.student=student;
        this.course=course;
        this.registrationDate=registrationDate;
    }

    public  Registration(Student student, Course course, LocalDate registrationDate){
        this.student=student;
        this.course=course;
        this.registrationDate=registrationDate;
    }

    public int getRegistrationId(){
        return registrationId;
    }
    public void setRegistrationId(int registrationId){
        this.registrationId=registrationId;
    }
    public Student getStudent(){
        return student;
    }
    public void setStudent(Student student){
        this.student=student;
    }
    public Course getCourse(){
        return course;
    }
    public void setCourse(Course course){
        this.course=course;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }


    @Override
    public String toReportString() {
        return registrationId + "," +
                student.getStudentId() + "," +
                course.getCourseId() + "," +
                registrationDate;
    }
}
