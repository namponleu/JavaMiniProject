package fileIO.model;

import java.time.LocalDate;

public class Student {
    private String id;
    private String name;
    private LocalDate dateOfBirth;
    private String classroom;
    private String subjects;

    public Student(){}

    public Student(String id, String name, LocalDate dateOfBirth, String classroom, String subjects) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.classroom = classroom;
        this.subjects = subjects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", classroom='" + classroom + '\'' +
                ", subjects='" + subjects + '\'' +
                '}';
    }
}
