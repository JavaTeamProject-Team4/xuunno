package checker;

import model.Course;
import model.GeneralCourse;
import model.Student;
import model.TakenCourse;

public class SDGChecker implements RequirementChecker {

    private static final int REQUIRED_SDG_CREDIT = 3;

    public int getSDGCredit(Student student) {
        int total = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof GeneralCourse && isPassed(tc.getGrade())) {
                GeneralCourse gc = (GeneralCourse) course;

                if (gc.isSDG()) {
                    total += gc.getLectureCredit();
                }
            }
        }

        return total;
    }

    private boolean isPassed(String grade) {
        return !grade.equalsIgnoreCase("F") && !grade.equalsIgnoreCase("NP");
    }

    @Override
    public boolean check(Student student) {
        return getSDGCredit(student) >= REQUIRED_SDG_CREDIT;
    }

    @Override
    public String getMessage(Student student) {
        return check(student)
                ? "SDG 요건 충족 (" + getSDGCredit(student) + "/3)"
                : "SDG 요건 미충족 (" + getSDGCredit(student) + "/3)";
    }
}