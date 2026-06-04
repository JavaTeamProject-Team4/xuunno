package checker;

import model.Course;
import model.GeneralCourse;
import model.Student;
import model.TakenCourse;

public class CheomseongChecker implements RequirementChecker {

    private int basicCommunicationCredit = 0;
    private int basicScienceCredit = 0;
    private int coreHumanCredit = 0;
    private int coreScienceCredit = 0;

    private void calculate(Student student) {
        basicCommunicationCredit = 0;
        basicScienceCredit = 0;
        coreHumanCredit = 0;
        coreScienceCredit = 0;

        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course course = tc.getCourse();

            if (course instanceof GeneralCourse && isPassed(tc.getGrade())) {
                GeneralCourse gc = (GeneralCourse) course;

                String cat1 = gc.getCategory1();
                String cat2 = gc.getCategory2();
                String cat3 = gc.getCategory3();

                if (cat1.equals("첨성인기초")) {
                    if (cat2.equals("독서와토론") || cat2.equals("사고교육")
                            || cat2.equals("글쓰기") || cat2.equals("외국어")
                            || cat2.equals("실용영어")) {
                        basicCommunicationCredit += gc.getLectureCredit();
                    }

                    if (cat2.equals("수리") || cat2.equals("기초과학")) {
                        basicScienceCredit += gc.getLectureCredit();
                    }
                }

                if (cat1.equals("첨성인핵심")) {
                    if (cat2.equals("인문사회")) {
                        coreHumanCredit += gc.getLectureCredit();
                    }

                    if (cat2.equals("자연과학")
                            || cat3.equals("과학과기술")
                            || cat3.equals("자연과 환경")) {
                        coreScienceCredit += gc.getLectureCredit();
                    }
                }
            }
        }
    }

    private boolean isPassed(String grade) {
        return !grade.equalsIgnoreCase("F")
                && !grade.equalsIgnoreCase("NP");
    }

    @Override
    public boolean check(Student student) {
        calculate(student);

        return basicCommunicationCredit >= 3
                && basicScienceCredit >= 3
                && coreHumanCredit >= 3
                && coreScienceCredit >= 3;
    }

    @Override
    public String getMessage(Student student) {
        calculate(student);

        return check(student)
                ? "첨성인 기초/핵심 요건 충족"
                : "첨성인 요건 미충족 - 기초(독서/사고/글쓰기/외국어): "
                + basicCommunicationCredit + "/3, 기초(수리/기초과학): "
                + basicScienceCredit + "/3, 핵심(인문사회): "
                + coreHumanCredit + "/3, 핵심(자연과학): "
                + coreScienceCredit + "/3";
    }
}