package model;

public class Score {
    private String studentId;
    private String subjectId;
    private int round;
    private int scoreNum;
    private char grade;


    public Score(String studentId, String subjectId, int round, int scoreNum) {
        this.studentId = studentId;
        this.subjectId = subjectId;
        this.round = round;
        this.scoreNum = scoreNum;
    }

    // Getter
    public String getStudentId() {
        return studentId;
    }
}