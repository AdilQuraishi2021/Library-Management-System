import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton, cancelButton, signUpButton;

    public Login() {
        setTitle("📚 Library Management System - Login");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); // Removes default window frame

        // Gradient Background Panel
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, new Color(30, 144, 255), getWidth(), getHeight(), new Color(70, 130, 180));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title Label
        JLabel titleLabel = new JLabel("📚 Library Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("👤 Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        usernameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(usernameLabel, gbc);

        // Username Field
        usernameField = new JTextField(15);
        styleTextField(usernameField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("🔒 Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(15);
        styleTextField(passwordField);
        passwordField.setEchoChar('*');
        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);

        // Login Button
        loginButton = new JButton("Login");
        styleButton(loginButton, new Color(0, 200, 0));

        // Cancel Button
        cancelButton = new JButton("Cancel");
        styleButton(cancelButton, Color.RED);

        // Sign Up Button
        signUpButton = new JButton("Sign Up");
        styleButton(signUpButton, new Color(0, 150, 255));

        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        // Button Actions
        loginButton.addActionListener(e -> authenticateUser());
        cancelButton.addActionListener(e -> System.exit(0));
        signUpButton.addActionListener(e -> new SignUpForm());

        add(mainPanel);
    }

    // Styling text fields
    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        textField.setPreferredSize(new Dimension(200, 30));
    }

    // Styling buttons
    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(100, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String url = "jdbc:mysql://localhost:3306/LibraryManagementSystem";
        String dbUser = "root";
        String dbPassword = "AdIl@6969";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword)) {
            // Fetch userId and userType along with authentication check
            String query = "SELECT id, userType FROM users WHERE name = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int userId = rs.getInt("id"); // Retrieve user ID
                String userType = rs.getString("userType"); // Retrieve user type

                JOptionPane.showMessageDialog(this, "✅ Login Successful!");
                setVisible(false);

                // Pass userType and userId correctly
                new LibraryDashboard(userType, userId);
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Database connection error!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
class SignUpForm extends JFrame {
    private JTextField nameField, emailField, phoneField, addressField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public SignUpForm() {
        setTitle("Sign Up - Library Management");
        setSize(400, 350);
        setLayout(new GridLayout(6, 2, 10, 10));
        setLocationRelativeTo(null);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Email:"));
        emailField = new JTextField();
        add(emailField);

        add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        add(passwordField);

        add(new JLabel("Phone:"));
        phoneField = new JTextField();
        add(phoneField);

        add(new JLabel("Address:"));
        addressField = new JTextField();
        add(addressField);

        registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser());
        add(registerButton);

        setVisible(true);
    }

    private void registerUser() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String phone = phoneField.getText();
        String address = addressField.getText();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969")) {
            String query = "INSERT INTO users (name, email, password, phone, address) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, phone);
            stmt.setString(5, address);

            stmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Registration Successful!");
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "⚠️ Error registering user!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
