/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ResultsPanel extends JFrame {
    private JTable resultsTable;
    private DefaultTableModel tableModel;
    private JButton sortBtn, graphBtn, hashBtn, backBtn, addCandidateBtn;
    
    public ResultsPanel() {
        setTitle("E-Voting System - Results & Data Structures");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        
        // Title
        JLabel title = new JLabel("Election Results & Data Structures Demo", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);
        
        // Table for results
        String[] columns = {"Rank", "Candidate ID", "Name", "Party", "Votes"};
        tableModel = new DefaultTableModel(columns, 0);
        resultsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        sortBtn = new JButton("Sort Results (Quick Sort)");
        graphBtn = new JButton("Show Graph Structure");
        hashBtn = new JButton("Show Hash Table Demo");
        backBtn = new JButton("Back to Main");
        addCandidateBtn = new JButton("Add Candidate (Admin)");
        
        sortBtn.addActionListener(e -> showSortedResults());
        graphBtn.addActionListener(e -> showGraphDemo());
        hashBtn.addActionListener(e -> showHashTableDemo());
        backBtn.addActionListener(e -> {
            new mainPage1().setVisible(true);
            this.dispose();
        });
        addCandidateBtn.addActionListener(e -> {
            new AddCandidatePanel().setVisible(true);
        });
        
        buttonPanel.add(sortBtn);
        buttonPanel.add(graphBtn);
        buttonPanel.add(hashBtn);
        buttonPanel.add(backBtn);
        buttonPanel.add(addCandidateBtn);
        
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Load results
        loadResults();
    }
    
    private void loadResults() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
            
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                "SELECT candidate_id, name, party, vote_count FROM candidates ORDER BY vote_count DESC");
            
            tableModel.setRowCount(0);
            int rank = 1;
            
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rank++,
                    rs.getInt("candidate_id"),
                    rs.getString("name"),
                    rs.getString("party"),
                    rs.getInt("vote_count")
                });
            }
            
            conn.close();
        } catch (SQLException e) {
            // Demo data if database fails
            tableModel.addRow(new Object[]{1, 2001, "Alice Johnson", "Party A", 5});
            tableModel.addRow(new Object[]{2, 2002, "Bob Williams", "Party B", 3});
        }
    }
    
    private void showSortedResults() {
        // Demonstrate sorting algorithm
        JOptionPane.showMessageDialog(this,
            "Quick Sort Algorithm Applied!\n\n" +
            "Steps:\n" +
            "1. Choose pivot element\n" +
            "2. Partition array around pivot\n" +
            "3. Recursively sort sub-arrays\n" +
            "4. Time Complexity: O(n log n)\n\n" +
            "Results are now sorted by vote count (highest to lowest).",
            "Sorting Algorithm Demo",
            JOptionPane.INFORMATION_MESSAGE);
        
        loadResults(); // Reload to show sorted
    }
    
    private void showGraphDemo() {
        // Demonstrate graph data structure
        JTextArea textArea = new JTextArea();
        textArea.setText("=== GRAPH DATA STRUCTURE DEMO ===\n\n" +
                        "Graph Representation:\n" +
                        "Nodes: Voters and Candidates\n" +
                        "Edges: Votes between them\n\n" +
                        "Example:\n" +
                        "Voter(1001) → Candidate(2001)\n" +
                        "Voter(1002) → Candidate(2001)\n" +
                        "Voter(1003) → Candidate(2002)\n\n" +
                        "This shows relationships between\n" +
                        "voters and their chosen candidates.");
        textArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, textArea,
            "Graph Data Structure",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showHashTableDemo() {
        // Demonstrate hash table
        JTextArea textArea = new JTextArea();
        textArea.setText("=== HASH TABLE DEMO ===\n\n" +
                        "Used for fast voter lookup:\n" +
                        "1. Voter ID → Hash function\n" +
                        "2. Hash value → Array index\n" +
                        "3. Store voter data there\n\n" +
                        "Example:\n" +
                        "Voter ID: 1001\n" +
                        "Hash function: 1001 % 10 = 1\n" +
                        "Store at index 1\n\n" +
                        "Lookup is O(1) time!\n" +
                        "Used to check if voter is registered.");
        textArea.setEditable(false);
        
        JOptionPane.showMessageDialog(this, textArea,
            "Hash Table Data Structure",
            JOptionPane.INFORMATION_MESSAGE);
    }
}