package fileIO.view;

import fileIO.model.Student;
import java.util.List;
import java.util.Scanner;

public class View {
    public static final Scanner scanner = new Scanner(System.in);

    public static void displayStudents(List<Student> students) {
        System.out.println("[*] STUDENTS' DATA");
        System.out.println("ID           STUDENT'S NAME    STUDENT'S DATE OF BIRTH    STUDENTS classroom    STUDENTS SUBJECT");
        for (Student student : students) {
            System.out.printf("%-13s%-17s%-27s%-23s%-20s%n",
                    student.getId(), student.getName(), student.getDateOfBirth(), String.join(", ", student.getClassroom()), String.join(", ", student.getSubjects()));
        }
    }

    public static void displayStudent(Student student) {
        System.out.println("[*] STUDENTS' information                  DATA");
        System.out.printf("%-30s%s%n", "ID", student.getId());
        System.out.printf("%-30s%s%n", "Name", student.getName());
        System.out.printf("%-30s%s%n", "BIRTH", student.getDateOfBirth());
        System.out.printf("%-30s%s%n", "CLASSROOM", String.join(", ", student.getClassroom()));
        System.out.printf("%-30s%s%n", "SUBJECT", String.join(", ", student.getSubjects()));
    }

    public static String getInputString(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static int getInputInt(String prompt) {
        System.out.print(prompt + ": ");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input. Please enter an integer.");
            System.out.print(prompt + ": ");
            scanner.next(); // Consume invalid input
        }
        int input = scanner.nextInt();
        scanner.nextLine(); // Consume newline character
        return input;
    }
}
