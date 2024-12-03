package com.lostandfound.service;

import com.lostandfound.dao.ItemDAO;
import com.lostandfound.model.Item;
import java.sql.SQLException;
import java.util.List;

public class ItemService {
    private ItemDAO itemDAO;

    public ItemService(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public void createItem(Item item) throws SQLException {
        itemDAO.createItem(item);
    }

    public void updateItem(Item item) throws SQLException {
        itemDAO.updateItem(item);
    }

    public void deleteItem(int itemId) throws SQLException {
        itemDAO.deleteItem(itemId);
    }

    public Item getItemById(int itemId) throws SQLException {
        return itemDAO.getItemById(itemId);
    }

    public List<Item> getItemsByOwnerId(int ownerId) throws SQLException {
        return itemDAO.getItemsByOwnerId(ownerId);
    }

    public void approveItem(Item item) throws SQLException {
        item.setStatus(Item.Status.Confirmed);
        itemDAO.updateItem(item);
    }

    public void rejectItem(Item item) throws SQLException {
        item.setStatus(Item.Status.Rejected);
        itemDAO.updateItem(item);
    }
}
