/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

public class AddCandidatePanel extends JFrame implements ActionListener {

    private JLabel lbltitle, lblcandname, lblcandid, lblparty;
    private JTextField txtcandname, txtcandid, txtparty;
    private JButton btnAddcand, btnBack;

    public AddCandidatePanel() {
        setTitle("E-VOTING SYSTEM - Add Candidate (Admin)");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        lbltitle = new JLabel("ADD CANDIDATE (Admin)");
        lbltitle.setBounds(160, 70, 250, 25);
        lbltitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lbltitle);

        lblcandname = new JLabel("Name: ");
        lblcandname.setBounds(100, 120, 100, 25);
        add(lblcandname);

        txtcandname = new JTextField();
        txtcandname.setBounds(200, 120, 200, 25);
        add(txtcandname);

        lblcandid = new JLabel("Candidate's ID: ");
        lblcandid.setBounds(100, 160, 200, 25);
        add(lblcandid);

        txtcandid = new JTextField();
        txtcandid.setBounds(200, 160, 200, 25);
        add(txtcandid);

        lblparty = new JLabel("Party: ");
        lblparty.setBounds(100, 200, 200, 25);
        add(lblparty);

        txtparty = new JTextField();
        txtparty.setBounds(200, 200, 200, 25);
        add(txtparty);

        btnAddcand = new JButton("Add Candidate");
        btnAddcand.setFont(new Font("Arial", Font.BOLD, 12));
        btnAddcand.setBackground(new Color(0, 100, 0));
        btnAddcand.setForeground(Color.WHITE);
        btnAddcand.setBounds(235, 250, 120, 30);
        btnAddcand.addActionListener(this);   
        add(btnAddcand);
        
        btnBack = new JButton("Back to Admin");
        btnBack.setBounds(235, 290, 120, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        add(btnBack);

        JLabel candidborder = new JLabel();
        candidborder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        candidborder.setBounds(70, 60, 360, 280);
        add(candidborder);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddcand) {
            String candName = txtcandname.getText().trim();
            String candIdStr = txtcandid.getText().trim();
            String candParty = txtparty.getText().trim();

            if (candName.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter name", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (candIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Candidate ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (candParty.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter party", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int candId = Integer.parseInt(candIdStr);
                
                // Save to database
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
                
                // Check if candidate already exists
                PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT candidate_id FROM candidates WHERE candidate_id = ?");
                checkStmt.setInt(1, candId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Candidate ID " + candId + " already exists!", 
                        "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                    conn.close();
                    return;
                }
                
                PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO candidates (candidate_id, name, party) VALUES (?, ?, ?)");
                stmt.setInt(1, candId);
                stmt.setString(2, candName);
                stmt.setString(3, candParty);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Candidate Added Successfully!\n" +
                    "ID: " + candId + "\n" +
                    "Name: " + candName + "\n" +
                    "Party: " + candParty,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                txtcandname.setText("");
                txtcandid.setText("");
                txtparty.setText("");
                conn.close();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Candidate ID must be a number!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (e.getSource() == btnBack) {
            this.dispose(); // Just close this window, admin stays logged in
        }
    }
}