package Components;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ContainerPage extends JPanel {

    private static final long serialVersionUID = 1L;
    private JPanel panel;
    private JButton logoutButton;
    private JButton transactionsPAGE;
    private JButton addTransactionButton;
    private JButton homeButton;
    private JPanel overlayPanel;
    private JTextField transactionNameField;
    private JTextField transactionAmountField;
    private JTextField transactionDateField;
    private JComboBox<String> transactionTypeComboBox;

    private JPanel transactionsPanel; // Panel to display transactions
    private String jsonFilePath = "src/Data/bankaccounts.json"; // Path to your JSON file
    private JLabel usernameLabel;
    private String loggedInUser;  // Store the logged-in username

    // Method to update the displayed username
    public void updateUsername(String username) {
        loggedInUser = username;
        usernameLabel.setText("Current User: " + username);
    }

    /**
     * Create the panel.
     */
    public ContainerPage() {

        // Initialize username label
        usernameLabel = new JLabel("Current User: ");
        usernameLabel.setBounds(10, 70, 400, 30);  // Position it below the top panel
        this.add(usernameLabel);

        // Set panel dimensions and layout
        setPreferredSize(new Dimension(450, 686));
        setBackground(Color.WHITE);
        setLayout(null);

        // Create top panel
        panel = new JPanel();
        panel.setBounds(0, 0, 450, 67); // Set panel to the top (y=0)
        panel.setBackground(Color.WHITE);
        add(panel);
        panel.setLayout(new GridLayout(1, 0, 0, 0)); // Horizontal layout with equal spacing

        // Add buttons to the panel
        homeButton = new NavButton("");
        homeButton.setIcon(new ImageIcon(ContainerPage.class.getResource("/Resources/home (1).png")));
        panel.add(homeButton);

        logoutButton = new NavButton("");
        logoutButton.setIcon(new ImageIcon(ContainerPage.class.getResource("/Resources/logout (2).png")));
        logoutButton.setFont(new Font("Tahoma", Font.PLAIN, 21));
        panel.add(logoutButton);

        transactionsPAGE = new NavButton("Transactions page");
        panel.add(transactionsPAGE);

        addTransactionButton = new NavButton("+");
        panel.add(addTransactionButton);

        // Create overlay panel for adding transaction
        overlayPanel = new JPanel();
        overlayPanel.setBounds(0, 0, 450, 686); // Cover entire frame
        overlayPanel.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        overlayPanel.setLayout(null); // Absolute positioning for form elements

        // Create form elements on overlay
        JLabel nameLabel = new JLabel("Transaction Name:");
        nameLabel.setBounds(100, 150, 150, 30);
        overlayPanel.add(nameLabel);

        transactionNameField = new JTextField();
        transactionNameField.setBounds(250, 150, 150, 30);
        overlayPanel.add(transactionNameField);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(100, 200, 150, 30);
        overlayPanel.add(amountLabel);

        transactionAmountField = new JTextField();
        transactionAmountField.setBounds(250, 200, 150, 30);
        overlayPanel.add(transactionAmountField);

        JLabel dateLabel = new JLabel("Date:");
        dateLabel.setBounds(100, 250, 150, 30);
        overlayPanel.add(dateLabel);

        transactionDateField = new JTextField(getCurrentDate());
        transactionDateField.setBounds(250, 250, 150, 30);
        overlayPanel.add(transactionDateField);

        JLabel typeLabel = new JLabel("Type:");
        typeLabel.setBounds(100, 300, 150, 30);
        overlayPanel.add(typeLabel);

        transactionTypeComboBox = new JComboBox<>(new String[]{"Receive", "Payment"});
        transactionTypeComboBox.setBounds(250, 300, 150, 30);
        overlayPanel.add(transactionTypeComboBox);

        // Add a button to submit the transaction
        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(175, 350, 100, 30);
        overlayPanel.add(submitButton);

        // Initially, hide the overlay
        overlayPanel.setVisible(false);
        add(overlayPanel);

        // Transactions panel to display transactions
        transactionsPanel = new JPanel();
        transactionsPanel.setBounds(0, 67, 450, 619);
        transactionsPanel.setLayout(new BoxLayout(transactionsPanel, BoxLayout.Y_AXIS));
        add(transactionsPanel);

        // Add button actions
        onClick();

        // Submit button action for adding a transaction
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String transactionName = transactionNameField.getText();
                String transactionAmountText = transactionAmountField.getText();
                String transactionDate = transactionDateField.getText();
                String transactionType = (String) transactionTypeComboBox.getSelectedItem();

                if (transactionName.isEmpty() || transactionAmountText.isEmpty() || transactionDate.isEmpty()) {
                    JOptionPane.showMessageDialog(ContainerPage.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    double amount = Double.parseDouble(transactionAmountText);
                    // Add the transaction and save it to the JSON file
                    addTransaction(transactionName, amount, transactionDate, transactionType);
                    overlayPanel.setVisible(false); // Hide overlay after submission
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(ContainerPage.this, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void addTransaction(String type, double value, String date, String transactionType) {
        try {
            // Read the current data from the JSON file
            JSONArray users = readJsonFile();

            // Find the user matching the logged-in username
            JSONObject user = null;
            for (int i = 0; i < users.length(); i++) {
                JSONObject currentUser = users.getJSONObject(i);
                if (currentUser.getString("username").equals(loggedInUser)) {
                    user = currentUser;
                    break;
                }
            }

            if (user == null) {
                JOptionPane.showMessageDialog(this, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Add the transaction to the expenses array
            JSONArray expenses = user.optJSONArray("expenses");
            if (expenses == null) {
                expenses = new JSONArray();
            }

            JSONObject newTransaction = new JSONObject();
            newTransaction.put("transactionType", transactionType);
            newTransaction.put("date", date);
            newTransaction.put("type", type);
            newTransaction.put("value", value);
            expenses.put(newTransaction);

            // Update the user's expenses
            user.put("expenses", expenses);

            // Save the updated data back to the JSON file
            writeJsonFile(users);

            // Refresh the transactions panel
            displayTransactions();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading/writing the JSON file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JSONArray readJsonFile() throws IOException {
        FileReader reader = new FileReader(jsonFilePath);
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = reader.read()) != -1) {
            sb.append((char) c);
        }
        reader.close();

        return new JSONArray(sb.toString());
    }

    private void writeJsonFile(JSONArray jsonData) throws IOException {
        FileWriter writer = new FileWriter(jsonFilePath);
        writer.write(jsonData.toString(4)); // Indent with 4 spaces for better readability
        writer.close();
    }

    private void displayTransactions() {
        try {
            // Read the current data from the JSON file
            JSONArray users = readJsonFile();

            // Find the user matching the logged-in username
            JSONObject user = null;
            for (int i = 0; i < users.length(); i++) {
                JSONObject currentUser = users.getJSONObject(i);
                if (currentUser.getString("username").equals(loggedInUser)) {
                    user = currentUser;
                    break;
                }
            }

            if (user == null) {
                JOptionPane.showMessageDialog(this, "User data not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Clear the current contents of the transactions panel
            transactionsPanel.removeAll();

            // Create a map to group transactions by date
            Map<String, StringBuilder> transactionsByDate = new HashMap<>();

            // Get the user's expenses and display them
            JSONArray expenses = user.optJSONArray("expenses");
            if (expenses != null) {
                for (int i = 0; i < expenses.length(); i++) {
                    JSONObject transaction = expenses.getJSONObject(i);
                    String type = transaction.getString("type");
                    double value = transaction.getDouble("value");
                    String name = transaction.getString("transactionType");
                    String date = transaction.getString("date");

                    // Format the amount with "+" or "-" based on the transaction type
                    String formattedAmount = (type.equals("Receive") ? "+" : "-") + " " + value;

                    // Group transactions by date
                    if (!transactionsByDate.containsKey(date)) {
                        transactionsByDate.put(date, new StringBuilder());
                        transactionsByDate.get(date).append(formatDate(date) + "\n"); // Add formatted date
                    }

                    // Add the transaction to the respective date group
                    transactionsByDate.get(date).append(String.format("%-40s %s\n", name, formattedAmount));
                }
            }

            // Display the grouped transactions
            for (String date : transactionsByDate.keySet()) {
                JTextArea transactionTextArea = new JTextArea(transactionsByDate.get(date).toString());
                transactionTextArea.setEditable(false);
                transactionTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Set a monospaced font
                transactionsPanel.add(transactionTextArea);
            }

            // Refresh the transactions panel
            transactionsPanel.revalidate();
            transactionsPanel.repaint();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error reading the JSON file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String formatDate(String date) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM dd, yyyy");
            return outputFormat.format(inputFormat.parse(date));
        } catch (Exception e) {
            return date; // If parsing fails, return the original date
        }
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new java.util.Date());
    }

    private void onClick() {
        // Handle the click actions for logout, transactions, and other buttons
        transactionsPAGE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayTransactions(); // Show the transactions when clicked
            }
        });

        // Button to show the overlay for adding a transaction
        addTransactionButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Toggle visibility of the overlay panel
                overlayPanel.setVisible(!overlayPanel.isVisible());
            }
        });

        // Logout button logic (redirect or perform logout actions)
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Log out action (go back to login screen)
                // Call logout function or navigate to login screen
                System.exit(0); // For now, we will just exit the application
            }
        });
    }
}
