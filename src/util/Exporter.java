package util;

import interfaces.Reportable;
import model.Course;
import model.Registration;
import model.Student;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Exporter {
    public static <T extends Reportable> void export(List<T> items, String outputFileName) throws IOException {
        String exportDir = ConfigReader.get("export.dir");
        File dir = new File(exportDir);
        if (!dir.exists()) dir.mkdirs();

        File outFile = new File(dir, outputFileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
            for (T item : items) {
                writer.write(item.toReportString());
                writer.newLine();
            }
        }
    }
    public static void exportStudents(List<Student> students) throws IOException {
        export(students, "students.txt");
    }

    public static void exportCourses(List<Course> courses) throws IOException {
        export(courses, "courses.txt");
    }

    public static void exportRegistrations(List<Registration> regs) throws IOException {
        export(regs, "registrations.txt");
    }
}