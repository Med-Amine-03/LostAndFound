package com.lostandfound.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.lostandfound.controller.RegisterController;

public class RegisterView {
    private JFrame frame;
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JLabel profileImageLabel;
    private RegisterController registerController;

    public RegisterView(RegisterController registerController) {
        this.registerController = registerController;
        initializeUI();
    }

    private void initializeUI() {
        frame = new JFrame("Register");
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        profileImageLabel = new JLabel("No image selected");

        roleComboBox = new JComboBox<>(new String[]{"Admin", "NormalUser"});
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());

        JButton selectImageButton = new JButton("Select Image");
        selectImageButton.addActionListener(e -> handleImageSelection());

        frame.setLayout(new FlowLayout());
        frame.add(new JLabel("Name"));
        frame.add(nameField);
        frame.add(new JLabel("Email"));
        frame.add(emailField);
        frame.add(new JLabel("Password"));
        frame.add(passwordField);
        frame.add(new JLabel("Role"));
        frame.add(roleComboBox);
        frame.add(selectImageButton);
        frame.add(profileImageLabel);
        frame.add(registerButton);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void handleRegister() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        String profileImage = profileImageLabel.getText();

        boolean success = registerController.handleRegister(name, email, password, role, profileImage);

        if (success) {
            JOptionPane.showMessageDialog(frame, "Registration successful!");
            frame.dispose();
        } else {
            JOptionPane.showMessageDialog(frame, "Registration failed.");
        }
    }

    private void handleImageSelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image files", "jpg", "png", "jpeg"));
        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            profileImageLabel.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void setVisible(boolean visible) {
        frame.setVisible(visible);
    }
}
