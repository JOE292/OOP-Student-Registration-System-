package gui;

import DAO.StudentDAO;
import DAO.CourseDAO;
import DAO.RegistrationDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;

public class mainFrame extends JFrame {
    public mainFrame() {
        setTitle("Student Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/student_registration", "root", "");
            StudentDAO studentDAO = new StudentDAO(conn);
            CourseDAO courseDAO = new CourseDAO(conn);
            RegistrationDAO registrationDAO = new RegistrationDAO(conn);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Students", new studentPanel(studentDAO, registrationDAO));
            tabbedPane.addTab("Courses", new coursePanel(courseDAO, registrationDAO));
            tabbedPane.addTab("Registrations", new RegistrationPanel( registrationDAO, studentDAO,courseDAO));

            add(tabbedPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            mainFrame mainFrame = new mainFrame();
            mainFrame.setVisible(true);
        });
    }
}
