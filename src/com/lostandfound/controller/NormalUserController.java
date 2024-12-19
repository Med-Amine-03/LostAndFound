package com.lostandfound.controller;

import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.service.ItemService;
import com.lostandfound.service.MatchService;
import com.lostandfound.service.UserService;
import com.lostandfound.utils.SessionManager;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class NormalUserController {
    private final UserService userService;
    private final ItemService itemService;
    private final MatchService matchService;

    public NormalUserController(UserService userService, ItemService itemService, MatchService matchService) {
        this.userService = userService;
        this.itemService = itemService;
        this.matchService = matchService;
    }

    public boolean uploadItem(String description, String location, String type, String imagePath) {
        Item item = new Item(
            0,
            description,
            location,
            LocalDate.now(),
            imagePath,
            SessionManager.getCurrentUser().getUserId(),
            null,
            null
        );
        item.setType(Item.Type.valueOf(type));
        item.setStatus(Item.Status.Pending);
        return itemService.createItem(item);
    }

    public java.util.List<Item> viewMyItems() {
        return itemService.getItemsByUser(SessionManager.getCurrentUser().getUserId());
    }

    public java.util.List<Item> searchItems(String keyword, String location, String status, String type) {
        if ("All".equals(status)) status = "";
        if ("All".equals(type)) type = "";
        return itemService.searchItems(keyword, location, status, type);
    }

    public boolean updateProfile(String name, String email, String password, String profileImage) {
        User currentUser = SessionManager.getCurrentUser();
        currentUser.setName(name);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setProfileImage(profileImage);
        return userService.updateUser(currentUser);
    }

    public boolean createMatch(int lostItemId, int foundItemId) {
        Match match = new Match(lostItemId, foundItemId, calculateMatchScore(lostItemId, foundItemId));
        return matchService.createMatch(match);
    }

    public java.util.List<Match> viewMatchSuggestions() {
        return matchService.getAllMatches();
    }

    private double calculateMatchScore(int lostItemId, int foundItemId) {
        try {
            Item lostItem = itemService.getItemById(lostItemId);
            Item foundItem = itemService.getItemById(foundItemId);
            
            if (lostItem != null && foundItem != null) {
                double descriptionSimilarity = calculateSimilarity(lostItem.getDescription(), foundItem.getDescription());
                double locationSimilarity = calculateSimilarity(lostItem.getLocation(), foundItem.getLocation());
                
                return (descriptionSimilarity * 0.7) + (locationSimilarity * 0.3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    private double calculateSimilarity(String str1, String str2) {
        int maxLength = Math.max(str1.length(), str2.length());
        if (maxLength == 0) return 1.0;
        
        int distance = levenshteinDistance(str1.toLowerCase(), str2.toLowerCase());
        return 1.0 - ((double) distance / maxLength);
    }

    private int levenshteinDistance(String str1, String str2) {
        int[][] dp = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 0; i <= str1.length(); i++) {
            for (int j = 0; j <= str2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                        dp[i - 1][j - 1] + (str1.charAt(i - 1) == str2.charAt(j - 1) ? 0 : 1),
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1)
                    );
                }
            }
        }
        return dp[str1.length()][str2.length()];
    }

    public Item getItemById(int itemId) {
        return itemService.getItemById(itemId);
    }

    public java.util.List<Item> getAllItems() {
        return itemService.getAllItems();
    }

    public User getUserById(int userId) {
        return userService.getUserById(userId);
    }

    public List<Match> getMyMatches() {
        int userId = SessionManager.getCurrentUser().getUserId();
        List<Match> allMatches = matchService.getAllMatches();
        List<Match> myMatches = new ArrayList<>();
        
        for (Match match : allMatches) {
            Item lostItem = itemService.getItemById(match.getLostItemId());
            Item foundItem = itemService.getItemById(match.getFoundItemId());
            
            if (lostItem.getOwnerId() == userId || foundItem.getOwnerId() == userId) {
                myMatches.add(match);
            }
        }
        
        return myMatches;
    }

    public String getUserName(int userId) {
        User user = userService.getUserById(userId); 
        return user != null ? user.getName() : "Unknown User";
    }
}
