    package fileIO.controller;
    import fileIO.model.Student;
    import fileIO.model.service.StudentService;
    import org.nocrala.tools.texttablefmt.BorderStyle;
    import org.nocrala.tools.texttablefmt.CellStyle;
    import org.nocrala.tools.texttablefmt.ShownBorders;
    import org.nocrala.tools.texttablefmt.Table;
    import java.io.*;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.time.LocalDate;
    import java.time.format.DateTimeParseException;
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
        private static StudentService studentService = null;
        private static Scanner scanner = null;
        private final String FILE_NAME = "src/allFile/students.txt";
        private static final String TRANSACTION_FILE_NAME = "src/allFile/TransactionFile.txt";
        private List<Student> students = new ArrayList<>();
        private static int currentPage = 1;
        private static final int RECORDS_PER_PAGE = 10;
        public StudentController(StudentService studentService) {
            this.studentService = studentService;
            this.scanner = new Scanner(System.in);
        }
        public static void start() {
            // Check for pending transactions
            boolean transactionsProcessed = checkPendingTransactions();
            if (!transactionsProcessed) {
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
        }

        private static void displayTitle() {
            String reset = "\u001B[0m";  // Reset color
            String cyanBold = "\u001B[1;36m"; // Cyan color and bold
            String fontEnglish = "\u001B[3m"; // Italic font style


            System.out.println(cyanBold +"");
            System.out.println(cyanBold +" ".repeat(43) +" ██████╗███████╗████████╗ █████╗ ██████╗     ███████╗███╗   ███╗███████╗");
            System.out.println(cyanBold +" ".repeat(43) +"██╔════╝██╔════╝╚══██╔══╝██╔══██╗██╔══██╗    ██╔════╝████╗ ████║██╔════╝");
            System.out.println(cyanBold +" ".repeat(43) +"██║     ███████╗   ██║   ███████║██║  ██║    ███████╗██╔████╔██║███████╗");
            System.out.println(cyanBold +" ".repeat(43) +"██║     ╚════██║   ██║   ██╔══██║██║  ██║    ╚════██║██║╚██╔╝██║╚════██");
            System.out.println(cyanBold +" ".repeat(43) +"╚██████╗███████║   ██║   ██║  ██║██████╔╝    ███████║██║ ╚═╝ ██║███████║");
            System.out.println(cyanBold +" ".repeat(43) +" ╚═════╝╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═════╝     ╚══════╝╚═╝     ╚═╝╚══════╝"+ reset);
//        System.out.println(cyanBold +" ".repeat(53) +"មជ្ឈមណ្ឌលអភិវឌ្ឍន៍វិទ្យាសាស្រ្ត និង បច្ចេកទេសវិទ្យាកម្រិតខ្ពស់");
            System.out.println(cyanBold +" ".repeat(50) +"Center Of Science and Technology Advanced Development-CSTAD"+reset);
//        System.out.println(cyanBold +" ".repeat(53)+"Advanced"+" ".repeat(2)+ " Development-CDTSD"+ reset);

        }

        private static void displayMenu() {
            // Add new Code Today
            String reset = "\u001B[0m";
            String cyanBold = "\u001B[1;36m";
            String redColor = "\u001B[31m"; // Red color
            String resetColor = "\u001B[0m"; // Reset color
            //The End Code For Today
            //Menu
            System.out.println("=".repeat(156));
            Table table = new Table(3, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
            table.addCell(cyanBold + "[1].ADD NEW STUDENT"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[2].LIST ALL STUDENTS"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[3].COMMIT DATA TO FILE"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[4].SEARCH FOR STUDENT"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[5].UPDATE STUDENT'S INFO BY ID"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[6].DELETE STUDENT'S DATA"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[7].GENERATE DATA TO FILE"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[8].DELETE/CLEAR ALL DATA FROM DATA STORE"+reset, new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.addCell(cyanBold + "[0,99]. EXIT"+reset,new CellStyle(CellStyle.HorizontalAlign.CENTER));
            table.setColumnWidth(0,51,70);
            table.setColumnWidth(1,51,70);
            table.setColumnWidth(2,51,70);


            System.out.println(table.render());
            System.out.println(" \t".repeat(35) + redColor +"©\uFE0FCopyright-CSTAD" + resetColor);
            System.out.println("=".repeat(156));
            System.out.print("➡️\uFE0F Insert option: ");
        }
        private static void clearTransactionFile(String fileName) {
            File file = new File(fileName);
            try {
                if (file.exists()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write(""); // Clear file contents
                    writer.close();
                    System.out.println("Transaction file cleared.");
                } else {
                    System.out.println("Transaction file does not exist.");
                }
            } catch (IOException e) {
                System.out.println("Error clearing transaction file: " + e.getMessage());
                e.printStackTrace();
            }
        }
        private static boolean checkPendingTransactions() {
            if (Files.exists(Paths.get(TRANSACTION_FILE_NAME))) {
                try (BufferedReader reader = new BufferedReader(new FileReader(TRANSACTION_FILE_NAME))) {
                    long numRecords = reader.lines().count();
                    if (numRecords > 0) {
                        System.out.println("[*] SPENT TIME FOR READING DATA: 0.007S");
                        System.out.println("[*] NUMBER OF PENDING RECORDS: " + numRecords);
                        System.out.print("> Commit your pending data record(s) beforehand [Y/N]: ");
                        String choice = scanner.nextLine().toUpperCase();

                        if (choice.equals("Y")) {
                            commitDataToFile();
                            clearTransactionFile(TRANSACTION_FILE_NAME);
                            return true;
                        } else if (choice.equals("N")) {
                            System.out.println("Operation canceled.");
                        } else {
                            System.out.println("Invalid choice. Please enter Y or N.");
                        }
                    } else {
                        System.out.println("No pending records.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Transaction file not found.");
            }
            return false;
        }
private static void addNewStudent() {
    System.out.println("..............................");
    System.out.println("➡️ INSERT STUDENT'S INFO");
    System.out.print("[➕] Insert student's name: ");
    String name = scanner.nextLine();
    System.out.println("[➕] STUDENT DATE OF BIRTH");
    System.out.print("1. Year (number): ");
    int year = Integer.parseInt(scanner.nextLine());
    int month;
    int day;
    do {
        System.out.print("2. Month (number): ");
        month = Integer.parseInt(scanner.nextLine());
        // Check if the month is valid
        if (month < 1 || month > 12) {
            System.out.println("Invalid month. Please enter a month between 1 and 12.");
        } else {
            break; // Break the loop if the month is valid
        }
    } while (true);

    do {
        System.out.print("3. Day (number): ");
        day = Integer.parseInt(scanner.nextLine());
        // Check if the day is valid for the given month
        if (day < 1 || day > LocalDate.of(year, month, 1).lengthOfMonth()) {
            System.out.println("Invalid day. Please enter a valid day for the selected month.");
        } else {
            break; // Break the loop if the day is valid
        }
    } while (true);

    LocalDate dateOfBirth = LocalDate.of(year, month, day);
    System.out.println("[❗️] YOU CAN INSERT MULTI CLASSES BY SPLITTING [,] SYMBOL (C1,02)");
    System.out.print("[➕] Student's class: ");
    String classroom = scanner.nextLine();
    System.out.println("[❗️] YOU CAN INSERT MULTI SUBJECTS BY SPLITTING [,] SYMBOL (S1, 52)");
    System.out.print("[➕] Subject studied: ");
    String subjects = scanner.nextLine();
    LocalDate createdAt = LocalDate.now();
    // Generate default ID with prefix "CSTAD"
    String id = generateDefaultId();
    Student student = new Student(id, name, dateOfBirth, classroom, subjects, createdAt);
    // Write new student's data to the transaction file
    writeDataToTransactionFile(student);
    System.out.println("Student data added to the transaction file.");
}
        private static void writeDataToTransactionFile(Student student) {
            String transactionFileName = "src/allFile/TransactionFile.txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(transactionFileName, true))){
                // Append new student's data to the transaction file
                writer.write(String.format("%s,%s,%s,%s,%s,%s%n",
                        student.getId(),
                        student.getName(),
                        student.getDateOfBirth(),
                        student.getClassroom(),
                        student.getSubjects(),
                        student.getCreateAt()));
                System.out.println("Student data written to transaction file: " + transactionFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public static void commitDataToFile() {
        studentService.commitDataFromTransaction();
    }
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
        public static String generateDefaultId() {
            int randomNumber = new Random().nextInt(10000) + 1;
            return String.format("%dCSTAD", randomNumber);
        }
        private static List<Student> listAllStudents() {
            List<Student> students = studentService.listAllStudents();
            int totalPages = (int) Math.ceil((double) students.size() / RECORDS_PER_PAGE);
            while (true) {
                //add new Code Today

                // End new code Today
                int startIndex = (currentPage - 1) * RECORDS_PER_PAGE;
                int endIndex = Math.min(startIndex + RECORDS_PER_PAGE, students.size());

                Table table = new Table(6, BorderStyle.UNICODE_BOX_HEAVY_BORDER, ShownBorders.ALL);
                String reset = "\u001B[0m";
                String cyanBold = "\u001B[1;36m";

                table.addCell(cyanBold + "ID",new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(cyanBold + "STUDENT'S NAME", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(cyanBold + "STUDENT'S DATE OF BIRTH ", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(cyanBold + "STUDENT CLASSROOM", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(cyanBold + "STUDENTS SUBJECT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.addCell(cyanBold + "CREATE AT / UPDATE AT", new CellStyle(CellStyle.HorizontalAlign.CENTER));
                table.setColumnWidth(0,25,40);
                table.setColumnWidth(1,25,40);
                table.setColumnWidth(2,25,40);
                table.setColumnWidth(3,25,40);
                table.setColumnWidth(4,25,40);
                table.setColumnWidth(5,25,40);


                for (int i = startIndex; i < endIndex; i++) {
                    Student student = students.get(i);
                    String yellowColor = "\u001B[33m"; //code for colunm
                    String resetColor = "\u001B[0m"; // Reset color
                    table.addCell(yellowColor+student.getId()+resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(yellowColor+student.getName()+ resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(yellowColor+student.getDateOfBirth().toString()+ resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(yellowColor+student.getClassroom()+resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(yellowColor+student.getSubjects()+ resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                    table.addCell(yellowColor+student.getCreateAt().toString()+ resetColor, new CellStyle(CellStyle.HorizontalAlign.CENTER));
                }
                System.out.println(table.render());

                System.out.println("[\uD83D\uDCA2] Page Number: " + currentPage + "      [\uD83D\uDCA2] Actual record: " + (endIndex - startIndex) + "        [\uD83D\uDCA2] All Record: " + students.size());
                System.out.print("[➖]  Previous (P/p) - [➕] Next (n/N) - [\uD83D\uDD01] Back (B/b): ");
                String choice = scanner.nextLine().trim().toLowerCase();

                switch (choice) {
                    case "p":
                        if (currentPage > 1) {
                            currentPage--;
                            System.out.println("[❗️\uFE0F] PREVIOUS PAGE << [✴️\uFE0F] STUDENTS' DATA");
                        } else {
                            System.out.println("[❗️\uFE0F] FIRST PAGE << [✴️\uFE0F] STUDENTS' DATA");
                        }
                        break;
                    case "n":
                        if (currentPage < totalPages) {
                            currentPage++;
                            System.out.println("[❗️\uFE0F] NEXT PAGE << [✴️\uFE0F] STUDENTS' DATA");
                        } else {
                            System.out.println("[❗️\uFE0F] LAST PAGE << [✴️\uFE0F] STUDENTS' DATA");
                        }
                        break;
                    case "b":
                        return students;
                    default:
                        System.out.println("⛔️\uFE0FInvalid choice! Please try again.");
                }
            }
        }

        private static void displaySearchResults(List<Student> students) {
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
        private static void searchForStudent() {
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
        private static List<Student> searchStudentByName() {
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
                return students.stream().filter(e -> e.getId().equals(name)).toList();
            } catch (NullPointerException exception) {
                System.out.println(STR."[➕] Problem: \{exception.getMessage()}");
                return new ArrayList<>();
            }
        }

        private static List<Student> searchStudentById() {
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

        private static void updateStudentById() {
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
                System.out.println("[❗\uFE0F] Enter updated date of birth ");
                System.out.println("[➕] STUDENT DATE OF BIRTH");
                System.out.print("1. Year (number): ");
                int year = Integer.parseInt(scanner.nextLine());
                int month;
                int day;
                do {
                    System.out.print("2. Month (number): ");
                    month = Integer.parseInt(scanner.nextLine());
                    // Check if the month is valid
                    if (month < 1 || month > 12) {
                        System.out.println("Invalid month. Please enter a month between 1 and 12.");
                    } else {
                        break; // Break the loop if the month is valid
                    }
                } while (true);

                do {
                    System.out.print("3. Day (number): ");
                    day = Integer.parseInt(scanner.nextLine());
                    // Check if the day is valid for the given month
                    if (day < 1 || day > LocalDate.of(year, month, 1).lengthOfMonth()) {
                        System.out.println("Invalid day. Please enter a valid day for the selected month.");
                    } else {
                        break; // Break the loop if the day is valid
                    }
                } while (true);
//                int year = 0,month = 0,day = 0;
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

        private static Student deleteStudentData() {
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
        private static void deleteAllData() {
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
        private static void generateDataToFile() {
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
