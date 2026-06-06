import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class StudentGradeSystem {
    private final List<Student> students = new ArrayList<>();

    private final JTextField idField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField scoreField = new JTextField();
    private final JTextArea displayArea = new JTextArea();

    public StudentGradeSystem() {
        JFrame frame = new JFrame("Student Grade Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(720, 450);
        frame.setLocationRelativeTo(null);

        JLabel title = new JLabel(
                "Student Grade Management System",
                SwingConstants.CENTER
        );
        title.setFont(new Font("SansSerif", Font.BOLD, 20));

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.add(new JLabel("Student ID:"));
        inputPanel.add(idField);
        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Score:"));
        inputPanel.add(scoreField);

        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JButton addButton = new JButton("Add Student");
        JButton averageButton = new JButton("Calculate Average");
        JButton clearButton = new JButton("Clear Inputs");

        addButton.addActionListener(event -> addStudent(frame));
        averageButton.addActionListener(event -> showAverage(frame));
        clearButton.addActionListener(event -> clearInputs());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(averageButton);
        buttonPanel.add(clearButton);

        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        leftPanel.add(inputPanel, BorderLayout.NORTH);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(title, BorderLayout.NORTH);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.add(new JScrollPane(displayArea), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void addStudent(JFrame frame) {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String scoreText = scoreField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || scoreText.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Please complete all fields.",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            double score = Double.parseDouble(scoreText);

            if (score < 0 || score > 100) {
                throw new IllegalArgumentException();
            }

            Student student = new Student(id, name, score);
            students.add(student);

            displayArea.append(student + System.lineSeparator());
            clearInputs();
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Score must be a number.",
                    "Invalid Score",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (IllegalArgumentException exception) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Score must be between 0 and 100.",
                    "Invalid Score",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showAverage(JFrame frame) {
        if (students.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame,
                    "No student records are available."
            );
            return;
        }

        double total = 0;

        for (Student student : students) {
            total += student.score;
        }

        double average = total / students.size();

        JOptionPane.showMessageDialog(
                frame,
                String.format("Average score: %.2f", average)
        );
    }

    private void clearInputs() {
        idField.setText("");
        nameField.setText("");
        scoreField.setText("");
        idField.requestFocus();
    }

    private static class Student {
        private final String id;
        private final String name;
        private final double score;

        private Student(String id, String name, double score) {
            this.id = id;
            this.name = name;
            this.score = score;
        }

        @Override
        public String toString() {
            return String.format(
                    "ID: %-10s Name: %-20s Score: %.2f",
                    id,
                    name,
                    score
            );
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentGradeSystem::new);
    }
}
