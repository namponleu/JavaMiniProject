package fileIO;

import fileIO.controller.StudentController;
import fileIO.model.service.StudentService;
import fileIO.model.service.StudentServiceImpl;

public class Main {
    public static void main(String[] args) {
        // Initialize the service
        StudentService studentService = new StudentServiceImpl();

        // Initialize the controller with the service
        StudentController studentController = new StudentController(studentService);

        // Start the application
        studentController.start();
    }
}
