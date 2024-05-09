package fileIO.controller;

import fileIO.model.Student;
import fileIO.model.service.StudentService;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.StringTemplate.STR;

public class StudentController {
    private final StudentService studentService;
    private final Scanner scanner;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;
        displayTitle();
        do {
            displayMenu();
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    addNewStudent();
                    break;
                case 2:
                    listAllStudents();
                    break;
                case 3:
                    studentService.commitDataToFile();
                    break;
                case 4:
                    searchForStudent();
                    break;
                case 5:
                    updateStudentById();
                    break;
                case 6:
                    deleteStudentData();
                    break;
                case 7:
                    generateDataToFile();
                    break;
                case 8:
                    deleteAllData();
                    break;
                case 0:
                case 99:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        } while (option != 0 && option != 99);
    }
    private void displayTitle(){
        System.out.println("\t".repeat(5) + " ██████╗███████╗████████╗ █████╗ ██████╗     ███████╗███╗   ███╗███████╗");
        System.out.println("\t".repeat(5) + "██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗    ██╔════╝████╗ ████║██╔════╝");
        System.out.println("\t".repeat(5) + "██║     ███████╗   ██║   ███████║██║  ██║    ███████╗██╔████╔██║███████╗");
        System.out.println("\t".repeat(5) + "██║     ╚════██║   ██║   ██╔══██║██║  ██║    ╚════██║██║╚██╔╝██║╚════██║");
        System.out.println("\t".repeat(5) + "╚██████╗███████║   ██║   ██║  ██║██████╔╝    ███████║██║ ╚═╝ ██║███████║");
        System.out.println("\t".repeat(5) + " ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝     ╚══════╝╚═╝     ╚═╝╚══════╝");
    }
    private void displayMenu() {
        //Menu
        System.out.println("=".repeat(130));
        Table table  =new Table(3, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
        table.addCell("[1].ADD NEW STUDENT",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[2].LIST ALL STUDENTS",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[3].COMMIT DATA TO FILE",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[4].SEARCH FOR STUDENT",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[5].UPDATE STUDENT'S INFO BY ID",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[6].DELETE STUDENT'S DATA",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[7].GENERATE DATA TO FILE",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[8].DELETE/CLEAR ALL DATA FROM DATA STORE",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[0,99]. EXIT");
        System.out.println(table.render());
        System.out.println("\t".repeat(25) + "Copyright-CSTAD");
        System.out.println("=".repeat(130));
        System.out.print("> Insert option: ");
    }

    private void addNewStudent() {
        System.out.println("..............................");
        System.out.println("> INSERT STUDENT'S INFO");
        System.out.print("[+] Insert student's name: ");
        String name = scanner.nextLine();
        System.out.println("[+] STUDENT DATE OF BIRTH");
        System.out.print("1. Year (number): ");
//        int year = Integer.parseInt(scanner.nextLine());
        Integer year = new Scanner(System.in).nextInt();
        System.out.print("2. Month (number): ");
//        int month = Integer.parseInt(scanner.nextLine());
        Integer month = new Scanner(System.in).nextInt();
        System.out.print("3. Day (number): ");
//        int day = Integer.parseInt(scanner.nextLine());
        Integer day = new Scanner(System.in).nextInt();
        LocalDate dateOfBirth = LocalDate.of(year, month, day);
        System.out.println("[!] YOU CAN INSERT MULTI CLASSES BY SPLITTING [,] SYMBOL (C1,02)");
        System.out.print("[+] Student's class: ");
        String classroom = scanner.nextLine();
        System.out.println("[!] YOU CAN INSERT MULTI SUBJECTS BY SPLITTING [,] SYMBOL (S1, 52)");
        System.out.print("[+] Subject studied: ");
        String subjects = scanner.nextLine();
        // Generate default ID with prefix "CSTAD"
        String id = generateDefaultId();
        Student student = new Student(id, name, dateOfBirth, classroom, subjects);
        studentService.addNewStudent(student);
    }

    private String generateDefaultId() {
        int randomNumber = new Random().nextInt(100000) + 1;
        return String.format("%dCSTAD", randomNumber);
    }

    private void listAllStudents() {
        List<Student> students = studentService.listAllStudents();
        System.out.println("[!] LAST PAGE <<");
        System.out.println("[*] STUDENTS' DATA");
        Table table = new Table(5,BorderStyle.UNICODE_BOX_HEAVY_BORDER,ShownBorders.ALL);
        table.addCell("ID");
        table.addCell("STUDENT'S NAME");
        table.addCell(" STUDENT'S DATE OF BIRTH");
        table.addCell(" STUDENTS CLASS");
        table.addCell("STUDENT SUBJECT");
        for(Student student : students){
            table.addCell(student.getId());
            table.addCell(student.getName());
            table.addCell(student.getDateOfBirth().toString());
            table.addCell(student.getClassroom());
            table.addCell(student.getSubjects());
        }
        System.out.println(table.render());
        System.out.println("[*] Page Number: 1      [*] Actual record: " + students.size() + "        [*] All Record: " + students.size());
        System.out.print("[+] Insert to Navigate [p/N]: ");
        scanner.nextLine();
    }
    private void displaySearchResults(List<Student> students) {
        System.out.println("[!] LAST PAGE << [*] STUDENTS' DATA");
        Table table = new Table(5,BorderStyle.UNICODE_BOX_HEAVY_BORDER,ShownBorders.ALL);
        table.addCell("ID");
        table.addCell(" STUDENT'S NAME");
        table.addCell("STUDENT'S DATE OF BIRTH ");
        table.addCell("STUDENT CLASSROOM");
        table.addCell("STUDENTS SUBJECT");
        for(Student student: students){
            table.addCell(student.getId());
            table.addCell(student.getName());
            table.addCell(student.getDateOfBirth().toString());
            table.addCell(student.getClassroom());
            table.addCell(student.getSubjects());
        }
        System.out.println(table.render());
        System.out.println("[*] Page Number: 1      [*] Actual record: " + students.size() + "        [*] All Record: " + students.size());
        System.out.print("[+] Insert to Navigate [p/N]: ");
        scanner.nextLine();
    }

    private void searchForStudent() {
        System.out.println("....................");
        System.out.println("[+] SEARCHING STUDENT");
        System.out.println("....................");
        System.out.println("1. SEARCH BY NAME");
        System.out.println("2. SEARCH BY ID");
//        System.out.print("- (ВАСК/B) ТО ВАСК: ");
        System.out.print("please choose option 1 or 2 : ");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                searchStudentByName();
                break;
            case 2:
                searchStudentById();
                break;
            default:
                System.out.println("Invalid option! Please try again.");
        }
    }

    private List<Student> searchStudentByName() {
        System.out.print(">>> Insert student's NAME: ");
        String name = scanner.nextLine();
        List<Student> students = studentService.searchStudentByName(name);
//        displaySearchResults(students);
        if (students.isEmpty()) {
            System.out.println("Student not found.");
            displaySearchResults(students);
        } else {
            displaySearchResults(students);
        }
        try{

            return students.stream().filter(e->e.getId().equals(name)).toList();
        }catch (NullPointerException exception){
            System.out.println(STR. "[!] Problem: \{exception.getMessage()}");
            return new ArrayList<>();
        }
    }

    private List<Student> searchStudentById() {
        System.out.print(">>> Insert student's ID: ");
        String id = scanner.nextLine();
        List<Student> students = studentService.searchStudentById(id);

        if (students.isEmpty()) {
            System.out.println("Student not found.");
            displaySearchResults(students);
        } else {
            displaySearchResults(students);
        }

        try {
            return students.stream().filter(e -> e.getId().equals(id)).toList();
        } catch (NullPointerException exception) {
            System.out.println("[!] Problem: " + exception.getMessage());
            return new ArrayList<>();
        }
    }


    private void updateStudentById() {
        System.out.print(">>> Insert student's ID: ");
        String id = scanner.nextLine();
        List<Student> students = studentService.searchStudentById(id);
//        displaySearchResults(students);
        if (!students.isEmpty()) {
            System.out.println("[!] Enter updated student details:");

            // Prompt user to input updated details
            System.out.print("[!] Enter updated name: ");
            String name = scanner.nextLine();

            // Capture and parse the updated date of birth
            System.out.println("[!] Enter updated date of birth ");
            System.out.print("1. Year (number): ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("2. Month (number): ");
            int month = Integer.parseInt(scanner.nextLine());
            System.out.print("3. Day (number): ");
            int day = Integer.parseInt(scanner.nextLine());
            LocalDate dob = LocalDate.of(year, month, day);

            System.out.println("[!] YOU CAN INSERT MULTI CLASSES BY SPLITTING [,] SYMBOL (C1,02)");
            System.out.print("[+] Student's class: ");
            String classroom = scanner.nextLine();
            System.out.println("[!] YOU CAN INSERT MULTI SUBJECTS BY SPLITTING [,] SYMBOL (S1, 52)");
            System.out.print("[+] Subject studied: ");
            String subjects = scanner.nextLine();

            // Create a new Student object with updated details
            Student updatedStudent = new Student(id, name, dob, classroom, subjects);

            // Call the service method to update the student
            Student updated = studentService.updateStudentById(id, updatedStudent);

            // Display message if update is successful
            if (updated != null) {
                System.out.println("Student with ID " + id + " updated successfully.");

                List<Student> allStudents = studentService.listAllStudents();
                displaySearchResults(allStudents);
            } else {
                System.out.println("No student found with the given ID.");
            }
        } else {
            System.out.println("No student found with the given ID.");
        }
    }

    private Student deleteStudentData() {
        System.out.print(">>> Insert student's ID: ");
        String id = scanner.nextLine();
        Student deletedStudent = studentService.deleteStudentById(id);

        if (deletedStudent != null) {
            System.out.println("Student with ID " + id + " deleted successfully.");
            List<Student> students = studentService.listAllStudents();
            displaySearchResults(students);
        } else {
            System.out.println("No student found with the given ID.");
        }

        return deletedStudent;
    }

    private void generateDataToFile() {
        // Placeholder for generating data to file
        System.out.println("Generating data to file...");
    }

    private void deleteAllData() {
        System.out.print("Are you sure you want to delete all data? (Y/N) :");
        String choice = scanner.nextLine().toUpperCase();
        if (choice.equals("Y")) {
            studentService.deleteAllStudents();
            System.out.println("All data deleted successfully.");
            List<Student> students = studentService.listAllStudents();
            displaySearchResults(students);
        } else if (choice.equals("N")) {
            System.out.println("Operation canceled.");
        } else {
            System.out.println("Invalid choice. Please enter Y or N.");
        }
    }
}
