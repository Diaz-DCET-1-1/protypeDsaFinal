/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDataPanel extends JFrame {
    private JTabbedPane tabbedPane;
    
    public ViewDataPanel() {
        setTitle("Database Data Viewer");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        
        // Create tabs for each table
        tabbedPane.addTab("Voters", createTablePanel(
            "SELECT voter_id as 'Voter ID', name as 'Name', " +
            "CASE WHEN has_voted THEN 'Yes' ELSE 'No' END as 'Voted', " +
            "CASE WHEN is_admin THEN 'Yes' ELSE 'No' END as 'Admin', " +
            "registered_date as 'Registered' FROM voters ORDER BY voter_id"));
        
        tabbedPane.addTab("Candidates", createTablePanel(
            "SELECT candidate_id as 'Candidate ID', name as 'Name', " +
            "party as 'Party', vote_count as 'Votes' FROM candidates ORDER BY candidate_id"));
        
        tabbedPane.addTab("Votes", createTablePanel(
            "SELECT vote_id as 'Vote ID', voter_id as 'Voter ID', " +
            "candidate_id as 'Candidate ID', vote_timestamp as 'Time' FROM votes ORDER BY vote_id"));
        
        tabbedPane.addTab("Statistics", createStatsPanel());
        
        add(tabbedPane);
    }
    
    private JScrollPane createTablePanel(String query) {
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metaData = rs.getMetaData();
            
            // Add columns
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnLabel(i));
            }
            
            // Add rows
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
            
            conn.close();
        } catch (SQLException e) {
            model.addColumn("Error");
            model.addRow(new Object[]{"Database error: " + e.getMessage()});
        }
        
        return new JScrollPane(table);
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
            
            // Get statistics
            String[] statsQueries = {
                "Total Voters:SELECT COUNT(*) FROM voters",
                "Total Candidates:SELECT COUNT(*) FROM candidates",
                "Total Votes Cast:SELECT COUNT(*) FROM votes",
                "Voted Percentage:SELECT ROUND((SUM(has_voted)/COUNT(*)*100), 2) FROM voters",
                "Leading Candidate:SELECT CONCAT(name, ' (', party, ') - ', vote_count, ' votes') " +
                                 "FROM candidates ORDER BY vote_count DESC LIMIT 1",
                "Total Admins:SELECT COUNT(*) FROM voters WHERE is_admin = TRUE"
            };
            
            for (String statQuery : statsQueries) {
                String[] parts = statQuery.split(":", 2);
                String label = parts[0];
                String query = parts[1];
                
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                
                String value = "N/A";
                if (rs.next()) {
                    Object result = rs.getObject(1);
                    value = (result != null) ? result.toString() : "0";
                    if (label.equals("Voted Percentage")) value += "%";
                }
                
                JLabel statLabel = new JLabel(label + ": " + value);
                statLabel.setFont(new Font("Arial", Font.BOLD, 16));
                panel.add(statLabel);
            }
            
            conn.close();
        } catch (SQLException e) {
            panel.add(new JLabel("Error: " + e.getMessage()));
        }
        
        return panel;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ViewDataPanel viewer = new ViewDataPanel();
            viewer.setVisible(true);
        });
    }
}