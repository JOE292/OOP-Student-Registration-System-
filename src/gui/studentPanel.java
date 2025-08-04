package gui;

import DAO.RegistrationDAO;
import DAO.StudentDAO;
import model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class studentPanel extends JPanel {

    private final StudentDAO studentDAO;
    private final RegistrationDAO registrationDAO;
    private final DefaultTableModel tableModel;

    public studentPanel(StudentDAO studentDAO, RegistrationDAO registrationDAO) {
        this.studentDAO = studentDAO;
        this.registrationDAO=registrationDAO;
        setLayout(new BorderLayout());


        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField majorField = new JTextField();
        JButton addButton = new JButton("Add Student");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        formPanel.setBorder(BorderFactory.createTitledBorder("Student Form"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Major:"));
        formPanel.add(majorField);
        formPanel.add(new JLabel(""));
        formPanel.add(addButton);
        add(formPanel, BorderLayout.NORTH);
        formPanel.add(deleteButton);


        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Major"}, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        add(tableScrollPane, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

    
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                emailField.setText(tableModel.getValueAt(row, 2).toString());
                majorField.setText(tableModel.getValueAt(row, 3).toString());
            }
        });


        refreshStudentTable();

    
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String major = majorField.getText().trim();
            if (name.isEmpty() || email.isEmpty() || major.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try {
                Student s = new Student(name, email, major);
                studentDAO.addStudentIfNotExists(s);
                refreshStudentTable();
                nameField.setText("");
                emailField.setText("");
                majorField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        updateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a student to update");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String major = majorField.getText().trim();

            if (name.isEmpty() || email.isEmpty() || major.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields");
                return;
            }

            try {
                Student updatedStudent = new Student(id, name, email, major);
                studentDAO.updateStudent(updatedStudent);
                refreshStudentTable();
                nameField.setText("");
                emailField.setText("");
                majorField.setText("");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        });

        
        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Select a student to delete");
                return;
            }

            int id = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete student ID " + id + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    studentDAO.deleteById(id);
                    refreshStudentTable();
                    nameField.setText("");
                    emailField.setText("");
                    majorField.setText("");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.");
                return;
            }

            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this student and all related registrations?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    registrationDAO.deleteByStudentId(studentId);
                    studentDAO.deleteById(studentId);
                    refreshStudentTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            }
        });

    }

    private void refreshStudentTable() {
        try {
            List<Student> students = studentDAO.getAllStudents();
            tableModel.setRowCount(0);
            for (Student s : students) {
                tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail(), s.getMajor()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
