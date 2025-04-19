package main;

import model.Patient;
import model.Physiotherapist;
import model.Treatment;

import java.time.LocalDateTime;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        BookingSystem system = new BookingSystem();

        // Sample Physiotherapists
        Physiotherapist p1 = new Physiotherapist(1, "Dr. John Smith", "123 Main St", "1234567890", Arrays.asList("Physiotherapy", "Rehabilitation"));
        Physiotherapist p2 = new Physiotherapist(2, "Dr. Sarah Johnson", "456 Elm St", "0987654321", Arrays.asList("Osteopathy"));
        system.addPhysiotherapist(p1);
        system.addPhysiotherapist(p2);

        // Sample Patients
        Patient pat1 = new Patient(1, "Alice Brown", "789 Maple St", "1112223333");
        Patient pat2 = new Patient(2, "Bob White", "321 Oak St", "4445556666");
        system.addPatient(pat1);
        system.addPatient(pat2);

        // Sample Treatments over 4 weeks
        Treatment t1 = new Treatment("Massage", LocalDateTime.of(2025, 5, 1, 10, 0), p1);
        Treatment t2 = new Treatment("Rehabilitation", LocalDateTime.of(2025, 5, 3, 12, 0), p1);
        Treatment t3 = new Treatment("Acupuncture", LocalDateTime.of(2025, 5, 5, 14, 0), p2);
        p1.addTreatment(t1);
        p1.addTreatment(t2);
        p2.addTreatment(t3);

        // Simulate Appointments
        system.bookAppointment(pat1, t1); // Alice books Massage
        system.bookAppointment(pat2, t3); // Bob books Acupuncture

        // Attendance
        system.attendAppointment(t1);
        system.attendAppointment(t3);

        // Cancel & Change Appointment Example
        system.changeAppointment(pat1, t1, t2); // Alice changes from Massage to Rehab

        // Generate Report
        system.generateReport();
    }
}
