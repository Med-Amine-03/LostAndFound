package com.lostandfound.service;

import com.lostandfound.dao.MatchDAO;
import com.lostandfound.dao.ItemDAO;
import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.service.LevenshteinDistance;

public class AdminService {
    private MatchDAO matchDAO;
    private ItemDAO itemDAO;

    public AdminService(MatchDAO matchDAO, ItemDAO itemDAO) {
        this.matchDAO = matchDAO;
        this.itemDAO = itemDAO;
    }

    public void approveItem(Item item) {
        item.setStatus(Item.Status.Confirmed);
        itemDAO.updateItem(item);
    }

    public void deleteItem(int itemId) {
        itemDAO.deleteItem(itemId);
    }

    public void createMatch(Item lostItem, Item foundItem) {
        double levenshteinScore = calculateLevenshteinScore(lostItem, foundItem);

        if (levenshteinScore < 0.2) {
            Match match = new Match(lostItem.getItemId(), foundItem.getItemId(), levenshteinScore);
            matchDAO.createMatch(match);
        }
    }

    private double calculateLevenshteinScore(Item lostItem, Item foundItem) {
        String lostItemDescription = lostItem.getDescription();
        String foundItemDescription = foundItem.getDescription();
        return LevenshteinDistance.calculate(lostItemDescription, foundItemDescription);
    }
}
