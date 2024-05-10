
import model.Score;
import model.Status;
import model.Subject;
import model.Student;

import java.util.*;

public class CampManagementApplication {
    // 데이터 저장소
    private static List<Student> studentStore;
    private static List<Subject> subjectStore;

    // 과목 타입
    private static final String SUBJECT_TYPE_MANDATORY = "MANDATORY";
    private static final String SUBJECT_TYPE_CHOICE = "CHOICE";

    private static final int MANDATORY_MIN = 3;
    private static final int CHOICE_MIN = 2;

    // index 관리 필드
    private static int studentIndex;
    private static final String INDEX_TYPE_STUDENT = "ST";
    private static int subjectIndex;
    private static final String INDEX_TYPE_SUBJECT = "SU";
    private static int scoreIndex;
    private static final String INDEX_TYPE_SCORE = "SC";

    // 스캐너
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        setInitData();
        try {
            displayMainView();
        } catch (Exception e) {
            System.out.println("\n오류 발생!\n프로그램을 종료합니다.");
        }
    }

    // 초기 데이터 생성
    private static void setInitData() {
        studentStore = new ArrayList<>();
        subjectStore = List.of(
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Java",
                        SUBJECT_TYPE_MANDATORY),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "객체지향",
                        SUBJECT_TYPE_MANDATORY),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Spring",
                        SUBJECT_TYPE_MANDATORY),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "JPA",
                        SUBJECT_TYPE_MANDATORY),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "MySQL",
                        SUBJECT_TYPE_MANDATORY),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "디자인 패턴",
                        SUBJECT_TYPE_CHOICE),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Spring Security",
                        SUBJECT_TYPE_CHOICE),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "Redis",
                        SUBJECT_TYPE_CHOICE),
                new Subject(
                        sequence(INDEX_TYPE_SUBJECT),
                        "MongoDB",
                        SUBJECT_TYPE_CHOICE));

    }


    // index 자동 증가
    private static String sequence(String type) {
        switch (type) {
            case INDEX_TYPE_STUDENT -> {
                studentIndex++;
                return INDEX_TYPE_STUDENT + studentIndex;
            }
            case INDEX_TYPE_SUBJECT -> {
                subjectIndex++;
                return INDEX_TYPE_SUBJECT + subjectIndex;
            }
            default -> {
                scoreIndex++;
                return INDEX_TYPE_SCORE + scoreIndex;
            }
        }
    }

    private static void displayMainView() throws InterruptedException {
        boolean mainFlag = true;
        while (mainFlag) {
            System.out.println("\n==================================");
            System.out.println("내일배움캠프 수강생 관리 프로그램 실행 중...");
            System.out.println("1. 수강생 관리");
            System.out.println("2. 점수 관리");
            System.out.println("3. 프로그램 종료");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> displayStudentView(); // 수강생 관리
                case 2 -> displayScoreView(); // 점수 관리
                case 3 -> mainFlag = false; // 프로그램 종료
                default -> {
                    System.out.println("잘못된 입력입니다.\n되돌아갑니다!");
                    Thread.sleep(2000);
                }
            }
        }
        System.out.println("프로그램을 종료합니다.");
    }

    private static void displayStudentView() {
        boolean studentFlag = true;
        while (studentFlag) {
            System.out.println("==================================");
            System.out.println("수강생 관리 실행 중...");
            System.out.println("1. 수강생 등록");
            System.out.println("2. 수강생 목록 조회");
            System.out.println("3. 수강생 이름 수정");
            System.out.println("4. 수강생 상태 수정");
            System.out.println("5. 상태별 수강생 목록 조회 ");
            System.out.println("6. 수강생 삭제");
            System.out.println("7. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> createStudent(); // 수강생 등록
                case 2 -> inquireStudent(studentStore); // 수강생 목록 조회
                case 3 -> changeStudentName(); // 수강생 이름 수정
                case 4 -> changeStudentStatus(); // 수강생 상태 수정
                case 5 -> inquireStudentByStatus();
                case 6 -> removeStudent();
                case 7 -> studentFlag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    studentFlag = false;
                }
            }
        }
    }

    // 수강생 등록
    private static void createStudent() {
        System.out.println("\n수강생을 등록합니다...");
        System.out.print("수강생 이름 입력: ");
        String studentName = sc.next();
        sc.nextLine();

        boolean studentAddFlag;
        Status status = null;
        do {
            studentAddFlag = true;
            System.out.print("수강생 상태 입력[Green, Red, Yellow]: ");
            String input = sc.nextLine();
            try {
                status = Status.valueOf(input);
            } catch (IllegalArgumentException e) {
                studentAddFlag = false;
                System.out.println("Green, Red, Yellow 중에 하나를 입력하세요. ");
            }
        } while (!studentAddFlag);

        Student student = new Student(sequence(INDEX_TYPE_STUDENT), studentName, status);

        addSubjects(student, SUBJECT_TYPE_MANDATORY, MANDATORY_MIN); // 필수 과목 입력받기
        addSubjects(student, SUBJECT_TYPE_CHOICE, CHOICE_MIN); // 선택 과목 입력받기
        studentStore.add(student);

        System.out.println("수강생 등록 성공!");
    }

    public static void addSubjects(Student student, String subjectType, int minNum) {
        boolean subjectAddFlag;
        HashMap<String, Subject> subjectMap;

        do {
            subjectAddFlag = true;
            subjectMap = new HashMap<>();
            System.out.println("=========================");
            System.out.println("수강할 과목을 " + minNum + "개 이상 선택해주세요");

            printSubjects(subjectType);
            System.out.println("=========================");
            System.out.println("과목 번호를 입력하세요 (숫자로 입력)");
            String mandatorySubject = sc.nextLine();

            // 공백 또는 쉼표로 구분받기
            String[] subjectArr = mandatorySubject.split("[, ]");
            // 동일한 값 입력 시 제거
            subjectArr = Arrays.stream(subjectArr).distinct().toArray(String[]::new);

            if (subjectArr.length < minNum) {
                System.out.println(subjectType + " 과목은 " + minNum + "개 이상 선택해야 합니다. ");
                subjectAddFlag = false;
            } else {
                // 입력한 값 중 하나라도 과목 목록에 존재하지 않으면 다시 입력 받아야 함
                for (String subjectId : subjectArr) {
                    // 입력받은 번호를 과목 고유번호 형식으로 포맷팅
                    subjectId = INDEX_TYPE_SUBJECT + subjectId;
                    for (Subject sub : subjectStore) {
                        // 입력한 과목명이 등록된 과목 번호와 일치할 때
                        if (subjectId.equals(sub.getSubjectId()) && sub.getSubjectType().equals(subjectType)) {
                            subjectMap.put(sub.getSubjectId(), sub);
                            subjectAddFlag = true;
                            break;
                        }
                        subjectAddFlag = false;
                    }
                    // 기존 과목 리스트에 일치하는 항목이 없을 때
                    if (!subjectAddFlag) {
                        System.out.println("존재하지 않는 과목 번호가 포함되어 있습니다!");
                        break;
                    }
                }
            }
        } while (!subjectAddFlag);

        // 오류 없이 과목을 잘 선택한 경우 student 과목 목록 map에 추가
        student.getSubjectList().putAll(subjectMap);

        for (String subject : student.getSubjectList().keySet()) {
            int[] arr = new int[10];
            Arrays.fill(arr, -1);
            student.getScoreList().put(subject, arr);
        }
        System.out.println(subjectType + " 과목 등록 완료!");
    }

    public static void printSubjects(String subjectType) {
        System.out.println("=========================");
        if (subjectType.equals(SUBJECT_TYPE_MANDATORY)) {
            System.out.print("[필수] ");
            for (Subject sub : subjectStore) {
                if (sub.getSubjectType().equals(SUBJECT_TYPE_MANDATORY)) {
                    System.out.print(sub.getSubjectId().substring(2) + ". " + sub.getSubjectName() + " | ");
                }
            }
        } else {
            System.out.print("[선택] ");
            for (Subject sub : subjectStore) {
                if (sub.getSubjectType().equals(SUBJECT_TYPE_CHOICE)) {
                    System.out.print(sub.getSubjectId().substring(2) + ". " + sub.getSubjectName() + " | ");
                }
            }
        }
        System.out.println();
    }

    // 수강생 목록 조회
    private static void inquireStudent(List<Student> studentStore) {
        System.out.println("수강생 목록을 조회합니다...");
        for (Student students : studentStore) {
            System.out.print("[" + students.getStudentId() + "] ");
            System.out.println("이름 : " + students.getStudentName());
        }
    }

    // 학생 이름 정확히 입력받기
    private static String getExactStudentId(String studentName) {
        List<Student> sameNameStudentList = new ArrayList<>();
        for (Student student : studentStore) {
            if (student.getStudentName().equals(studentName)) {
                sameNameStudentList.add(student);
            }
        }

        // 두 명 이상인 경우
        if (sameNameStudentList.size() > 1) {
            while (true) {
                try {
                    System.out.println("=========================");
                    System.out.println("해당 이름을 가진 수강생 목록입니다!");
                    inquireStudent(sameNameStudentList);
                    System.out.println("=========================");
                    System.out.print("수강생의 번호를 입력해주세요! (숫자만 입력) : ");
                    int studentNum = sc.nextInt();
                    for (Student student : sameNameStudentList) {
                        if (student.getStudentId().equals(INDEX_TYPE_STUDENT + studentNum)) {
                            return student.getStudentId();
                        }
                    }
                    System.out.println("올바른 수강생 번호를 입력해주세요!");
                } catch (InputMismatchException e) {
                    System.out.println("숫자를 입력해주세요!");
                    sc.nextLine();
                }
            }
        } else if (sameNameStudentList.isEmpty()) { // 일치하는 수강생이 없을 때
            System.out.println("이름이 일치하는 수강생이 없습니다!");
            return "Invalid";
        } else {
            return sameNameStudentList.get(0).getStudentId();
        }
    }

    // 수강생 이름 변경
    private static void changeStudentName() {
        String studentName;
        String studentId;

        do {
            System.out.println("이름을 변경할 수강생 이름을 입력해주십시오.");
            studentName = sc.next();
            studentId = getExactStudentId(studentName);
        } while (studentId.equals("Invalid"));

        System.out.println("변경할 이름을 입력해 주십시오.");
        String changeName = sc.next();

        for (Student student : studentStore) {
            if (student.getStudentId().equals(studentId)) {
                student.changeName(changeName);
            }
        }
        System.out.println("이름 변경 완료!");
    }

    // 수강생 상태 수정
    private static void changeStudentStatus() {
        String studentName;
        String studentStatus;
        String studentId;
        Status status = null;
        boolean statusChangeFlag;

        do {
            System.out.println("상태를 변경할 수강생 이름을 입력해주십시오.");
            studentName = sc.next();
            studentId = getExactStudentId(studentName);
        } while (studentId.equals("Invalid"));

        do {
            statusChangeFlag = true;
            System.out.println("변경할 상태를 입력해 주십시오. [Green, Red, Yellow]");
            studentStatus = sc.next();
            try {
                status = Status.valueOf(studentStatus);
            } catch (IllegalArgumentException e) {
                statusChangeFlag = false;
                System.out.println("Green, Red, Yellow 중에 하나를 입력하세요. ");
            }
        } while (!statusChangeFlag);

        for (Student student : studentStore) {
            if (student.getStudentId().equals(studentId)) {
                student.changeStatus(Status.valueOf(studentStatus));
            }
        }
        System.out.println("상태 변경 완료!");
    }

    // 상태별 수강생 상태 조회
    private static void inquireStudentByStatus() {
        boolean statusInquireFlag;
        String state;
        Status status = null;
        do {
            statusInquireFlag = true;
            System.out.println("조회할 상태를 입력해주십시오. [Green, Red, Yellow]");
            state = sc.next();
            try {
                status = Status.valueOf(state);
            } catch (IllegalArgumentException e) {
                statusInquireFlag = false;
                System.out.println("Green, Red, Yellow 중에 하나를 입력하세요. ");
            }
        } while (!statusInquireFlag);
        for (Student student : studentStore) { // studentStore를 돌며
            if (student.getStatus().equals(Status.valueOf(state))) { // 상태가 입력받은 것과 같으면
                System.out.println(student.getStudentName() + " : " + state); // 수강생 이름과 상태를 출력하기
            }
        }
    }

    // 수강생 삭제
    private static void removeStudent() {
        String studentName;
        String studentId;

        do {
            System.out.println("삭제할 수강생 이름을 입력해주십시오.");
            studentName = sc.next();
            studentId = getExactStudentId(studentName);
        } while (studentId.equals("Invalid"));

        final String finalStudentId = studentId;
        studentStore.removeIf(student -> student.getStudentId().equals(finalStudentId));
        System.out.println(studentName +" 수강생 삭제 완료!");
    }

    private static void displayScoreView() {
        boolean scoreFlag = true;
        while (scoreFlag) {
            System.out.println("==================================");
            System.out.println("점수 관리 실행 중...");
            System.out.println("1. 수강생의 과목별 시험 회차 및 점수 등록");
            System.out.println("2. 수강생의 과목별 회차 점수 수정");
            System.out.println("3. 수강생의 특정 과목 회차별 등급 조회");
            System.out.println("4. 수강생의 과목별 평균 등급 조회");
            System.out.println("5. 특정 상태 수강생들의 필수 과목 평균 등급 조회");
            System.out.println("6. 메인 화면 이동");
            System.out.print("관리 항목을 선택하세요...");
            int input = sc.nextInt();

            switch (input) {
                case 1 -> createScore(); // 수강생의 과목별 시험 회차 및 점수 등록
                case 2 -> updateRoundScoreBySubject(); // 수강생의 과목별 회차 점수 수정
                case 3 -> inquireRoundGradeBySubject(); // 수강생의 특정 과목 회차별 등급 조회
                case 4 -> inquireAverageGradeBySubject();
                case 5 -> inquireAverageGradeByStatus();
                case 6 -> scoreFlag = false; // 메인 화면 이동
                default -> {
                    System.out.println("잘못된 입력입니다.\n메인 화면 이동...");
                    scoreFlag = false;
                }
            }
        }
    }

    // 수강생 번호 입력 받은 후 수강생이 존재하는지 확인
    private static String getStudentId() {
        System.out.print("\n관리할 수강생의 번호를 입력하시오...");
        String stu = INDEX_TYPE_STUDENT;
        String tmp = sc.next();
        stu = stu.concat(tmp);

        for (Student student : studentStore) {
            if (student.getStudentId().equals(stu)) {
                return stu;
            }
        }
        return "Invalid";
    }

    // 수강생의 과목별 시험 회차 및 점수 등록
    private static void createScore() {
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        Student student = null;

        // 수정 할 과목의 번호
        int subject_Num;

        // 입력받은 과목의 이름. (scoreList에서 key값으로 사용)
        String subject_Name = "";

        while (studentId.equals("Invalid")) {
            studentId = getStudentId();
            System.out.println("해당 학생은 존재 하지 않습니다.");
        }

        // 해당 학생의 객체 student에 저장
        for (Student tmp_student : studentStore) {
            if (tmp_student.getStudentId().equals(studentId)) {
                student = tmp_student;
                break;
            }
        }

        while (true) {
            int index = 1;

            // 숫자 입력 판별 하는 boolean 입니다.
            boolean check = true;
            String line = "";

            // 과목 번호, 과목 이름 출력
            for (Subject sub : subjectStore) {
                System.out.println(index++ + ". " + sub.getSubjectName());
            }

            System.out.println("점수를 등록하실 과목의 숫자를 입력해주세요.");

            line = sc.next();
            // 숫자 판별(숫자만 입력 가능)
            for (int i = 0; i < line.length(); i++) {
                if (!Character.isDigit(line.charAt(i))) {
                    check = false;
                    break;
                }
            }

            if (!check) {
                System.out.println("숫자만 입력 가능 합니다.");
                continue;
            }

            subject_Num = Integer.parseInt(line);

            // 1 ~ 9 까지만 입력 받고 list는 0 ~ 8 까지만 유효하다.
            if (subject_Num < 1 || subject_Num > 9) {
                System.out.println("유효하지 않은 숫자를 입력 하셨습니다.");
                continue;
            }

            // 수정한 예외처리 부분
            try {
                subject_Name = subjectStore.get(subject_Num - 1).getSubjectName();
                if (!Objects.isNull(student) &&
                        student.getSubjectList().get(INDEX_TYPE_SUBJECT + subject_Num).getSubjectName().equals(subject_Name))
                    break;
            } catch (Exception e) {
                System.out.println("해당 학생은 입력하신 과목을 수강하지 않습니다.");
            }

        }

        while (true) {
            // ★★ 입력받은 과목의 점수가 저장 되어있는 배열
            int[] arr = student.getScoreList().get(INDEX_TYPE_SUBJECT + subject_Num);

            System.out.println("등록 하실 회차(1 ~ 10)를 입력 해주세요.");
            int num = sc.nextInt();
            if (num < 1 || num > 10) {
                System.out.println("범위에 벗어난 숫자를 입력 하셨습니다.");
                System.out.println("==================================");
                continue;
            }

            if (arr[num - 1] != -1) {
                System.out.println("중복 등록은 불가능합니다. 이미 " + num + "회차에 점수가 등록 되어 있습니다.");
                System.out.println("==================================");
                continue;
            }

            System.out.println(num + "회차의 시험 점수를 입력해주세요.");
            int score_tmp = sc.nextInt();
            if (score_tmp < 0 || score_tmp > 100) {
                System.out.println("시험 점수의 범위를 벗어나셨습니다.");
                System.out.println("==================================");
                continue;
            }

            arr[num - 1] = score_tmp;
            // 수정 완료
            student.getScoreList().put(subject_Name, arr);
            break;
        }

        System.out.println("시험 점수를 등록합니다...");
        // 기능 구현
        System.out.println("\n점수 등록 성공!");
    }

    // 특정 학생이 수강하는 과목명과 과목 번호 출력
    public static void printStudentSubjects(Student student) {
        System.out.println("=========================");

        // 과목 번호 순으로 해시맵 오름차순 정렬
        List<String> mapKey = new ArrayList<>(student.getSubjectList().keySet());
        Collections.sort(mapKey);

        for (String key : mapKey) {
            String subjectName = student.getSubjectList().get(key).getSubjectName();
            System.out.println(key.substring(2) + ". " + subjectName);
        }

        System.out.println();
    }

    // 수강생의 과목별 회차 점수 수정
    private static void updateRoundScoreBySubject() {
        Student student = null;
        String studentId = getStudentId(); // 관리할 수강생 고유 번호
        String selectedSubjectId = ""; // 선택한 과목 고유 번호
        // int[] selectedSubjectArr; // 선택한 과목의 회차 점수가 보관될 배열
        boolean updateRoundFlag = true;

        // 학생 존재하는지 확인
        while (studentId.equals("Invalid")) {
            System.out.println("해당 학생은 존재 하지 않습니다.");
            studentId = getStudentId();
        }

        for (Student tmp_student : studentStore) {
            if (tmp_student.getStudentId().equals(studentId)) {
                student = tmp_student;
                break;
            }
        }

        do {
            updateRoundFlag = true;
            // 해당 학생이 수강하는 과목, 과목명 출력
            printStudentSubjects(student);
            System.out.print("점수를 수정할 과목 번호 하나를 입력해주세요! (숫자로 입력) : ");
            try {
                int inputSubjectId = sc.nextInt();
                selectedSubjectId = INDEX_TYPE_SUBJECT + inputSubjectId;

                // 과목 목록에 입력한 과목 번호와 일치하는 과목이 존재한다면..
                if (student.getSubjectList().containsKey(selectedSubjectId)) {
                    int[] selectedSubjectArr = student.getScoreList().get(selectedSubjectId);
                    for (int i = 0; i < selectedSubjectArr.length; i++) {
                        // 회차 점수가 등록되지 않은 경우
                        if (selectedSubjectArr[i] == -1) {
                            System.out.println((i + 1) + "회차 점수 : - (등록되지 않음)");
                        } else {
                            System.out.println((i + 1) + "회차 점수 : " + selectedSubjectArr[i]);
                        }
                    }
                } else {
                    updateRoundFlag = false;
                }
                // 과목 번호로 숫자가 아닌 값이 들어왔을 때
            } catch (InputMismatchException e) {
                System.out.println("숫자 하나를 입력해주세요!");
                updateRoundFlag = false;
            }
        } while (!updateRoundFlag);

        do {
            updateRoundFlag = true;
            System.out.print("수정할 회차를 입력해주세요! (회차 번호만 입력) : ");
            try {
                int selectedScoreNum = sc.nextInt();
                // 옳은 회차 번호를 입력했을 때만 점수 수정 실행
                if (selectedScoreNum >= 1 && selectedScoreNum <= 10) {
                    updateRealScore(student.getScoreList().get(selectedSubjectId), selectedScoreNum);
                } else {
                    updateRoundFlag = false;
                    System.out.println("올바른 회차 번호를 입력해주세요!");
                }
            } catch (InputMismatchException e) {
                updateRoundFlag = false;
                System.out.println("숫자를 입력해주세요!");
            }
        } while (!updateRoundFlag);

        System.out.println("\n점수 수정 성공!");
    }

    // 점수 입력 받고 실제 회차별 점수 배열 값 업데이트
    private static void updateRealScore(int[] selectedSubjectArr, int index) {
        boolean updateRealScoreFlag;
        do {
            updateRealScoreFlag = true;
            try {
                System.out.print("수정할 점수값을 입력해주세요! (0 - 100) : ");
                int score = sc.nextInt();
                if (score >= 0 && score <= 100) {
                    System.out.println("시험 점수를 수정합니다...");

                    selectedSubjectArr[index - 1] = score;
                    System.out.println(index + "회차 : " + score + " (수정)");
                } else {
                    System.out.println("0 ~ 100 사이의 숫자를 입력해주세요!");
                    updateRealScoreFlag = false;
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요!");
                updateRealScoreFlag = false;
            }
        } while (!updateRealScoreFlag);
    }

    // 선택한 과목 회차별 등급 조회
    private static void inquireRoundGradeBySubject() {
        int index = 1;
        int find_Sub_Num = 0;
        Student student = null;
        String studentId = getStudentId(); // 관리할 수강생 고유 번호

        while (studentId.equals("Invalid")) {
            System.out.println("해당 학생은 존재 하지 않습니다.");
            studentId = getStudentId();
        }

        // 해당 학생의 객체 student에 저장
        for (Student tmp_student : studentStore) {
            if (tmp_student.getStudentId().equals(studentId)) {
                student = tmp_student;
                break;
            }
        }

        // 기능 구현 (조회할 특정 과목)
        index = 1;
        System.out.println("조회 하실 과목을 입력 해주세요 : ");
        // 과목 번호, 과목 이름 출력
        for (Subject sub : subjectStore) {
            System.out.println(index++ + ". " + sub.getSubjectName());
        }


        //예외 처리 (알맞은 값만 입력 받도록)
        while (true) { // 수강하지 않는 과목 입력시 오류 발생
            try {
                String input = sc.next();
                find_Sub_Num = Integer.parseInt(input);
                if (find_Sub_Num < 1 || find_Sub_Num > 9) {
                    System.out.println();
                    throw new IllegalArgumentException("해당 과목 번호는 유효하지 않습니다. 다시 입력해 주십시오.");
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력 해주시길 바랍니다.");
                continue;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }
        }

        System.out.println("회차별 등급을 조회합니다...");
        if (find_Sub_Num <= 5) displayGradeMandatory(student, find_Sub_Num);
        else displayGradeChoice(student, find_Sub_Num);

        System.out.println("\n등급 조회 성공!");

    }

    // 수강생의 과목별 평균 등급 조회
    private static void inquireAverageGradeBySubject() {
        String studentId = getStudentId();
        Student student = null;

        while (studentId.equals("Invalid")) {
            System.out.println("해당 학생은 존재 하지 않습니다.");
            studentId = getStudentId();
        }

        for (Student tmp_student : studentStore) {
            if (tmp_student.getStudentId().equals(studentId)) {
                student = tmp_student;
            }
        }

        for (Subject sub : subjectStore) { // sub를 다 뒤져서 그런가 학생이 가진 과목만 봐야됨
            switch (sub.getSubjectName()) {
                case "Java", "객체지향", "Spring", "JPA", "MySQL":
                    if (student != null && student.getScoreList().get(sub.getSubjectName()) != null) {
                        System.out.print(sub.getSubjectName() + " 과목 평균 등급 :");
                        averageGradeMandatory(student, sub.getSubjectId());
                        System.out.println();
                    }
                    break;
                case "디자인 패턴", "Spring Security", "Redis", "MongoDB":
                    if (student != null && student.getSubjectList().get(sub.getSubjectName()) != null) {
                        System.out.print(sub.getSubjectName() + " 과목 평균 등급 :");
                        averageGradeChoice(student, sub.getSubjectId());
                        System.out.println();
                    }
                    break;

            }
        }
    }

    // 특정 상태 수강생들의 필수 과목 평균 등급 조회
    private static void inquireAverageGradeByStatus() {
        System.out.println("조회할 상태를 입력해주십시오. [Green, Red, Yellow]");
        String stat;
        boolean statusInquireFlag;
        Status status = null;
        do {
            statusInquireFlag = true;
            stat = sc.next();
            try {
                status = Status.valueOf(stat);
            } catch (IllegalArgumentException e) {
                statusInquireFlag = false;
                System.out.println("Green, Red, Yellow 중에 하나를 입력하세요. ");
            }
        } while (!statusInquireFlag);
        for (Student student : studentStore) {
            if (student.getStatus().equals(status)) {
                System.out.println(student.getStudentName() + " 님의 평균 과목 등급 ");
                Set<String> set = student.getSubjectList().keySet();
                for (String key : set) {
                    for (Subject sub : subjectStore) {
                        if (key.equals(sub.getSubjectId())) {
                            System.out.print(sub.getSubjectName() + " 평균 등급 :");
                            averageGradeMandatory(student, sub.getSubjectId());
                            System.out.println();
                        }
                    }
                }
            }
        }

    }

    // 필수과목 평균내기
    private static void averageGradeMandatory(Student student, String subjectId) {
        double sum = 0;
        double average;
        int count = 0;
        int[] arr = student.getScoreList().get(subjectId);

        for (int j : arr) {
            if (j == -1)
                continue;
            sum += j;
            count++;
        }
        average = sum / count;
        System.out.print(" " + changeGradeMandatory(average) + " ");

    }

    // 선택과목 평균내기
    private static void averageGradeChoice(Student student, String subjectId) {
        double sum = 0;
        double average;
        int count = 0;
        int[] arr = student.getScoreList().get(subjectId);

        for (int j : arr) {
            if (j == -1)
                continue;
            sum += j;
            count++;
        }
        average = sum / count;
        System.out.print(" " + changeGradeChoice(average) + " ");
    }


    // 해당 학생의 필수 과목 점수 조회
    private static void displayGradeMandatory(Student student, int findSubNum) {
        int[] arr = student.getScoreList().get(INDEX_TYPE_SUBJECT + findSubNum);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) continue;
            System.out.println(i + 1 + "회차 과목 점수 : " + changeGradeMandatory(arr[i]));
        }
    }

    // 해당 학생의 선택 과목 점수 조회
    private static void displayGradeChoice(Student student, int findSubNum) {
        int[] arr = student.getScoreList().get(INDEX_TYPE_SUBJECT + findSubNum);

        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == -1) continue;
            System.out.println(i + 1 + "회차 과목 점수 : " + changeGradeChoice(arr[i]));
        }
    }

    // 필수 과목 등급 반환
    private static char changeGradeMandatory(int num) {
        if (num >= 95) return 'A';
        else if (num >= 90) return 'B';
        else if (num >= 80) return 'C';
        else if (num >= 70) return 'D';
        else if (num >= 60) return 'F';
        else return 'N';
    }

    // 선택 과목 등급 반환
    private static char changeGradeChoice(int num) {
        if (num >= 90) return 'A';
        else if (num >= 80) return 'B';
        else if (num >= 70) return 'C';
        else if (num >= 60) return 'D';
        else if (num >= 50) return 'F';
        else return 'N';
    }

    // 수강생 필수 과목 성적 등급 변환
    private static char changeGradeMandatory(double num) {
        if (num >= 95)
            return 'A';
        else if (num >= 90)
            return 'B';
        else if (num >= 80)
            return 'C';
        else if (num >= 70)
            return 'D';
        else if (num >= 60)
            return 'F';
        else
            return 'N';
    }

    // 수강생 선택 과목 성적 등급 변환
    private static char changeGradeChoice(double num) {
        if (num >= 90)
            return 'A';
        else if (num >= 80)
            return 'B';
        else if (num >= 70)
            return 'C';
        else if (num >= 60)
            return 'D';
        else if (num >= 50)
            return 'F';
        else
            return 'N';
    }

}