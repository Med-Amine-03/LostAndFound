package com.lostandfound.controller;

import com.lostandfound.service.NormalUserService;
import com.lostandfound.model.Item;

import java.util.List;

public class NormalUserDashboardController {
    private NormalUserService normalUserService;

    public NormalUserDashboardController(NormalUserService normalUserService) {
        this.normalUserService = normalUserService;
    }

    public void onSubmitItemButtonClick(Item item) {
        normalUserService.submitItem(item);
        updateDashboardUI();
    }

    public void onViewSubmittedItemsButtonClick(int userId) {
        List<Item> items = normalUserService.viewSubmittedItems(userId);
        displayItems(items);
    }

    public void onLogoutButtonClick() {
        SessionManager.clearSession();
        
    }

    private void updateDashboardUI() {
        // Refresh the UI (show success message, update lists, etc.)
    }

    private void displayItems(List<Item> items) {
        // Display the submitted items (could use a JTable, ListView, etc.)
    }
}
