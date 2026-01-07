/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManageVotersPanel extends JFrame {
    private JTable voterTable;
    private DefaultTableModel tableModel;
    private JButton refreshBtn, deleteBtn, closeBtn;
    
    public ManageVotersPanel() {
        setTitle("Manage Voters");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JLabel title = new JLabel("Voter Management", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
        
        // Create table
        String[] columns = {"Voter ID", "Name", "Has Voted", "Is Admin", "Registered Date"};
        tableModel = new DefaultTableModel(columns, 0);
        voterTable = new JTable(tableModel);
        
        // Load data
        loadVoters();
        
        JScrollPane scrollPane = new JScrollPane(voterTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        refreshBtn = new JButton("Refresh");
        deleteBtn = new JButton("Delete Selected");
        closeBtn = new JButton("Close");
        
        refreshBtn.addActionListener(e -> loadVoters());
        deleteBtn.addActionListener(e -> deleteVoter());
        closeBtn.addActionListener(e -> this.dispose());
        
        buttonPanel.add(refreshBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(closeBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void loadVoters() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT voter_id, name, has_voted, is_admin, registered_date FROM voters ORDER BY voter_id");
            
            tableModel.setRowCount(0);
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getInt("voter_id"),
                    rs.getString("name"),
                    rs.getBoolean("has_voted") ? "Yes" : "No",
                    rs.getBoolean("is_admin") ? "Yes" : "No",
                    rs.getTimestamp("registered_date")
                });
            }
            
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading voters: " + e.getMessage(),
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteVoter() {
        int selectedRow = voterTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a voter to delete", 
                "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int voterId = (int) tableModel.getValueAt(selectedRow, 0);
        String voterName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete voter?\n" +
            "ID: " + voterId + "\n" +
            "Name: " + voterName,
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
                
                // Delete voter
                PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM voters WHERE voter_id = ?");
                stmt.setInt(1, voterId);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this,
                    "Voter deleted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                loadVoters(); // Refresh table
                conn.close();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                    "Error deleting voter: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}