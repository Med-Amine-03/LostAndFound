package com.lostandfound.view;

import com.lostandfound.controller.NormalUserController;
import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.utils.Constants;
import com.lostandfound.utils.SessionManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class NormalUserDashboard extends BaseView {
    private final NormalUserController controller;
    private JPanel contentPanel;
    
    public NormalUserDashboard(NormalUserController controller) {
        super();
        this.controller = controller;
        initialize();
    }
    
    private void initialize() {
        JPanel mainPanel = createMainPanel();
        
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel sidebarPanel = createSidebar();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        showWelcomeMessage();
        
        frame.add(mainPanel);
        showFrame("Lost and Found System - Dashboard");
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(51, 51, 51));
        
        User currentUser = SessionManager.getCurrentUser();
        
        ImageIcon profileIcon = null;
        try {
            String imagePath = null;
            if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
                imagePath = Constants.PROFILE_IMAGES_PATH + currentUser.getProfileImage();
            } else {
                imagePath = Constants.DEFAULT_PROFILE_IMAGE;
            }
            
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                BufferedImage img = ImageIO.read(imageFile);
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                profileIcon = new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            System.out.println("Could not load profile image: " + e.getMessage());
        }
        
        if (profileIcon == null) {
            profileIcon = createDefaultProfileIcon();
        }
        
        JLabel profileLabel = new JLabel(profileIcon);
        headerPanel.add(profileLabel);
        
        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerPanel.add(nameLabel);
        
        return headerPanel;
    }
    
    private ImageIcon createDefaultProfileIcon() {
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2d.setColor(new Color(100, 149, 237)); 
        g2d.fillOval(0, 0, 49, 49);
        
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        String initial = SessionManager.getCurrentUser().getName().substring(0, 1).toUpperCase();
        FontMetrics fm = g2d.getFontMetrics();
        int x = (50 - fm.stringWidth(initial)) / 2;
        int y = ((50 - fm.getHeight()) / 2) + fm.getAscent();
        g2d.drawString(initial, x, y);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(51, 51, 51));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(220, frame.getHeight()));
        
        addMenuButton(sidebar, "Dashboard", this::showWelcomeMessage);
        addMenuButton(sidebar, "Report Lost Item", () -> showReportItem("Lost"));
        addMenuButton(sidebar, "Report Found Item", () -> showReportItem("Found"));
        addMenuButton(sidebar, "My Items", this::showMyItems);
        addMenuButton(sidebar, "Match Suggestions", this::showMatchSuggestions);
        addMenuButton(sidebar, "Profile", this::showProfile);
        addMenuButton(sidebar, "Logout", this::logout);
        
        return sidebar;
    }
    
    private void addMenuButton(JPanel sidebar, String text, Runnable action) {
        JButton button = createStyledButton(text);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(e -> action.run());
        sidebar.add(button);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private void showWelcomeMessage() {
        contentPanel.removeAll();
        User currentUser = SessionManager.getCurrentUser();
        
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentUser.getName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        contentPanel.add(welcomePanel);
        refreshContent();
    }
    
    private void showReportItem(String type) {
        contentPanel.removeAll();
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField descriptionField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JLabel imagePathLabel = new JLabel("No file selected");
        
        addFormField(formPanel, "Description:", descriptionField, gbc, 0);
        addFormField(formPanel, "Location:", locationField, gbc, 1);
        
        JButton uploadImageButton = createStyledButton("Upload Image");
        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                imagePathLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        
        JButton submitButton = createStyledButton("Submit");
        submitButton.addActionListener(e -> {
            if (validateFields(descriptionField.getText(), locationField.getText(), imagePathLabel.getText())) {
                controller.uploadItem(
                    descriptionField.getText(),
                    locationField.getText(),
                    type,
                    imagePathLabel.getText()
                );
                JOptionPane.showMessageDialog(frame, "Item reported successfully!");
                showMyItems();
            }
        });
        
        gbc.gridy = 3;
        formPanel.add(uploadImageButton, gbc);
        gbc.gridy = 4;
        formPanel.add(imagePathLabel, gbc);
        gbc.gridy = 5;
        formPanel.add(submitButton, gbc);
        
        contentPanel.add(formPanel, BorderLayout.CENTER);
        refreshContent();
    }
    
    private void addFormField(JPanel panel, String label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridy = row;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }
    
    private void showMyItems() {
        contentPanel.removeAll();
        
        List<Item> items = controller.viewMyItems();
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                itemsPanel.add(createItemPanel(item));
                itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            itemsPanel.add(new JLabel("No items found"));
        }
        
        JScrollPane scrollPane = new JScrollPane(itemsPanel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        refreshContent();
    }
    
    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setMaximumSize(new Dimension(600, 200));
        
        try {
            BufferedImage img = ImageIO.read(new File(Constants.ITEM_IMAGES_PATH + item.getImagePath()));
            Image scaledImg = img.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            JLabel imageLabel = new JLabel(new ImageIcon(scaledImg));
            panel.add(imageLabel, BorderLayout.WEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.add(new JLabel("Description: " + item.getDescription()));
        detailsPanel.add(new JLabel("Location: " + item.getLocation()));
        detailsPanel.add(new JLabel("Status: " + item.getStatus()));
        detailsPanel.add(new JLabel("Date: " + item.getDate()));
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        
        if (item.getType() == Item.Type.Lost) {
            JButton suggestMatchButton = createStyledButton("Suggest Match");
            suggestMatchButton.addActionListener(e -> showMatchSuggestionDialog(item));
            panel.add(suggestMatchButton, BorderLayout.EAST);
        }
        
        return panel;
    }
    
    private void showMatchSuggestionDialog(Item item) {
        JDialog dialog = new JDialog(frame, "Suggest Match", true);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        
        List<Item> foundItems = controller.searchItems("", "", "Pending", "Found");
        JComboBox<Item> foundItemsCombo = new JComboBox<>(foundItems.toArray(new Item[0]));
        
        formPanel.add(new JLabel("Select Found Item:"));
        formPanel.add(foundItemsCombo);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = createStyledButton("Submit Match");
        submitButton.addActionListener(e -> {
            Item foundItem = (Item) foundItemsCombo.getSelectedItem();
            if (foundItem != null) {
                controller.createMatch(item.getItemId(), foundItem.getItemId());
                dialog.dispose();
                JOptionPane.showMessageDialog(frame, "Match suggestion submitted successfully!");
            }
        });
        
        buttonPanel.add(submitButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
    
    private void showMatchSuggestions() {
        contentPanel.removeAll();
        
        List<Match> matches = controller.viewMatchSuggestions();
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        
        if (matches != null && !matches.isEmpty()) {
            for (Match match : matches) {
                matchesPanel.add(createMatchPanel(match));
                matchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            matchesPanel.add(new JLabel("No matches found"));
        }
        
        JScrollPane scrollPane = new JScrollPane(matchesPanel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);
        refreshContent();
    }
    
    private JPanel createMatchPanel(Match match) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setMaximumSize(new Dimension(600, 100));
        
        JLabel matchLabel = new JLabel("Match ID: " + match.getMatchId());
        panel.add(matchLabel);
        
        return panel;
    }
    
    private void showProfile() {
        contentPanel.removeAll();
        
        User currentUser = SessionManager.getCurrentUser();
        JPanel profilePanel = new JPanel(new GridBagLayout());
        profilePanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JTextField nameField = new JTextField(currentUser.getName(), 20);
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        JPasswordField passwordField = new JPasswordField(20);
        
        addFormField(profilePanel, "Name:", nameField, gbc, 0);
        addFormField(profilePanel, "Email:", emailField, gbc, 1);
        addFormField(profilePanel, "New Password:", passwordField, gbc, 2);
        
        JButton saveButton = createStyledButton("Save Changes");
        saveButton.addActionListener(e -> {
            controller.updateProfile(
                nameField.getText(),
                emailField.getText(),
                new String(passwordField.getPassword()),
                currentUser.getProfileImage()
            );
            JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
        });
        
        gbc.gridy = 3;
        profilePanel.add(saveButton, gbc);
        
        contentPanel.add(profilePanel, BorderLayout.CENTER);
        refreshContent();
    }
    
    private void logout() {
        SessionManager.clearSession();
        frame.dispose();
        new LoginView(null).setVisible(true);
    }
    
    private void refreshContent() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private boolean validateFields(String description, String location, String imagePath) {
        if (description.isEmpty() || location.isEmpty() || "No file selected".equals(imagePath)) {
            JOptionPane.showMessageDialog(frame, "Please fill in all fields and select an image.");
            return false;
        }
        return true;
    }
} 