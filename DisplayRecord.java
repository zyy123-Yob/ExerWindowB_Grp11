package com.javaswing.practicedb;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DisplayRecord extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtYear; // Fixed label Name
    private JTextField txtDailyRate; // Fixed label Name
    private JCheckBox chkAvailable; // Added for project requirements

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DisplayRecord window = new DisplayRecord();
                    window.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public DisplayRecord() {
        initialize();
        dbConnect(); 
        loadData();
    }

    public static void dbConnect() {
        try {
            // Using your root/root credentials
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsample", "root", "root");
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Failed!");
            e.printStackTrace();
        }
    }

    private void loadData() {
        try {
            rs = stmt.executeQuery("SELECT * FROM tblcars");
            if (rs.next()) displayValues();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void displayValues() throws SQLException {
        // Mapping ResultSet to your specific project variables
        txtId.setText(rs.getString("id"));
        txtName.setText(rs.getString("name"));
        txtYear.setText(rs.getString("year")); 
        txtDailyRate.setText(rs.getString("dailyRate")); 
        chkAvailable.setSelected(rs.getBoolean("isAvailable"));
    }

    private void initialize() {
        setTitle("CAR INVENTORY - GROUP 11"); 
        setBounds(100, 100, 420, 250); // Adjusted height for visibility
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        getContentPane().setLayout(null); 

        // ID Field
        JLabel lblId = new JLabel("ID #:"); 
        lblId.setBounds(22, 21, 80, 14); 
        getContentPane().add(lblId);

        txtId = new JTextField(); 
        txtId.setBounds(120, 18, 100, 20); 
        txtId.setEditable(false);
        getContentPane().add(txtId);

        // NAME Field (Combined Make/Model)
        JLabel lblName = new JLabel("NAME:"); 
        lblName.setBounds(22, 52, 80, 14); 
        getContentPane().add(lblName);

        txtName = new JTextField(); 
        txtName.setBounds(120, 49, 150, 20); 
        getContentPane().add(txtName);

        // YEAR Field (Replaced Age)
        JLabel lblYear = new JLabel("YEAR:"); 
        lblYear.setBounds(22, 83, 80, 14); 
        getContentPane().add(lblYear);

        txtYear = new JTextField(); 
        txtYear.setBounds(120, 80, 100, 20); 
        getContentPane().add(txtYear);

        // DAILY RATE Field (Replaced Sex)
        JLabel lblRate = new JLabel("DAILY RATE:"); 
        lblRate.setBounds(22, 117, 80, 14); 
        getContentPane().add(lblRate);

        txtDailyRate = new JTextField(); 
        txtDailyRate.setBounds(120, 114, 100, 20); 
        getContentPane().add(txtDailyRate);

        // AVAILABILITY Checkbox
        JLabel lblAvail = new JLabel("AVAIL:");
        lblAvail.setBounds(22, 150, 80, 14);
        getContentPane().add(lblAvail);

        chkAvailable = new JCheckBox();
        chkAvailable.setBounds(120, 147, 50, 20);
        chkAvailable.setEnabled(false);
        getContentPane().add(chkAvailable);

        // --- Buttons ---
        JButton btnFirst = new JButton("FIRST"); 
        btnFirst.setBounds(280, 17, 100, 23); 
        getContentPane().add(btnFirst);
        btnFirst.addActionListener(e -> { try { rs.first(); displayValues(); } catch (Exception ex) {} });

        JButton btnNext = new JButton("Next"); 
        btnNext.setBounds(280, 48, 100, 23); 
        getContentPane().add(btnNext);
        btnNext.addActionListener(e -> {
            try {
                if (!rs.isLast()) { rs.next(); displayValues(); } 
                else { JOptionPane.showMessageDialog(null, "Last record reached."); }
            } catch (Exception ex) {}
        });

        JButton btnPrev = new JButton("Prev"); 
        btnPrev.setBounds(280, 79, 100, 23); 
        getContentPane().add(btnPrev);
        btnPrev.addActionListener(e -> {
            try {
                if (!rs.isFirst()) { rs.previous(); displayValues(); } 
                else { JOptionPane.showMessageDialog(null, "First record reached."); }
            } catch (Exception ex) {}
        });

        JButton btnSearch = new JButton("SEARCH"); 
        btnSearch.setBounds(280, 113, 100, 30); 
        getContentPane().add(btnSearch);
        btnSearch.addActionListener(e -> {
            String val = JOptionPane.showInputDialog("Enter ID to search:"); 
            try {
                rs = stmt.executeQuery("SELECT * FROM tblcars WHERE id='" + val + "'"); 
                if (rs.next()) displayValues(); 
                else { JOptionPane.showMessageDialog(null, "ID not found."); loadData(); } 
            } catch (Exception ex) {}
        });
    }
}
