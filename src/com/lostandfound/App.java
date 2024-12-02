package com.lostandfound;

import com.lostandfound.config.DBConnection;
import com.lostandfound.controller.LoginController;
import com.lostandfound.dao.UserDAO;

import java.sql.Connection;

public class App {
    public static void main(String[] args) {
        Connection connection = DBConnection.getConnection();
        UserDAO userDAO = new UserDAO(connection);
        LoginController loginController = new LoginController(userDAO);
        loginController.Login("amine@gmail.com", "password123");
        


    }
}
