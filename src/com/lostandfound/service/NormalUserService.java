package com.lostandfound.service;

import com.lostandfound.dao.ItemDAO;
import com.lostandfound.model.Item;
import java.util.List;

public class NormalUserService {
    private ItemDAO itemDAO;

    public NormalUserService(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public void submitItem(Item item) {
        itemDAO.createItem(item);
    }

    public List<Item> viewSubmittedItems(int userId) {
        return itemDAO.getItemsByOwnerId(userId);
    }
}
