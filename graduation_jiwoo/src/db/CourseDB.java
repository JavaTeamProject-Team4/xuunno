package db;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import model.Course;
import model.GeneralCourse;
import model.MajorCourse;

public class CourseDB {
    public static ArrayList<MajorCourse> majorCourses = new ArrayList<>();
    public static ArrayList<GeneralCourse> genEdCourses = new ArrayList<>();

    public static void loadGeneralCourses() {
        genEdCourses.clear();
        // 프로젝트 내부의 data 폴더를 가리키는 상대 경로로 복구
        loadGeneralFile("data/courses_cleaned.txt");
    }

    public static void loadMajorCoursesByMajor(int major) {
        majorCourses.clear();

        // 프로젝트 내부의 data 폴더를 가리키는 상대 경로로 복구
        if (major == 2) {
            loadMajorFile("data/platform_software_courses.txt");
        } else if (major == 3) {
            loadMajorFile("data/ai_computing_courses.txt");
        } else {
            System.out.println("해당 전공의 전공 과목 파일이 아직 없습니다.");
        }
    }

    private static void loadMajorFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) continue;

                String[] data = line.split(",");

                String name = data[0].trim();
                int credit = Integer.parseInt(data[1].trim());
                String type = data[2].trim();
                String requiredText = data[3].trim();

                if (type.equals("MAJOR")) {
                    boolean required = requiredText.equals("전필");
                    majorCourses.add(new MajorCourse(name, name, credit, required));
                }
            }

            System.out.println("전공 과목 데이터 로드 완료: " + filename);

        } catch (IOException e) {
            System.out.println("전공 파일 읽기 실패: " + filename);
        }
    }

    private static void loadGeneralFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.trim().equals("")) continue;

                String[] data = line.split(",");

                int n = data.length;

                String sdgText = data[n - 1].trim();
                String cat3 = data[n - 2].trim();
                String cat2 = data[n - 3].trim();
                String cat1 = data[n - 4].trim();
                String type = data[n - 5].trim();
                int credit = Integer.parseInt(data[n - 6].trim());

                String name = data[0].trim();
                for (int i = 1; i <= n - 7; i++) {
                    name += "," + data[i].trim();
                }

                if (type.equals("GENERAL")) {
                    boolean sdg = sdgText.equals("SDG");
                    boolean english = isEnglishCourse(name, cat2);

                    genEdCourses.add(
                        new GeneralCourse(name, name, credit, cat1, cat2, cat3, sdg, english)
                    );
                }
            }

            System.out.println("교양 과목 데이터 로드 완료");

        } catch (IOException e) {
            System.out.println("교양 파일 읽기 실패: " + filename);
        }
    }

    private static boolean isEnglishCourse(String name, String area) {
        return name.contains("영어")
                || name.toLowerCase().contains("english")
                || area.equals("실용영어");
    }

    public static Course findCourse(String input) {
        // 입력받은 문자열에서 모든 띄어쓰기(공백)를 제거
        String noSpaceInput = input.replace(" ", "");

        for (MajorCourse mc : majorCourses) {
            // DB에 있는 과목명과 과목코드에서도 띄어쓰기를 제거한 후 비교
            String noSpaceName = mc.getLectureName().replace(" ", "");
            String noSpaceCode = mc.getCourseCode().replace(" ", "");
            
            if (noSpaceName.equals(noSpaceInput) || noSpaceCode.equals(noSpaceInput)) {
                return mc;
            }
        }

        for (GeneralCourse gc : genEdCourses) {
            // 교양 과목도 동일하게 띄어쓰기 제거 후 비교
            String noSpaceName = gc.getLectureName().replace(" ", "");
            String noSpaceCode = gc.getCourseCode().replace(" ", "");
            
            if (noSpaceName.equals(noSpaceInput) || noSpaceCode.equals(noSpaceInput)) {
                return gc;
            }
        }

        return null;
    }
}