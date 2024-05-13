package fileIO.controller;

import fileIO.model.Student;
import fileIO.model.service.StudentService;
import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.ShownBorders;
import org.nocrala.tools.texttablefmt.Table;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import static java.lang.StringTemplate.STR;

public class StudentController {
    private final StudentService studentService;
    private final Scanner scanner;

    private int currentPage = 1;
    private static final int RECORDS_PER_PAGE = 10;

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
                    commitDataToFile();
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

    private void displayTitle() {
        System.out.println("\t".repeat(5) + " ██████╗███████╗████████╗ █████╗ ██████╗     ███████╗███╗   ███╗███████╗");
        System.out.println("\t".repeat(5) + "██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗    ██╔════╝████╗ ████║██╔════╝");
        System.out.println("\t".repeat(5) + "██║     ███████╗   ██║   ███████║██║  ██║    ███████╗██╔████╔██║███████╗");
        System.out.println("\t".repeat(5) + "██║     ╚════██║   ██║   ██╔══██║██║  ██║    ╚════██║██║╚██╔╝██║╚════██║");
        System.out.println("\t".repeat(5) + "╚██████╗███████║   ██║   ██║  ██║██████╔╝    ███████║██║ ╚═╝ ██║███████║");
        System.out.println("\t".repeat(5) + " ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝     ╚══════╝╚═╝     ╚═╝╚══════╝");
    }

