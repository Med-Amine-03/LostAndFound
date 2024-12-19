package com.lostandfound.dao;

import com.lostandfound.model.Item;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private Connection connection;

    public ItemDAO(Connection connection) {
        this.connection = connection;
    }

    public void createItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (description, date, location, status, type, image_path, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, item.getDescription());
            statement.setDate(2, java.sql.Date.valueOf(item.getDate()));
            statement.setString(3, item.getLocation());
            statement.setString(4, item.getStatus().toString());
            statement.setString(5, item.getType().toString());
            statement.setString(6, item.getImagePath());
            statement.setInt(7, item.getOwnerId());

            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        item.setItemId(generatedKeys.getInt(1)); 
                    }
                }
            }
        }
    }

    public Item getItemById(int itemId) throws SQLException {
        String sql = "SELECT * FROM items WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Item(
                        rs.getInt("item_id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("image_path"),
                        rs.getInt("owner_id"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                    );
                }
            }
        }
        return null;
    }

    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET description = ?, date = ?, location = ?, status = ?, type = ?, image_path = ?, owner_id = ? WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, item.getDescription());
            statement.setDate(2, java.sql.Date.valueOf(item.getDate()));
            statement.setString(3, item.getLocation());
            statement.setString(4, item.getStatus().toString());
            statement.setString(5, item.getType().toString());
            statement.setString(6, item.getImagePath());
            statement.setInt(7, item.getOwnerId());
            statement.setInt(8, item.getItemId());

            statement.executeUpdate();
        }
    }

    public void deleteItem(int itemId) throws SQLException {
        String sql = "DELETE FROM items WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        }
    }

    public List<Item> getItemsByOwnerId(int ownerId) throws SQLException {
        String sql = "SELECT * FROM items WHERE owner_id = ?";
        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ownerId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    items.add(new Item(
                        rs.getInt("item_id"),
                        rs.getString("description"),
                        rs.getString("location"),
                        rs.getDate("date").toLocalDate(),
                        rs.getString("image_path"),
                        rs.getInt("owner_id"),
                        rs.getTimestamp("created_at"),
                        rs.getTimestamp("updated_at")
                    ));
                }
            }
        }
        return items;
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

    public int getNextItemId() throws SQLException {
        String query = "SELECT MAX(itemId) FROM items";  
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;  
            } else {
                return 1;  
            }
        }
    }
    
    
    // Inside ItemDAO class
    private Item mapResultSetToItem(ResultSet resultSet) throws SQLException {
        int itemId = resultSet.getInt("item_id");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        LocalDate date = resultSet.getDate("date").toLocalDate(); 
        String imagePath = resultSet.getString("image_path");
        int ownerId = resultSet.getInt("owner_id");
        
        Timestamp createdAt = resultSet.getTimestamp("created_at");
        Timestamp updatedAt = resultSet.getTimestamp("updated_at");
        
        return new Item(itemId, description, location, date, imagePath, ownerId, createdAt, updatedAt);
    }



    public List<Item> getSuggestionsForUser(int userId) throws SQLException {
        String query = "SELECT * FROM items WHERE owner_id != ? AND status = 'Available' AND " +
                       "(description LIKE ? OR location LIKE ?)";
        List<Item> suggestions = new ArrayList<>();
    
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setString(2, "%" + getUserReportedKeywords(userId) + "%");
            statement.setString(3, "%" + getUserReportedKeywords(userId) + "%");
    
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Item item = mapResultSetToItem(resultSet);
                suggestions.add(item);
            }
        }
        return suggestions;
    }
    
    private String getUserReportedKeywords(int userId) throws SQLException {
        String query = "SELECT description FROM items WHERE owner_id = ?";
        StringBuilder keywords = new StringBuilder();
    
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
    
            while (resultSet.next()) {
                if (keywords.length() > 0) keywords.append(" ");
                keywords.append(resultSet.getString("description"));
            }
        }
        return keywords.toString();
    }
    
    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY created_at DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                items.add(mapResultSetToItem(rs));
            }
        }
        return items;
    }
}
