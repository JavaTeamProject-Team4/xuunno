package app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import checker.GraduationChecker;
import db.CourseDB;
import model.Student;
import model.Course;
import model.TakenCourse;

public class GraduationGUI extends JFrame {
    
    // 기존 콘솔 앱에서 쓰던 변수들 그대로 가져오기
    private Student student = null;
    private GraduationChecker graduationChecker = new GraduationChecker();

    public GraduationGUI() {
        CourseDB.loadGeneralCourses();

        // 메인 창 기본 설정
        setTitle("경북대학교 컴퓨터학부 졸업요건 확인 프로그램");
        setSize(400, 350); // 창 크기 (가로, 세로) - 유지
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // X버튼 누르면 프로그램 종료
        setLocationRelativeTo(null); // 창을 화면 정중앙에 띄우기
        setLayout(new BorderLayout(10, 10)); // 화면 배치 방법 설정

        // 3. 상단 제목 라벨 
        JLabel titleLabel = new JLabel("            졸업요건 검사            ", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        add(titleLabel, BorderLayout.NORTH);

        // 4. 중앙 버튼들을 담을 패널 (세로로 4칸 쪼개기)
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20)); // 여백 주기

        JButton btn1 = new JButton("1. 학생 정보 입력");
        JButton btn2 = new JButton("2. 수강 과목 관리"); // 텍스트만 '관리'로 변경
        JButton btn3 = new JButton("3. 졸업요건 확인");
        JButton btn4 = new JButton("종료");
        Font buttonFont = new Font("맑은 고딕", Font.BOLD, 18);
        btn1.setFont(buttonFont);
        btn2.setFont(buttonFont);
        btn3.setFont(buttonFont);
        btn4.setFont(buttonFont);
        
        buttonPanel.add(btn1);
        buttonPanel.add(btn2);
        buttonPanel.add(btn3);
        buttonPanel.add(btn4);
        
        add(buttonPanel, BorderLayout.CENTER);

