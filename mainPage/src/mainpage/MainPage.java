/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package mainpage;

public class MainPage {
    public static void main(String[] args) {
        // Test database first
        DatabaseTest.testConnection();
        
        // Show ONLY main page
        new mainPage1().setVisible(true);
    }
}