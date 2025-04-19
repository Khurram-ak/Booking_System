package model;

import java.time.LocalDateTime;

public class Treatment {

    private String name;
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private String status; // Booked, Cancelled, Attended

    public Treatment(String name, LocalDateTime dateTime, Physiotherapist physio) {
        this.name = name;
        this.dateTime = dateTime;
        this.physiotherapist = physio;
        this.status = "Available";
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Physiotherapist getPhysiotherapist() {
        return physiotherapist;
    }

    public Patient getPatient() {
        return patient;
    }

    public String getStatus() {
        return status;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setPhysiotherapist(Physiotherapist physiotherapist) {
        this.physiotherapist = physiotherapist;
    }

    @Override
    public String toString() {
        return "Treatment: " + name
                + ", Time: " + dateTime
                + ", Physio: " + (physiotherapist != null ? physiotherapist.getName() : "None")
                + ", Patient: " + (patient != null ? patient.getName() : "None")
                + ", Status: " + status;
    }

}
