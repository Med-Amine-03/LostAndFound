package com.lostandfound.model;

public class Admin extends User {
    public Admin(int userId, String name, String email, String password, String role, String profileImage) {
        super(name, email, password, role, profileImage);
        this.setUserId(userId);
    }

    public Admin(String name, String email, String password, String role, String profileImage) {
        super(name, email, password, role, profileImage);
    }
}
