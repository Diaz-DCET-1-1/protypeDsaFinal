/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class AdminLoginPanel extends JFrame implements ActionListener {
    private JLabel lblTitle, lblAdminID, lblPassword;
    private JTextField txtAdminID;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnBack;
    
    public AdminLoginPanel() {
        setTitle("E-Voting System - Admin Login");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        lblTitle = new JLabel("ADMIN LOGIN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBounds(190, 70, 200, 30);
        add(lblTitle);
        
        lblAdminID = new JLabel("Admin ID:");
        lblAdminID.setBounds(100, 120, 100, 25);
        add(lblAdminID);
        
        txtAdminID = new JTextField();
        txtAdminID.setBounds(200, 120, 200, 25);
        add(txtAdminID);
        
        lblPassword = new JLabel("Password:");
        lblPassword.setBounds(100, 160, 100, 25);
        add(lblPassword);
        
        txtPassword = new JPasswordField();
        txtPassword.setBounds(200, 160, 200, 25);
        add(txtPassword);
        
        btnLogin = new JButton("Login");
        btnLogin.setBounds(235, 210, 120, 30);
        btnLogin.setBackground(new Color(0, 100, 0)); // Green
        btnLogin.setForeground(Color.WHITE);
        btnLogin.addActionListener(this);
        add(btnLogin);
        
        btnBack = new JButton("Back to Main");
        btnBack.setBounds(235, 250, 120, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        add(btnBack);
        
        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        border.setBounds(70, 60, 360, 220);
        add(border);
        
        // Default admin credentials (for demo)
        txtAdminID.setText("9999");
        txtPassword.setText("admin123");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnLogin) {
            String adminIdStr = txtAdminID.getText().trim();
            String password = new String(txtPassword.getPassword());
            
            if (adminIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Admin ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter password", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int adminId = Integer.parseInt(adminIdStr);
                
                // Check if admin exists
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
                
                PreparedStatement stmt = conn.prepareStatement(
                    "SELECT name FROM voters WHERE voter_id = ? AND is_admin = TRUE");
                stmt.setInt(1, adminId);
                ResultSet rs = stmt.executeQuery();
                
                if (rs.next()) {
                    // For simplicity, hardcoded password check
                    // In real system, passwords would be hashed in database
                    if (password.equals("admin123")) {
                        String adminName = rs.getString("name");
                        JOptionPane.showMessageDialog(this,
                            "Welcome, " + adminName + "!\n" +
                            "Admin ID: " + adminId,
                            "Admin Login Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Open Admin Dashboard
                        new AdminDashboard(adminId, adminName).setVisible(true);
                        this.dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                            "Invalid password!",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Admin ID not found or not authorized!",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
                }
                
                conn.close();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Admin ID must be a number!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (e.getSource() == btnBack) {
            new mainPage1().setVisible(true);
            this.dispose();
        }
    }
}