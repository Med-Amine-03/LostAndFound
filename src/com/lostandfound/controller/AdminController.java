package com.lostandfound.controller;

import com.lostandfound.service.AdminService;
import com.lostandfound.model.Item;

public class AdminController {
    private AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void approveItem(Item item) {
        try {
            adminService.approveItem(item);  
        } catch (SQLException e) {
            System.out.println("Error approving item: " + e.getMessage());
        }
    }

    public void rejectItem(Item item) {
        try {
            adminService.rejectItem(item);  
        } catch (SQLException e) {
            System.out.println("Error rejecting item: " + e.getMessage());
        }
    }

    public void deleteItem(int itemId) {
        try {
            adminService.deleteItem(itemId);  
        } catch (SQLException e) {
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    public void createMatch(Item lostItem, Item foundItem) {
        try {
            adminService.createMatch(lostItem, foundItem);  
        } catch (SQLException e) {
            System.out.println("Error creating match: " + e.getMessage());
        }
    }
}
