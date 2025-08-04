package gui;

import DAO.CourseDAO;
import DAO.RegistrationDAO;
import model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class coursePanel extends JPanel {
    private final CourseDAO courseDAO;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final RegistrationDAO registrationDAO;
    private final RegistrationPanel registrationPanel;

    public coursePanel(CourseDAO courseDAO, RegistrationDAO registrationDAO, RegistrationPanel registrationPanel) {
        this.courseDAO = courseDAO;
        this.registrationDAO = registrationDAO;
        this.registrationPanel = registrationPanel;
        setLayout(new BorderLayout());

    
        JTextField courseIdField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField instructorField = new JTextField();
        JTextField creditsField = new JTextField();

        JButton addButton = new JButton("Add Course");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        JPanel formPanel = new JPanel(new GridLayout(2, 5, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Details"));
        formPanel.add(new JLabel("Course ID:"));
        formPanel.add(courseIdField);
        formPanel.add(new JLabel("Title:"));
        formPanel.add(titleField);
        formPanel.add(addButton);
        formPanel.add(new JLabel("Instructor:"));
        formPanel.add(instructorField);
        formPanel.add(new JLabel("Credits:"));
        formPanel.add(creditsField);
        formPanel.add(updateButton);
        formPanel.add(deleteButton);


        tableModel = new DefaultTableModel(new Object[]{"Course ID", "Title", "Instructor", "Credits"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(deleteButton);

        add(formPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        refreshCourseTable();
   
        addButton.addActionListener(e -> {
            int courseId = Integer.parseInt(courseIdField.getText().trim());
            String title = titleField.getText().trim();
            String instructor = instructorField.getText().trim();
            String creditStr = creditsField.getText().trim();

            if (courseId==0 || title.isEmpty() || instructor.isEmpty() || creditStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try {
                int credits = Integer.parseInt(creditStr);
                Course c = new Course(courseId, title, instructor, credits);
                courseDAO.addCourseIfNotExists(c);
                refreshCourseTable();
                courseIdField.setText("");
                titleField.setText("");
                instructorField.setText("");
                creditsField.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        // Update course
        updateButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a course to update");
                return;
            }

            int courseId = (int) tableModel.getValueAt(row, 0);
            String newTitle = titleField.getText().trim();
            String newInstructor = instructorField.getText().trim();
            String newCreditsStr = creditsField.getText().trim();

            if (newTitle.isEmpty() || newInstructor.isEmpty() || newCreditsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields to update");
                return;
            }

            try {
                int newCredits = Integer.parseInt(newCreditsStr);
                Course c = new Course(courseId, newTitle, newInstructor, newCredits);
                courseDAO.updateCourse(c);
                refreshCourseTable();
                registrationPanel.refreshTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Credits must be a number");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

     
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a Course to delete.");
                return;
            }

            int courseId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this course and all related registrations?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    registrationDAO.deleteByCourseId(courseId);
                    courseDAO.deleteById(courseId);
                    refreshCourseTable();
                    registrationPanel.refreshTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });


        
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                courseIdField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
                titleField.setText((String) tableModel.getValueAt(row, 1));
                instructorField.setText((String) tableModel.getValueAt(row, 2));
                creditsField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                courseIdField.setEditable(false);
            }
        });
    }

    private void refreshCourseTable() {
        try {
            List<Course> courses = courseDAO.getAllCourses();
            tableModel.setRowCount(0);
            for (Course c : courses) {
                tableModel.addRow(new Object[]{
                        c.getCourseId(),
                        c.getTitle(),
                        c.getInstructor(),
                        c.getCreditHours()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
