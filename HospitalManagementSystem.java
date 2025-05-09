import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/HospitalDB";
    private static final String USER = "shivam.tomar.cs.2022@mitmeerut.ac.in";
    private static final String PASSWORD = "@Shivam123";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}


class Patient {
    private String id;
    private String name;
    private int age;
    private String contact;

    public Patient(String id, String name, int age, String contact) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.contact = contact;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getContact() { return contact; }
}


class PatientDAO {
    public void addPatient(Patient patient) throws SQLException {
        String query = "INSERT INTO Patients (id, name, age, contact) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, patient.getId());
            pstmt.setString(2, patient.getName());
            pstmt.setInt(3, patient.getAge());
            pstmt.setString(4, patient.getContact());
            pstmt.executeUpdate();
        }
    }

    public List<Patient> getAllPatients() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM Patients";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Patient patient = new Patient(rs.getString("id"), rs.getString("name"),
                        rs.getInt("age"), rs.getString("contact"));
                patients.add(patient);
            }
        }
        return patients;
    }

    
}


public class HospitalManagementSystem extends JFrame {
    private JTextField idField, nameField, ageField, contactField;
    private PatientDAO patientDAO;

    public HospitalManagementSystem() {
        patientDAO = new PatientDAO();

        setTitle("Patient Registration");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2));

        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);

        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        ageField = new JTextField();
        panel.add(ageField);

        panel.add(new JLabel("Contact:"));
        contactField = new JTextField();
        panel.add(contactField);

        JButton addButton = new JButton("Add Patient");
        panel.add(addButton);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatient();
            }
        });

        JButton listButton = new JButton("List Patients");
        panel.add(listButton);
        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listPatients();
            }
        });

        add(panel);
    }

    private void addPatient() {
        String id = idField.getText();
        String name = nameField.getText();
        int age = Integer.parseInt(ageField.getText());
        String contact = contactField.getText();

        Patient patient = new Patient(id, name, age, contact);
        try {
            patientDAO.addPatient(patient);
            JOptionPane.showMessageDialog(this, "Patient added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding patient.");
        }
    }

    private void listPatients() {
        try {
            List<Patient> patients = patientDAO.getAllPatients();
            StringBuilder sb = new StringBuilder();
            for (Patient patient : patients) {
                sb.append(patient.getId()).append(", ")
                  .append(patient.getName()).append(", ")
                  .append(patient.getAge()).append(", ")
                  .append(patient.getContact()).append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error retrieving patients.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HospitalManagementSystem().setVisible(true);
            }
        });
    }
}

