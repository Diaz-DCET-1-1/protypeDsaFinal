package mainpage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ManageVotersPanel extends JFrame implements ActionListener {

    private JLabel lblTitle;
    private JTable table;
    private DefaultTableModel tableModel;
    private JScrollPane scrollPane;
    private JButton btnRefresh, btnDelete, btnClose;

    public ManageVotersPanel() {
        setTitle("E-Voting System - Voter Management");
        setSize(700, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);

        // Title
        lblTitle = new JLabel("VOTER MANAGEMENT", SwingConstants.CENTER);
        lblTitle.setBounds(150, 30, 400, 30);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle);

        // Table
        String[] columns = {"Voter ID", "Name", "Has Voted", "Is Admin", "Registered Date"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        // Center text and alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tbl, Object val, boolean isSelected,
                                                           boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, isSelected, hasFocus, row, col);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : Color.LIGHT_GRAY);
                } else {
                    c.setBackground(Color.GRAY);
                }
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 80, 600, 300);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(scrollPane);

        // Buttons
        btnRefresh = new JButton("Refresh");
        btnDelete = new JButton("Delete Selected");
        btnClose = new JButton("Close");

        styleButton(btnRefresh);
        styleButton(btnDelete);
        styleButton(btnClose);

        btnRefresh.setBounds(150, 400, 120, 30);
        btnDelete.setBounds(300, 400, 150, 30);
        btnClose.setBounds(480, 400, 120, 30);

        add(btnRefresh);
        add(btnDelete);
        add(btnClose);

        // Border around content
        JLabel border = new JLabel();
        border.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        border.setBounds(40, 70, 620, 360);
        add(border);

        // Load initial data
        loadVoters();

        // Action listeners
        btnRefresh.addActionListener(this);
        btnDelete.addActionListener(this);
        btnClose.addActionListener(this);
    }

    private void styleButton(JButton btn) {
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setBackground(Color.GRAY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
    }

    private void loadVoters() {
        tableModel.setRowCount(0);
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(
                    "SELECT voter_id, name, has_voted, is_admin, registered_date FROM voters ORDER BY voter_id");

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("voter_id"),
                        rs.getString("name"),
                        rs.getBoolean("has_voted") ? "Yes" : "No",
                        rs.getBoolean("is_admin") ? "Yes" : "No",
                        rs.getTimestamp("registered_date")
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading voters: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteVoter() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a voter to delete",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int voterId = (int) tableModel.getValueAt(selectedRow, 0);
        String voterName = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete voter?\nID: " + voterId + "\nName: " + voterName,
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {

                PreparedStatement stmt = conn.prepareStatement(
                        "DELETE FROM voters WHERE voter_id = ?");
                stmt.setInt(1, voterId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(this,
                        "Voter deleted successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);

                loadVoters();

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Error deleting voter: " + e.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRefresh) {
            loadVoters();
        } else if (e.getSource() == btnDelete) {
            deleteVoter();
        } else if (e.getSource() == btnClose) {
            this.dispose();
        }
    }

}
