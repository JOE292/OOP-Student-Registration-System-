package gui;

import DAO.StudentDAO;
import DAO.CourseDAO;
import DAO.RegistrationDAO;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class mainFrame extends JFrame {
    public mainFrame(Connection conn) {
        setTitle("Student Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        try {
            StudentDAO studentDAO = new StudentDAO(conn);
            CourseDAO courseDAO = new CourseDAO(conn);
            RegistrationDAO registrationDAO = new RegistrationDAO(conn);

            RegistrationPanel registrationPanel = new RegistrationPanel(registrationDAO, studentDAO, courseDAO);

            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.addTab("Students", new studentPanel(studentDAO, registrationDAO));
            tabbedPane.addTab("Courses", new coursePanel(courseDAO, registrationDAO, registrationPanel));
            tabbedPane.addTab("Registrations", registrationPanel);

            add(tabbedPane, BorderLayout.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed");
        }
    }
}
