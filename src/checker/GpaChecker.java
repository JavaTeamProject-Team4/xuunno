package checker;

import model.Student;

public class GpaChecker implements RequirementChecker {
    private static final double REQUIRED_GPA = 1.7;

    @Override
    public boolean check(Student student) {
        return student.getGpa() >= REQUIRED_GPA;
    }

    @Override
    public String getMessage(Student student) {
        return check(student)
                ? String.format("평균 성적 요건 충족 (%.2f/%.2f)", student.getGpa(), REQUIRED_GPA)
                : String.format("평균 성적 요건 미충족 (%.2f/%.2f)", student.getGpa(), REQUIRED_GPA);
    }
}