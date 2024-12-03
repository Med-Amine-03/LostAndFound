package com.lostandfound;

import com.lostandfound.config.DBConnection;
import com.lostandfound.controller.LoginController;
import com.lostandfound.controller.RegisterController;
import com.lostandfound.dao.UserDAO;
import com.lostandfound.view.LoginView;

public class App {
    public static void main(String[] args) {
        // Setup the database connection
        DBConnection dbConnection = new DBConnection();
        
        // Create the DAO and Controllers
        UserDAO userDAO = new UserDAO(dbConnection.getConnection());
        LoginController loginController = new LoginController(userDAO);
        RegisterController registerController = new RegisterController(userDAO);

        // Launch the Login View
        LoginView loginView = new LoginView(loginController);
        loginView.setVisible(true);
    }
}
