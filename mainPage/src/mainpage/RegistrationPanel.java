/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;

public class RegistrationPanel extends JFrame implements ActionListener{

    private JLabel lbltitle, lblregname, lblvoterid;
    private JTextField txtregname, txtvoterid;
    private JButton btnRegister, btnBack;

    public RegistrationPanel() {
        setTitle("E-VOTING SYSTEM - Registration");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        lbltitle = new JLabel("REGISTRATION");
        lbltitle.setFont(new Font("Arial", Font.BOLD, 16));
        lbltitle.setBounds(190, 70, 200, 25);
        add(lbltitle);

        lblregname = new JLabel("Name: ");
        lblregname.setBounds(100, 120, 100, 25);
        add(lblregname);

        txtregname = new JTextField();
        txtregname.setBounds(200, 120, 200, 25);
        add(txtregname);

        lblvoterid = new JLabel("Voter's ID: ");
        lblvoterid.setBounds(100, 160, 200, 25);
        add(lblvoterid);

        txtvoterid = new JTextField();
        txtvoterid.setBounds(200, 160, 200, 25);
        add(txtvoterid);

        btnRegister = new JButton("Register");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 12));
        btnRegister.setBackground(Color.GREEN.darker());
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBounds(235, 210, 120, 30);
        btnRegister.addActionListener(this); 
        add(btnRegister);
        
        btnBack = new JButton("Back to Main");
        btnBack.setFont(new Font("Arial", Font.BOLD, 12));
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.setBounds(235, 250, 120, 30);
        btnBack.addActionListener(this);
        add(btnBack);

        JLabel regisborder = new JLabel();
        regisborder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        regisborder.setBounds(70, 60, 360, 240);
        add(regisborder);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegister) {
            String name = txtregname.getText().trim();
            String idStr = txtvoterid.getText().trim();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter name", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (idStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Voter ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int voterId = Integer.parseInt(idStr);
                
                // Save to database
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
                
                // Check if voter already exists
                PreparedStatement checkStmt = conn.prepareStatement(
                    "SELECT * FROM voters WHERE voter_id = ?");
                checkStmt.setInt(1, voterId);
                ResultSet rs = checkStmt.executeQuery();
                
                if (rs.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Voter ID " + voterId + " is already registered!", 
                        "Duplicate ID", JOptionPane.ERROR_MESSAGE);
                    conn.close();
                    return;
                }
                
                // Insert new voter
                PreparedStatement insertStmt = conn.prepareStatement(
                    "INSERT INTO voters (voter_id, name) VALUES (?, ?)");
                insertStmt.setInt(1, voterId);
                insertStmt.setString(2, name);
                insertStmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Registration Successful!\n\n" +
                    "Name: " + name + "\n" +
                    "Voter ID: " + voterId + "\n\n" +
                    "You can now login to cast your vote.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Go to Cast Vote (Login) panel
                new CastVotePanel().setVisible(true);
                this.dispose();
                conn.close();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Voter ID must be a number!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Database Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (e.getSource() == btnBack) {
            // Go back to main menu
            new mainPage1().setVisible(true);
            this.dispose();
        }
    }
}