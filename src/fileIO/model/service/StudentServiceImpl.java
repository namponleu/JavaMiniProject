package fileIO.model.service;

import fileIO.model.Student;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static fileIO.controller.StudentController.generateDefaultId;

public class StudentServiceImpl implements StudentService {
    private final String FILE_NAME = "students.txt";
    private final List<Student> students;

    public StudentServiceImpl() {
        this.students = new ArrayList<>();
        readDataFromFile();
    }
    private void readDataFromFile() {
        String line = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0];
                String name = data[1];
                LocalDate dateOfBirth = LocalDate.parse(data[2]);
                String classroom = data[3];
                String subjects = data[4];
                LocalDate createAt = LocalDate.parse(data[5]); // Parse createAt field

                Student student = new Student(id, name, dateOfBirth, classroom, subjects, createAt);
                students.add(student);
            }
        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException | IOException e) {
            // Handle parsing errors
            System.err.println("Error parsing line: " + line);
            e.printStackTrace();
        }
    }


//    private void readDataFromFile() {
//        String line = null;
//        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                String id = data[0];
//                String name = data[1];
//                LocalDate dateOfBirth = LocalDate.parse(data[2]);
//                String classroom = data[3];
//                String subjects = data[4];
//                String createAt = data[5];
//
//                Student student = new Student(id, name, dateOfBirth, classroom, subjects, createAt);
//                students.add(student);
//            }
//        } catch (DateTimeParseException | ArrayIndexOutOfBoundsException | IOException e) {
//            // Handle parsing errors
//            System.err.println("Error parsing line: " + line);
//            e.printStackTrace();
//        }
//    }


    private void writeDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            writer.flush();
            for (Student student : students) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s%n",
                        student.getId(),
                        student.getName(),
                        student.getDateOfBirth(),
                        student.getClassroom(),
                        student.getSubjects(),
                        student.getCreateAt(),
                        student.getCreateAt()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addNewStudent(Student student) {
        students.add(student);
        writeDataToFile();
        return students.size();
    }

    @Override
    public List<Student> listAllStudents() {
        return students;
    }

    @Override
    public void commitDataToFile() {
        writeDataToFile();
    }

    @Override
    public void commitDataFromTransaction() {
        // Implement if using transaction manager
    }

    @Override
    public void deleteDataFromFile() {
        // Implement if needed
    }

    @Override
    public List<Student> searchStudentById(String id) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getId().equals(id)) {
                result.add(student);
            }
        }
        return result;
    }
    //updateStudentInfoById in class StudentServiceImpl
    @Override
    public List<Student> searchStudentByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public Student deleteStudentById(String id) {
        Student studentToRemove = null;
        for (Student student : students) {
            if (student.getId().equals(id)) {
                studentToRemove = student;
                break;
            }
        }
        if (studentToRemove != null) {
            students.remove(studentToRemove);
            writeDataToFile();
        }
        return studentToRemove;
    }


    @Override
    public Student updateStudentById(String id, Student updatedStudent) {
        try {
            boolean updated = students.stream()
                    .filter(student -> student.getId().equals(id))
                    .peek(student -> {
                        student.setName(updatedStudent.getName());
                        student.setDateOfBirth(updatedStudent.getDateOfBirth());
                        student.setClassroom(updatedStudent.getClassroom());
                        student.setSubjects(updatedStudent.getSubjects());
                    })
                    .findFirst()
                    .isPresent();

            if (updated) {
                writeDataToFile();
                System.out.println("Your update is successful.");
                return updatedStudent;
            } else {
                System.out.println("No student found with the given ID.");
                return null;
            }
        } catch (NullPointerException exception) {
            System.out.println("[!] Problem: " + exception.getMessage());
            return null;
        }
    }

    @Override
    public void deleteAllStudents() {
        students.clear();
        writeDataToFile();
    }

    @Override
    public void generateData(int numRecords) {

    }
}
