package gui;

import main.BookingSystem;
import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.swing.table.DefaultTableModel;

public class BookingSystemGUI extends JFrame {

    private final BookingSystem system;
    private JTextArea outputArea;

    public BookingSystemGUI() {
        system = new BookingSystem();
        initSampleData();
        initGUI();
    }

    private void initGUI() {
        setTitle("Boost Physio Clinic Booking System");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel and layout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Text area for output
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        // Button panel with actions
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JButton bookBtn = new JButton("Book Appointment");
        JButton attendBtn = new JButton("Mark Attended");
        JButton cancelBtn = new JButton("Cancel Appointment");
        JButton changeBtn = new JButton("Change Appointment");
        JButton reportBtn = new JButton("Generate Report");
        JButton exitBtn = new JButton("Exit");

        // Add buttons to panel
        buttonPanel.add(bookBtn);
        buttonPanel.add(attendBtn);
        buttonPanel.add(cancelBtn);
        buttonPanel.add(changeBtn);
        buttonPanel.add(reportBtn);
        buttonPanel.add(exitBtn);

        // Add components to main panel
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to the frame
        add(mainPanel);

        // Action listeners for each button
        bookBtn.addActionListener(this::handleBook);
        attendBtn.addActionListener(this::handleAttend);
        cancelBtn.addActionListener(this::handleCancel);
        changeBtn.addActionListener(this::handleChange);

        // Update the report generation to output into the text area
        reportBtn.addActionListener(this::generateAndDisplayReport);

        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private void generateAndDisplayReport(ActionEvent e) {
        // 1. List all treatment appointments for each physiotherapist
        StringBuilder report = new StringBuilder("\n--- All Treatment Appointments ---\n");

        // Iterate through all physiotherapists
        for (Physiotherapist physio : system.getAllPhysiotherapists()) {
            report.append("\nPhysiotherapist: ").append(physio.getName()).append("\n");

            // For each physiotherapist, list the treatments in their timetable
            for (Treatment treatment : physio.getTimetable()) {
                report.append("Treatment: ").append(treatment.getName())
                        .append(", Patient: ").append(treatment.getPatient() != null ? treatment.getPatient().getName() : "None")
                        .append(", Time: ").append(treatment.getDateTime())
                        .append(", Status: ").append(treatment.getStatus())
                        .append("\n");
            }
        }

        // 2. List physiotherapists in descending order of the number of attended appointments
        report.append("\n--- Physiotherapists by Attended Appointments ---\n");

        // Map to store the count of attended appointments for each physiotherapist
        Map<Physiotherapist, Long> attendedCountMap = new HashMap<>();

        // Iterate through all physiotherapists and count the attended appointments
        for (Physiotherapist physio : system.getAllPhysiotherapists()) {
            long attendedCount = physio.getTimetable().stream()
                    .filter(t -> "Attended".equalsIgnoreCase(t.getStatus()))
                    .count();
            attendedCountMap.put(physio, attendedCount);
        }

        // Sort physiotherapists by attended count in descending order
        List<Physiotherapist> sortedPhysios = attendedCountMap.entrySet().stream()
                .sorted(Map.Entry.<Physiotherapist, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Print sorted physiotherapists and their attended appointments
        for (Physiotherapist physio : sortedPhysios) {
            report.append("Physiotherapist: ").append(physio.getName())
                    .append(" - Attended Appointments: ").append(attendedCountMap.get(physio))
                    .append("\n");
            for (Treatment treatment : physio.getTimetable()) {
                if ("Attended".equalsIgnoreCase(treatment.getStatus())) {
                    report.append("  Treatment: ").append(treatment.getName())
                            .append(", Patient: ").append(treatment.getPatient() != null ? treatment.getPatient().getName() : "None")
                            .append(", Time: ").append(treatment.getDateTime())
                            .append("\n");
                }
            }
        }

        // Display the report in the output area
        outputArea.setText(report.toString());
    }

    private void initSampleData() {
        // Initialize physiotherapists
        Physiotherapist p1 = new Physiotherapist(1, "Dr. John Smith", "123 Main St", "1234567890", Arrays.asList("Physiotherapy", "Rehabilitation"));
        Physiotherapist p2 = new Physiotherapist(2, "Dr. Sarah Johnson", "456 Elm St", "0987654321", Arrays.asList("Osteopathy"));
        Physiotherapist p3 = new Physiotherapist(3, "Dr. Michael Davis", "789 Pine St", "1122334455", Arrays.asList("Chiropractic", "Massage"));
        Physiotherapist p4 = new Physiotherapist(4, "Dr. Emma Wilson", "101 Birch St", "2233445566", Arrays.asList("Physiotherapy", "Sports Medicine"));
        Physiotherapist p5 = new Physiotherapist(5, "Dr. Olivia Brown", "202 Cedar St", "3344556677", Arrays.asList("Rehabilitation", "Acupuncture"));

        system.addPhysiotherapist(p1);
        system.addPhysiotherapist(p2);
        system.addPhysiotherapist(p3);
        system.addPhysiotherapist(p4);
        system.addPhysiotherapist(p5);

        // Initialize patients
        Patient pat1 = new Patient(1, "Alice Brown", "789 Maple St", "1112223333");
        Patient pat2 = new Patient(2, "Bob White", "321 Oak St", "4445556666");
        Patient pat3 = new Patient(3, "Charlie Green", "654 Pine St", "5556667777");
        Patient pat4 = new Patient(4, "David Black", "321 Birch St", "6667778888");
        Patient pat5 = new Patient(5, "Eva Blue", "987 Elm St", "7778889999");
        Patient pat6 = new Patient(6, "Frank Red", "213 Maple St", "8889990000");
        Patient pat7 = new Patient(7, "Grace Yellow", "432 Oak St", "9990001111");
        Patient pat8 = new Patient(8, "Helen Purple", "654 Cedar St", "1001112222");
        Patient pat9 = new Patient(9, "Ivan White", "987 Birch St", "1112223333");
        Patient pat10 = new Patient(10, "Jack Grey", "123 Oak St", "2223334444");
        Patient pat11 = new Patient(11, "Lily Violet", "234 Pine St", "3334445555");
        Patient pat12 = new Patient(12, "Mona Pink", "876 Maple St", "4445556666");

        system.addPatient(pat1);
        system.addPatient(pat2);
        system.addPatient(pat3);
        system.addPatient(pat4);
        system.addPatient(pat5);
        system.addPatient(pat6);
        system.addPatient(pat7);
        system.addPatient(pat8);
        system.addPatient(pat9);
        system.addPatient(pat10);
        system.addPatient(pat11);
        system.addPatient(pat12);

        // Initialize treatments (4-week timetable)
        LocalDateTime startDate = LocalDateTime.of(2025, 5, 1, 9, 0);
        LocalDateTime treatmentDate = startDate;

        // Physiotherapist p1
        p1.addTreatment(new Treatment("Massage", treatmentDate, p1));
        treatmentDate = treatmentDate.plusDays(1); // next day
        p1.addTreatment(new Treatment("Rehabilitation", treatmentDate, p1));
        treatmentDate = treatmentDate.plusDays(2); // next day
        p1.addTreatment(new Treatment("Acupuncture", treatmentDate, p1));
        treatmentDate = treatmentDate.plusDays(3); // next day

        // Physiotherapist p2
        treatmentDate = startDate.plusDays(4); // new week
        p2.addTreatment(new Treatment("Osteopathy", treatmentDate, p2));
        treatmentDate = treatmentDate.plusDays(2); // next session
        p2.addTreatment(new Treatment("Chiropractic", treatmentDate, p2));
        treatmentDate = treatmentDate.plusDays(3); // next session

        // Physiotherapist p3
        treatmentDate = startDate.plusDays(1); // new week
        p3.addTreatment(new Treatment("Massage", treatmentDate, p3));
        treatmentDate = treatmentDate.plusDays(2); // next session
        p3.addTreatment(new Treatment("Rehabilitation", treatmentDate, p3));

        // Physiotherapist p4
        treatmentDate = startDate.plusDays(5); // next week
        p4.addTreatment(new Treatment("Physiotherapy", treatmentDate, p4));
        treatmentDate = treatmentDate.plusDays(3); // next session
        p4.addTreatment(new Treatment("Sports Medicine", treatmentDate, p4));

        // Physiotherapist p5
        treatmentDate = startDate.plusDays(6); // new week
        p5.addTreatment(new Treatment("Acupuncture", treatmentDate, p5));
        treatmentDate = treatmentDate.plusDays(2); // next session
        p5.addTreatment(new Treatment("Rehabilitation", treatmentDate, p5));
    }

    private void handleBook(ActionEvent e) {
        // Show table of patients and physiotherapists to select from
        JTable patientTable = new JTable(createPatientTableModel());
        JTable physioTable = new JTable(createPhysiotherapistTableModel());

        int patientChoice = JOptionPane.showOptionDialog(this, new JScrollPane(patientTable), "Select Patient", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
        int physioChoice = JOptionPane.showOptionDialog(this, new JScrollPane(physioTable), "Select Physiotherapist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (patientChoice == JOptionPane.OK_OPTION && physioChoice == JOptionPane.OK_OPTION) {
            int patientId = (int) patientTable.getValueAt(patientTable.getSelectedRow(), 0);
            String physioName = (String) physioTable.getValueAt(physioTable.getSelectedRow(), 1);

            Optional<Patient> patient = system.getPatientById(patientId);
            Optional<Physiotherapist> physio = system.searchPhysiotherapistByName(physioName);

            if (patient.isPresent() && physio.isPresent()) {
                List<Treatment> available = system.getAvailableTreatments(physio.get());
                if (available.isEmpty()) {
                    output("No available treatments.");
                    return;
                }
                Treatment selected = (Treatment) JOptionPane.showInputDialog(this, "Select Treatment:", "Treatment Selection",
                        JOptionPane.PLAIN_MESSAGE, null, available.toArray(), available.get(0));

                if (selected != null) {
                    boolean booked = system.bookAppointment(patient.get(), selected);
                    if (booked) {
                        output("Appointment booked successfully.");
                    } else {
                        output("Booking failed. Possibly already booked.");
                    }
                }
            } else {
                output("Invalid patient or physiotherapist.");
            }
        }
    }

    private DefaultTableModel createPatientTableModel() {
        List<Patient> patients = system.getAllPatients();
        String[] columnNames = {"ID", "Name", "Phone"};
        Object[][] data = new Object[patients.size()][3];
        for (int i = 0; i < patients.size(); i++) {
            data[i][0] = patients.get(i).getId();
            data[i][1] = patients.get(i).getName();
            data[i][2] = patients.get(i).getPhone();
        }
        return new DefaultTableModel(data, columnNames);
    }

    private DefaultTableModel createPhysiotherapistTableModel() {
        List<Physiotherapist> physios = system.getAllPhysiotherapists();
        String[] columnNames = {"ID", "Name", "Specializations"};
        Object[][] data = new Object[physios.size()][3];
        for (int i = 0; i < physios.size(); i++) {
            data[i][0] = physios.get(i).getId();
            data[i][1] = physios.get(i).getName();
            data[i][2] = String.join(", ", physios.get(i).getAreasOfExpertise());
        }
        return new DefaultTableModel(data, columnNames);
    }

    private void handleAttend(ActionEvent e) {
        // Similar to booking but for marking attendance
        JTable physioTable = new JTable(createPhysiotherapistTableModel());
        int physioChoice = JOptionPane.showOptionDialog(this, new JScrollPane(physioTable), "Select Physiotherapist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);

        if (physioChoice == JOptionPane.OK_OPTION) {
            String physioName = (String) physioTable.getValueAt(physioTable.getSelectedRow(), 1);
            Optional<Physiotherapist> physio = system.searchPhysiotherapistByName(physioName);
            if (physio.isPresent()) {
                List<Treatment> booked = physio.get().getTimetable().stream()
                        .filter(t -> "Booked".equalsIgnoreCase(t.getStatus()))
                        .collect(Collectors.toList());

                if (booked.isEmpty()) {
                    output("No booked appointments found.");
                    return;
                }
                Treatment selected = (Treatment) JOptionPane.showInputDialog(this, "Select Appointment to Mark Attended:", "Attend",
                        JOptionPane.PLAIN_MESSAGE, null, booked.toArray(), booked.get(0));
                if (selected != null) {
                    system.attendAppointment(selected);
                    output("Appointment marked as attended.");
                }
            }
        }
    }

    private void handleCancel(ActionEvent e) {
        // Cancel appointments like the attend functionality
        handleAttend(e);
    }

    private void handleChange(ActionEvent e) {
        // Change appointments like the attend functionality
        handleAttend(e);
    }

    private void output(String message) {
        outputArea.append(message + "\n");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BookingSystemGUI::new);
    }
}
