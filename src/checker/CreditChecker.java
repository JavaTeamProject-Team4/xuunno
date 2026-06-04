package checker;

import model.Student;
import model.Course;
import model.MajorCourse;
import model.GeneralCourse;
import model.TakenCourse;

public class CreditChecker implements RequirementChecker {

    private static final int REQUIRED_TOTAL_CREDIT = 130;
    private static final int REQUIRED_MAJOR_CREDIT = 65;
    private static final int REQUIRED_GENERAL_CREDIT = 30;

    public int getTotalCredit(Student student) {
        int total = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];

            if (isPassed(tc.getGrade())) {
                total += tc.getCourse().getLectureCredit();
            }
        }

        return total;
    }

    public int getMajorCredit(Student student) {
        int total = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof MajorCourse && isPassed(tc.getGrade())) {
                total += course.getLectureCredit();
            }
        }

        return total;
    }

    public int getGeneralCredit(Student student) {
        int total = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof GeneralCourse && isPassed(tc.getGrade())) {
                total += course.getLectureCredit();
            }
        }

        return total;
    }

    private boolean isPassed(String grade) {
        return !grade.equalsIgnoreCase("F") && !grade.equalsIgnoreCase("NP");
    }

    @Override
    public boolean check(Student student) {
        return getTotalCredit(student) >= REQUIRED_TOTAL_CREDIT
                && getMajorCredit(student) >= REQUIRED_MAJOR_CREDIT
                && getGeneralCredit(student) >= REQUIRED_GENERAL_CREDIT;
    }

    @Override
    public String getMessage(Student student) {
        return check(student)
                ? "학점 요건 충족"
                : "학점 요건 미충족 - 총학점: " + getTotalCredit(student)
                + "/130, 전공: " + getMajorCredit(student)
                + "/65, 교양: " + getGeneralCredit(student)
                + "/30";
    }
}