package com.lostandfound.controller;

import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.service.ItemService;
import com.lostandfound.service.MatchService;
import com.lostandfound.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

public class AdminDashboardController {
    private final UserService userService;
    private final ItemService itemService;
    private final MatchService matchService;

    public AdminDashboardController(UserService userService, ItemService itemService, MatchService matchService) {
        this.userService = userService;
        this.itemService = itemService;
        this.matchService = matchService;
    }

    public List<Match> getPendingMatches() {
        return matchService.getAllMatches();
    }

    public boolean acceptMatch(int matchId) {
        Match match = matchService.getMatchById(matchId);
        if (match != null) {
            match.setStatus(Match.Status.Confirmed);
            match.setReviewedByAdmin(true);
            
            Item lostItem = itemService.getItemById(match.getLostItemId());
            Item foundItem = itemService.getItemById(match.getFoundItemId());
            
            if (lostItem != null && foundItem != null) {
                lostItem.setStatus(Item.Status.Matched);
                foundItem.setStatus(Item.Status.Matched);
                itemService.updateItem(lostItem);
                itemService.updateItem(foundItem);
            }
            
            return matchService.updateMatch(match);
        }
        return false;
    }

    public boolean rejectMatch(int matchId) {
        try {
            matchService.deleteMatch(matchId);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    public boolean deleteUser(int userId) {
        return userService.deleteUser(userId);
    }

    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    public Item getItemById(int itemId) {
        return itemService.getItemById(itemId);
    }

    public String getUserName(int userId) {
        User user = userService.getUserById(userId);
        return user != null ? user.getName() : "Unknown";
    }

    public int getUserItemsCount(int userId) {
        List<Item> userItems = itemService.getItemsByUser(userId);
        return userItems != null ? userItems.size() : 0;
    }

    public boolean deleteItem(int itemId) {
        return itemService.deleteItem(itemId);
    }

    public int getPendingMatchesCount() {
        List<Match> matches = matchService.getAllMatches();
        return (int) matches.stream().filter(m -> m.getStatus() == Match.Status.Pending).count();
    }

    public int getTotalUsersCount() {
        List<User> users = userService.getAllUsers();
        return users != null ? users.size() : 0;
    }

    public int getTotalItemsCount() {
        List<Item> items = itemService.getAllItems();
        return items != null ? items.size() : 0;
    }

    public int getMatchedItemsCount() {
        List<Match> matches = matchService.getAllMatches();
        return (int) matches.stream().filter(m -> m.getStatus() == Match.Status.Confirmed).count();
    }

    public int getLostItemsCount() {
        List<Item> items = itemService.getAllItems();
        return (int) items.stream().filter(i -> i.getType() == Item.Type.Lost).count();
    }

    public int getFoundItemsCount() {
        List<Item> items = itemService.getAllItems();
        return (int) items.stream().filter(i -> i.getType() == Item.Type.Found).count();
    }

    public int getCompletedMatchesCount() {
        List<Match> matches = matchService.getAllMatches();
        return (int) matches.stream().filter(m -> m.getStatus() == Match.Status.Confirmed).count();
    }

    public List<Match> getAcceptedMatches() {
        List<Match> allMatches = matchService.getAllMatches();
        return allMatches.stream()
            .filter(m -> m.getStatus() == Match.Status.Confirmed)
            .collect(Collectors.toList());
    }
}
