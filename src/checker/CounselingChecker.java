package checker;

import model.Student;

public class CounselingChecker implements RequirementChecker {
    private static final int REQUIRED_COUNSELING = 8;

    @Override
    public boolean check(Student student) {
        return student.getCounselingCount() >= REQUIRED_COUNSELING;
    }

    @Override
    public String getMessage(Student student) {
        int count = student.getCounselingCount();

        return check(student)
                ? "지도교수 상담 요건 충족 (" + count + "/8)"
                : "지도교수 상담 요건 미충족 (" + count + "/8)";
    }
}