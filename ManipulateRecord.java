package com.javaswing.practicedb;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class ManipulateRecord extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtId, txtName, txtYear, txtDailyRate;
    private JCheckBox chkAvailable; 
    private JButton btnNew, btnSave, btnDelete, btnUpdate, btnSearch;

    static Connection conn;
    static Statement stmt;
    static ResultSet rs;
    static String query;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ManipulateRecord window = new ManipulateRecord();
                window.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ManipulateRecord() {
        setTitle("SAMPLE DATABASE");
        initialize();
        dbConnect();

        try {
            query = "SELECT * FROM tblcars";
            rs = stmt.executeQuery(query);
            if (rs.first()) {
                displayValues();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void dbConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbsample", "root", "root");
            // Keeps the ResultSet updatable for our SAVE button
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Connection Failed! Please check if MySQL is running.");
        }
    }

    public void displayValues() throws SQLException {
        txtId.setText(rs.getString("id"));
        txtName.setText(rs.getString("name"));
        txtYear.setText(rs.getString("year"));
        txtDailyRate.setText(rs.getString("dailyRate"));
        chkAvailable.setSelected(rs.getBoolean("isAvailable"));
    }

    private void initialize() {
        setBounds(100, 100, 420, 260); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JLabel lblId = new JLabel("ID #:");
        lblId.setBounds(25, 26, 46, 14);
        getContentPane().add(lblId);

        JLabel lblName = new JLabel("NAME:");
        lblName.setBounds(25, 57, 46, 14);
        getContentPane().add(lblName);

        JLabel lblYear = new JLabel("YEAR:");
        lblYear.setBounds(25, 88, 46, 14);
        getContentPane().add(lblYear);

        JLabel lblRate = new JLabel("RATE:");
        lblRate.setBounds(25, 122, 46, 14);
        getContentPane().add(lblRate);

        JLabel lblAvail = new JLabel("AVAIL:");
        lblAvail.setBounds(25, 155, 46, 14);
        getContentPane().add(lblAvail);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBounds(81, 23, 86, 20);
        getContentPane().add(txtId);

        txtName = new JTextField();
        txtName.setBounds(81, 54, 150, 20);
        getContentPane().add(txtName);

        txtYear = new JTextField();
        txtYear.setBounds(81, 85, 86, 20);
        getContentPane().add(txtYear);

        txtDailyRate = new JTextField();
        txtDailyRate.setBounds(81, 116, 86, 20);
        getContentPane().add(txtDailyRate);

        chkAvailable = new JCheckBox();
        chkAvailable.setBounds(81, 152, 50, 20);
        getContentPane().add(chkAvailable);

        btnNew = new JButton("NEW");
        btnNew.setBounds(180, 20, 180, 23);
        getContentPane().add(btnNew);

        btnSave = new JButton("SAVE");
        btnSave.setBounds(180, 51, 85, 23);
        getContentPane().add(btnSave);

        btnUpdate = new JButton("UPDATE");
        btnUpdate.setBounds(275, 51, 85, 23);
        getContentPane().add(btnUpdate);

        btnDelete = new JButton("DELETE");
        btnDelete.setBounds(180, 82, 180, 23);
        getContentPane().add(btnDelete);

        btnSearch = new JButton("SEARCH");
        btnSearch.setBounds(220, 113, 100, 23);
        getContentPane().add(btnSearch);

        // Listeners
        btnSearch.addActionListener(this);
        btnNew.addActionListener(this);
        btnSave.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // SEARCH
        if (e.getSource() == btnSearch) {
            try {
                String idnum = JOptionPane.showInputDialog("Type id number : ");
                query = "SELECT * FROM tblcars WHERE id='" + idnum + "'";
                rs = stmt.executeQuery(query);

                if (rs.next()) {
                    displayValues();
                } else {
                    JOptionPane.showMessageDialog(null, "id number not found...");
                    query = "SELECT * FROM tblcars";
                    rs = stmt.executeQuery(query);
                    if (rs.first()) displayValues();
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        }

        // NEW
        if (e.getSource() == btnNew) {
            txtName.setText("");
            txtYear.setText("");
            txtDailyRate.setText("");
            chkAvailable.setSelected(false);
            txtId.requestFocus();

            try {
                query = "SELECT * FROM tblcars";
                rs = stmt.executeQuery(query);
                rs.last(); 
                int nextId = Integer.parseInt(rs.getString("id")) + 1;
                txtId.setText(String.valueOf(nextId));
            } catch (SQLException ex) { ex.printStackTrace();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid ID data in table.");
            }
        }

        // SAVE
        if (e.getSource() == btnSave) {
            try {
                // Refreshing the ResultSet right before saving prevents the "closed" error
                query = "SELECT * FROM tblcars";
                rs = stmt.executeQuery(query);

                rs.moveToInsertRow();
                rs.updateString("id", txtId.getText());
                rs.updateString("name", txtName.getText());
                rs.updateString("year", txtYear.getText());
                rs.updateString("dailyRate", txtDailyRate.getText());
                rs.updateBoolean("isAvailable", chkAvailable.isSelected());
                rs.insertRow();

                JOptionPane.showMessageDialog(null, "Record saved...");
                
                // Reload data to keep the window updated
                query = "SELECT * FROM tblcars";
                rs = stmt.executeQuery(query);
                if (rs.last()) displayValues();

            } catch (SQLException ex) { ex.printStackTrace(); }
        }

        // UPDATE
        if (e.getSource() == btnUpdate) {
            try {
                int isAvail = chkAvailable.isSelected() ? 1 : 0;
                
                query = "UPDATE tblcars SET name='" + txtName.getText()
                        + "', year='" + txtYear.getText()
                        + "', dailyRate='" + txtDailyRate.getText()
                        + "', isAvailable=" + isAvail
                        + " WHERE id='" + txtId.getText() + "'";

                int r = stmt.executeUpdate(query);
                if (r > 0) {
                    JOptionPane.showMessageDialog(null, "Record updated...");
                    // Refresh ResultSet right after updating
                    query = "SELECT * FROM tblcars";
                    rs = stmt.executeQuery(query);
                    if (rs.first()) displayValues();
                } else {
                    JOptionPane.showMessageDialog(null, "Error occurred...");
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        }

        // DELETE
        if (e.getSource() == btnDelete) {
            int res = JOptionPane.showConfirmDialog(null, "Delete Record?", "Delete", JOptionPane.YES_NO_OPTION);
            if (res == JOptionPane.YES_OPTION) {
                try {
                    query = "DELETE FROM tblcars WHERE id='" + txtId.getText() + "'";
                    int r = stmt.executeUpdate(query);

                    if (r > 0) {
                        JOptionPane.showMessageDialog(null, "A user was deleted successfully!");
                        // Refresh ResultSet right after deleting
                        query = "SELECT * FROM tblcars";
                        rs = stmt.executeQuery(query);
                        if (rs.first()) displayValues();
                    }
                } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
    }
}