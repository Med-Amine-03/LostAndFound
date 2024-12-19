package com.lostandfound.view;

import com.lostandfound.config.DBConnection;
import com.lostandfound.controller.LoginController;
import com.lostandfound.controller.RegisterController;
import com.lostandfound.dao.UserDAO;
import java.awt.*;
import javax.swing.*;

public class RegisterView extends JFrame {
    private RegisterController registerController;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JLabel profileImageLabel;
    private String selectedImagePath;

    public RegisterView(RegisterController registerController) {
        this.registerController = registerController;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Lost & Found System - Register");
        setSize(450, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(51, 51, 51);
                Color color2 = new Color(66, 139, 202);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel titleLabel = new JLabel("Create Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(6, 1, 15, 15));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(350, 400));

        nameField = createStyledTextField("Full Name");
        emailField = createStyledTextField("Email");
        passwordField = createStyledPasswordField("Password");
        
        JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        imagePanel.setOpaque(false);
        profileImageLabel = new JLabel("No image selected");
        profileImageLabel.setForeground(Color.WHITE);
        JButton selectImageButton = createStyledButton("Select Profile Picture");
        selectImageButton.setBackground(new Color(108, 117, 125));
        selectImageButton.addActionListener(e -> handleImageSelection());
        imagePanel.add(selectImageButton);
        imagePanel.add(profileImageLabel);

        formPanel.add(nameField);
        formPanel.add(emailField);
        formPanel.add(passwordField);
        formPanel.add(imagePanel);

        JButton registerButton = createStyledButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        
        JButton backButton = createStyledButton("Back to Login");
        backButton.setBackground(new Color(108, 117, 125));
        backButton.addActionListener(e -> {
            dispose();
            new LoginView(new LoginController(new UserDAO(DBConnection.getConnection()))).setVisible(true);
        });

        formPanel.add(registerButton);
        formPanel.add(backButton);

        mainPanel.add(formPanel);
        add(mainPanel);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 220));
        return field;
    }

    private JPasswordField createStyledPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(new Color(255, 255, 255, 220));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String profileImage = selectedImagePath;
        String role = "NormalUser";  // Set default role

        boolean success = registerController.handleRegister(name, email, password, role, profileImage);

        if (success) {
            JOptionPane.showMessageDialog(this, "Registration successful! Please login.");
            dispose();
            new LoginView(new LoginController(new UserDAO(DBConnection.getConnection()))).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed.");
        }
    }

    private void handleImageSelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImagePath = fileChooser.getSelectedFile().getAbsolutePath();
            profileImageLabel.setText(selectedImagePath);
        }
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }
}
