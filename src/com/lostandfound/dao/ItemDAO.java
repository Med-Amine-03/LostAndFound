package com.lostandfound.dao;

import com.lostandfound.model.Item;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {
    private Connection connection;

    public ItemDAO(Connection connection) {
        this.connection = connection;
    }

    public void createItem(Item item) throws SQLException {
        String sql = "INSERT INTO item (description, date, location, status, type, image_path, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
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
        String sql = "SELECT * FROM item WHERE item_id = ?";
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
        String sql = "UPDATE item SET description = ?, date = ?, location = ?, status = ?, type = ?, image_path = ?, owner_id = ? WHERE item_id = ?";
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
        String sql = "DELETE FROM item WHERE item_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, itemId);
            statement.executeUpdate();
        }
    }

    public List<Item> getItemsByOwnerId(int ownerId) throws SQLException {
        String sql = "SELECT * FROM item WHERE owner_id = ?";
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
}
