package model;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist {

    private int id;
    private String name;
    private String address;
    private String phone;
    private List<String> areasOfExpertise;
    private List<Treatment> timetable;

    public Physiotherapist(int id, String name, String address, String phone, List<String> areasOfExpertise) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.areasOfExpertise = areasOfExpertise;
        this.timetable = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getAreasOfExpertise() {
        return areasOfExpertise;
    }

    public List<Treatment> getTimetable() {
        return timetable;
    }

    public void addTreatment(Treatment treatment) {
        timetable.add(treatment);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Physiotherapist: " + name;
    }
  
}
