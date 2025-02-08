import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AddBook extends JFrame {
    private JTextField titleField, authorField, categoryField;
    private JCheckBox availabilityCheckBox;

    // Database credentials (Replace with your actual credentials)
    private static final String URL = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
    private static final String USER = "root";
    private static final String PASSWORD = "AdIl@6969";

    public AddBook() {
        setTitle("Add New Book");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Gradient Background Panel
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0, new Color(80, 80, 150), getWidth(), getHeight(), new Color(20, 20, 80));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setLayout(new GridBagLayout());

        // Form Components
        JLabel header = new JLabel("Add a New Book");
        header.setFont(new Font("Serif", Font.BOLD, 24));
        header.setForeground(Color.WHITE);

        JLabel titleLabel = createStyledLabel("Title:");
        titleField = createStyledTextField();

        JLabel authorLabel = createStyledLabel("Author:");
        authorField = createStyledTextField();

        JLabel categoryLabel = createStyledLabel("Category:");
        categoryField = createStyledTextField();

        JLabel availabilityLabel = createStyledLabel("Available:");
        availabilityCheckBox = new JCheckBox();
        availabilityCheckBox.setOpaque(false);
        availabilityCheckBox.setSelected(true);

        JButton saveButton = createStyledButton("Save Book");

        // Layout Settings
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(header, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(titleLabel, gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(authorLabel, gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(categoryLabel, gbc);
        gbc.gridx = 1;
        panel.add(categoryField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(availabilityLabel, gbc);
        gbc.gridx = 1;
        panel.add(availabilityCheckBox, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(saveButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Button Action Listener
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBookToDatabase();
            }
        });

        setVisible(true);
    }

    // Save Book Data to Database
    private void saveBookToDatabase() {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String category = categoryField.getText().trim();
        boolean isAvailable = availabilityCheckBox.isSelected();

        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO books (title, author, category, isAvailable) VALUES (?, ?, ?, ?)")) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, category.isEmpty() ? null : category);
            pstmt.setBoolean(4, isAvailable);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "✅ Book Added Successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(null, "❌ Book Addition Failed!", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database Error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Clear Fields After Insertion
    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        categoryField.setText("");
        availabilityCheckBox.setSelected(true);
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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(AddBook::new);
    }
}
