package com.lostandfound.controller;

import com.lostandfound.dao.UserDAO;
import com.lostandfound.model.User;
import com.lostandfound.utils.SessionManager;

public class LoginController {
    private UserDAO userDAO;

    public LoginController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean login(String email, String password) {
        User user = userDAO.authenticate(email, password);

        if (user != null) {
            SessionManager.setCurrentUser(user);  
            return true;  
        } else {
            return false;  
        }
    }

    public void logout() {
        SessionManager.clearSession();  
    }
}
