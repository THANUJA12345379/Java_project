package Project_1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;



class Student {
    String name;
    int age;
    String course;
    String phone;
    String address;
    String email;

    public Student(String name, int age, String course, String phone, String address, String email) {
        this.name = name;
        this.age = age;
        this.course = course;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public String toString() {
        return "<html><b>" + name + "</b> | Age: " + age + " | Course: " + course + "<br>📞 " + phone + " | 📧 " + email + "<br>🏠 " + address + "</html>";
    }
}

public class StudentManagementGUI {
    private JFrame frame;
    private DefaultListModel<String> studentListModel;
    private JList<String> studentList;
    private ArrayList<Student> students = new ArrayList<>();

    public StudentManagementGUI() {
        frame = new JFrame("🎓 Student Management System");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Set main background color
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        // Title Label
        JLabel title = new JLabel("📚 Manage Student Information", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25, 25, 112));
        frame.add(title, BorderLayout.NORTH);

        // Student List Panel
        studentListModel = new DefaultListModel<>();
        studentList = new JList<>(studentListModel);
        studentList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentList.setSelectionBackground(new Color(173, 216, 230));
        frame.add(new JScrollPane(studentList), BorderLayout.CENTER);

        // Buttons Panel
        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 255));
        JButton addButton = createButton("➕ Add Student", new Color(60, 179, 113));
        JButton editButton = createButton("✏️ Edit Student", new Color(255, 165, 0));
        JButton deleteButton = createButton("🗑️ Delete", new Color(255, 99, 71));

        addButton.setToolTipText("Add a new student");
        editButton.setToolTipText("Edit selected student");
        deleteButton.setToolTipText("Delete selected student");

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        frame.add(panel, BorderLayout.SOUTH);

        // Button Actions
        addButton.addActionListener(e -> addStudent());
        editButton.addActionListener(e -> editStudent());
        deleteButton.addActionListener(e -> deleteStudent());

        frame.setVisible(true);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    private void addStudent() {
        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField emailField = new JTextField();

        Object[] fields = {
            "👤 Name:", nameField,
            "🎂 Age:", ageField,
            "📖 Course:", courseField,
            "📞 Phone:", phoneField,
            "🏠 Address:", addressField,
            
            "📧 Email:", emailField
        };

        int option = JOptionPane.showConfirmDialog(frame, fields, "➕ Add Student", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                int age = Integer.parseInt(ageField.getText().trim());
                String course = courseField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressField.getText().trim();
                String email = emailField.getText().trim();

                if (name.isEmpty() || course.isEmpty()) {
                    throw new IllegalArgumentException("Name and course cannot be empty.");
                }

                Student student = new Student(name, age, course, phone, address, email);
                students.add(student);
                studentListModel.addElement(student.toString());
                try {
                    Connection conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/studentinfodb", "root", "123456789"
                    );

                    String sql = "INSERT INTO student (name, age, course, phone, address, email) VALUES (?, ?, ?, ?, ?, ?)";
                    java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, name);
                    stmt.setInt(2, age);
                    stmt.setString(3, course);
                    stmt.setString(4, phone);
                    stmt.setString(5, address);
                    stmt.setString(6, email);

                    stmt.executeUpdate();
                    stmt.close();
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "❌ Database insert failed: " + ex.getMessage());
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter all fields correctly.");
            }
        }
    }

    private void editStudent() {
        int index = studentList.getSelectedIndex();
        if (index != -1) {
            Student student = students.get(index);

            JTextField nameField = new JTextField(student.name);
            JTextField ageField = new JTextField(String.valueOf(student.age));
            JTextField courseField = new JTextField(student.course);
            JTextField phoneField = new JTextField(student.phone);
            JTextField addressField = new JTextField(student.address);
            JTextField emailField = new JTextField(student.email);

            Object[] fields = {
                "👤 Name:", nameField,
                "🎂 Age:", ageField,
                "📖 Course:", courseField,
                "📞 Phone:", phoneField,
                "🏠 Address:", addressField,
                "📧 Email:", emailField
            };

            int option = JOptionPane.showConfirmDialog(frame, fields, "✏️ Edit Student", JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                try {
                    student.name = nameField.getText().trim();
                    student.age = Integer.parseInt(ageField.getText().trim());
                    student.course = courseField.getText().trim();
                    student.phone = phoneField.getText().trim();
                    student.address = addressField.getText().trim();
                    student.email = emailField.getText().trim();

                    studentListModel.set(index, student.toString());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter all fields correctly.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a student to edit.");
        }
    }

    private void deleteStudent() {
        int index = studentList.getSelectedIndex();
        if (index != -1) {
            int confirm = JOptionPane.showConfirmDialog(frame, "Are you sure you want to delete this student?", "Delete Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                students.remove(index);
                studentListModel.remove(index);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a student to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentManagementGUI::new);
    }
}
