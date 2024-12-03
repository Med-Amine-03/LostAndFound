package com.lostandfound.controller;

import com.lostandfound.service.AdminService;
import com.lostandfound.model.Item;

public class AdminDashboardController {
    private AdminService adminService;

    public AdminDashboardController(AdminService adminService) {
        this.adminService = adminService;
    }

    public void onApproveItemButtonClick(Item item) {
        adminService.approveItem(item);
        updateDashboardUI();
    }

    public void onRejectItemButtonClick(Item item) {
        adminService.rejectItem(item);
        updateDashboardUI();
    }

    public void onCreateMatchButtonClick(Item lostItem, Item foundItem) {
        adminService.createMatch(lostItem, foundItem);
        updateDashboardUI();
    }

    public void onLogoutButtonClick() {
        // Logout logic (clear session, redirect to login screen, etc.)
        SessionManager.clearSession();
    }

    private void updateDashboardUI() {
        // Logic to update the dashboard UI (refresh lists, show success messages, etc.)
    }
}
