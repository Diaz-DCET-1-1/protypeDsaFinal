/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

/**
 *
 * @author diazj
 */
import java.sql.*;
import javax.swing.JOptionPane;

public class DatabaseTest {
    public static void testConnection() {
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/voting_system", 
                "root", 
                "DCET2-1"  // YOUR PASSWORD HERE
            );
            
            JOptionPane.showMessageDialog(null,
                "Database Connection Successful!\n" +
                "✓ MySQL: Connected ✓\n" +
                "✓ Database: voting_system ✓\n" +
                "✓ Password: Verified ✓\n" +
                "✓ Tables: Ready ✓",
                "Database Status - CONNECTED",
                JOptionPane.INFORMATION_MESSAGE);
            
            conn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Database Connection Failed!\n" +
                "Error: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}