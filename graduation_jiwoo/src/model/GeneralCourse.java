package model;

public class GeneralCourse extends Course {
    private String category1; // 첨성인기초, 첨성인핵심, 일반, 소양
    private String category2; // 외국어, 수리, 인문사회, 자연과학 등
    private String category3; // 필요 없으면 NONE
    private boolean sdg;
    private boolean english;

    public GeneralCourse(String courseCode, String lectureName, int lectureCredit,
                         String category1, String category2, String category3,
                         boolean sdg, boolean english) {
        super(courseCode, lectureName, lectureCredit);
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.sdg = sdg;
        this.english = english;
    }

    public String getCategory1() {
        return category1;
    }

    public String getCategory2() {
        return category2;
    }

    public String getCategory3() {
        return category3;
    }

    public boolean isSDG() {
        return sdg;
    }

    public boolean isEnglish() {
        return english;
    }
}