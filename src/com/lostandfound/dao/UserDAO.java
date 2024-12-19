package com.lostandfound.dao;

import com.lostandfound.model.Admin;
import com.lostandfound.model.NormalUser;
import com.lostandfound.model.User;
import com.lostandfound.utils.SessionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                        user = new Admin(userId, name, email, password, role, profileImage);
                    } else {
                        user = new NormalUser(userId, name, email, password, role, profileImage);
                    }
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
    public boolean updateUserProfile(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, email = ?, password = ?, profile_image = ? WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getProfileImage());
            statement.setInt(5, user.getUserId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                SessionManager.setCurrentUser(user);
                return true;
            }
            return false;
        }
    }
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM users WHERE user_id = ?";
        User user = null;
    
        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
    
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("user_id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");
                String profileImage = resultSet.getString("profile_image");
                String role = resultSet.getString("role");
    
                if ("NormalUser".equals(role)) {
                    user = new NormalUser(id, name, email, password, role, profileImage);
                } else if ("Admin".equals(role)) {
                    user = new Admin(id, name, email, password, role, profileImage);
                }
            }
        }
        return user;
    }
    
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new NormalUser(
                    rs.getInt("user_id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("profile_image")
                );
                users.add(user);
            }
        }
        return users;
    }

    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
