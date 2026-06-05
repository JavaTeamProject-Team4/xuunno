package checker;

import model.*;

public class EnglishChecker implements RequirementChecker {

    // 학생 정보에 맞춰 요구 토익 점수를 동적으로 반환하는 메서드 추가
    private int getRequiredToeic(Student student) {
        // 글로벌소프트웨어 전공(4)이면서 해외복수학위 트랙(2)인 경우만 800점
        if (student.getStudentMajor() == 4 && student.getTrack() == 2) {
            return 800;
        }
        // 나머지 전공 및 글솦 타 트랙은 모두 700점
        return 700;
    }

    public boolean hasEnglishCourse(Student student) {
        for (int i = 0; i < student.getCourseCount(); i++) {
            TakenCourse tc = student.getTakenCourses()[i];
            Course c = tc.getCourse();
            String grade = tc.getGrade().toUpperCase();

            if (grade.equals("F") || grade.equals("NP")) {
                continue;
            }

            if (c instanceof GeneralCourse) {
                GeneralCourse gc = (GeneralCourse)c;

                if (gc.isEnglish()) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean check(Student student) {
        int requiredToeic = getRequiredToeic(student);
        return student.getToeicScore() >= requiredToeic
                || hasEnglishCourse(student);
    }

    @Override
    public String getMessage(Student student) {
        int requiredToeic = getRequiredToeic(student);

        if (check(student)) {
            if (student.getToeicScore() >= requiredToeic) {
                // 요구 점수에 맞게 동적으로 출력
                return "영어 요건 충족 - 토익 " + student.getToeicScore() + "/" + requiredToeic;
            }

            return "영어 요건 충족 - 영어 교양 이수";
        }

        // 미충족 시에도 부족한 점수와 기준 점수를 동적으로 계산하여 출력
        return "영어 요건 미충족 - 토익 "
                + (requiredToeic - student.getToeicScore())
                + "점 부족(" + student.getToeicScore()
                + "/" + requiredToeic + ") 또는 영어 교양 이수 필요";
    }
}