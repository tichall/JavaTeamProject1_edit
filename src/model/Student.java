package model;

import java.util.HashMap;

public class Student {
    private String studentId;
    private String studentName;
    private Status studentStatus;

    private HashMap<String, Subject> subjectList;
    private HashMap<String, int[]> scoreList;

    public Student(String seq, String studentName, Status status) {
        this.studentId = seq;
        this.studentName = studentName;
        this.subjectList = new HashMap<>();
        this.scoreList = new HashMap<>();
        this.studentStatus = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public HashMap<String, Subject> getSubjectList() {
        return subjectList;
    }

    public HashMap<String, int[]> getScoreList(){
        return scoreList;
    }
    public Status getStudentStatus() { return studentStatus; }

    public void changeName(String changeName) {
        this.studentName = changeName;
    }

    public void changeStatus(Status studentStatus) {
        this.studentStatus = studentStatus;
    }

    public Object getStatus() {
        return studentStatus;
    }
}
