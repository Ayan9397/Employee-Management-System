package com.ems.model;

/**
 * Model representing a Department entity.
 */
public class Department {
    private int departmentId;
    private String departmentCode;
    private String departmentName;
    private String location;
    private double budget;

    public Department() {
    }

    public Department(int departmentId, String departmentCode, String departmentName, String location, double budget) {
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.location = location;
        this.budget = budget;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - Location: %s | Budget: $%,.2f",
                departmentId, departmentName, departmentCode, location, budget);
    }
}

