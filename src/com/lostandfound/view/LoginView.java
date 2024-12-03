package com.lostandfound.view;

import com.lostandfound.controller.LoginController;
import com.lostandfound.utils.SessionManager;
import com.lostandfound.controller.RegisterController;
import com.lostandfound.dao.UserDAO;
import com.lostandfound.config.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private LoginController loginController;

    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;

    public LoginView(LoginController loginController) {
        this.loginController = loginController;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        setLayout(new FlowLayout());
        add(new JLabel("Email:"));
        add(emailField);
        add(new JLabel("Password:"));
        add(passwordField);
        add(loginButton);
        add(registerButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                if (loginController.login(email, password)) {
                    String role = SessionManager.getCurrentUserRole();
                    if ("Admin".equals(role)) {
                        System.out.println("Admin logged in. Redirecting to Admin Dashboard...");
                    } else if ("NormalUser".equals(role)) {
                        System.out.println("Normal User logged in. Redirecting to Normal User Dashboard...");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RegisterView(new RegisterController(new UserDAO(DBConnection.getConnection()))).setVisible(true);
                dispose();  
            }
        });
    }
}
