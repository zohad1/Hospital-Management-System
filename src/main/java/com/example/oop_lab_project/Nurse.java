package com.example.oop_lab_project;

public class Nurse {
    private String username;
    private String department;
    private String gender;
    private String contactNumber;

    public Nurse(String username, String department, String gender, String contactNumber) {
        this.username = username;
        this.department = department;
        this.gender = gender;
        this.contactNumber = contactNumber;
    }

    public String getUsername() {
        return username;
    }

    public String getDepartment() {
        return department;
    }

    public String getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
}
