package com.lostandfound.dao;

import com.lostandfound.model.Admin;
import com.lostandfound.model.NormalUser;
import com.lostandfound.model.User;
import java.sql.*;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User authenticate(String email, String password) {
        User user = null;
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);  

            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    int userId = resultSet.getInt("user_id");
                    String name = resultSet.getString("name");
                    String role = resultSet.getString("role");
                    String profileImage = resultSet.getString("profile_image");

                    if ("Admin".equals(role)) {
                        user = new Admin(name, email, password, role, profileImage);
                    } else if ("NormalUser".equals(role)) {
                        user = new NormalUser(name, email, password, role, profileImage);
                    }
                    user.setUserId(userId);
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in authenticate: " + ex.getMessage());
        }
        return user;
    }

    public boolean registerUser(User user) {
        String query = "INSERT INTO users (name, email, password, role, profile_image) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());  
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getProfileImage());  

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));  
                        return true;
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in registerUser: " + ex.getMessage());
        }
        return false;
    }

    public boolean isEmailAlreadyRegistered(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println("Error in isEmailAlreadyRegistered: " + ex.getMessage());
        }
        return false;
    }
}
