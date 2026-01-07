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
    private JButton RegBtn, AlrBtn, AdminBtn, ResultsBtn, ExitBtn;
    
    public mainPage1() {
        setTitle("E-Voting System - Main Menu");
        setSize(600, 600);
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
        
        AlrBtn = new JButton("Cast Vote");
        AlrBtn.setBounds(180, 260, 200, 40);
        AlrBtn.setBackground(Color.GRAY);
        AlrBtn.setForeground(Color.WHITE);
        AlrBtn.setFont(new Font("Arial", Font.BOLD, 14));
        AlrBtn.addActionListener(this);
        add(AlrBtn);
 
        AdminBtn = new JButton("Admin Login");
        AdminBtn.setBounds(180, 320, 200, 40);
        AdminBtn.setBackground(Color.GRAY);
        AdminBtn.setForeground(Color.WHITE);
        AdminBtn.setFont(new Font("Arial", Font.BOLD, 14));
        AdminBtn.addActionListener(this);
        add(AdminBtn);
    
        ResultsBtn = new JButton("View Election Results");
        ResultsBtn.setBounds(180, 380, 200, 40);
        ResultsBtn.setBackground(Color.GRAY);
        ResultsBtn.setForeground(Color.WHITE);
        ResultsBtn.setFont(new Font("Arial", Font.BOLD, 14));
        ResultsBtn.addActionListener(this);
        add(ResultsBtn);
        
      
        ExitBtn = new JButton("EXIT");
        ExitBtn.setBounds(180, 440, 200, 40);
        ExitBtn.setBackground(Color.GRAY); 
        ExitBtn.setForeground(Color.WHITE);
        ExitBtn.setFont(new Font("Arial", Font.BOLD, 14));
        ExitBtn.addActionListener(this);
        add(ExitBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == RegBtn) {
            new RegistrationPanel().setVisible(true);
            this.dispose();
        } 
        else if (e.getSource() == AlrBtn) {
            new CastVotePanel().setVisible(true);
            this.dispose();
        } 
        else if (e.getSource() == AdminBtn) {
            new AdminLoginPanel().setVisible(true);
            this.dispose();
        } 
        else if (e.getSource() == ResultsBtn) {
            new ResultsPanel().setVisible(true);
            this.dispose();
        }
        else if (e.getSource() == ExitBtn) {
            System.exit(0);
        }
    }
}
