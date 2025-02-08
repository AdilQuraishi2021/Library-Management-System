import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class BookSelection extends JFrame {
    private JComboBox<String> bookDropdown;
    private JTextField searchField, authorField, issueDateField, returnDateField;
    private JButton submitButton, decreaseReturnDateButton;
    private JLabel messageLabel;
    private Connection connection;
    private ArrayList<String> bookList;
    private int returnDays = 15; // Default return days

    public BookSelection() {
        setTitle("Book Selection");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel with gradient background
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
        JLabel titleLabel = new JLabel("📚 Select a Book");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(titleLabel, gbc);

        // Fetch book names from database
        bookList = getBooksFromDatabase();

        // Search Field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(searchField, gbc);

        // Dropdown for books
        bookDropdown = new JComboBox<>(bookList.toArray(new String[0]));
        bookDropdown.setFont(new Font("Arial", Font.PLAIN, 16));
        bookDropdown.setPreferredSize(new Dimension(250, 35));
        bookDropdown.setEditable(true); // Enables live search
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(bookDropdown, gbc);

        // Author Field
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        mainPanel.add(authorLabel, gbc);

        authorField = new JTextField(20);
        authorField.setEditable(false);
        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(authorField, gbc);



        // Issue Date
        JLabel issueDateLabel = new JLabel("Issue Date:");
        issueDateLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(issueDateLabel, gbc);

        issueDateField = new JTextField(20);
        issueDateField.setEditable(false);
        issueDateField.setText(getTodayDate());
        gbc.gridx = 1;
        gbc.gridy = 4;
        mainPanel.add(issueDateField, gbc);

        // Return Date
        JLabel returnDateLabel = new JLabel("Return Date:");
        returnDateLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        mainPanel.add(returnDateLabel, gbc);

        returnDateField = new JTextField(20);
        returnDateField.setEditable(false);
        returnDateField.setText(getReturnDate(returnDays));
        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(returnDateField, gbc);

        // Decrease Return Date Button
        decreaseReturnDateButton = new JButton("Reduce Return Date");
        gbc.gridx = 1;
        gbc.gridy = 6;
        mainPanel.add(decreaseReturnDateButton, gbc);

        decreaseReturnDateButton.addActionListener(e -> {
            if (returnDays > 1) {
                returnDays--;
                returnDateField.setText(getReturnDate(returnDays));
            }
        });

        // Submit Button
        submitButton = new JButton("Submit");
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        mainPanel.add(submitButton, gbc);

        // Message Label
        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        mainPanel.add(messageLabel, gbc);

        // Attach search functionality
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filterDropdown(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filterDropdown(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filterDropdown(); }
        });

        // Show author on book selection
        bookDropdown.addActionListener(e -> updateAuthorField());

        submitButton.addActionListener(e -> validateSelection());

        // Add panel to frame
        add(mainPanel);
    }

    private void filterDropdown() {
        String searchText = searchField.getText().toLowerCase();
        bookDropdown.removeAllItems();

        for (String book : bookList) {
            if (book.toLowerCase().contains(searchText)) {
                bookDropdown.addItem(book);
            }
        }

        if (bookDropdown.getItemCount() == 0) {
            bookDropdown.addItem("No match found");
        }
    }

    private void updateAuthorField() {
        String selectedBook = (String) bookDropdown.getSelectedItem();
        if (selectedBook != null && !selectedBook.equals("-- Select a Book --")) {
            authorField.setText(getAuthorFromDatabase(selectedBook));
        } else {
            authorField.setText("");
        }
    }

    private void validateSelection() {
        String selectedBook = (String) bookDropdown.getSelectedItem();
        String issueDate = getTodayDate();
        String returnDate = getReturnDate(returnDays);
        String userName = JOptionPane.showInputDialog("Enter your name:");

        if (userName == null || userName.trim().isEmpty()) {
            messageLabel.setText("⚠️ Please enter a valid name.");
            return;
        }

        if (selectedBook == null || selectedBook.equals("-- Select a Book --") || selectedBook.equals("No match found")) {
            messageLabel.setText("⚠️ Please select a valid book.");
            return;
        }

        if (updateBookAvailability(selectedBook)) {
            storeIssuedBook(userName, selectedBook, issueDate, returnDate);
        } else {
            messageLabel.setText("⚠️ This book is already issued.");
        }
    }


    private boolean updateBookAvailability(String bookTitle) {
        boolean updated = false;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");

            // Check if the book is available
            PreparedStatement checkStmt = connection.prepareStatement("SELECT isAvailable FROM books WHERE title = ?");
            checkStmt.setString(1, bookTitle);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("isAvailable")) {  // If available, mark as issued
                PreparedStatement updateStmt = connection.prepareStatement("UPDATE books SET isAvailable = false WHERE title = ?");
                updateStmt.setString(1, bookTitle);
                updateStmt.executeUpdate();
                updateStmt.close();
                updated = true;
            }

            rs.close();
            checkStmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return updated;
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

    private String getAuthorFromDatabase(String bookTitle) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");
            PreparedStatement stmt = connection.prepareStatement("SELECT author FROM books WHERE title = ?");
            stmt.setString(1, bookTitle);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getString("author");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    private String getTodayDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    private String getReturnDate(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }

    private void storeIssuedBook(String name, String bookTitle, String issueDate, String returnDate) {
        Connection connection = null;
        PreparedStatement stmt = null;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/LibraryManagementSystem", "root", "AdIl@6969");
            connection.setAutoCommit(false); // Disable auto-commit for transaction safety

            // Check if book already issued
            String checkQuery = "SELECT COUNT(*) FROM issueBooks WHERE title = ? AND name = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setString(1, bookTitle);
            checkStmt.setString(2, name);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            checkStmt.close();

            if (count > 0) {
                JOptionPane.showMessageDialog(null, "⚠️ This book is already issued to this user.");
                return;
            }

            // Insert into issueBooks
            String query = "INSERT INTO issueBooks (name, title, issue_date, return_date) VALUES (?, ?, ?, ?)";
            stmt = connection.prepareStatement(query);
            stmt.setString(1, name);
            stmt.setString(2, bookTitle);
            stmt.setString(3, issueDate);
            stmt.setString(4, returnDate);

            int rowsInserted = stmt.executeUpdate();
            connection.commit(); // Commit changes

            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "✔️ Book Issued Successfully!");
            } else {
                JOptionPane.showMessageDialog(null, "⚠️ Failed to issue book.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
            try {
                if (connection != null) connection.rollback(); // Rollback in case of error
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> new BookSelection().setVisible(true));

    }
}
