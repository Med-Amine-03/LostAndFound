package com.lostandfound;

import com.lostandfound.config.DBConnection;
import com.lostandfound.controller.LoginController;
import com.lostandfound.dao.ItemDAO;
import com.lostandfound.dao.MatchDAO;
import com.lostandfound.dao.UserDAO;
import com.lostandfound.service.ItemService;
import com.lostandfound.service.MatchService;
import com.lostandfound.view.LoginView;

public class App {
    public static void main(String[] args) {
        DBConnection dbConnection = new DBConnection();
        
        UserDAO userDAO = new UserDAO(dbConnection.getConnection());
        ItemDAO itemDAO = new ItemDAO(dbConnection.getConnection());
        MatchDAO matchDAO = new MatchDAO(dbConnection.getConnection());
        
        ItemService itemService = new ItemService(itemDAO);
        MatchService matchService = new MatchService(matchDAO);
        
        LoginController loginController = new LoginController(userDAO);
        
        LoginView loginView = new LoginView(loginController);
        loginView.setVisible(true);
    }
}
