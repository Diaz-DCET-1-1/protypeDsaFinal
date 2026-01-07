/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AdminDashboard extends JFrame implements ActionListener {
    private int adminId;
    private String adminName;
    private JLabel lblWelcome;
    private JButton btnAddCandidate, btnViewData, btnManageVoters, btnLogout;
    
    public AdminDashboard(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
        
        setTitle("E-Voting System - Admin Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        // Welcome message
        lblWelcome = new JLabel("Admin Dashboard - " + adminName + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setBounds(50, 30, 500, 40);
        add(lblWelcome);
        
        JLabel instruction = new JLabel("Administrative Functions:");
        instruction.setFont(new Font("Arial", Font.PLAIN, 14));
        instruction.setBounds(50, 70, 300, 30);
        add(instruction);
        
        // Buttons
        btnAddCandidate = new JButton("Add Candidate");
        btnAddCandidate.setBounds(200, 120, 200, 40);
        btnAddCandidate.setBackground(Color.GRAY);
        btnAddCandidate.setForeground(Color.WHITE);
        btnAddCandidate.setFont(new Font("Arial", Font.BOLD, 14));
        btnAddCandidate.addActionListener(this);
        add(btnAddCandidate);
        
        btnViewData = new JButton("View Database");
        btnViewData.setBounds(200, 180, 200, 40);
        btnViewData.setBackground(Color.GRAY);
        btnViewData.setForeground(Color.WHITE);
        btnViewData.setFont(new Font("Arial", Font.BOLD, 14));
        btnViewData.addActionListener(this);
        add(btnViewData);
        
        btnManageVoters = new JButton("Manage Voters");
        btnManageVoters.setBounds(200, 240, 200, 40);
        btnManageVoters.setBackground(Color.GRAY);
        btnManageVoters.setForeground(Color.WHITE);
        btnManageVoters.setFont(new Font("Arial", Font.BOLD, 14));
        btnManageVoters.addActionListener(this);
        add(btnManageVoters);
        
        btnLogout = new JButton("Logout");
        btnLogout.setBounds(200, 300, 200, 40);
        btnLogout.setBackground(Color.GRAY);
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogout.addActionListener(this);
        add(btnLogout);
        
        // Border
        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        border.setBounds(30, 20, 520, 350);
        add(border);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAddCandidate) {
            new AddCandidatePanel().setVisible(true);
            
        } else if (e.getSource() == btnViewData) {
            new ViewDataPanel().setVisible(true);
            
        } else if (e.getSource() == btnManageVoters) {
            new ManageVotersPanel().setVisible(true);
            
        } else if (e.getSource() == btnLogout) {
            new mainPage1().setVisible(true);
            this.dispose();
        }
    }
}