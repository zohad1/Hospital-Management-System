package com.example.oop_lab_project;

public class Appointment {
    private int id;
    private String name;
    private String gender;
    private String issue;
    private String medicalHistory;
    private String medications;
    private String room;
    private String time;
    private String status;
    private String ward;
    private int bedNumber;

    public Appointment(int id, String name, String gender, String issue, String medicalHistory, String medications, String room, String time, String status, String ward, int bedNumber) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.issue = issue;
        this.medicalHistory = medicalHistory;
        this.medications = medications;
        this.room = room;
        this.time = time;
        this.status = status;
        this.ward = ward;
        this.bedNumber = bedNumber;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public int getBedNumber() {
        return bedNumber;
    }

    public void setBedNumber(int bedNumber) {
        this.bedNumber = bedNumber;
    }
}
