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
        // 1. 프로그램 시작 시 DB 로드
        CourseDB.loadGeneralCourses();

        // 2. 메인 창(JFrame) 기본 설정
        setTitle("경북대학교 컴퓨터학부 졸업요건 확인 프로그램");
        setSize(400, 350); // 창 크기 (가로, 세로)
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
        JButton btn2 = new JButton("2. 수강 과목 입력");
        JButton btn3 = new JButton("3. 졸업요건 확인");
        JButton btn4 = new JButton("0. 종료");
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
        
        // 2번 버튼: 수강 과목 입력
        btn2.addActionListener(e -> {

            if (student == null) {
                JOptionPane.showMessageDialog(null, "학생 정보를 먼저 입력하세요!", "경고", JOptionPane.WARNING_MESSAGE);
                return;
            }

            
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
                
                //방어 로직
                if (code.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "과목명을 1글자 이상 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
                    continue;
                }

                Course course = CourseDB.findCourse(code);

                // 과목을 못 찾았을 때
                if (course == null) {
                    JOptionPane.showMessageDialog(null, "해당 과목을 찾을 수 없습니다.\n다시 입력해주세요.", "검색 실패", JOptionPane.ERROR_MESSAGE);
                    continue;
                }

                // 중복 검사 로직 (기존 콘솔 로직과 동일)
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

                // 문제없으면 성적 가져와서 학생 객체에 추가
                String grade = (String) gradeCombo.getSelectedItem();
                student.addTakenCourse(new TakenCourse(course, grade));

                // 몇 개 추가했는지 보여주는 성공 메시지
                JOptionPane.showMessageDialog(null, course.getLectureName() + " 과목이 성공적으로 추가되었습니다!\n(현재까지 수강한 과목: " + student.getCourseCount() + "개)", "추가 완료", JOptionPane.INFORMATION_MESSAGE);
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