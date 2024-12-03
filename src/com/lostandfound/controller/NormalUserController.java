package com.lostandfound.controller;

import com.lostandfound.service.NormalUserService;
import com.lostandfound.model.Item;
import java.util.List;

public class NormalUserController {
    private NormalUserService normalUserService;

    public NormalUserController(NormalUserService normalUserService) {
        this.normalUserService = normalUserService;
    }

    public void submitItem(Item item) {
        try {
            normalUserService.submitItem(item);  
        } catch (SQLException e) {
            System.out.println("Error submitting item: " + e.getMessage());
        }
    }

    public List<Item> viewSubmittedItems(int userId) {
        try {
            return normalUserService.viewSubmittedItems(userId);  
        } catch (SQLException e) {
            System.out.println("Error viewing submitted items: " + e.getMessage());
        }
        return null;
    }
}
