package com.lostandfound.service;

import com.lostandfound.dao.ItemDAO;
import com.lostandfound.model.Item;
import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private final ItemDAO itemDAO;

    public ItemService(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public boolean createItem(Item item) {
        try {
            itemDAO.createItem(item);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> searchItems(String keyword, String location, String status, String type) {
        try {
            return itemDAO.searchItems(keyword, location, status, type);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Item> getItemsByUser(int userId) {
        try {
            return itemDAO.getItemsByOwnerId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Item getItemById(int itemId) {
        try {
            return itemDAO.getItemById(itemId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean updateItem(Item item) {
        try {
            itemDAO.updateItem(item);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Item> getAllItems() {
        try {
            return itemDAO.getAllItems();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean deleteItem(int itemId) {
        try {
            itemDAO.deleteItem(itemId);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
