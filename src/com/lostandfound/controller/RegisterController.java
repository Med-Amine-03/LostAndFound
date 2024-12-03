package com.lostandfound.controller;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.model.Admin;
import com.lostandfound.model.NormalUser;
import com.lostandfound.model.User;
import com.lostandfound.utils.SessionManager;

public class RegisterController {
    private UserDAO userDAO;

    public RegisterController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean handleRegister(String name, String email, String password, String role, String profileImage) {
        if (userDAO.isEmailAlreadyRegistered(email)) {
            System.out.println("This email is already registered.");
            return false;
        }

        User user = null;
        if ("Admin".equalsIgnoreCase(role)) {
            user = new Admin(name, email, password, role, profileImage);
        } else if ("NormalUser".equalsIgnoreCase(role)) {
            user = new NormalUser(name, email, password, role, profileImage);
        } else {
            System.out.println("Invalid role specified.");
            return false;
        }

        boolean success = userDAO.registerUser(user);
        if (success) {
            SessionManager.setCurrentUser(user);
            return true;
        } else {
            System.out.println("Failed to register user.");
            return false;
        }
    }
}
