package com.lostandfound.controller;

import java.sql.Connection;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.model.Admin;
import com.lostandfound.model.NormalUser;
import com.lostandfound.model.User;



public class RegisterController {

    private Connection connection;
    private UserDAO userDAO;

    public RegisterController(Connection connection){
        this.connection = connection;
        this.userDAO = new UserDAO(connection);
    }

    public boolean handelRegister(String name, String email, String password, String role, String profileImage){
        if (userDAO.isEmailAlreadyRegistered(email)) {
            System.out.println("This email is already registered.");
            return false;
        }

        User user= null;
        if (role.equalsIgnoreCase("Admin")) {
            user = new Admin(name, email, password, role, profileImage);
        }
        else if (role.equalsIgnoreCase("NormalUser")) {
            user = new NormalUser(name, email, password, role, profileImage);
        }
        else {
            return false;
        }
        return userDAO.registerUser(user);
    }
}