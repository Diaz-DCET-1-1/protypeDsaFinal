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

public class CastVotePanel extends JFrame implements ActionListener {

    private JLabel lbltitle, lblcandname, lblcandid, lblparty;
    private JTextField txtVoteID, txtVoterCast, txtCandCast;
    private JButton btnCast, btnBack;

    public CastVotePanel() {
        setTitle("E-VOTING SYSTEM - Cast Vote");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        lbltitle = new JLabel("CAST VOTE / LOGIN", SwingConstants.CENTER);
        lbltitle.setBounds(150, 50, 200, 25);
        lbltitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lbltitle);
        
        // Instructions
        JLabel lblInstruction = new JLabel("Enter your Voter ID to login and vote");
        lblInstruction.setBounds(100, 80, 300, 25);
        add(lblInstruction);

        lblcandname = new JLabel("Vote ID: ");
        lblcandname.setBounds(100, 120, 100, 25);
        add(lblcandname);

        txtVoteID = new JTextField();
        txtVoteID.setBounds(200, 120, 200, 25);
        add(txtVoteID);

        lblcandid = new JLabel("Voter's ID: ");
        lblcandid.setBounds(100, 160, 200, 25);
        add(lblcandid);

        txtVoterCast = new JTextField();
        txtVoterCast.setBounds(200, 160, 200, 25);
        add(txtVoterCast);

        lblparty = new JLabel("Candidate ID: ");
        lblparty.setBounds(100, 200, 200, 25);
        add(lblparty);

        txtCandCast = new JTextField();
        txtCandCast.setBounds(200, 200, 200, 25);
        add(txtCandCast);

        btnCast = new JButton("Cast Vote");
        btnCast.setFont(new Font("Arial", Font.BOLD, 12));
        btnCast.setBackground(Color.GREEN.darker());
        btnCast.setForeground(Color.WHITE);
        btnCast.setBounds(235, 250, 120, 30);
        btnCast.addActionListener(this);   
        add(btnCast);
        
        btnBack = new JButton("Back to Main");
        btnBack.setBounds(235, 290, 120, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        add(btnBack);

        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        border.setBounds(70, 40, 360, 300);
        add(border);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCast) {
            String voteId = txtVoteID.getText().trim();
            String voterId = txtVoterCast.getText().trim();
            String candId = txtCandCast.getText().trim();

            if (voteId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Vote ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (voterId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Voter ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (candId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Candidate ID", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int voteIdNum = Integer.parseInt(voteId);
                int voterIdNum = Integer.parseInt(voterId);
                int candIdNum = Integer.parseInt(candId);
                
                // First check if voter exists and hasn't voted
                Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1");
                
                // Check voter
                PreparedStatement checkVoter = conn.prepareStatement(
                    "SELECT name, has_voted FROM voters WHERE voter_id = ?");
                checkVoter.setInt(1, voterIdNum);
                ResultSet rs = checkVoter.executeQuery();
                
                if (!rs.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Voter ID " + voterIdNum + " not found!\nPlease register first.", 
                        "Voter Not Found", JOptionPane.ERROR_MESSAGE);
                    conn.close();
                    return;
                }
                
                String voterName = rs.getString("name");
                boolean hasVoted = rs.getBoolean("has_voted");
                
                if (hasVoted) {
                    JOptionPane.showMessageDialog(this, 
                        "Welcome back, " + voterName + "!\n\n" +
                        "You have already voted.\n" +
                        "Thank you for participating!",
                        "Already Voted",
                        JOptionPane.INFORMATION_MESSAGE);
                    conn.close();
                    
                    // Show voting results after message
                    new ResultsPanel().setVisible(true);
                    this.dispose();
                    return;
                }
                
                // Check candidate
                PreparedStatement checkCand = conn.prepareStatement(
                    "SELECT name FROM candidates WHERE candidate_id = ?");
                checkCand.setInt(1, candIdNum);
                ResultSet rs2 = checkCand.executeQuery();
                
                if (!rs2.next()) {
                    JOptionPane.showMessageDialog(this, 
                        "Candidate ID " + candIdNum + " not found!", 
                        "Candidate Not Found", JOptionPane.ERROR_MESSAGE);
                    conn.close();
                    return;
                }
                
                String candidateName = rs2.getString("name");
                
                // Cast vote
                PreparedStatement insertVote = conn.prepareStatement(
                    "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)");
                insertVote.setInt(1, voterIdNum);
                insertVote.setInt(2, candIdNum);
                insertVote.executeUpdate();
                
                // Update voter
                PreparedStatement updateVoter = conn.prepareStatement(
                    "UPDATE voters SET has_voted = TRUE WHERE voter_id = ?");
                updateVoter.setInt(1, voterIdNum);
                updateVoter.executeUpdate();
                
                // Update candidate
                PreparedStatement updateCand = conn.prepareStatement(
                    "UPDATE candidates SET vote_count = vote_count + 1 WHERE candidate_id = ?");
                updateCand.setInt(1, candIdNum);
                updateCand.executeUpdate();
                
                JOptionPane.showMessageDialog(this, 
                    "Vote Cast Successfully!\n\n" +
                    "Voter: " + voterName + " (ID: " + voterIdNum + ")\n" +
                    "Candidate: " + candidateName + " (ID: " + candIdNum + ")\n\n" +
                    "Thank you for voting!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Clear fields
                txtVoteID.setText("");
                txtVoterCast.setText("");
                txtCandCast.setText("");
                
                // Show results
                new ResultsPanel().setVisible(true);
                this.dispose();
                
                conn.close();
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "All IDs must be numbers!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } else if (e.getSource() == btnBack) {
            // Go back to main menu
            new mainPage1().setVisible(true);
            this.dispose();
        }
    }
}