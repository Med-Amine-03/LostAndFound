package com.lostandfound.controller;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.model.Admin;
import com.lostandfound.model.NormalUser;
import com.lostandfound.model.User;

public class LoginController {

    private UserDAO userDAO;

    public LoginController(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public void Login(String email, String password){
        User user =userDAO.authenticate(email, password);
        if(user != null){
            System.out.println("Login successful!");
            if (user instanceof Admin) {
                System.out.println("Admin logged in");
                
            }else if (user instanceof NormalUser) {
                System.out.println("Normal user logged in");
                
            }


        }
        else{
            System.out.println("Invalid email or password");
        }
    }
}