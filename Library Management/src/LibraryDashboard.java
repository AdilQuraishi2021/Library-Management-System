import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class LibraryDashboard extends JFrame {
    private int userId; // Store logged-in user ID

    public LibraryDashboard(String userType, int userId) {
        this.userId = userId; // Assign userId

        setTitle("Library Dashboard");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true); // Removes title bar for a sleek look

        // Main Panel with Gradient Background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(25, 25, 112); // Dark blue
                Color color2 = new Color(0, 102, 204); // Light blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout(10, 10));

        // Header
        JLabel welcomeLabel = new JLabel("Welcome " + userType + " to Library Management System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Serif", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        // Grid panel for buttons
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 15, 15)); // 2x3 Grid Layout
        gridPanel.setOpaque(false);
        gridPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        String[][] sections = {
                {"Search Book", "book-search.png"},
                {"Return Book", "return.png"},
                {"Add Membership", "membership.png"},
                {"Update Membership", "update.png"},
                {"Add Book", "add-book.png"},
                {"Log Out", "logout.png"}
        };

        for (String[] section : sections) {
            JButton button = createStyledButton(section[0], section[1]);
            gridPanel.add(button);
        }

        mainPanel.add(gridPanel, BorderLayout.CENTER);

        // Close Button
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(Color.RED);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setContentAreaFilled(false);
        closeButton.setOpaque(true);
        closeButton.setBackground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));

        mainPanel.add(closeButton, BorderLayout.SOUTH);
        add(mainPanel);
        setVisible(true);
    }

    // Method to create styled buttons with icons
    private JButton createStyledButton(String text, String iconPath) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 153, 76)); // Green Button
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border
            }
        };

        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setPreferredSize(new Dimension(150, 100));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        // Load icon with scaling
        ImageIcon icon = new ImageIcon(new ImageIcon(iconPath).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        button.setIcon(icon);

        // Hover Effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setForeground(Color.YELLOW);
            }

            public void mouseExited(MouseEvent evt) {
                button.setForeground(Color.WHITE);
            }
        });

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (text.equals("Search Book")) {
                    new BookSelection().setVisible(true);
                } else if (text.equals("Return Book")) {
                    new BookReturn().setVisible(true);
                }
                else if (text.equals("Add Membership")) {
                    new AddMembership();
                }
                else if (text.equals("Update Membership")) {
                    new UpdateMembership();
                }
                else if (text.equals("Add Book")) {
                    new AddBook();
                }
                else if (text.equals("Update Book")) {
                    new UpdateBook();
                }
                else if (text.equals("Log Out")) {
                    new LogOut(userId); // ✅ Pass userId to LogOut class
                }
                else {
                    JOptionPane.showMessageDialog(null, "You clicked: " + text);
                }
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LibraryDashboard("Admin", 1); // ✅ Pass userId (replace with actual logged-in user ID)
        });
    }
}
