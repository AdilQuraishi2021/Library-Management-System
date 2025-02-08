import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class BookReturn extends JFrame {
    private JComboBox<String> bookDropdown;
    private JButton returnButton;
    private JLabel messageLabel;
    private Connection connection;
    private String userName = "John Doe"; // Replace with actual user input

    public BookReturn() {
        setTitle("📚 Return a Book");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with Gradient Background
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
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Title Label
        JLabel titleLabel = new JLabel("📖 Return Your Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Fetch issued books for the user
        ArrayList<String> issuedBooks = getIssuedBooks();
        bookDropdown = new JComboBox<>(issuedBooks.toArray(new String[0]));
        bookDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        bookDropdown.setPreferredSize(new Dimension(250, 35));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(bookDropdown, gbc);

        // Return Button
        returnButton = new JButton("✅ Return Book");
        returnButton.setFont(new Font("Arial", Font.BOLD, 16));
        returnButton.setBackground(new Color(50, 205, 50));
        returnButton.setForeground(Color.WHITE);
        returnButton.setFocusPainted(false);
        returnButton.setBorderPainted(false);
        returnButton.setPreferredSize(new Dimension(180, 40));
        returnButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(returnButton, gbc);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Arial", Font.BOLD, 14));
        messageLabel.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(messageLabel, gbc);

        returnButton.addActionListener(this::returnBook);

        add(mainPanel);
    }

    private ArrayList<String> getIssuedBooks() {
        ArrayList<String> books = new ArrayList<>();

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");
            String query = "SELECT title FROM issueBooks WHERE name = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, userName);
            System.out.println("Executing Query: " + stmt.toString()); // Debugging line
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                books.add(rs.getString("title"));
            }

            System.out.println("Executing Query: 2" + books);
            rs.close();
            stmt.close();
            connection.close();
            System.out.println("Executing Query: 3" + stmt.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (books.isEmpty()) {
            books.add("No books issued");
        }

        return books;
    }


    private void returnBook(ActionEvent e) {
        String selectedBook = (String) bookDropdown.getSelectedItem();
        if (selectedBook == null || selectedBook.equals("No books issued")) {
            messageLabel.setText("⚠️ No book selected.");
            return;
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");

            // Fetch issue date from issueBooks table
            String dateQuery = "SELECT issue_date FROM issueBooks WHERE title = ? AND name = ?";
            PreparedStatement dateStmt = connection.prepareStatement(dateQuery);
            dateStmt.setString(1, selectedBook);
            dateStmt.setString(2, userName);
            ResultSet rs = dateStmt.executeQuery();

            if (rs.next()) {
                LocalDate issueDate = rs.getDate("issue_date").toLocalDate();
                LocalDate returnDate = LocalDate.now();
                long daysIssued = ChronoUnit.DAYS.between(issueDate, returnDate);

                if (daysIssued > 15) {
                    int fine = (int) ((daysIssued - 15) * 10); // Fine: 10 per extra day
                    messageLabel.setText("⚠️ Late return! Fine: ₹" + fine);
                    if(!payFine(fine))
                        return;
                } else {
                    messageLabel.setText("✔️ Book returned on time.");
                }

                // Remove book from issueBooks table
                String deleteQuery = "DELETE FROM issueBooks WHERE title = ? AND name = ?";
                PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
                deleteStmt.setString(1, selectedBook);
                deleteStmt.setString(2, userName);
                deleteStmt.executeUpdate();
                deleteStmt.close();

                // Update book availability
                String updateQuery = "UPDATE books SET isAvailable = true WHERE title = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, selectedBook);
                updateStmt.executeUpdate();
                updateStmt.close();

                bookDropdown.removeItem(selectedBook); // Remove returned book from dropdown
            } else {
                messageLabel.setText("⚠️ Book not found in issue records.");
            }

            rs.close();
            dateStmt.close();
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            messageLabel.setText("⚠️ Database error.");
        }
    }

    private static boolean payFine(int fine) {
        int response = JOptionPane.showConfirmDialog(null,
                "Do you want to pay the fine?", "Fine Payment",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        return response == JOptionPane.YES_OPTION;
    }

    private ArrayList<String> getBooksFromDatabase() {
        ArrayList<String> books = new ArrayList<>();
        books.add("-- Select a Book --");
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT title FROM books");
            while (rs.next()) {
                books.add(rs.getString("title"));
            }
            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookReturn().setVisible(true));
    }
}
