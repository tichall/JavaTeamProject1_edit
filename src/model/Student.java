package model;

import java.util.HashMap;

public class Student {
    private String studentId;
    private String studentName;
    private Status studentStatus;

    private HashMap<String, Subject> subjectHashMap;
    private HashMap<String, int[]> scoreHashMap;

    public Student(String seq, String studentName, Status status) {
        this.studentId = seq;
        this.studentName = studentName;
        this.subjectHashMap = new HashMap<>();
        this.scoreHashMap = new HashMap<>();
        this.studentStatus = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public HashMap<String, Subject> getSubjectHashMap() {
        return subjectHashMap;
    }

    public HashMap<String, int[]> getScoreHashMap(){
        return scoreHashMap;
    }

    public void changeName(String studentName) {
        this.studentName = studentName;
    }

    public void changeStatus(Status studentStatus) {
        this.studentStatus = studentStatus;
    }

    public Object getStudentStatus() {
        return studentStatus;
    }
}
