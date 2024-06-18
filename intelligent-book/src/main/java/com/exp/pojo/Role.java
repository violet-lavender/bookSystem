package com.exp.pojo;

public enum Role {
    ADMIN("admin"),
    USER("user"),
    ANALYST("analyst");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
