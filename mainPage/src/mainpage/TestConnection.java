/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mainpage;

import javax.swing.JOptionPane;
import java.sql.*;

public class TestConnection {
    public static void main(String[] args) {
        try {
            // 1. Test if driver class can be loaded
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL Driver found!");
            
            // 2. Try to connect (even if database doesn't exist yet)
            String url = "jdbc:mysql://localhost:3306/";
            String user = "root";
            String password = ""; // your password or empty for XAMPP
            
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("✓ Connected to MySQL Server!");
            
            // 3. Create database if not exists
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE DATABASE IF NOT EXISTS voting_system");
            System.out.println("✓ Database ready!");
            
            conn.close();
            
            JOptionPane.showMessageDialog(null,
                "SUCCESS!\n" +
                "MySQL Driver: Working ✓\n" +
                "MySQL Connection: Working ✓\n" +
                "Database 'voting_system' created ✓",
                "Library Test - PASSED",
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null,
                "MySQL Driver NOT FOUND!\n\n" +
                "The mysql-connector JAR is not in your project.\n" +
                "Right-click project → Properties → Libraries\n" +
                "Click 'Add JAR/Folder' and select the JAR file.",
                "Library Error",
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Connected to driver but MySQL server issue:\n" +
                e.getMessage() + "\n\n" +
                "Make sure:\n" +
                "1. MySQL is running\n" +
                "2. Correct password\n" +
                "3. Try empty password if using XAMPP",
                "Connection Error",
                JOptionPane.WARNING_MESSAGE);
        }
    }
}