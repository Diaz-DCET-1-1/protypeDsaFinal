package mainpage;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.*;

public class CastVotePanel extends JFrame implements ActionListener {

    private JLabel lblTitle, lblVoteID, lblVoterID, lblCandidates;
    private JTextField txtVoteID, txtVoterCast;
    private JButton btnCast, btnBack, btnCandidateDropdown;
    private JPopupMenu candidateMenu;
    private ArrayList<JCheckBox> candidateCheckBoxes;

    public CastVotePanel() {
        setTitle("E-VOTING SYSTEM - Cast Vote");
        setSize(550, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        lblTitle = new JLabel("CAST VOTE", SwingConstants.CENTER);
        lblTitle.setBounds(180, 20, 200, 25);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitle);

        JLabel lblInstruction = new JLabel("Enter your Voter ID and select candidates");
        lblInstruction.setBounds(120, 50, 350, 25);
        add(lblInstruction);

        // Vote ID
        lblVoteID = new JLabel("Vote ID: ");
        lblVoteID.setBounds(100, 90, 100, 25);
        add(lblVoteID);

        txtVoteID = new JTextField();
        txtVoteID.setBounds(200, 90, 200, 25);
        txtVoteID.setEditable(false);
        add(txtVoteID);

        // Voter ID
        lblVoterID = new JLabel("Voter's ID: ");
        lblVoterID.setBounds(100, 130, 100, 25);
        add(lblVoterID);

        txtVoterCast = new JTextField();
        txtVoterCast.setBounds(200, 130, 200, 25);
        add(txtVoterCast);

        // Candidate dropdown button
        lblCandidates = new JLabel("Select Candidate: ");
        lblCandidates.setBounds(100, 170, 150, 25);
        add(lblCandidates);

        btnCandidateDropdown = new JButton("Choose Candidates:");
        btnCandidateDropdown.setBounds(200, 170, 200, 25);
        btnCandidateDropdown.addActionListener(this);
        add(btnCandidateDropdown);

        candidateMenu = new JPopupMenu();
        candidateCheckBoxes = new ArrayList<>();
        populateCandidatesDropdown();

        // Buttons
        btnCast = new JButton("Cast Vote");
        btnCast.setBounds(200, 220, 150, 30);
        btnCast.setBackground(Color.GREEN.darker());
        btnCast.setForeground(Color.WHITE);
        btnCast.addActionListener(this);
        add(btnCast);

        btnBack = new JButton("Back to Main");
        btnBack.setBounds(200, 270, 150, 30);
        btnBack.setBackground(Color.GRAY);
        btnBack.setForeground(Color.WHITE);
        btnBack.addActionListener(this);
        add(btnBack);
        
        JLabel regisborder = new JLabel();
        regisborder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        regisborder.setBounds(70, 20, 390,310);
        add(regisborder);
        
        generateVoteID();
    }

    private void generateVoteID() {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM votes");
            int voteCount = 0;
            if (rs.next()) voteCount = rs.getInt(1);
            txtVoteID.setText(String.format("%02d", voteCount + 1));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error generating Vote ID: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateCandidatesDropdown() {
        candidateMenu.removeAll();
        candidateCheckBoxes.clear();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT candidate_id, name, party FROM candidates");
            while (rs.next()) {
                String id = rs.getString("candidate_id");
                String name = rs.getString("name");
                String party = rs.getString("party");

                JCheckBox cb = new JCheckBox(id + " | " + name + " | " + party);
                cb.setBackground(Color.WHITE);
                candidateMenu.add(cb);
                candidateCheckBoxes.add(cb);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading candidates: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCandidateDropdown) {
            candidateMenu.show(btnCandidateDropdown, 0, btnCandidateDropdown.getHeight());
        } else if (e.getSource() == btnCast) {
            String voterIdStr = txtVoterCast.getText().trim();
            if (voterIdStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your Voter ID",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int voterIdNum;
            try {
                voterIdNum = Integer.parseInt(voterIdStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Voter ID must be a number!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<String> selectedCandidates = new ArrayList<>();
            ArrayList<Integer> selectedCandidateIDs = new ArrayList<>();
            for (JCheckBox cb : candidateCheckBoxes) {
                if (cb.isSelected()) {
                    selectedCandidates.add(cb.getText());
                    int candId = Integer.parseInt(cb.getText().split("\\|")[0].trim());
                    selectedCandidateIDs.add(candId);
                }
            }

            if (selectedCandidates.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select at least one candidate!",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {

                // Check voter existence
                PreparedStatement psVoter = conn.prepareStatement(
                        "SELECT name, has_voted FROM voters WHERE voter_id = ?");
                psVoter.setInt(1, voterIdNum);
                ResultSet rsVoter = psVoter.executeQuery();

                if (!rsVoter.next()) {
                    JOptionPane.showMessageDialog(this,
                            "Voter ID " + voterIdNum + " not found! Please register first.",
                            "Voter Not Found", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean hasVoted = rsVoter.getBoolean("has_voted");
                if (hasVoted) {
                    JOptionPane.showMessageDialog(this,
                            "You have already voted. Multiple voting is not allowed.",
                            "Already Voted", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Insert votes and update candidates
                for (int candId : selectedCandidateIDs) {
                    PreparedStatement psInsert = conn.prepareStatement(
                            "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)");
                    psInsert.setInt(1, voterIdNum);
                    psInsert.setInt(2, candId);
                    psInsert.executeUpdate();

                    PreparedStatement psUpdate = conn.prepareStatement(
                            "UPDATE candidates SET vote_count = vote_count + 1 WHERE candidate_id = ?");
                    psUpdate.setInt(1, candId);
                    psUpdate.executeUpdate();
                }

                // Update voter
                PreparedStatement psVoted = conn.prepareStatement(
                        "UPDATE voters SET has_voted = TRUE WHERE voter_id = ?");
                psVoted.setInt(1, voterIdNum);
                psVoted.executeUpdate();

                // Show voted candidates
                StringBuilder votedList = new StringBuilder();
                for (String s : selectedCandidates) votedList.append(s).append("\n");

                JOptionPane.showMessageDialog(this,
                        "Vote Cast Successfully!\nYou voted for:\n" + votedList.toString(),
                        "Success", JOptionPane.INFORMATION_MESSAGE);

                // Reset
                generateVoteID();
                txtVoterCast.setText("");
                for (JCheckBox cb : candidateCheckBoxes) cb.setSelected(false);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else if (e.getSource() == btnBack) {
            new mainPage1().setVisible(true);
            this.dispose();
        }
    }
}
