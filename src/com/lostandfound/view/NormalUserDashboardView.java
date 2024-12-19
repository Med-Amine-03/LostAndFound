package com.lostandfound.view;

import com.lostandfound.controller.NormalUserController;
import com.lostandfound.model.Item;
import com.lostandfound.model.Match;
import com.lostandfound.model.User;
import com.lostandfound.utils.Constants;
import com.lostandfound.utils.ImageUtils;
import com.lostandfound.utils.SessionManager;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class NormalUserDashboardView {
    private NormalUserController controller;
    private String selectedProfileImagePath;
    private String selectedItemImagePath;
    private JPanel mainPanel;
    private JFrame mainFrame;

    public NormalUserDashboardView(NormalUserController controller) {
        this.controller = controller;
    }

    public void showDashboard(JFrame frame) {
        this.mainFrame = frame;
        frame.getContentPane().removeAll();
        
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel headerPanel = new JPanel(new BorderLayout(15, 15));
        headerPanel.setBackground(new Color(51, 51, 51));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        
        JPanel profileSection = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        profileSection.setBackground(new Color(51, 51, 51));
        
        User currentUser = SessionManager.getCurrentUser();
        JLabel profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(60, 60));
        
        try {
            if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(Constants.PROFILE_IMAGES_PATH + currentUser.getProfileImage());
                Image image = imageIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                profileImageLabel.setIcon(new ImageIcon(image));
            } else {
                BufferedImage defaultImage = new BufferedImage(60, 60, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = defaultImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(70, 130, 180));
                g2d.fillOval(0, 0, 59, 59);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                String initial = currentUser.getName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(initial, (60 - fm.stringWidth(initial)) / 2, ((60 - fm.getHeight()) / 2) + fm.getAscent());
                g2d.dispose();
                profileImageLabel.setIcon(new ImageIcon(defaultImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JPanel welcomeTextPanel = new JPanel(new GridLayout(2, 1));
        welcomeTextPanel.setBackground(new Color(51, 51, 51));
        
        JLabel welcomeLabel = new JLabel("Welcome back,");
        welcomeLabel.setForeground(new Color(200, 200, 200));
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        welcomeTextPanel.add(welcomeLabel);
        welcomeTextPanel.add(nameLabel);
        
        profileSection.add(profileImageLabel);
        profileSection.add(welcomeTextPanel);
        
        headerPanel.add(profileSection, BorderLayout.WEST);
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel menuPanel = new JPanel(new GridLayout(6, 1, 5, 5));  
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setBackground(Color.WHITE);

        JButton reportLostButton = createStyledButton("Report Lost Item");
        JButton reportFoundButton = createStyledButton("Report Found Item");
        JButton searchItemsButton = createStyledButton("Search Items");
        JButton myMatchesButton = createStyledButton("My Matches");
        JButton profileButton = createStyledButton("Profile Settings");
        JButton logoutButton = createStyledButton("Logout");  

        reportLostButton.addActionListener(e -> showReportItemForm(frame, "Lost"));
        reportFoundButton.addActionListener(e -> showReportItemForm(frame, "Found"));
        searchItemsButton.addActionListener(e -> showSearchForm(frame));
        myMatchesButton.addActionListener(e -> showMyMatches(frame));
        profileButton.addActionListener(e -> showProfileSettings(frame));  
        logoutButton.addActionListener(e -> logout(frame));  

        menuPanel.add(reportLostButton);
        menuPanel.add(reportFoundButton);
        menuPanel.add(searchItemsButton);
        menuPanel.add(myMatchesButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showReportItemForm(JFrame frame, String type) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField descriptionField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JLabel imageLabel = new JLabel("No image selected");
        selectedItemImagePath = null;

        gbc.gridy = 0;
        formPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(descriptionField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        formPanel.add(locationField, gbc);

        JButton imageButton = new JButton("Choose Image");
        imageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (ImageUtils.isValidImage(selectedFile)) {
                    selectedItemImagePath = ImageUtils.saveItemImage(selectedFile);
                    imageLabel.setText("Image selected: " + selectedFile.getName());
                }
            }
        });

        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(imageButton, gbc);
        gbc.gridx = 1;
        formPanel.add(imageLabel, gbc);

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            if (validateItemForm(descriptionField.getText(), locationField.getText(), selectedItemImagePath)) {
                boolean success = controller.uploadItem(
                    descriptionField.getText(),
                    locationField.getText(),
                    type,
                    selectedItemImagePath
                );
                if (success) {
                    JOptionPane.showMessageDialog(frame, "Item reported successfully!");
                    showDashboard(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Error reporting item!");
                }
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showDashboard(frame));

        gbc.gridy = 3;
        gbc.gridx = 0;
        formPanel.add(submitButton, gbc);
        gbc.gridx = 1;
        formPanel.add(backButton, gbc);

        frame.getContentPane().removeAll();
        frame.add(formPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showProfileSettings(JFrame frame) {
        JPanel profilePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        User currentUser = SessionManager.getCurrentUser();
        
        JPanel imagePanel = new JPanel(new BorderLayout(5, 5));
        imagePanel.setPreferredSize(new Dimension(200, 200));
        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        if (currentUser.getProfileImage() != null && !currentUser.getProfileImage().isEmpty()) {
            try {
                String imagePath = Constants.PROFILE_IMAGES_PATH + currentUser.getProfileImage();
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    ImageIcon imageIcon = new ImageIcon(imagePath);
                    Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(image));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        
        JButton changeImageButton = createStyledButton("Change Profile Picture");
        changeImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png"));
            if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                if (ImageUtils.isValidImage(selectedFile)) {
                    String savedImagePath = ImageUtils.saveProfileImage(selectedFile);
                    if (savedImagePath != null) {
                        try {
                            ImageIcon imageIcon = new ImageIcon(Constants.PROFILE_IMAGES_PATH + savedImagePath);
                            Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                            imageLabel.setIcon(new ImageIcon(image));
                            selectedProfileImagePath = savedImagePath;
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "Error loading new profile image");
                        }
                    }
                }
            }
        });
        
        imagePanel.add(changeImageButton, BorderLayout.SOUTH);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        profilePanel.add(imagePanel, gbc);
        
        gbc.gridwidth = 1;
        
        JTextField nameField = new JTextField(currentUser.getName(), 20);
        JTextField emailField = new JTextField(currentUser.getEmail(), 20);
        JPasswordField passwordField = new JPasswordField(20);
        
        gbc.gridy = 1;
        gbc.gridx = 0;
        profilePanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(nameField, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        profilePanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(emailField, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        profilePanel.add(new JLabel("New Password:"), gbc);
        gbc.gridx = 1;
        profilePanel.add(passwordField, gbc);
        
        JButton saveButton = createStyledButton("Save Changes");
        JButton backButton = createStyledButton("Back");
        
        saveButton.addActionListener(e -> {
            String password = new String(passwordField.getPassword());
            if (password.isEmpty()) {
                password = currentUser.getPassword();
            }
            
            boolean success = controller.updateProfile(
                nameField.getText(),
                emailField.getText(),
                password,
                selectedProfileImagePath
            );
            
            if (success) {
                JOptionPane.showMessageDialog(frame, "Profile updated successfully!");
                showDashboard(frame);
            } else {
                JOptionPane.showMessageDialog(frame, "Error updating profile!");
            }
        });
        
        backButton.addActionListener(e -> showDashboard(frame));
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        profilePanel.add(saveButton, gbc);
        gbc.gridx = 1;
        profilePanel.add(backButton, gbc);
        
        frame.getContentPane().removeAll();
        frame.add(profilePanel);
        frame.revalidate();
        frame.repaint();
    }

    private void logout(JFrame frame) {
        int confirm = JOptionPane.showConfirmDialog(
            frame,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.clearSession();
            frame.dispose();
            new LoginView(null).setVisible(true);
        }
    }

    private boolean validateItemForm(String description, String location, String imagePath) {
        if (description.isEmpty() || location.isEmpty() || imagePath == null) {
            JOptionPane.showMessageDialog(null, "Please fill in all fields and select an image");
            return false;
        }
        return true;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setPreferredSize(new Dimension(200, 40));
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void showAllItems(JFrame frame) {
        JPanel itemsPanel = new JPanel();
        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.Y_AXIS));
        
        // Get all items instead of just user's items
        java.util.List<Item> items = controller.getAllItems();
        if (items != null && !items.isEmpty()) {
            for (Item item : items) {
                itemsPanel.add(createItemPanel(item));
                itemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            itemsPanel.add(new JLabel("No items found"));
        }
        
        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> showDashboard(frame));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(itemsPanel), BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);
        
        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showSearchForm(JFrame frame) {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField keywordField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"All", "Pending", "Matched", "Restored"});
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"All", "Lost", "Found"});

        gbc.gridy = 0;
        searchPanel.add(new JLabel("Keyword:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(keywordField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        searchPanel.add(new JLabel("Location:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(locationField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        searchPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(statusCombo, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        searchPanel.add(new JLabel("Type:"), gbc);
        gbc.gridx = 1;
        searchPanel.add(typeCombo, gbc);

        JButton searchButton = createStyledButton("Search");
        JButton backButton = createStyledButton("Back");

        searchButton.addActionListener(e -> {
            java.util.List<Item> results = controller.searchItems(
                keywordField.getText(),
                locationField.getText(),
                statusCombo.getSelectedItem().toString(),
                typeCombo.getSelectedItem().toString()
            );
            displaySearchResults(frame, results);
        });

        backButton.addActionListener(e -> showDashboard(frame));

        gbc.gridy = 4;
        gbc.gridx = 0;
        searchPanel.add(searchButton, gbc);
        gbc.gridx = 1;
        searchPanel.add(backButton, gbc);

        frame.getContentPane().removeAll();
        frame.add(searchPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void displaySearchResults(JFrame frame, java.util.List<Item> results) {
        JPanel resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));

        if (results != null && !results.isEmpty()) {
            for (Item item : results) {
                resultsPanel.add(createItemPanel(item));
                resultsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            resultsPanel.add(new JLabel("No items found"));
        }

        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> showSearchForm(frame));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(resultsPanel), BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private void showMatchSuggestions(JFrame frame) {
        if (mainFrame == null) {
            mainFrame = frame;
        }
        mainFrame.getContentPane().removeAll();
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        
        java.util.List<Match> matches = controller.viewMatchSuggestions();
        if (matches != null && !matches.isEmpty()) {
            for (Match match : matches) {
                JPanel matchPanel = new JPanel(new BorderLayout());
                matchPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                
                Item lostItem = controller.getItemById(match.getLostItemId());
                Item foundItem = controller.getItemById(match.getFoundItemId());
                
                JLabel matchLabel = new JLabel(String.format("Match Score: %.2f%%", match.getLevenshteinScore() * 100));
                JLabel itemsLabel = new JLabel(String.format("Lost: %s | Found: %s", 
                    lostItem != null ? lostItem.getDescription() : "Unknown",
                    foundItem != null ? foundItem.getDescription() : "Unknown"));
                
                matchPanel.add(matchLabel, BorderLayout.NORTH);
                matchPanel.add(itemsLabel, BorderLayout.CENTER);
                matchesPanel.add(matchPanel);
                matchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            matchesPanel.add(new JLabel("No matches found"));
        }
        
        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> showDashboard(frame));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(matchesPanel), BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);
        
        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel createProfilePanel() {
        JPanel profilePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        profilePanel.setBackground(new Color(51, 51, 51));
        
        User currentUser = SessionManager.getCurrentUser();
        
        JLabel profileImageLabel = new JLabel();
        profileImageLabel.setPreferredSize(new Dimension(50, 50));
        
        try {
            String imagePath = currentUser.getProfileImage();
            if (imagePath != null && !imagePath.isEmpty()) {
                ImageIcon imageIcon = new ImageIcon(Constants.PROFILE_IMAGES_PATH + imagePath);
                Image image = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                profileImageLabel.setIcon(new ImageIcon(image));
            } else {
                BufferedImage defaultImage = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = defaultImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(70, 130, 180));
                g2d.fillOval(0, 0, 49, 49);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                String initial = currentUser.getName().substring(0, 1).toUpperCase();
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(initial, (50 - fm.stringWidth(initial)) / 2, ((50 - fm.getHeight()) / 2) + fm.getAscent());
                g2d.dispose();
                profileImageLabel.setIcon(new ImageIcon(defaultImage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        JLabel nameLabel = new JLabel(currentUser.getName());
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        profilePanel.add(profileImageLabel);
        profilePanel.add(nameLabel);
        
        return profilePanel;
    }

    private void createMatchSuggestion(Item selectedItem) {
        // First, verify the selected item belongs to the current user
        if (selectedItem.getOwnerId() != SessionManager.getCurrentUser().getUserId()) {
            JOptionPane.showMessageDialog(mainFrame, 
                "You can only create matches with your own items!", 
                "Invalid Operation", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(mainFrame, "Create Match", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(null);

        JPanel selectedItemPanel = new JPanel(new BorderLayout(10, 10));
        selectedItemPanel.setBorder(BorderFactory.createTitledBorder("Selected Item"));
        
        if (selectedItem.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + selectedItem.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                selectedItemPanel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.add(new JLabel("Description: " + selectedItem.getDescription()));
        detailsPanel.add(new JLabel("Location: " + selectedItem.getLocation()));
        detailsPanel.add(new JLabel("Type: " + selectedItem.getType()));
        detailsPanel.add(new JLabel("Posted by: " + getUserName(selectedItem.getOwnerId())));
        selectedItemPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel yourItemsPanel = new JPanel();
        yourItemsPanel.setLayout(new BoxLayout(yourItemsPanel, BoxLayout.Y_AXIS));
        yourItemsPanel.setBorder(BorderFactory.createTitledBorder("Select Your Item"));
        
        List<Item> userItems = controller.viewMyItems();
        ButtonGroup buttonGroup = new ButtonGroup();

        for (Item item : userItems) {
            if (item.getStatus() == Item.Status.Pending) {
                JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                
                JRadioButton radioBtn = new JRadioButton();
                buttonGroup.add(radioBtn);
                radioBtn.setActionCommand(String.valueOf(item.getItemId()));
                itemPanel.add(radioBtn, BorderLayout.WEST);
                
                if (item.getImagePath() != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + item.getImagePath());
                        Image image = imageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(image));
                        itemPanel.add(imageLabel, BorderLayout.CENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                JPanel itemDetails = new JPanel(new GridLayout(3, 1));
                itemDetails.add(new JLabel("Description: " + item.getDescription()));
                itemDetails.add(new JLabel("Location: " + item.getLocation()));
                itemDetails.add(new JLabel("Type: " + item.getType()));
                itemPanel.add(itemDetails, BorderLayout.EAST);
                
                yourItemsPanel.add(itemPanel);
                yourItemsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createButton = createStyledButton("Create Match");
        JButton cancelButton = createStyledButton("Cancel");

        createButton.addActionListener(e -> {
            ButtonModel selectedButton = buttonGroup.getSelection();
            if (selectedButton != null) {
                int userItemId = Integer.parseInt(selectedButton.getActionCommand());
                boolean success = controller.createMatch(userItemId, selectedItem.getItemId());
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Match suggestion created successfully!\nWaiting for admin approval.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error creating match suggestion");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select one of your items to match");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(selectedItemPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(yourItemsPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private JPanel createItemDetailsPanel(Item item, String title) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        if (title != null) {
            panel.setBorder(BorderFactory.createTitledBorder(title));
        }

        if (item.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + item.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                panel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.add(new JLabel("Description: " + item.getDescription()));
        detailsPanel.add(new JLabel("Location: " + item.getLocation()));
        detailsPanel.add(new JLabel("Date: " + item.getDate()));
        detailsPanel.add(new JLabel("Posted by: " + getUserName(item.getOwnerId())));
        
        panel.add(detailsPanel, BorderLayout.CENTER);
        return panel;
    }

    private String getUserName(int userId) {
        User user = controller.getUserById(userId);
        return user != null ? user.getName() : "Unknown User";
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

    private JPanel createItemPanel(Item item) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        panel.setBackground(new Color(240, 240, 240)); 

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

        
        if (item.getOwnerId() != SessionManager.getCurrentUser().getUserId()) {
            JButton createMatchButton = createStyledButton("Suggest Match");
            createMatchButton.addActionListener(e -> showMatchDialog(item));
            panel.add(createMatchButton, BorderLayout.EAST);
        }

        panel.add(detailsPanel, BorderLayout.CENTER);
        return panel;
    }

    private void showMatchDialog(Item otherItem) {
        JDialog dialog = new JDialog(mainFrame, "Select Your Item to Match", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(null);

        JPanel selectedItemPanel = new JPanel(new BorderLayout(10, 10));
        selectedItemPanel.setBorder(BorderFactory.createTitledBorder("Selected Item"));
        
        if (otherItem.getImagePath() != null) {
            try {
                ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + otherItem.getImagePath());
                Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                JLabel imageLabel = new JLabel(new ImageIcon(image));
                selectedItemPanel.add(imageLabel, BorderLayout.WEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        JPanel detailsPanel = new JPanel(new GridLayout(4, 1));
        detailsPanel.add(new JLabel("Description: " + otherItem.getDescription()));
        detailsPanel.add(new JLabel("Location: " + otherItem.getLocation()));
        detailsPanel.add(new JLabel("Type: " + otherItem.getType()));
        detailsPanel.add(new JLabel("Posted by: " + getUserName(otherItem.getOwnerId())));
        selectedItemPanel.add(detailsPanel, BorderLayout.CENTER);

        JPanel yourItemsPanel = new JPanel();
        yourItemsPanel.setLayout(new GridLayout(0, 2, 10, 10)); 
        yourItemsPanel.setBorder(BorderFactory.createTitledBorder("Select Your Item"));
        
        List<Item> userItems = controller.viewMyItems();
        ButtonGroup buttonGroup = new ButtonGroup();

        for (Item item : userItems) {
            if (item.getStatus() == Item.Status.Pending) {
                JPanel itemPanel = new JPanel(new BorderLayout(10, 10));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                itemPanel.setBackground(new Color(240, 240, 240)); 
                
                JRadioButton radioBtn = new JRadioButton();
                buttonGroup.add(radioBtn);
                radioBtn.setActionCommand(String.valueOf(item.getItemId()));
                itemPanel.add(radioBtn, BorderLayout.WEST);
                
                if (item.getImagePath() != null) {
                    try {
                        ImageIcon imageIcon = new ImageIcon(Constants.ITEM_IMAGES_PATH + item.getImagePath());
                        Image image = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        JLabel imageLabel = new JLabel(new ImageIcon(image));
                        itemPanel.add(imageLabel, BorderLayout.CENTER);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                
                JPanel itemDetails = new JPanel(new GridLayout(3, 1));
                itemDetails.add(new JLabel("Description: " + item.getDescription()));
                itemDetails.add(new JLabel("Location: " + item.getLocation()));
                itemDetails.add(new JLabel("Type: " + item.getType()));
                itemPanel.add(itemDetails, BorderLayout.EAST);
                
                yourItemsPanel.add(itemPanel);
            }
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createButton = createStyledButton("Create Match");
        JButton cancelButton = createStyledButton("Cancel");

        createButton.addActionListener(e -> {
            ButtonModel selectedButton = buttonGroup.getSelection();
            if (selectedButton != null) {
                int userItemId = Integer.parseInt(selectedButton.getActionCommand());
                boolean success = controller.createMatch(userItemId, otherItem.getItemId());
                
                if (success) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Match suggestion created successfully!\nWaiting for admin approval.");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error creating match suggestion");
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select one of your items to match");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        buttonPanel.add(createButton);
        buttonPanel.add(cancelButton);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(selectedItemPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(yourItemsPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(mainPanel);
        dialog.setVisible(true);
    }

    private void showMyMatches(JFrame frame) {
        JPanel matchesPanel = new JPanel();
        matchesPanel.setLayout(new BoxLayout(matchesPanel, BoxLayout.Y_AXIS));
        
        List<Match> matches = controller.getMyMatches();
        
        if (matches != null && !matches.isEmpty()) {
            for (Match match : matches) {
                JPanel matchPanel = new JPanel(new BorderLayout(10, 10));
                matchPanel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GRAY),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
                ));
                matchPanel.setMaximumSize(new Dimension(800, 250));
                
                Item lostItem = controller.getItemById(match.getLostItemId());
                Item foundItem = controller.getItemById(match.getFoundItemId());
                
                JPanel itemsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
                
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
                lostDetailsPanel.add(new JLabel("Posted by: " + getUserName(lostItem.getOwnerId())));
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
                foundDetailsPanel.add(new JLabel("Posted by: " + getUserName(foundItem.getOwnerId())));
                foundItemPanel.add(foundDetailsPanel, BorderLayout.CENTER);
                
                itemsPanel.add(lostItemPanel);
                itemsPanel.add(foundItemPanel);
                
                JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                JLabel statusLabel = new JLabel("Status: " + match.getStatus());
                statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
                if (match.getStatus() == Match.Status.Confirmed) {
                    statusLabel.setForeground(new Color(0, 150, 0));  
                } else if (match.getStatus() == Match.Status.Rejected) {
                    statusLabel.setForeground(new Color(150, 0, 0)); 
                }
                statusPanel.add(statusLabel);
                
                matchPanel.add(itemsPanel, BorderLayout.CENTER);
                matchPanel.add(statusPanel, BorderLayout.SOUTH);
                
                matchesPanel.add(matchPanel);
                matchesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        } else {
            JLabel noMatchesLabel = new JLabel("No matches found");
            noMatchesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            matchesPanel.add(noMatchesLabel);
        }
        
        JButton backButton = createStyledButton("Back");
        backButton.addActionListener(e -> showDashboard(frame));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(matchesPanel), BorderLayout.CENTER);
        mainPanel.add(backButton, BorderLayout.SOUTH);
        
        frame.getContentPane().removeAll();
        frame.add(mainPanel);
        frame.revalidate();
        frame.repaint();
    }
}
    
