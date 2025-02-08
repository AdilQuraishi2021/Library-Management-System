import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UpdateBook extends JFrame {
    private JTextField bookIdField, titleField, authorField, categoryField;
    private JCheckBox availabilityCheckBox;

    // Database credentials (Change according to your setup)
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryManagementSystem"; // Database name
    private static final String USER = "root"; // MySQL username
    private static final String PASSWORD = "AdIl@6969"; // MySQL password

    public UpdateBook() {
        setTitle("Update Book Details");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(34, 193, 195), getWidth(), getHeight(), new Color(253, 187, 45));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());

        // Form Components
        JLabel header = new JLabel("Update Book Details");
        header.setFont(new Font("Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);

        JLabel idLabel = createStyledLabel("Book ID:");
        bookIdField = createStyledTextField();

        JLabel titleLabel = createStyledLabel("Title:");
        titleField = createStyledTextField();

        JLabel authorLabel = createStyledLabel("Author:");
        authorField = createStyledTextField();

        JLabel categoryLabel = createStyledLabel("Category:");
        categoryField = createStyledTextField();

        JLabel availabilityLabel = createStyledLabel("Available:");
        availabilityCheckBox = new JCheckBox();
        availabilityCheckBox.setBackground(new Color(0, 0, 0, 0)); // Transparent background

        JButton updateButton = createStyledButton("Update Book");

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
        panel.add(bookIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(authorLabel, gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(availabilityLabel, gbc);
        gbc.gridx = 1;
        panel.add(availabilityCheckBox, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(updateButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Button Action Listener
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBookInDatabase();
            }
        });

        setVisible(true);
    }

    // 🔹 Update Book Data in Database
    private void updateBookInDatabase() {
        String bookId = bookIdField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        boolean isAvailable = availabilityCheckBox.isSelected();

        if (bookId.isEmpty() || title.isEmpty() || author.isEmpty() || category.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int id = Integer.parseInt(bookId);

            int response = JOptionPane.showConfirmDialog(null,
                    "Confirm update for Book ID: " + id + "\nTitle: " + title + "\nAuthor: " + author +
                            "\nCategory: " + category + "\nAvailable: " + (isAvailable ? "Yes" : "No"),
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement(
                             "UPDATE books SET title=?, author=?, category=?, isAvailable=? WHERE id=?")) {

                    pstmt.setString(1, title);
                    pstmt.setString(2, author);
                    pstmt.setString(3, category);
                    pstmt.setBoolean(4, isAvailable);
                    pstmt.setInt(5, id);

                    int rowsAffected = pstmt.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "✅ Book Updated Successfully!");
                        dispose(); // Close window after update
                    } else {
                        JOptionPane.showMessageDialog(null, "❌ Update Failed! Book ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Invalid Book ID! Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
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

        SwingUtilities.invokeLater(UpdateBook::new);
    }
}
