package checker;

import model.Course;
import model.MajorCourse;
import model.Student;
import model.TakenCourse;

public class RequiredMajorChecker implements RequirementChecker {

    private static final int REQUIRED_MAJOR_CREDIT = 72;
    private static final int REQUIRED_REQUIRED_MAJOR_COUNT = 6;

    @Override
    public boolean check(Student student) {
        return getMajorCredit(student) >= REQUIRED_MAJOR_CREDIT
                && getRequiredMajorCount(student) >= REQUIRED_REQUIRED_MAJOR_COUNT;
    }

    private int getMajorCredit(Student student) {
        int majorCredit = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof MajorCourse && isPassed(tc.getGrade())) {
                majorCredit += course.getLectureCredit();
            }
        }

        return majorCredit;
    }

    private int getRequiredMajorCount(Student student) {
        int count = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof MajorCourse && isPassed(tc.getGrade())) {
                MajorCourse mc = (MajorCourse) course;

                if (mc.isRequired()) {
                    count++;
                }
            }
        }

        return count;
    }

    private boolean isPassed(String grade) {
        return !grade.equalsIgnoreCase("F")
                && !grade.equalsIgnoreCase("NP");
    }

    @Override
    public String getMessage(Student student) {
        return check(student)
                ? "전공 요건 충족"
                : "전공 요건 미충족 - 전공학점: " + getMajorCredit(student)
                + "/72, 전필 과목 수: " + getRequiredMajorCount(student)
                + "/6";
    }
}