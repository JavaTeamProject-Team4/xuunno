// ========================================
// GraduationChecker.java
// 전체 졸업 요건 검사 클래스
// ========================================

package checker;

import model.Student;

public class GraduationChecker {

    // 모든 checker 저장
    private RequirementChecker[] checkers;

    public GraduationChecker() {

        checkers =
                new RequirementChecker[] {

                new CreditChecker(),
                new RequiredMajorChecker(),
                new CheomseongChecker(),
                new SDGChecker(),
                new EnglishChecker(),
                new CounselingChecker(),
                new GpaChecker(),
                new GsTrackChecker()
        };
    }

    // 전체 결과 출력
    public void printGraduationResult(
            Student student) {

        boolean canGraduate = true;

        System.out.println(
                "===== 졸업 요건 검사 ====="
        );
        
        String majorName = "";
        switch (student.getStudentMajor()) {
            case 1: majorName = "심화컴퓨팅"; break;
            case 2: majorName = "플랫폼SW"; break;
            case 3: majorName = "인공지능컴퓨팅"; break;
            case 4: majorName = "글로벌SW"; break;
            default: majorName = "알수없음"; break;
        }
        
        
        // 학생 이름 및 학번 출력 추가
        System.out.println(
        		" " +majorName + " " + student.getStudentName() + " (" + student.getStudentId() + ")"
        		);
        System.out.println("");

        // 모든 checker 실행
        for (int i = 0; i < checkers.length; i++) {
            String msg = checkers[i].getMessage(student);
            
            // [수정된 부분] 메시지가 비어있지 않을 때만 결과창 텍스트에 추가
            if (msg != null && !msg.isEmpty()) {
            	System.out.println(msg);
            }
            
            if (!checkers[i].check(student)) {
                canGraduate = false;
            }
  
        }

        System.out.println();

        // 최종 결과
        if(canGraduate) {

            System.out.println(
                    "졸업 가능합니다."
            );
        }
        else {

            System.out.println(
                    "졸업 불가능합니다."
            );
        }
        
        
    }
    public String getGraduationResultText(Student student) {
        StringBuilder sb = new StringBuilder(); 
        boolean canGraduate = true;

        String majorName = "";
        switch (student.getStudentMajor()) {
            case 1: majorName = "심화컴퓨팅"; break;
            case 2: majorName = "플랫폼SW"; break;
            case 3: majorName = "인공지능컴퓨팅"; break;
            case 4: majorName = "글로벌SW"; break;
            default: majorName = "알수없음"; break;
        }

        sb.append("========== 졸업 요건 검사 ==========\n");
        sb.append("전공: ").append(majorName).append("\n");
        sb.append("이름: ").append(student.getStudentName()).append(" (").append(student.getStudentId()).append(")\n\n");

        for (int i = 0; i < checkers.length; i++) {
            String msg = checkers[i].getMessage(student);
            
            // [방어 로직] 빈 문자열이 아닐 때만 결과창 텍스트에 추가
            if (msg != null && !msg.isEmpty()) {
                sb.append(msg).append("\n");
            }
            
            if (!checkers[i].check(student)) {
                canGraduate = false;
            }
        }

        sb.append("\n==================================\n");
        if (canGraduate) {
            sb.append("🎉 최종 결과: 졸업 가능합니다! 🎉");
        } else {
            sb.append("❌ 최종 결과: 졸업 불가능합니다. ❌");
        }

        return sb.toString(); 
    }
    
    
}