     // 1번 버튼: 학생 정보 입력
        btn1.addActionListener(e -> {

            JPanel panel = new JPanel(new GridLayout(5, 2, 5, 10));
            panel.setPreferredSize(new Dimension(400, 200));

            JTextField idField = new JTextField(); 
            JTextField nameField = new JTextField();
            String[] majors = {"심화컴퓨팅", "플랫폼SW", "인공지능컴퓨팅", "글로벌SW"};
            JComboBox<String> majorCombo = new JComboBox<>(majors);
            JTextField counselingField = new JTextField();
            JTextField toeicField = new JTextField();

            panel.add(new JLabel("학번:")); panel.add(idField);
            panel.add(new JLabel("이름:")); panel.add(nameField);
            panel.add(new JLabel("전공 선택:")); panel.add(majorCombo);
            panel.add(new JLabel("지도교수 상담 횟수:")); panel.add(counselingField);
            panel.add(new JLabel("토익 점수:")); panel.add(toeicField);

            int result = JOptionPane.showConfirmDialog(null, panel, 
                     "학생 정보 입력", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int id = Integer.parseInt(idField.getText().trim());
                    String name = nameField.getText().trim();
                    int major = majorCombo.getSelectedIndex() + 1; // 4번이면 글로벌SW
                    int counseling = Integer.parseInt(counselingField.getText().trim());
                    int toeic = Integer.parseInt(toeicField.getText().trim());

                    // ---- [글솦 전용 트랙 변수 초기화] ----
                    int track = 0, globalCredit = 0, practiceCredit = 0, startupCredit = 0;
                    String isStartup = "N", isMultiMajor = "N", isDualDegree = "N";

                    if (major == 4) {
                        String[] tracks = {"1. 다중전공 트랙", "2. 해외복수학위 트랙", "3. 학석사연계 트랙"};
                        JComboBox<String> trackCombo = new JComboBox<>(tracks);
                        int trackResult = JOptionPane.showConfirmDialog(null, trackCombo, 
                                "글로벌SW 트랙 선택", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                        
                        if (trackResult != JOptionPane.OK_OPTION) return; // 도중에 취소하면 저장 안 함
                        track = trackCombo.getSelectedIndex() + 1;

                        JPanel trackPanel = new JPanel(new GridLayout(0, 2, 5, 10));
                        JTextField globalField = new JTextField("0");
                        JTextField practiceField = new JTextField("0");
                        JTextField startupField = new JTextField("0");
                        String[] yn = {"Y", "N"};
                        JComboBox<String> ynCombo1 = new JComboBox<>(yn);
                        JComboBox<String> ynCombo2 = new JComboBox<>(yn);

                        // 트랙에 따라 필요한 입력창만 패널에 조립
                        if (track == 1) {
                            trackPanel.add(new JLabel("해외 대학 인정 학점:")); trackPanel.add(globalField);
                            trackPanel.add(new JLabel("현장실습 학점:")); trackPanel.add(practiceField);
                            trackPanel.add(new JLabel("창업교과목 학점:")); trackPanel.add(startupField);
                            trackPanel.add(new JLabel("스타트업 창업 여부:")); trackPanel.add(ynCombo1);
                            trackPanel.add(new JLabel("다중전공 여부:")); trackPanel.add(ynCombo2);
                        } else if (track == 2) {
                            trackPanel.add(new JLabel("창업교과목 학점:")); trackPanel.add(startupField);
                            trackPanel.add(new JLabel("해외복수학위 이수 여부:")); trackPanel.add(ynCombo1);
                        } else if (track == 3) {
                            trackPanel.add(new JLabel("해외 대학 인정 학점:")); trackPanel.add(globalField);
                            trackPanel.add(new JLabel("현장실습 학점:")); trackPanel.add(practiceField);
                        }

                        int detailResult = JOptionPane.showConfirmDialog(null, trackPanel, 
                                "트랙 세부 요건 입력", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                        if (detailResult != JOptionPane.OK_OPTION) return;


                        globalCredit = Integer.parseInt(globalField.getText().trim());
                        practiceCredit = Integer.parseInt(practiceField.getText().trim());
                        startupCredit = Integer.parseInt(startupField.getText().trim());
                        if (track == 1) {
                            isStartup = (String) ynCombo1.getSelectedItem();
                            isMultiMajor = (String) ynCombo2.getSelectedItem();
                        } else if (track == 2) {
                            isDualDegree = (String) ynCombo1.getSelectedItem();
                        }
                    }

                    CourseDB.loadMajorCoursesByMajor(major);
                    student = new Student(id, name, major, counseling, toeic);

                    if (major == 4) {
                        student.setTrack(track);
                        student.setGlobalCredit(globalCredit);
                        student.setPracticeCredit(practiceCredit);
                        student.setStartupCredit(startupCredit);
                        student.setIsStartup(isStartup);
                        student.setIsMultiMajor(isMultiMajor);
                        student.setIsDualDegree(isDualDegree);
                    }

                    JOptionPane.showMessageDialog(null, "학생 정보가 성공적으로 저장되었습니다!", "저장 완료", JOptionPane.INFORMATION_MESSAGE);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "숫자 입력란에 문자를 입력하셨습니다. 다시 입력해주세요!", "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        }); 
        
        // ==========================================
        // 2번 버튼: 수강 과목 관리
        // ==========================================
        btn2.addActionListener(e -> {

            if (student == null) {
                JOptionPane.showMessageDialog(null, "학생 정보를 먼저 입력하세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            while (true) {
            	
            	final int[] choice = {-1}; 
                
                JDialog dialog = new JDialog((JFrame)null, "수강 과목 관리", true);
                dialog.setSize(500, 300); // 서브 메뉴 창 크기
                dialog.setLocationRelativeTo(null); // 화면 중앙 배치
                dialog.setLayout(new BorderLayout(10, 10));

                // 상단 안내 문구
                JLabel titleLabel1 = new JLabel("원하시는 작업을 선택하세요. (입력된 과목: " + student.getCourseCount() + "개)", SwingConstants.CENTER);
                titleLabel1.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
                
                titleLabel1.setFont(new Font("맑은 고딕", Font.BOLD, 16)); //폰트
                dialog.add(titleLabel1, BorderLayout.NORTH);

                // 중앙 버튼 패널 (세로 4칸 쪼개기)
                JPanel subButtonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
                subButtonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
                
                JButton btnAdd = new JButton("1. 수강 과목 추가");
                JButton btnView = new JButton("2. 수강 내역 확인");
                JButton btnDel = new JButton("3. 수강 과목 삭제");
                JButton btnClose = new JButton("닫기");

                Font subMenuFont = new Font("맑은 고딕", Font.BOLD, 14);
                btnAdd.setFont(subMenuFont);
                btnView.setFont(subMenuFont);
                btnDel.setFont(subMenuFont);
                btnClose.setFont(subMenuFont);
                
                // 각 버튼을 눌렀을 때 choice 배열에 결과를 저장하고 창을 닫음
                btnAdd.addActionListener(ev -> { choice[0] = 0; dialog.dispose(); });
                btnView.addActionListener(ev -> { choice[0] = 1; dialog.dispose(); });
                btnDel.addActionListener(ev -> { choice[0] = 2; dialog.dispose(); });
                btnClose.addActionListener(ev -> { choice[0] = 3; dialog.dispose(); });

                subButtonPanel.add(btnAdd);
                subButtonPanel.add(btnView);
                subButtonPanel.add(btnDel);
                subButtonPanel.add(btnClose);

                dialog.add(subButtonPanel, BorderLayout.CENTER);
                dialog.setVisible(true); // 👈 여기서 창이 뜨고 사용자가 클릭할 때까지 대기합니다.

                // 선택된 결과값 가져오기
                int menuResult = choice[0];

                if (menuResult == 0) { 
                    // [1. 수강 과목 추가] - 기존과 동일 (크기 유지)
                    while (true) {
                        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 10));
                        panel.setPreferredSize(new Dimension(400, 100));

                        JTextField courseField = new JTextField();
                        String[] grades = {"A+", "A0", "A-", "B+", "B0", "B-", "C+", "C0", "C-", "D+", "D0", "D-", "F", "P", "NP"};
                        JComboBox<String> gradeCombo = new JComboBox<>(grades);

                        panel.add(new JLabel("과목코드 또는 과목명:"));
                        panel.add(courseField);
                        panel.add(new JLabel("성적:"));
                        panel.add(gradeCombo);

                        int result = JOptionPane.showConfirmDialog(null, panel, 
                                "수강 과목 추가 (그만 추가하려면 '취소' 클릭)", 
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (result != JOptionPane.OK_OPTION) {
                            break;
                        }

                        String code = courseField.getText().trim();
                        
                        if (code.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "과목명을 1글자 이상 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                            continue;
                        }

                        Course course = CourseDB.findCourse(code);

                        if (course == null) {
                            JOptionPane.showMessageDialog(null, "해당 과목을 찾을 수 없습니다.\n다시 입력해주세요.", "검색 실패", JOptionPane.ERROR_MESSAGE);
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
                            JOptionPane.showMessageDialog(null, "이미 추가된 과목입니다.", "중복 오류", JOptionPane.WARNING_MESSAGE);
                            continue;
                        }

                        String grade = (String) gradeCombo.getSelectedItem();
                        student.addTakenCourse(new TakenCourse(course, grade));

                        JOptionPane.showMessageDialog(null, course.getLectureName() + " 과목이 성공적으로 추가되었습니다!\n(현재까지 수강한 과목: " + student.getCourseCount() + "개)", "추가 완료", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } else if (menuResult == 1) { 
                    // [2. 수강 내역 확인]
                    if (student.getCourseCount() == 0) {
                        JOptionPane.showMessageDialog(null, "입력된 수강 과목이 없습니다.", "수강 내역 확인", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("현재 수강 내역: 총 ").append(student.getCourseCount()).append("과목\n");
                        sb.append("--------------------------------------------------\n");
                        for (int i = 0; i < student.getCourseCount(); i++) {
                            TakenCourse tc = student.getTakenCourses()[i];
                            sb.append("- ").append(tc.getCourse().getLectureName())
                              .append(" (").append(tc.getCourse().getCourseCode()).append(") : ")
                              .append(tc.getGrade()).append("\n");
                        }
                        
                        JTextArea textArea = new JTextArea(sb.toString());
                        textArea.setEditable(false);
                        textArea.setFont(new Font("맑은 고딕", Font.BOLD, 14));
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        scrollPane.setPreferredSize(new Dimension(350, 250)); // 목록 스크롤용 적당한 사이즈
                        
                        JOptionPane.showMessageDialog(null, scrollPane, "수강 내역 확인", JOptionPane.PLAIN_MESSAGE);
                    }
                    
                } else if (menuResult == 2) { 
                    // [3. 수강 과목 삭제]
                    if (student.getCourseCount() == 0) {
                        JOptionPane.showMessageDialog(null, "삭제할 수강 과목이 없습니다.", "경고", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }

                    while (true) {
                        if (student.getCourseCount() == 0) {
                            JOptionPane.showMessageDialog(null, "모든 수강 과목이 삭제되었습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
                            break;
                        }

                        String target = JOptionPane.showInputDialog(null, 
                                "삭제할 과목명 또는 코드를 입력하세요.\n(취소를 누르면 이전 메뉴로 돌아갑니다.)", 
                                "수강 과목 삭제", JOptionPane.PLAIN_MESSAGE);

                        // 취소 버튼 누르거나 창을 닫은 경우 탈출
                        if (target == null || target.trim().isEmpty()) { 
                            break;
                        }

                        boolean isRemoved = student.removeTakenCourse(target);

                        if (isRemoved) {
                            JOptionPane.showMessageDialog(null, target + " 과목이 수강 내역에서 삭제되었습니다.\n(남은 과목: " + student.getCourseCount() + "개)", "삭제 성공", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "입력하신 과목을 찾을 수 없습니다.", "삭제 실패", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    // [0. 닫기] (X 버튼 누르거나 닫기 버튼 클릭)
                    break;
                }
            }
        });
        

     // 3번 버튼: 졸업요건 확인
        btn3.addActionListener(e -> {
            if (student == null) {
                JOptionPane.showMessageDialog(null, "학생 정보를 먼저 입력하세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double totalScore = 0.0;
            int totalCredits = 0;

            for (int i = 0; i < student.getCourseCount(); i++) {
                TakenCourse tc = student.getTakenCourses()[i];
                String g = tc.getGrade();
                int credit = tc.getCourse().getLectureCredit();

                double score = -1.0;
                switch (g) {
                    case "A+": score = 4.3; break; case "A0": score = 4.0; break; case "A-": score = 3.7; break;
                    case "B+": score = 3.3; break; case "B0": score = 3.0; break; case "B-": score = 2.7; break;
                    case "C+": score = 2.3; break; case "C0": score = 2.0; break; case "C-": score = 1.7; break;
                    case "D+": score = 1.3; break; case "D0": score = 1.0; break; case "D-": score = 0.7; break;
                    case "F": score = 0.0; break;
                }

                if (score >= 0.0) { // P, NP는 계산에서 제외됨
                    totalScore += (score * credit);
                    totalCredits += credit;
                }
            }

            if (totalCredits > 0) {
                student.setGpa(totalScore / totalCredits);
            } else {
                student.setGpa(0.0);
            }
            // ===================================================

            // 결과 텍스트 받아오기 
            String resultText = graduationChecker.getGraduationResultText(student);

            JTextArea textArea = new JTextArea(resultText);
            textArea.setEditable(false);
            textArea.setFont(new Font("맑은 고딕", Font.BOLD, 15));
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(1000, 400)); 

            JOptionPane.showMessageDialog(null, scrollPane, "졸업 요건 검사 결과", JOptionPane.PLAIN_MESSAGE);
        });

        // 0번 버튼: 종료
        btn4.addActionListener(e -> {
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); 
            //UIManager.setLookAndFeel(new FlatLightLaf()); 
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            GraduationGUI gui = new GraduationGUI();
            gui.setVisible(true);
        });
    }
}