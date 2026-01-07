package mainpage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewDataPanel extends JFrame {

    private JLabel lblTitle;
    private JTable table;
    private JScrollPane scrollPane;

    public ViewDataPanel() {
        setTitle("E-Voting System - Voters Panel");
        setSize(700, 500);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        lblTitle = new JLabel("VOTERS LIST", SwingConstants.CENTER);
        lblTitle.setBounds(50, 20, 600, 40);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        add(lblTitle);


        table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);
        table.getTableHeader().setForeground(Color.BLACK);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(table);
        scrollPane.setBounds(50, 80, 600, 350);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(scrollPane);


        loadVoters();

        getContentPane().setBackground(Color.WHITE);
    }

    private void loadVoters() {
        DefaultTableModel model = new DefaultTableModel();
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", "root", "DCET2-1")) {

            String query = "SELECT voter_id AS 'Voter ID', name AS 'Name', " +
                    "CASE WHEN has_voted THEN 'Yes' ELSE 'No' END AS 'Voted', " +
                    "CASE WHEN is_admin THEN 'Yes' ELSE 'No' END AS 'Admin', " +
                    "registered_date AS 'Registered' FROM voters ORDER BY voter_id";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            for (int i = 1; i <= colCount; i++) {
                model.addColumn(meta.getColumnLabel(i));
            }

            while (rs.next()) {
                Object[] row = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }

        } catch (SQLException ex) {
            model.addColumn("Error");
            model.addRow(new Object[]{"Database error: " + ex.getMessage()});
        }

        table.setModel(model);
    }
}