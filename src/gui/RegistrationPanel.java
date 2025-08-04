package gui;

import DAO.RegistrationDAO;
import DAO.StudentDAO;
import DAO.CourseDAO;
import model.Registration;
import model.Student;
import util.Exporter;
import model.Course;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class RegistrationPanel extends JPanel {

    private final RegistrationDAO registrationDAO;
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final DefaultTableModel tableModel;

    private final JComboBox<Student> studentComboBox = new JComboBox<>();
    private final JComboBox<Course> courseComboBox = new JComboBox<>();

    public RegistrationPanel(RegistrationDAO registrationDAO, StudentDAO studentDAO, CourseDAO courseDAO) {
        this.registrationDAO = registrationDAO;
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;

        setLayout(new BorderLayout());


        loadStudentsIntoDropdown();
        loadCoursesIntoDropdown();

          studentComboBox.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadStudentsIntoDropdown();
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });
        
        courseComboBox.addPopupMenuListener(new PopupMenuListener() {
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                loadCoursesIntoDropdown();
            }
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {}
            public void popupMenuCanceled(PopupMenuEvent e) {}
        });

        
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Register Student"));

        JButton registerButton = new JButton("Register");
        JButton deleteButton = new JButton("Delete Selected");
        JButton exportStudentButton = new JButton("Export Selected Student");

        formPanel.add(new JLabel("Student:"));
        formPanel.add(studentComboBox);
        formPanel.add(new JLabel("Course:"));
        formPanel.add(courseComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(registerButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportStudentButton);

        tableModel = new DefaultTableModel(new Object[]{"Student Name", "Course Title", "Date"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);

        add(formPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(tableScrollPane, BorderLayout.SOUTH);

        
        refreshTable();

    
        registerButton.addActionListener(e -> {
            Student student = (Student) studentComboBox.getSelectedItem();
            Course course = (Course) courseComboBox.getSelectedItem();

            if (student == null || course == null) {
                JOptionPane.showMessageDialog(this, "Please select both student and course.");
                return;
            }

            try {
                Registration registration = new Registration(student, course, LocalDate.now());
                registrationDAO.addRegistrationIfNotExists(registration);
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

          exportStudentButton.addActionListener(e -> {
            Student selectedStudent = (Student) studentComboBox.getSelectedItem();

            if (selectedStudent == null) {
                JOptionPane.showMessageDialog(this, "Please select a student from the dropdown.");
                return;
            }

            int studentId = selectedStudent.getStudentId();

            try {
                
                List<Course> courses = registrationDAO.getCoursesByStudentId(studentId);

                Exporter.exportStudentWithCourses(selectedStudent, courses);

                JOptionPane.showMessageDialog(this, "Exported to student_" + studentId + ".txt successfully.");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Export failed: " + ex.getMessage());
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a registration to delete.");
                return;
            }

            String studentName = (String) tableModel.getValueAt(selectedRow, 0);
            String courseTitle = (String) tableModel.getValueAt(selectedRow, 1);

            try {
                int studentId = findStudentIdByName(studentName);
                int courseId = findCourseIdByTitle(courseTitle);
                registrationDAO.deleteRegistration(studentId, courseId);
                refreshTable();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });
    }

    private void loadStudentsIntoDropdown() {
        try {
            studentComboBox.removeAllItems();
            for (Student s : studentDAO.getAllStudents()) {
                studentComboBox.addItem(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load students: " + e.getMessage());
        }
    }

    private void loadCoursesIntoDropdown() {
        try {
            courseComboBox.removeAllItems();
            for (Course c : courseDAO.getAllCourses()) {
                courseComboBox.addItem(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load courses: " + e.getMessage());
        }
    }

    public void refreshTable() {
        try {
            tableModel.setRowCount(0);
            for (Registration r : registrationDAO.getAllRegistrations()) {
                tableModel.addRow(new Object[]{
                        r.getStudent().getName(),
                        r.getCourse().getTitle(),
                        r.getRegistrationDate().toString()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int findStudentIdByName(String name) throws Exception {
        for (Student s : studentDAO.getAllStudents()) {
            if (s.getName().equals(name)) return s.getStudentId();
        }
        throw new Exception("Student not found");
    }

    private int findCourseIdByTitle(String title) throws Exception {
        for (Course c : courseDAO.getAllCourses()) {
            if (c.getTitle().equals(title)) return c.getCourseId();
        }
        throw new Exception("Course not found");
    }
}
