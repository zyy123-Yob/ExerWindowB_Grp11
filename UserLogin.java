package com.javaswing.practicedb;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class UserLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UserLogin window = new UserLogin();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void dbConnect() {
        try {
            // Updated with root/root credentials
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsample", "root", "root"); 
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch(Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Failed! Check if MySQL service is running."); 
            e.printStackTrace();
        }
    }

    public UserLogin() {
        setTitle("GROUP 11 - CAR RENTAL LOGIN");
        setBounds(100, 100, 400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 70, 100, 25);
        getContentPane().add(lblUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(150, 70, 150, 25);
        getContentPane().add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 110, 100, 25);
        getContentPane().add(lblPass);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(150, 110, 150, 25);
        getContentPane().add(txtPassword);

        JButton btnSubmit = new JButton("SUBMIT");
        btnSubmit.setBounds(80, 180, 105, 41);
        btnSubmit.addActionListener(e -> {
            try {
                String query = "SELECT * FROM tbluser WHERE uname='" + txtUsername.getText() + "' AND pword='" + txtPassword.getText() + "'";
                rs = stmt.executeQuery(query);
                if (rs.next()) {
                    JOptionPane.showMessageDialog(null, "password accepted..."); 
                    // Link to the next window
                    DisplayRecord dr = new DisplayRecord();
                    dr.setVisible(true);
                    dispose(); 
                } else {
                    JOptionPane.showMessageDialog(null, "username and/or password does not match...");
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        });
        getContentPane().add(btnSubmit);
        
        dbConnect();
        getRootPane().setDefaultButton(btnSubmit); // Enables Enter key submission
    }
}