package model;

public class Course {
    protected String courseCode;    // 과목코드
    protected String lectureName;   // 과목명 
    protected int lectureCredit;    // 학점 

    public Course(String courseCode, String lectureName, int lectureCredit) {
        this.courseCode = courseCode;
        this.lectureName = lectureName;
        this.lectureCredit = lectureCredit;
    }

    public String getCourseCode() { return courseCode; }
    public String getLectureName() { return lectureName; }
    public int getLectureCredit() { return lectureCredit; }
}