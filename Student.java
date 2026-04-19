import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;


abstract class Person {
    protected String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() { return name; }


    public abstract String getRole();
}

class StudentData extends Person {
    private int grade;
    private int attendance;

    public StudentData(String name, int grade, int attendance) {
        super(name); //
        this.grade = grade;
        this.attendance = attendance;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public int getGrade() { return grade; }
    public int getAttendance() { return attendance; }
}



public class Student extends JFrame {
    private ArrayList<StudentData> studentList = new ArrayList<>();
    private DefaultTableModel tableModel;
    private JTextField nameField, gradeField, attendanceField;

    public Student() {

        setTitle("Student Management System");
        setSize(600, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());


        String[] columns = {"Name", "Grade", "Attendance (%)", "Role"};
        tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);


        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        inputPanel.add(new JLabel(" Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel(" Grade:"));
        gradeField = new JTextField();
        inputPanel.add(gradeField);

        inputPanel.add(new JLabel(" Attendance:"));
        attendanceField = new JTextField();
        inputPanel.add(attendanceField);


        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Selected");
        JButton saveButton = new JButton("Save");
        JButton loadButton = new JButton("Load");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);


        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int grade = Integer.parseInt(gradeField.getText());
                int attend = Integer.parseInt(attendanceField.getText());

                if (name.isEmpty() || grade < 0 || grade > 100) throw new Exception();

                studentList.add(new StudentData(name, grade, attend));
                updateTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid data (0-100)!");
            }
        });


        deleteButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                studentList.remove(row);
                updateTable();
            }
        });


        saveButton.addActionListener(e -> saveToFile());
        loadButton.addActionListener(e -> loadFromFile());

        setVisible(true);
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        for (StudentData s : studentList) {
            tableModel.addRow(new Object[]{s.getName(), s.getGrade(), s.getAttendance(), s.getRole()});
        }
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("data.csv"))) {
            for (StudentData s : studentList) {
                pw.println(s.getName() + "," + s.getGrade() + "," + s.getAttendance());
            }
            JOptionPane.showMessageDialog(this, "Saved!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error!");
        }
    }

    private void loadFromFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))) {
            studentList.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                studentList.add(new StudentData(p[0], Integer.parseInt(p[1]), Integer.parseInt(p[2])));
            }
            updateTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "File not found.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Student::new);
    }
}