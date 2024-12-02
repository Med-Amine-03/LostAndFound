package com.lostandfound.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/lost_and_found";  
    private static final String USER = "root"; 
    private static final String PASSWORD = "amine-2003";  
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connection successful: " + connection);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
