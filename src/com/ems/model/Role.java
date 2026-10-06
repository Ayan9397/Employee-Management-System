package com.ems.model;

/**
 * Model representing a job Role in the organization.
 */
public class Role {
    private int roleId;
    private String roleTitle;
    private String description;

    public Role() {
    }

    public Role(int roleId, String roleTitle, String description) {
        this.roleId = roleId;
        this.roleTitle = roleTitle;
        this.description = description;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s: %s", roleId, roleTitle, description);
    }
}

