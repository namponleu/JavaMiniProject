package fileIO.model.service;

import fileIO.model.Student;
import java.util.List;
import java.util.Scanner;

public interface StudentService {
    int addNewStudent(Student student);
    List<Student> listAllStudents();

    void commitDataToFile();
    void commitDataFromTransaction();
//    void clearTransactionFile();

    List<Student> searchStudentById(String id);
    List<Student> searchStudentByName(String name);
    Student deleteStudentById(String id);
    Student updateStudentById(String id, Student updatedStudent);
    void deleteAllStudents();
    void generateRecords(int startIndex, int endIndex);

}
