package com.lostandfound.model;

public class NormalUser extends User {
    public NormalUser(int userId, String name, String email, String password, String role, String profileImage) {
        super(name, email, password, role, profileImage);
        this.setUserId(userId); 
    }

    public NormalUser(String name, String email, String password, String role, String profileImage) {
        super(name, email, password, role, profileImage);
    }
}
