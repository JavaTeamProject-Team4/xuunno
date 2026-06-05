package checker;

import model.Student;

public interface RequirementChecker {
    boolean check(Student student);
    String getMessage(Student student);
}