import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateMembership extends JFrame {
    private JTextField memberIdField, userIdField;
    private JComboBox<String> membershipOptions;

    // Database credentials (Replace with your actual credentials)
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryManagementSystem"; // Your database name
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "AdIl@6969"; // MySQL password

    public UpdateMembership() {
        setTitle("Update Membership");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(0, 100, 200), getWidth(), getHeight(), new Color(0, 50, 150));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());

        // Form Components
        JLabel header = new JLabel("Update Membership Details");
        header.setFont(new Font("Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);

        JLabel idLabel = createStyledLabel("Membership ID:");
        memberIdField = createStyledTextField();

        JLabel userIdLabel = createStyledLabel("User ID:");
        userIdField = createStyledTextField();

        JLabel durationLabel = createStyledLabel("New Duration:");
        String[] options = {"6 months", "1 year", "2 years"};
        membershipOptions = new JComboBox<>(options);
        membershipOptions.setFont(new Font("Arial", Font.BOLD, 14));

        JButton updateButton = createStyledButton("Update Membership");

        // Layout Settings
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(idLabel, gbc);
        gbc.gridx = 1;
        panel.add(memberIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(userIdLabel, gbc);
        gbc.gridx = 1;
        panel.add(userIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(durationLabel, gbc);
        gbc.gridx = 1;
        panel.add(membershipOptions, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Button Action Listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMembershipInDatabase();
            }
        });

        setVisible(true);
    }

    // 🔹 Save Membership Data to Database
    private void updateMembershipInDatabase() {
        String memberId = memberIdField.getText().trim();
        String userId = userIdField.getText().trim();
        String selectedOption = (String) membershipOptions.getSelectedItem();

        if (memberId.isEmpty() || userId.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int memId = Integer.parseInt(memberId);
            int usrId = Integer.parseInt(userId);

            int response = JOptionPane.showConfirmDialog(null,
                    "Confirm update for Membership ID: " + memId + "\nUser ID: " + usrId + "\nNew Duration: " + selectedOption,
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement(
                             "INSERT INTO membership (membership_id, user_id, duration) VALUES (?, ?, ?) " +
                                     "ON DUPLICATE KEY UPDATE duration=?")) {

                    pstmt.setInt(1, memId);
                    pstmt.setInt(2, usrId);
                    pstmt.setString(3, selectedOption);
                    pstmt.setString(4, selectedOption);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "✅ Membership Updated Successfully!");
                        dispose(); // Close window after update
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Update Failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Membership ID or User ID! Please enter numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Styled Label
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Styled Text Field
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(15);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        return textField;
    }

    // Styled Button with Hover Effect
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(255, 100, 50));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(200, 50, 20));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 100, 50));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        // Ensure JDBC Driver is loaded
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(UpdateMembership::new);
    }
}
