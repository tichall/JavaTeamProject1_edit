package model;
import model.Subject;

import java.util.HashMap;

public class Student {
    private String studentId;
    private String studentName;
    private String studentStatus;

    private HashMap<String, Subject> subjectList;
    private HashMap<String, int[]> scoreList;

    public Student(String seq, String studentName, String status) {
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
    public String getStudentStatus() { return studentStatus; }

    public void changeName(String changeName) {
        this.studentName = changeName;
    }

    public void changeStatus(String studentStatus) {
        this.studentStatus = studentStatus;
    }

    public Object getStatus() {
        return studentStatus;
    }
}
