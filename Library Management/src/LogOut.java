import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LogOut extends JFrame {
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryManagementSystem"; // Your DB name
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "AdIl@6969"; // MySQL password

    private int userId; // To store the logged-in user ID

    public LogOut(int userId) {
        this.userId = userId;

        setTitle("Log Out");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel messageLabel = new JLabel("Are you sure you want to log out?", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JPanel buttonPanel = new JPanel();
        JButton logoutButton = new JButton("Log Out");
        JButton cancelButton = new JButton("Cancel");

        // Logout button action
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = JOptionPane.showConfirmDialog(null, "Do you really want to log out and delete your data?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    logout(); // Call the logout method
                }
            }
        });

        // Cancel button action
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the logout window
            }
        });

        buttonPanel.add(logoutButton);
        buttonPanel.add(cancelButton);

        add(messageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // ✅ Method to delete user info from the database
    private void logout() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) { // Fixed column name

            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "✅ User logged out successfully and data deleted!");
                System.exit(0); // Exit the application
            } else {
                JOptionPane.showMessageDialog(null, "⚠ User not found or already deleted!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "❌ Database Error! " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LogOut(1)); // Replace 1 with actual logged-in user ID
    }
}
