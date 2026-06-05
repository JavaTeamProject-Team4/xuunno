package model;

public class TakenCourse {
    private Course course;
    private String grade;

    public TakenCourse(Course course, String grade) {
        this.course = course;
        this.grade = grade;
    }

    public Course getCourse() {
        return course;
    }

    public String getGrade() {
        return grade;
    }
}