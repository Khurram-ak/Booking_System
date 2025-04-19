package main;

import model.Patient;
import model.Physiotherapist;
import model.Treatment;

import java.util.*;
import java.util.stream.Collectors;

public class BookingSystem {

    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;

    public BookingSystem() {
        physiotherapists = new ArrayList<>();
        patients = new ArrayList<>();
    }

    public void addPhysiotherapist(Physiotherapist physio) {
        physiotherapists.add(physio);
    }

    public void removePhysiotherapist(int physioId) {
        physiotherapists.removeIf(physio -> physio.getId() == physioId);
    }

    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(int patientId) {
        patients.removeIf(patient -> patient.getId() == patientId);
    }

    public Optional<Patient> getPatientById(int id) {
        return patients.stream().filter(p -> p.getId() == id).findFirst();
    }

    public List<Physiotherapist> searchPhysiotherapistsByExpertise(String expertise) {
        return physiotherapists.stream()
                .filter(p -> p.getAreasOfExpertise().contains(expertise))
                .collect(Collectors.toList());
    }

    public Optional<Physiotherapist> searchPhysiotherapistByName(String name) {
        return physiotherapists.stream()
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Treatment> getAvailableTreatments(Physiotherapist physio) {
        return physio.getTimetable().stream()
                .filter(t -> "Available".equalsIgnoreCase(t.getStatus()))
                .collect(Collectors.toList());
    }

    public boolean hasTimeConflict(Patient patient, Treatment newTreatment) {
        for (Physiotherapist physio : physiotherapists) {
            for (Treatment t : physio.getTimetable()) {
                if (t.getPatient() != null
                        && t.getPatient().getId() == patient.getId()
                        && t.getDateTime().equals(newTreatment.getDateTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean bookAppointment(Patient patient, Treatment treatment) {
        if (!"Available".equalsIgnoreCase(treatment.getStatus())) {
            System.out.println("Selected treatment is not available.");
            return false;
        }

        if (hasTimeConflict(patient, treatment)) {
            System.out.println("Time conflict! You already have an appointment at this time.");
            return false;
        }

        treatment.setPatient(patient);
        treatment.setStatus("Booked");
        System.out.println("Appointment booked for " + patient.getName() + " with " + treatment.getPhysiotherapist().getName());
        return true;
    }

    public boolean cancelAppointment(Treatment treatment) {
        if ("Booked".equalsIgnoreCase(treatment.getStatus())) {
            treatment.setPatient(null);
            treatment.setStatus("Cancelled");
            System.out.println("Appointment cancelled.");
            return true;
        }
        System.out.println("Appointment is not booked, cannot cancel.");
        return false;
    }

    public boolean changeAppointment(Patient patient, Treatment oldTreatment, Treatment newTreatment) {
        if (!cancelAppointment(oldTreatment)) {
            return false;
        }
        return bookAppointment(patient, newTreatment);
    }

    public boolean attendAppointment(Treatment treatment) {
        if ("Booked".equalsIgnoreCase(treatment.getStatus())) {
            treatment.setStatus("Attended");
            System.out.println("Appointment marked as attended.");
            return true;
        }
        System.out.println("Cannot attend an unbooked appointment.");
        return false;
    }

    public void generateReport() {
        // 1. List all treatment appointments for each physiotherapist
        System.out.println("\n--- All Treatment Appointments ---");

        // Iterate through all physiotherapists
        for (Physiotherapist physio : physiotherapists) {
            System.out.println("\nPhysiotherapist: " + physio.getName());

            // For each physiotherapist, list the treatments in their timetable
            for (Treatment treatment : physio.getTimetable()) {
                System.out.println("Treatment: " + treatment.getName()
                        + ", Patient: " + (treatment.getPatient() != null ? treatment.getPatient().getName() : "None")
                        + ", Time: " + treatment.getDateTime()
                        + ", Status: " + treatment.getStatus());
            }
        }

        // 2. List physiotherapists in descending order of number of attended appointments
        System.out.println("\n--- Physiotherapists by Attended Appointments ---");

        // Map to store the count of attended appointments for each physiotherapist
        Map<Physiotherapist, Long> attendedCountMap = new HashMap<>();

        // Iterate through all physiotherapists and count the attended appointments
        for (Physiotherapist physio : physiotherapists) {
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
            System.out.println("Physiotherapist: " + physio.getName() + " - Attended Appointments: " + attendedCountMap.get(physio));
            for (Treatment treatment : physio.getTimetable()) {
                if ("Attended".equalsIgnoreCase(treatment.getStatus())) {
                    System.out.println("  Treatment: " + treatment.getName()
                            + ", Patient: " + (treatment.getPatient() != null ? treatment.getPatient().getName() : "None")
                            + ", Time: " + treatment.getDateTime());
                }
            }
        }
    }

    public List<Patient> getAllPatients() {
        return patients;
    }

    public List<Physiotherapist> getAllPhysiotherapists() {
        return physiotherapists;
    }

}
