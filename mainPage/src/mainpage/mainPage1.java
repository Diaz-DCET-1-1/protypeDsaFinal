package mainpage;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class mainPage1 extends JFrame implements ActionListener {
    private JLabel TitleLabel, DescLabel1, DescLabel2;
    private JButton RegBtn, AlrBtn, AdminBtn, ResultsBtn;
    
    public mainPage1() {
        setTitle("E-Voting System - Main Menu");
        setSize(600, 600); // Increased height
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        
        JLabel regisborder = new JLabel();
        regisborder.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        regisborder.setBounds(30, 25, 513, 500);
        add(regisborder);
        
        TitleLabel = new JLabel("E-Voting System", SwingConstants.CENTER);
        TitleLabel.setBounds(90, 30, 400, 50);
        TitleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        add(TitleLabel);

        DescLabel1 = new JLabel("Welcome to Online Voting System", SwingConstants.CENTER);
        DescLabel1.setBounds(30, 100, 500, 40);
        add(DescLabel1);

        DescLabel2 = new JLabel("Please select an option:", SwingConstants.CENTER);
        DescLabel2.setBounds(30, 140, 500, 40);
        add(DescLabel2);
        
        RegBtn = new JButton("Register New Account");
        RegBtn.setBounds(180, 200, 200, 40);
        RegBtn.setBackground(Color.GRAY);
        RegBtn.setForeground(Color.WHITE);
        RegBtn.setFont(new Font("Arial", Font.BOLD, 14));
        RegBtn.addActionListener(this);
        add(RegBtn);
        
        AlrBtn = new JButton("Already Have an Account");
        AlrBtn.setBounds(180, 260, 200, 40);
        AlrBtn.setBackground(Color.GRAY);
        AlrBtn.setForeground(Color.WHITE);
        AlrBtn.setFont(new Font("Arial", Font.BOLD, 14));
        AlrBtn.addActionListener(this);
        add(AlrBtn);
        
        ResultsBtn = new JButton("View Election Results");
        ResultsBtn.setBounds(180, 320, 200, 40);
        ResultsBtn.setBackground(new Color(0, 100, 0)); // Green
        ResultsBtn.setForeground(Color.WHITE);
        ResultsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        ResultsBtn.addActionListener(this);
        add(ResultsBtn);
        
        AdminBtn = new JButton("Admin Login");
        AdminBtn.setBounds(180, 380, 200, 40);
        AdminBtn.setBackground(new Color(139, 0, 0)); // Dark red
        AdminBtn.setForeground(Color.WHITE);
        AdminBtn.setFont(new Font("Arial", Font.BOLD, 14));
        AdminBtn.addActionListener(this);
        add(AdminBtn);
        
        // Database status
        JLabel dbStatus = new JLabel("Database: Connected ✓", SwingConstants.CENTER);
        dbStatus.setBounds(30, 450, 500, 30);
        dbStatus.setForeground(Color.GREEN.darker());
        add(dbStatus);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RegBtn) {
            new RegistrationPanel().setVisible(true);
            this.dispose();
        } else if (e.getSource() == AlrBtn) {
            new CastVotePanel().setVisible(true);
            this.dispose();
        } else if (e.getSource() == ResultsBtn) {
            new ResultsPanel().setVisible(true);
            this.dispose();
        } else if (e.getSource() == AdminBtn) {
            new AdminLoginPanel().setVisible(true);
            this.dispose();
        }
    }
}