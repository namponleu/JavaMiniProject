//package transaction;
//
//import fileIO.model.Student;
//import fileIO.model.service.StudentServiceImpl;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.List;
//import java.util.Scanner;
//
//public class transaction {
//    private Scanner scanner;
//    private StudentServiceImpl studentServiceImpl;
//    private List<Student> studentslist;
//    private Student student ;
//
//    public void start() throws IOException {
//        String op=null;
//        if(Files.exists(Paths.get("src/allFile/TransectionFile.txt"))){
//            if (studentServiceImpl.commitDataFromTransaction("src/allFile/TransectionFile.txt",scanner)){
//                op= studentServiceImpl.commitDataToFile(studentslist,"src/allFile/TransectionFile.txt","src/allFile/students.txt","startcommit",scanner);
//            }
//        }
//        if(op==null){
//            studentServiceImpl.readDataFromFile(studentslist,"src/allFile/students.txt","startcommit");
//        }
//        if(!studentslist.isEmpty()){
//            int lastId=studentslist.get(studentslist.size()-1).getId();
//            student.setLastAssignedId( lastId);
//            studentServiceImpl.writeSizeToFile(lastId,"src/allFile/lastId.txt");
//        }
//    }
//    public void commit() throws IOException {
//        if (studentServiceImpl.commitCheck("src/allFile/TransectionFile.txt",scanner)){
//            studentServiceImpl.commit(studentslist,"src/allFile/TransectionFile.txt","src/allFile/dataFile.txt","commit",scanner);
//        }else
//            System.out.println("THERE ARE NOTHING TO COMMIT ....");
//    }
//}