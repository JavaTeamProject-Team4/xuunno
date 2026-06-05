package checker;

import model.Student;

public class GsTrackChecker implements RequirementChecker {

    @Override
    public boolean check(Student student) {
        if (student.getStudentMajor() != 4) {
            return true;
        }

        int track = student.getTrack();

        if (track == 1) { // 1. 다중전공 트랙
            boolean globalPass = student.getGlobalCredit() >= 9;
            // || (OR) 조건을 && (AND) 조건으로 모두 확실하게 수정함
            boolean techPass = student.getPracticeCredit() >= 3 
                            && student.getStartupCredit() >= 9 
                            && student.getIsStartup().equals("Y");
            boolean swPass = student.getIsMultiMajor().equals("Y");
            
            return globalPass && techPass && swPass;

        } else if (track == 2) { // 2. 해외복수학위 트랙
            boolean globalPass = student.getIsDualDegree().equals("Y");
            boolean techPass = student.getStartupCredit() >= 3;
            
            return globalPass && techPass;

        } else if (track == 3) { // 3. 학석사연계 트랙
            boolean globalPass = student.getGlobalCredit() >= 6;
            boolean techPass = student.getPracticeCredit() >= 3;
            
            return globalPass && techPass;
        }

        return false;
    }

    @Override
    public String getMessage(Student student) {
        if (student.getStudentMajor() != 4) {
            return "";
        }

        int track = student.getTrack();
        boolean isPassed = check(student);
        
        String status = isPassed ? "충족" : "미충족";
        String trackName = "";
        String details = "";

        if (track == 1) {
            trackName = "다중전공 트랙";
            // 이곳의 판정 조건도 && (AND) 로 동일하게 수정함
            boolean techPass = student.getPracticeCredit() >= 3 
                            && student.getStartupCredit() >= 9 
                            && student.getIsStartup().equals("Y");
            
            details = String.format("해외대학 인정 학점: %d/9, 현장실습/창업/스타트업: %s, 다중전공: %s", 
                    student.getGlobalCredit(), 
                    techPass ? "충족" : "미충족", 
                    student.getIsMultiMajor().equals("Y") ? "이수" : "미이수");
        } else if (track == 2) {
            trackName = "해외복수학위 트랙";
            details = String.format("해외복수학위: %s, 창업교과목: %d/3", 
                    student.getIsDualDegree().equals("Y") ? "이수" : "미이수", 
                    student.getStartupCredit());
        } else if (track == 3) {
            trackName = "학석사연계 트랙";
            details = String.format("해외대학 인정 학점: %d/6, 현장실습: %d/3", 
                    student.getGlobalCredit(), 
                    student.getPracticeCredit());
        } else {
            return "글로벌소프트웨어 트랙 요건 미충족 - 등록된 트랙 정보가 없습니다.";
        }

        return String.format("글로벌소프트웨어 트랙 요건 %s - %s, %s", status, trackName, details);
    }
}