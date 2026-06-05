package model;

public class MajorCourse extends Course {
    private boolean required;

    public MajorCourse(String courseCode, String lectureName, int lectureCredit, boolean required) {
        super(courseCode, lectureName, lectureCredit);
        this.required = required;
    }

    public boolean isRequired() {
        return required;
    }
}