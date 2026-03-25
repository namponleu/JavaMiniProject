package fileIO;
<<<<<<< HEAD

import fileIO.controller.StudentController;
import fileIO.model.service.StudentService;
import fileIO.model.service.StudentServiceImpl;

=======
import fileIO.controller.StudentController;
import fileIO.model.service.StudentService;
import fileIO.model.service.StudentServiceImpl;
>>>>>>> 169b4f3fea616fa6c80048bdea54fdb383087d77
public class Main {
    public static void main(String[] args) {
        // Initialize the service
        StudentService studentService = new StudentServiceImpl();
<<<<<<< HEAD

        // Initialize the controller with the service
        StudentController studentController = new StudentController(studentService);

=======
        // Initialize the controller with the service
        StudentController studentController = new StudentController(studentService);
>>>>>>> 169b4f3fea616fa6c80048bdea54fdb383087d77
        // Start the application
        studentController.start();
    }
}
