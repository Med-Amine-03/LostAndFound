package com.lostandfound.service;

import com.lostandfound.dao.ItemDAO;
import com.lostandfound.dao.MatchDAO;
import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.utils.SessionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class NormalUserService {

    private final Connection connection;
    private final ItemDAO itemDAO;
    private final MatchDAO matchDAO;

    public NormalUserService(Connection connection) {
        this.connection = connection;
        this.itemDAO = new ItemDAO(connection);
        this.matchDAO = new MatchDAO(connection);
    }

    public boolean reportItem(Item item) {
        try {
            itemDAO.createItem(item);
            return true;
        } catch (SQLException e) {
            System.out.println("Error reporting item: " + e.getMessage());
            return false;
        }
    }

    public List<Item> viewMyItems() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            try {
                return itemDAO.getItemsByOwnerId(currentUser.getUserId());
            } catch (SQLException e) {
                System.out.println("Error retrieving items: " + e.getMessage());
            }
        }
        return null;
    }

    public List<Item> searchItems(String keyword, String location, String status) {
        try {
            return searchItems(keyword, location, status, "unknown"); 
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while searching for items.");
            return new ArrayList<>();  
        }
    }
    
    public List<Item> searchItems(String keyword, String location, String status, String type) throws SQLException {
        List<Item> items = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM items WHERE 1=1");
    
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND description LIKE ?");
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location LIKE ?");
        }
        if (status != null && !status.isEmpty()) {
            sql.append(" AND status = ?");
        }
        if (type != null && !type.isEmpty()) {
            sql.append(" AND type = ?");
        }
    
        try (PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            int paramIndex = 1;
    
            if (keyword != null && !keyword.isEmpty()) {
                statement.setString(paramIndex++, "%" + keyword + "%");
            }
            if (location != null && !location.isEmpty()) {
                statement.setString(paramIndex++, "%" + location + "%");
            }
            if (status != null && !status.isEmpty()) {
                statement.setString(paramIndex++, status);
            }
            if (type != null && !type.isEmpty()) {
                statement.setString(paramIndex++, type);
            }
    
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Item item = new Item(
                        resultSet.getInt("item_id"),
                        resultSet.getString("description"),
                        resultSet.getString("location"),
                        resultSet.getDate("date").toLocalDate(),
                        resultSet.getString("image_path"),
                        resultSet.getInt("owner_id"),
                        resultSet.getTimestamp("created_at"),
                        resultSet.getTimestamp("updated_at")
                    );
                    item.setStatus(Item.Status.valueOf(resultSet.getString("status")));
                    item.setType(Item.Type.valueOf(resultSet.getString("type")));
                    items.add(item);
                }
            }
        }
        return items;
    }
    

    public boolean suggestMatch(Match match) {
        try {
            matchDAO.createMatch(match);
            return true;
        } catch (SQLException e) {
            System.out.println("Error suggesting match: " + e.getMessage());
            return false;
        }
    }

    public List<Match> viewMatchSuggestions() {
        User currentUser = SessionManager.getCurrentUser();
        if (currentUser != null) {
            try {
                return matchDAO.getAllMatches(); 
            } catch (SQLException e) {
                System.out.println("Error retrieving match suggestions: " + e.getMessage());
            }
        }
        return null;
    }

    public Match viewMatchDetails(int matchId) {
        try {
            return matchDAO.getMatchById(matchId); 
        } catch (SQLException e) {
            System.out.println("Error retrieving match details: " + e.getMessage());
        }
        return null;
    }
}
