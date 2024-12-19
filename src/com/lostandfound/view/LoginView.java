package com.lostandfound.view;

import com.lostandfound.config.DBConnection;
import com.lostandfound.controller.AdminDashboardController;
import com.lostandfound.controller.LoginController;
import com.lostandfound.controller.NormalUserController;
import com.lostandfound.controller.RegisterController;
import com.lostandfound.dao.ItemDAO;
import com.lostandfound.dao.MatchDAO;
import com.lostandfound.dao.UserDAO;
import com.lostandfound.service.ItemService;
import com.lostandfound.service.MatchService;
import com.lostandfound.service.UserService;
import com.lostandfound.utils.SessionManager;
import java.awt.*;
import javax.swing.*;

public class LoginView extends JFrame {
    private LoginController loginController;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Lost & Found System - Login");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(66, 139, 202);
                Color color2 = new Color(51, 51, 51);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));

        JLabel titleLabel = new JLabel("Lost & Found");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 10, 10));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(300, 200));

        emailField = createStyledTextField("Email");
        formPanel.add(emailField);

        passwordField = createStyledPasswordField("Password");
        formPanel.add(passwordField);

        JButton loginButton = createStyledButton("Login");
        loginButton.addActionListener(e -> handleLogin());
        formPanel.add(loginButton);

        JButton registerButton = createStyledButton("Create Account");
        registerButton.setBackground(new Color(40, 167, 69));
        registerButton.addActionListener(e -> {
            dispose();
            new RegisterView(new RegisterController(new UserDAO(DBConnection.getConnection()))).setVisible(true);
        });
        formPanel.add(registerButton);

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

    private void handleLogin() {
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        
        if (loginController.login(email, password)) {
            String role = SessionManager.getCurrentUserRole();
            System.out.println("User role: " + role);
            
            if ("NormalUser".equals(role)) {
                System.out.println("Normal User logged in. Redirecting to Normal User Dashboard...");
                dispose();
                
                UserDAO userDAO = new UserDAO(DBConnection.getConnection());
                ItemDAO itemDAO = new ItemDAO(DBConnection.getConnection());
                MatchDAO matchDAO = new MatchDAO(DBConnection.getConnection());
                
                UserService userService = new UserService(userDAO);
                ItemService itemService = new ItemService(itemDAO);
                MatchService matchService = new MatchService(matchDAO);
                
                NormalUserController normalUserController = new NormalUserController(userService, itemService, matchService);
                
                JFrame dashboardFrame = new JFrame("Normal User Dashboard");
                dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                dashboardFrame.setSize(1000, 700);
                dashboardFrame.setLocationRelativeTo(null);
                
                NormalUserDashboardView dashboard = new NormalUserDashboardView(normalUserController);
                dashboard.showDashboard(dashboardFrame);
                
                dashboardFrame.setVisible(true);
                
            } else if ("Admin".equals(role)) {
                System.out.println("Admin logged in. Redirecting to Admin Dashboard...");
                dispose();
                
                UserDAO userDAO = new UserDAO(DBConnection.getConnection());
                ItemDAO itemDAO = new ItemDAO(DBConnection.getConnection());
                MatchDAO matchDAO = new MatchDAO(DBConnection.getConnection());
                
                UserService userService = new UserService(userDAO);
                ItemService itemService = new ItemService(itemDAO);
                MatchService matchService = new MatchService(matchDAO);
                
                AdminDashboardController adminController = new AdminDashboardController(userService, itemService, matchService);
                new AdminDashboardView(adminController);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid email or password");
        }
    }
}
