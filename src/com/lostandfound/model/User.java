package com.lostandfound.model;

public abstract class User {
    private int userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String profileImage;

    public User(String name, String email, String password, String role, String profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profileImage = profileImage;
    }

    public User() {}

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isAdmin() {
        return "Admin".equalsIgnoreCase(this.role);
    }

    public boolean isNormalUser() {
        return "NormalUser".equalsIgnoreCase(this.role);
    }
}