    private void displayMenu() {
        //Menu
        System.out.println("=".repeat(156));
        Table table = new Table(3, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
        table.addCell("[1].ADD NEW STUDENT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[2].LIST ALL STUDENTS", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[3].COMMIT DATA TO FILE", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[4].SEARCH FOR STUDENT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[5].UPDATE STUDENT'S INFO BY ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[6].DELETE STUDENT'S DATA", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[7].GENERATE DATA TO FILE", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[8].DELETE/CLEAR ALL DATA FROM DATA STORE", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("[0,99]. EXIT",new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.setColumnWidth(0,51,70);
        table.setColumnWidth(1,51,70);
        table.setColumnWidth(2,51,70);


        System.out.println(table.render());
        System.out.println("\t".repeat(35) + "Copyright-CSTAD");
        System.out.println("=".repeat(156));
        System.out.print("➡\uFE0F Insert option: ");
    }

    private void addNewStudent() {
        System.out.println("..............................");
        System.out.println("➡\uFE0F INSERT STUDENT'S INFO");
        System.out.print("[➕] Insert student's name: ");
        String name = scanner.nextLine();
        System.out.println("[➕] STUDENT DATE OF BIRTH");
        System.out.print("1. Year (number): ");
        Integer year = new Scanner(System.in).nextInt();
        System.out.print("2. Month (number): ");
        Integer month = new Scanner(System.in).nextInt();
        System.out.print("3. Day (number): ");
        Integer day = new Scanner(System.in).nextInt();
        LocalDate dateOfBirth = LocalDate.of(year, month, day);

        System.out.println("[❗\uFE0F] YOU CAN INSERT MULTI CLASSES BY SPLITTING [,] SYMBOL (C1,02)");
        System.out.print("[➕] Student's class: ");
        String classroom = scanner.nextLine();
        System.out.println("[❗\uFE0F] YOU CAN INSERT MULTI SUBJECTS BY SPLITTING [,] SYMBOL (S1, 52)");
        System.out.print("[➕] Subject studied: ");
        String subjects = scanner.nextLine();

        LocalDate createAt = LocalDate.now();
        // Generate default ID with prefix "CSTAD"
        String id = generateDefaultId();
        Student student = new Student(id, name, dateOfBirth, classroom, subjects, createAt);
        studentService.addNewStudent(student);
    }
    public void commitDataToFile(){
        studentService.commitDataToFile();
    }
    public void commitDataFromTransaction(){
        studentService.commitDataFromTransaction();
    }

    public static String generateDefaultId() {
        int randomNumber = new Random().nextInt(10000) + 1;
        return String.format("%dCSTAD", randomNumber);
    }

    private List<Student> listAllStudents() {
        List<Student> students = studentService.listAllStudents();
        int totalPages = (int) Math.ceil((double) students.size() / RECORDS_PER_PAGE);

        while (true) {
            int startIndex = (currentPage - 1) * RECORDS_PER_PAGE;
            int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, students.size());

            Table table = new Table(6, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
            table.addCell("ID",new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("STUDENT'S NAME", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("STUDENT'S DATE OF BIRTH ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("STUDENT CLASSROOM", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("STUDENTS SUBJECT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell("CREATE AT / UPDATE AT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.setColumnWidth(0,25,40);
            table.setColumnWidth(1,25,40);
            table.setColumnWidth(2,25,40);
            table.setColumnWidth(3,25,40);
            table.setColumnWidth(4,25,40);
            table.setColumnWidth(5,25,40);


            for (int i = startIndex; i < endIndex; i++) {
                Student student = students.get(i);
                table.addCell(student.getId(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(student.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(student.getDateOfBirth().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(student.getClassroom(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(student.getSubjects(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(student.getCreateAt().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            }
            System.out.println(table.render());

            System.out.println("[\uD83D\uDCA2] Page Number: " + currentPage + "      [\uD83D\uDCA2] Actual record: " + (endIndex - startIndex) + "        [\uD83D\uDCA2] All Record: " + students.size());
            System.out.print("[➖]  Previous (P/p) - [➕] Next (n/N) - [\uD83D\uDD01] Back (B/b): ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "p":
                    if (currentPage > 1) {
                        currentPage--;
                        System.out.println("[❗\uFE0F] FIRST PAGE << [✴\uFE0F] STUDENTS' DATA");
                    } else {
                        System.out.println("[❗\uFE0F] ALREADY FIRST PAGE << [✴\uFE0F] STUDENTS' DATA");
                    }
                    break;
                case "n":
                    if (currentPage < totalPages) {
                        currentPage++;
                        System.out.println("[❗\uFE0F] LAST PAGE << [✴\uFE0F] STUDENTS' DATA");
                    } else {
                        System.out.println("[❗\uFE0F] ALREADY LAST PAGE << [✴\uFE0F] STUDENTS' DATA");
                    }
                    break;
                case "b":
                    return students;
                default:
                    System.out.println("⛔\uFE0FInvalid choice! Please try again.");
            }
        }
    }

    private void displaySearchResults(List<Student> students) {
        int startIndex = (currentPage - 1) * RECORDS_PER_PAGE;
        int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, students.size());

        System.out.println("[❗\uFE0F] LAST PAGE << [✴\uFE0F] STUDENTS' DATA");
        Table table = new Table(6, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
        table.addCell("ID", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("STUDENT'S NAME", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("STUDENT'S DATE OF BIRTH ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("STUDENT CLASSROOM", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("STUDENTS SUBJECT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.addCell("CREATE AT / UPDATE AT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
        table.setColumnWidth(0,25,40);
        table.setColumnWidth(1,25,40);
        table.setColumnWidth(2,25,40);
        table.setColumnWidth(3,25,40);
        table.setColumnWidth(4,25,40);
        table.setColumnWidth(5,25,40);

        for (int i = startIndex; i < endIndex; i++) {
            Student student = students.get(i);
            table.addCell(student.getId(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getName(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getDateOfBirth().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getClassroom(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getSubjects(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(student.getCreateAt().toString(), new CellStyle(CellStyle.HorizontalAlign.CENTER));
        }
        System.out.println(table.render());

        System.out.println("[\uD83D\uDCA2] Page Number: " + currentPage + "      [\uD83D\uDCA2] Actual record: " + (endIndex - startIndex) + "        [\uD83D\uDCA2] All Record: " + students.size());
        System.out.print("[➖] Previous (P/p) - [➕] Next (n/N) - [\uD83D\uDD01] Back (B/b): ");
        scanner.nextLine();

    }

    private void searchForStudent() {
        System.out.println("[➕] SEARCHING STUDENT");
        System.out.println("1. SEARCH BY NAME");
        System.out.println("2. SEARCH BY ID");
        System.out.print(" \uD83D\uDCBB please choose option 1 or 2 : ");
        int option = Integer.parseInt(scanner.nextLine());
        switch (option) {
            case 1:
                searchStudentByName();
                break;
            case 2:
                searchStudentById();
                break;
            default:
                System.out.println("⛔\uFE0F Invalid option! Please try again.");
        }
    }

    private List<Student> searchStudentByName() {
        System.out.print("\uD83D\uDD0D Insert student's NAME: ");
        String name = scanner.nextLine();
        List<Student> students = studentService.searchStudentByName(name);
//        displaySearchResults(students);
        if (students.isEmpty()) {
            System.out.println("\uD83D\uDD0D Student not found.");
            displaySearchResults(students);
        } else {
            displaySearchResults(students);
        }
        try {
            // search students
            return students.stream().filter(e -> e.getId().equals(name)).toList();
        } catch (NullPointerException exception) {
            System.out.println(STR."[➕] Problem: \{exception.getMessage()}");
            return new ArrayList<>();
        }
    }

    private List<Student> searchStudentById() {
        System.out.print("\uD83D\uDD0D Insert student's ID: ");
        String id = scanner.nextLine();
        List<Student> students = studentService.searchStudentById(id);

        if (students.isEmpty()) {
            System.out.println("\uD83D\uDD0D Student not found.");
            displaySearchResults(students);
        } else {
            displaySearchResults(students);
        }
        // search students
        try {
            return students.stream().filter(e -> e.getId().equals(id)).toList();
        } catch (NullPointerException exception) {
            System.out.println("[➕] Problem: " + exception.getMessage());
            return new ArrayList<>();
        }
    }

    private void updateStudentById() {
        System.out.print("\uD83D\uDD0D Insert student's ID: ");
        String id = scanner.nextLine();
        // search students
        List<Student> students = studentService.searchStudentById(id);
//        displaySearchResults(students);
        if (!students.isEmpty()) {
            System.out.println("[❗\uFE0F] Enter updated student details:");

            // Prompt user to input updated details
            System.out.print("[❗\uFE0F] Enter updated name: ");
            String name = scanner.nextLine();

            // Capture and parse the updated date of birth
            System.out.println("[❗\uFE0F] Enter updated date of birth ");
            System.out.print("1. Year (number): ");
            int year = Integer.parseInt(scanner.nextLine());
            System.out.print("2. Month (number): ");
            int month = Integer.parseInt(scanner.nextLine());
            System.out.print("3. Day (number): ");
            int day = Integer.parseInt(scanner.nextLine());
            LocalDate dob = LocalDate.of(year, month, day);

            System.out.println("[❗\uFE0F] YOU CAN INSERT MULTI CLASSES BY SPLITTING [,] SYMBOL (C1,02)");
            System.out.print("[+] Student's class: ");
            String classroom = scanner.nextLine();
            System.out.println("[❗\uFE0F] YOU CAN INSERT MULTI SUBJECTS BY SPLITTING [,] SYMBOL (S1, 52)");
            System.out.print("[➕] Subject studied: ");
            String subjects = scanner.nextLine();

            LocalDate createAt = LocalDate.now();
            // Create a new Student object with updated details
            Student updatedStudent = new Student(id, name, dob, classroom, subjects,createAt);

            // Call the service method to update the student
            Student updated = studentService.updateStudentById(id, updatedStudent);

            if (updated != null) {
                System.out.println("\uD83D\uDD0D\n Student with ID " + id + " updated successfully \uD83C\uDF89\n");

                List<Student> allStudents = studentService.listAllStudents();
                displaySearchResults(allStudents);
            } else {
                System.out.println("\uD83D\uDEAB No student found with the given ID.");
            }
        } else {
            System.out.println(" \uD83D\uDEAB No student found with the given ID.");
        }
    }

    private Student deleteStudentData() {
        System.out.print("\uD83D\uDD0D Insert student's ID: ");
        String id = scanner.nextLine();
        Student deletedStudent = studentService.deleteStudentById(id);

        if (deletedStudent != null) {
            System.out.println("\uD83D\uDD0D Student Search with ID " + id + " deleted successfully \uD83D\uDDD1\uFE0F. ");
            List<Student> students = studentService.listAllStudents();
            displaySearchResults(students);
        } else {
            System.out.println(" \uD83D\uDEAB No student found with the given ID.");
        }
        return deletedStudent;
    }

    private void deleteAllData() {
        System.out.print("Are you sure you want to delete all data? (Y/N) :");
        String choice = scanner.nextLine().toUpperCase();
        if (choice.equals("Y")) {
            studentService.deleteAllStudents();
            System.out.println("All data deleted successfully \uD83D\uDDD1\uFE0F.");
            List<Student> students = studentService.listAllStudents();
            displaySearchResults(students);
        } else if (choice.equals("N")) {
            System.out.println("Operation canceled.");
        } else {
            System.out.println("⛔\uFE0F Invalid choice. Please enter Y or N.");
        }
    }

    private void generateDataToFile() {
        System.out.print("[➕] Number of objects you want to generate (100M - 100_000_000): ");
        int numRecords = Integer.parseInt(scanner.nextLine());

        long startTime = System.currentTimeMillis();

        // Define the number of threads to use
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // Divide the total number of records by the number of threads
        int recordsPerThread = numRecords / numThreads;

        // Submit tasks to the executor
        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * recordsPerThread;
            int endIndex = (i == numThreads - 1) ? numRecords : (i + 1) * recordsPerThread;
            executorService.submit(() -> studentService.generateRecords(startIndex, endIndex));
        }

        // Shutdown the executor after all tasks are completed
        executorService.shutdown();

        // Wait for all tasks to finish
        while (!executorService.isTerminated()) {
            // Do nothing
        }

        long endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 10000.00;

        System.out.printf("[➕] SPENT TIME FOR WRITING DATA: %.3f S%n", elapsedTime);
        System.out.printf("[➕] WROTE DATA %d RECORD SUCCESSFULLY.%n", numRecords);
    }
}
