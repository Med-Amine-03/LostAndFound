package com.lostandfound.view;

import com.lostandfound.controller.AdminDashboardController;
import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.service.LevenshteinDistance;
import com.lostandfound.utils.Constants;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class AdminDashboardView extends BaseView {
    private final AdminDashboardController controller;
    private JPanel contentPanel;

    public AdminDashboardView(AdminDashboardController controller) {
        super();
        this.controller = controller;
        initialize();
    }

    private void initialize() {
        frame.setTitle("Admin Dashboard");
        frame.setSize(1000, 700);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        showWelcomePanel();
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(200, frame.getHeight()));

        addMenuButton(sidebar, "Pending Matches", this::showPendingMatches);
        addMenuButton(sidebar, "Accepted Matches", this::showAcceptedMatches);
        addMenuButton(sidebar, "Manage Users", this::showUsers);
        addMenuButton(sidebar, "All Items", this::showAllItems);
        addMenuButton(sidebar, "Statistics", this::showStatistics);
        addMenuButton(sidebar, "Logout", this::logout);

        return sidebar;
    }

    private void addMenuButton(JPanel sidebar, String text, Runnable action) {
        JButton button = createStyledButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    private void showWelcomePanel() {
        contentPanel.removeAll();
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        addStatCard(statsPanel, "Pending Matches", controller.getPendingMatchesCount());
        addStatCard(statsPanel, "Total Users", controller.getTotalUsersCount());
        addStatCard(statsPanel, "Total Items", controller.getTotalItemsCount());
        addStatCard(statsPanel, "Matched Items", controller.getMatchedItemsCount());

        welcomePanel.add(statsPanel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void addStatCard(JPanel panel, String title, int count) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(new Color(240, 240, 240));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel countLabel = new JLabel(String.valueOf(count));
        countLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(countLabel, BorderLayout.CENTER);
        panel.add(card);
    }

    private void showPendingMatches() {
        contentPanel.removeAll();
        List<Match> pendingMatches = controller.getPendingMatches();
        
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        
        for (Match match : pendingMatches) {
            matchesPanel.add(createMatchPanel(match));
            matchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        JScrollPane scrollPane = new JScrollPane(matchesPanel);
        contentPanel.add(scrollPane);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createMatchPanel(Match match) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Item lostItem = controller.getItemById(match.getLostItemId());
        Item foundItem = controller.getItemById(match.getFoundItemId());

        JPanel itemsPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        JPanel lostItemPanel = new JPanel(new BorderLayout(5, 5));
        lostItemPanel.setBorder(BorderFactory.createTitledBorder("Lost Item"));
        
        if (lostItem.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + lostItem.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                lostItemPanel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel lostDetailsPanel = new JPanel(new GridLayout(3, 1));
        lostDetailsPanel.add(new JLabel("Description: " + lostItem.getDescription()));
        lostDetailsPanel.add(new JLabel("Location: " + lostItem.getLocation()));
        lostDetailsPanel.add(new JLabel("Posted by: " + controller.getUserName(lostItem.getOwnerId())));
        lostItemPanel.add(lostDetailsPanel, BorderLayout.CENTER);

        JPanel foundItemPanel = new JPanel(new BorderLayout(5, 5));
        foundItemPanel.setBorder(BorderFactory.createTitledBorder("Found Item"));
        
        if (foundItem.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + foundItem.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                foundItemPanel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel foundDetailsPanel = new JPanel(new GridLayout(3, 1));
        foundDetailsPanel.add(new JLabel("Description: " + foundItem.getDescription()));
        foundDetailsPanel.add(new JLabel("Location: " + foundItem.getLocation()));
        foundDetailsPanel.add(new JLabel("Posted by: " + controller.getUserName(foundItem.getOwnerId())));
        foundItemPanel.add(foundDetailsPanel, BorderLayout.CENTER);

        itemsPanel.add(lostItemPanel);
        itemsPanel.add(foundItemPanel);

        int distance = LevenshteinDistance.calculate(lostItem.getDescription(), foundItem.getDescription());
        int maxLength = Math.max(lostItem.getDescription().length(), foundItem.getDescription().length());
        double percentage = (1.0 - (double) distance / maxLength) * 100;

        JLabel percentageLabel = new JLabel(String.format("Match Percentage: %.2f%%", percentage));
        percentageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        percentageLabel.setForeground(new Color(0, 150, 0)); 
        panel.add(percentageLabel, BorderLayout.NORTH);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton acceptButton = createStyledButton("Accept");
        JButton rejectButton = createStyledButton("Reject");

        acceptButton.addActionListener(e -> {
            if (controller.acceptMatch(match.getMatchId())) {
                JOptionPane.showMessageDialog(frame, "Match accepted successfully!");
                showPendingMatches();
            }
        });

        rejectButton.addActionListener(e -> {
            if (controller.rejectMatch(match.getMatchId())) {
                JOptionPane.showMessageDialog(frame, "Match rejected successfully!");
                showPendingMatches();
            }
        });

        buttonsPanel.add(acceptButton);
        buttonsPanel.add(rejectButton);

        panel.add(itemsPanel, BorderLayout.CENTER);
        panel.add(buttonsPanel, BorderLayout.SOUTH);
        return panel;
    }

    private void showAcceptedMatches() {
        contentPanel.removeAll();
        List<Match> acceptedMatches = controller.getAcceptedMatches();
        
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        
        if (acceptedMatches != null && !acceptedMatches.isEmpty()) {
            for (Match match : acceptedMatches) {
                matchesPanel.add(createAcceptedMatchPanel(match));
                matchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            JLabel noMatchesLabel = new JLabel("No accepted matches found");
            noMatchesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            matchesPanel.add(noMatchesLabel);
        }
        
        JScrollPane scrollPane = new JScrollPane(matchesPanel);
        contentPanel.add(scrollPane);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createAcceptedMatchPanel(Match match) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 0)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        Item lostItem = controller.getItemById(match.getLostItemId());
        Item foundItem = controller.getItemById(match.getFoundItemId());

        JPanel itemsPanel = new JPanel(new GridLayout(1, 2, 10, 0));

        itemsPanel.add(createItemView(lostItem, "Lost Item"));
        itemsPanel.add(createItemView(foundItem, "Found Item"));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.add(new JLabel("Accepted on: " + match.getUpdatedAt()));

        panel.add(itemsPanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createItemView(Item item, String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(title));
        
        if (item.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + item.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                panel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel detailsPanel = new JPanel(new GridLayout(3, 1));
        detailsPanel.add(new JLabel("Description: " + item.getDescription()));
        detailsPanel.add(new JLabel("Location: " + item.getLocation()));
        detailsPanel.add(new JLabel("Posted by: " + controller.getUserName(item.getOwnerId())));
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        return panel;
    }

    private void showUsers() {
        contentPanel.removeAll();
        List<User> users = controller.getAllUsers();
        
        JPanel usersPanel = new JPanel();
        usersPanel.setLayout(new BoxLayout(usersPanel, BoxLayout.Y_AXIS));
        
        for (User user : users) {
            if (!"Admin".equals(user.getRole())) {  
                usersPanel.add(createUserPanel(user));
                usersPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(usersPanel);
        contentPanel.add(scrollPane);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createUserPanel(User user) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.PROFILE_IMAGES_PATH + user.getProfileImage());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel detailsPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        detailsPanel.add(new JLabel("Name: " + user.getName()));
        detailsPanel.add(new JLabel("Email: " + user.getEmail()));
        
        int itemsCount = controller.getUserItemsCount(user.getUserId());
        detailsPanel.add(new JLabel("Items Posted: " + itemsCount));

        JButton deleteButton = createStyledButton("Delete User");
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                frame,
                "Are you sure you want to delete this user?\nThis will also delete all their items and matches.",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (controller.deleteUser(user.getUserId())) {
                    JOptionPane.showMessageDialog(frame, "User deleted successfully!");
                    showUsers();
                } else {
                    JOptionPane.showMessageDialog(frame, "Error deleting user!");
                }
            }
        });

        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.EAST);
        return panel;
    }

    private void showAllItems() {
        contentPanel.removeAll();
        List<Item> items = controller.getAllItems();
        
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        
        for (Item item : items) {
            itemsPanel.add(createItemPanel(item));
            itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        contentPanel.add(scrollPane);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        if (item.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + item.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                imageLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                panel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel detailsPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        detailsPanel.add(new JLabel("Type: " + item.getType()));
        detailsPanel.add(new JLabel("Description: " + item.getDescription()));
        detailsPanel.add(new JLabel("Location: " + item.getLocation()));
        detailsPanel.add(new JLabel("Status: " + item.getStatus()));
        detailsPanel.add(new JLabel("Posted by: " + controller.getUserName(item.getOwnerId())));

        JButton deleteButton = createStyledButton("Delete Item");
        deleteButton.addActionListener(e -> {
            if (controller.deleteItem(item.getItemId())) {
                JOptionPane.showMessageDialog(frame, "Item deleted successfully!");
                showAllItems();
            } else {
                JOptionPane.showMessageDialog(frame, "Error deleting item!");
            }
        });

        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.EAST);
        return panel;
    }

    private void showStatistics() {
        contentPanel.removeAll();

        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 20));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        addStatCard(statsPanel, "Total Users", controller.getTotalUsersCount());
        addStatCard(statsPanel, "Total Items", controller.getTotalItemsCount());
        addStatCard(statsPanel, "Pending Matches", controller.getPendingMatchesCount());
        addStatCard(statsPanel, "Completed Matches", controller.getCompletedMatchesCount());

        contentPanel.add(statsPanel);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void logout() {
        frame.dispose();
        new LoginView(null).setVisible(true);
    }
}
