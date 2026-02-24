package com.javaswing.practicedb;

import java.awt.EventQueue;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TableRecord extends JFrame { // Fixed to match your file name

    private static final long serialVersionUID = 1L;

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static String query;
    JTable table;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                TableRecord window = new TableRecord(); // Fixed here too
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public TableRecord() { // And fixed the constructor
        initialize();
    }

    public static void dbConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsample", "root", "root");
            stmt = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Failed!");
        }
    }

    private void initialize() {
        setTitle("SAMPLE DATABASE"); 
        setBounds(200, 200, 469, 361); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        showRecords();
    }

    public void showRecords() {
        try {
            dbConnect();
            query = "SELECT * FROM tblcars"; 
            rs = stmt.executeQuery(query);

            String[] columns = {"ID", "NAME", "YEAR", "RATE", "AVAIL"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            int rowCount = 0;

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String year = rs.getString("year");
                String rate = rs.getString("dailyRate");
                String avail = rs.getBoolean("isAvailable") ? "Yes" : "No";
                
                model.addRow(new Object[]{id, name, year, rate, avail});
                rowCount++;
            }

            while (rowCount < 10) {
                model.addRow(new Object[]{"", "", "", "", ""});
                rowCount++;
            }

            table = new JTable(model);
            table.setFillsViewportHeight(true); 

            getContentPane().add(new JScrollPane(table));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}