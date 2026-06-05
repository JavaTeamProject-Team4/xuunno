package model;

public class Student {
    private int studentId;       // 학번
    private String studentName;  // 이름
    private int studentMajor;    // 전공 (1:심컴, 2:플숲, 3:인컴, 4:글숲)
    private int counselingCount; // 지도교수 상담 횟수
    private int toeicScore;      // 토익 성적
    private double gpa;          // 평점 (요구사항에 맞춰 추가됨)
    
    // 수강한 과목 배열
    private TakenCourse[] takenCourses = new TakenCourse[100];
    private int courseCount = 0;

    // ==== 글로벌소프트웨어(글솦) 전용 트랙 필드 추가 ====
    private int track = 0;          // 1: 다중전공, 2: 해외복수학위, 3: 학석사연계
    private int globalCredit = 0;   // 해외 대학 인정 학점
    private int practiceCredit = 0; // 현장실습 학점
    private int startupCredit = 0;  // 창업교과목 학점
    private String isStartup = "N";    // 스타트업 창업 여부 (Y/N)
    private String isMultiMajor = "N"; // 다중전공 여부 (Y/N)
    private String isDualDegree = "N"; // 해외복수학위과정 이수 여부 (Y/N)

    public Student(int studentId, String studentName, int studentMajor, int counselingCount, int toeicScore) {
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentMajor = studentMajor;
        this.counselingCount = counselingCount;
        this.toeicScore = toeicScore;
        this.gpa = 0.0; 
    }

    public void addTakenCourse(TakenCourse tc) {
        if (courseCount < takenCourses.length) {
            takenCourses[courseCount++] = tc;
        }
    }

    // Getters & Setters
    public TakenCourse[] getTakenCourses() { return takenCourses; }
    public int getCourseCount() { return courseCount; }
    public int getCounselingCount() { return counselingCount; }
    public int getToeicScore() { return toeicScore; }
    public String getStudentName() { return studentName; }
    public int getStudentId() { return studentId; }
    public int getStudentMajor() { return studentMajor; }
    
    public double getGpa() { return gpa; }
    public void setGpa(double gpa) { this.gpa = gpa; }

    // ==== 글솦 트랙 전용 Getters & Setters 추가 ====
    public int getTrack() { return track; }
    public void setTrack(int track) { this.track = track; }

    public int getGlobalCredit() { return globalCredit; }
    public void setGlobalCredit(int globalCredit) { this.globalCredit = globalCredit; }

    public int getPracticeCredit() { return practiceCredit; }
    public void setPracticeCredit(int practiceCredit) { this.practiceCredit = practiceCredit; }

    public int getStartupCredit() { return startupCredit; }
    public void setStartupCredit(int startupCredit) { this.startupCredit = startupCredit; }

    public String getIsStartup() { return isStartup; }
    public void setIsStartup(String isStartup) { this.isStartup = isStartup; }

    public String getIsMultiMajor() { return isMultiMajor; }
    public void setIsMultiMajor(String isMultiMajor) { this.isMultiMajor = isMultiMajor; }

    public String getIsDualDegree() { return isDualDegree; }
    public void setIsDualDegree(String isDualDegree) { this.isDualDegree = isDualDegree; }
}