import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddMembership {
    private JFrame frame;
    private JComboBox<String> membershipOptions;

    public AddMembership() {
        frame = new JFrame("Add Membership");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null); // Center window
        frame.setUndecorated(true); // Removes default title bar

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
        mainPanel.setLayout(new GridBagLayout());

        // Inner Card Panel for Classic Look
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new GridLayout(4, 1, 10, 10));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setPreferredSize(new Dimension(400, 250));
        cardPanel.setOpaque(true);
        cardPanel.setBorder(BorderFactory.createLineBorder(new Color(192, 192, 192), 2, true));

        // Header Label
        JLabel headerLabel = new JLabel("Add Membership", JLabel.CENTER);
        headerLabel.setFont(new Font("Serif", Font.BOLD, 22));
        headerLabel.setForeground(new Color(0, 51, 102));
        cardPanel.add(headerLabel);

        // Membership Selection
        JLabel label = new JLabel("Select Duration:", JLabel.CENTER);
        label.setFont(new Font("Serif", Font.PLAIN, 18));
        cardPanel.add(label);

        String[] options = {"6 months", "1 year", "2 years"};
        membershipOptions = new JComboBox<>(options);
        membershipOptions.setFont(new Font("Serif", Font.PLAIN, 16));
        membershipOptions.setBackground(new Color(230, 230, 250));
        cardPanel.add(membershipOptions);

        // Submit Button with Rounded Corners & Hover Effect
        JButton submitButton = new JButton("Submit") {
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
        submitButton.setFont(new Font("Serif", Font.BOLD, 18));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setOpaque(false);
        submitButton.setPreferredSize(new Dimension(100, 40));

        // Hover Effect
        submitButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                submitButton.setForeground(Color.YELLOW);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                submitButton.setForeground(Color.WHITE);
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedOption = (String) membershipOptions.getSelectedItem();
                JOptionPane.showMessageDialog(frame, "You selected: " + selectedOption);
            }
        });

        cardPanel.add(submitButton);

        // Close Button
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 16));
        closeButton.setForeground(Color.RED);
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        closeButton.setContentAreaFilled(false);
        closeButton.setOpaque(true);
        closeButton.setBackground(Color.WHITE);
        closeButton.addActionListener(e -> System.exit(0));

        // Position Components
        mainPanel.add(cardPanel);
        frame.add(closeButton, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new AddMembership();
    }
}
