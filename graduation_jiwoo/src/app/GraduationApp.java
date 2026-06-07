package app;

import java.util.Scanner;

import checker.GraduationChecker;
import db.CourseDB;
import model.Course;
import model.Student;
import model.TakenCourse;

public class GraduationApp {

    // ==== [추가됨] 안전하게 정수만 입력받는 도우미 메서드 (추측입니다) ====
    private static int getValidInt(Scanner sc) {
        while (true) {
            try {
                // 입력을 한 줄 통째로 받아서 숫자로 변환 시도
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // 문자가 입력되면 에러를 무시하고 다시 입력 요구
                System.out.print("정수만 입력 가능합니다. 다시 입력해주세요: ");
            }
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        CourseDB.loadGeneralCourses();

        Student student = null;
        GraduationChecker graduationChecker = new GraduationChecker();

        while (true) {
            System.out.println("========== 졸업요건 충족 여부 판단 프로그램 ==========");
            System.out.println("1. 학생 정보 입력");
            System.out.println("2. 수강 과목 관리");
            System.out.println("3. 졸업요건 확인");
            System.out.println("0. 종료");
            System.out.print("메뉴 선택: ");

            // sc.nextInt() 대신 방어 코드가 적용된 getValidInt() 사용
            int menu = getValidInt(sc);

            if (menu == 1) {
                System.out.print("학번: ");
                int id = getValidInt(sc);

                System.out.print("이름: ");
                // next() 대신 nextLine().trim()을 사용하여 버퍼 충돌 방지
                String name = sc.nextLine().trim();

                System.out.print("전공 선택(1.심컴 2.플숲 3.인컴 4.글숲): ");
                int major = getValidInt(sc);

                CourseDB.loadMajorCoursesByMajor(major);

                // ---- [글솦 전용 트랙 변수 초기화] ----
                int track = 0;
                int globalCredit = 0;
                int practiceCredit = 0;
                int startupCredit = 0;
                String isStartup = "N";
                String isMultiMajor = "N";
                String isDualDegree = "N";

                if (major == 4) {
                    while (true) {
                        System.out.print("트랙 선택 (1.다중전공 트랙  2.해외복수학위 트랙  3.학석사연계 트랙): ");
                        track = getValidInt(sc);
                        if (track >= 1 && track <= 3) break;
                        System.out.println("잘못된 트랙 번호입니다. 다시 선택해주세요.");
                    }
                    // getValidInt()가 엔터를 소비하므로 기존에 있던 버퍼 비우기(sc.nextLine())는 삭제됨

                    if (track == 1) {
                        System.out.print("해외 대학 인정 학점: ");
                        globalCredit = getValidInt(sc);
                        System.out.print("현장실습 학점: ");
                        practiceCredit = getValidInt(sc);
                        System.out.print("창업교과목 학점: ");
                        startupCredit = getValidInt(sc);
                        
                        while (true) {
                            System.out.print("스타트업 창업 여부 (Y/N): ");
                            isStartup = sc.nextLine().trim().toUpperCase();
                            if (isStartup.equals("Y") || isStartup.equals("N")) break;
                            System.out.println("잘못된 입력입니다. Y 또는 N으로 입력해주세요.");
                        }
                        while (true) {
                            System.out.print("다중전공 여부 (Y/N): ");
                            isMultiMajor = sc.nextLine().trim().toUpperCase();
                            if (isMultiMajor.equals("Y") || isMultiMajor.equals("N")) break;
                            System.out.println("잘못된 입력입니다. Y 또는 N으로 입력해주세요.");
                        }
                    } else if (track == 2) {
                        System.out.print("창업교과목 학점: ");
                        startupCredit = getValidInt(sc);
                        
                        while (true) {
                            System.out.print("해외복수학위과정 이수 여부 (Y/N): ");
                            isDualDegree = sc.nextLine().trim().toUpperCase();
                            if (isDualDegree.equals("Y") || isDualDegree.equals("N")) break;
                            System.out.println("잘못된 입력입니다. Y 또는 N으로 입력해주세요.");
                        }
                    } else if (track == 3) {
                        System.out.print("해외 대학 인정 학점: ");
                        globalCredit = getValidInt(sc);
                        System.out.print("현장실습 학점: ");
                        practiceCredit = getValidInt(sc);
                    }
                }

                System.out.print("지도교수 상담 횟수: ");
                int counseling = getValidInt(sc);

                System.out.print("토익 점수: ");
                int toeic = getValidInt(sc);

                student = new Student(id, name, major, counseling, toeic);
                
                // ---- [글솦 전공인 경우 수집한 트랙 데이터를 Student에 주입] ----
                if (major == 4) {
                    student.setTrack(track);
                    student.setGlobalCredit(globalCredit);
                    student.setPracticeCredit(practiceCredit);
                    student.setStartupCredit(startupCredit);
                    student.setIsStartup(isStartup);
                    student.setIsMultiMajor(isMultiMajor);
                    student.setIsDualDegree(isDualDegree);
                }

                System.out.println("학생 정보 입력 완료");
            }

            else if (menu == 2) {
                if (student == null) {
                    System.out.println("학생 정보를 먼저 입력하세요.");
                    continue;
                }

                while (true) {
                    System.out.println("\n========== 수강 과목 관리 ==========");
                    System.out.println("1. 수강 과목 추가");
                    System.out.println("2. 수강 내역 확인");
                    System.out.println("3. 수강 과목 삭제");
                    System.out.println("0. 이전 메뉴로");
                    System.out.print("메뉴 선택: ");
                    
                    int subMenu = getValidInt(sc);

                    if (subMenu == 1) { // 1. 과목 추가
                        while (true) {
                            System.out.print("추가할 과목명 또는 코드 입력(exit 입력 시 종료): ");
                            String code = sc.nextLine().trim();

                            if (code.equalsIgnoreCase("exit")) {
                                break;
                            }

                            Course course = CourseDB.findCourse(code);

                            if (course == null) {
                                System.out.println("해당 과목을 찾을 수 없습니다.");
                                continue;
                            }
                            
                            boolean isDuplicate = false;
                            for (int i = 0; i < student.getCourseCount(); i++) {
                                Course taken = student.getTakenCourses()[i].getCourse();
                                if (taken.getCourseCode().equals(course.getCourseCode()) ||
                                    taken.getLectureName().equals(course.getLectureName())) {
                                    isDuplicate = true;
                                    break;
                                }
                            }

                            if (isDuplicate) {
                                System.out.println("이미 추가된 과목입니다.");
                                continue;
                            }
                            
                            String grade = "";
                            while (true) {
                                System.out.print("성적 입력(A+, A0, A-, B+, B0, B-, C+, C0, C-, D+, D0, D-, F, P, NP): ");
                                grade = sc.nextLine().trim().toUpperCase();

                                if (grade.equals("A+") || grade.equals("A0") || grade.equals("A-") ||
                                    grade.equals("B+") || grade.equals("B0") || grade.equals("B-") ||
                                    grade.equals("C+") || grade.equals("C0") || grade.equals("C-") ||
                                    grade.equals("D+") || grade.equals("D0") || grade.equals("D-") ||
                                    grade.equals("F") || grade.equals("P") || grade.equals("NP")) {
                                    break;
                                } else {
                                    System.out.println("잘못된 입력입니다. 정확한 성적을 다시 입력해주세요.");
                                }
                            }

                            student.addTakenCourse(new TakenCourse(course, grade));
                            System.out.println(course.getLectureName() + " 추가 완료");
                        }
                        
                    } else if (subMenu == 2) { // 2. 내역 확인
                        System.out.println("\n[ 현재 수강 내역: 총 " + student.getCourseCount() + "과목 ]");
                        if (student.getCourseCount() == 0) {
                            System.out.println("입력된 수강 과목이 없습니다.");
                        } else {
                            for (int i = 0; i < student.getCourseCount(); i++) {
                                TakenCourse tc = student.getTakenCourses()[i];
                                System.out.printf("- %s (%s) : %s\n", 
                                        tc.getCourse().getLectureName(), 
                                        tc.getCourse().getCourseCode(), 
                                        tc.getGrade());
                            }
                        }
                        
                    } else if (subMenu == 3) { // 3. 과목 삭제
                        if (student.getCourseCount() == 0) {
                            System.out.println("삭제할 수강 과목이 없습니다.");
                            continue;
                        }
                        
                        while (true) {
                            // 삭제 중 모든 과목이 지워지면 자동으로 루프 탈출
                            if (student.getCourseCount() == 0) {
                                System.out.println("모든 수강 과목이 삭제되었습니다.");
                                break;
                            }
                            
                            System.out.print("삭제할 과목명 또는 코드 입력 (exit 입력 시 종료): ");
                            String target = sc.nextLine().trim();
                            
                            // exit 입력 시 삭제 모드 종료
                            if (target.equalsIgnoreCase("exit")) {
                                break;
                            }
                            
                            boolean isRemoved = student.removeTakenCourse(target);
                            
                            if (isRemoved) {
                                System.out.println(target + " 과목이 수강 내역에서 삭제되었습니다.");
                                
                                // 삭제 성공 시 남은 수강 과목 리스트 출력
                                System.out.println("\n[ 남은 수강 내역: 총 " + student.getCourseCount() + "과목 ]");
                                for (int i = 0; i < student.getCourseCount(); i++) {
                                    TakenCourse tc = student.getTakenCourses()[i];
                                    System.out.printf("- %s (%s) : %s\n", 
                                            tc.getCourse().getLectureName(), 
                                            tc.getCourse().getCourseCode(), 
                                            tc.getGrade());
                                }
                                System.out.println("--------------------------------");
                            } else {
                                System.out.println("입력하신 과목을 수강 내역에서 찾을 수 없습니다.");
                            }
                        }
                        
                    } else if (subMenu == 0) { // 0. 이전 메뉴
                        break;
                    } else {
                        System.out.println("잘못된 메뉴입니다. 다시 선택해주세요.");
                    }
                }
            }

            else if (menu == 3) {
                if (student == null) {
                    System.out.println("학생 정보를 먼저 입력하세요.");
                    continue;
                }
                
                double totalScore = 0.0;
                int totalCredits = 0;

                for (int i = 0; i < student.getCourseCount(); i++) {
                    TakenCourse tc = student.getTakenCourses()[i];
                    String g = tc.getGrade();
                    int credit = tc.getCourse().getLectureCredit();

                    double score = -1.0;
                    switch (g) {
                        case "A+": score = 4.3; break;
                        case "A0": score = 4.0; break;
                        case "A-": score = 3.7; break;
                        case "B+": score = 3.3; break;
                        case "B0": score = 3.0; break;
                        case "B-": score = 2.7; break;
                        case "C+": score = 2.3; break;
                        case "C0": score = 2.0; break;
                        case "C-": score = 1.7; break;
                        case "D+": score = 1.3; break;
                        case "D0": score = 1.0; break;
                        case "D-": score = 0.7; break;
                        case "F": score = 0.0; break;
                    }

                    if (score >= 0.0) {
                        totalScore += (score * credit);
                        totalCredits += credit;
                    }
                }

                if (totalCredits > 0) {
                    student.setGpa(totalScore / totalCredits);
                } else {
                    student.setGpa(0.0);
                }
                
                graduationChecker.printGraduationResult(student);
            }

            else if (menu == 0) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }

            else {
                System.out.println("잘못된 메뉴입니다.");
            }
        }

        sc.close();
    }
}