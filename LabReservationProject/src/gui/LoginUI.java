package gui;

import data.MaintainUser;
import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Login screen — first window the user sees.
 * Validates email + password against the users CSV.
 */
public class LoginUI extends JFrame {

    private static final String USERS_CSV = "Deliverable2/LabReservationProject/data/users.csv";

    private JTextField     emailField;
    private JPasswordField passwordField;
    private JLabel         errorLabel;

    public LoginUI() {
        super("Lab Reservation System — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setResizable(false);
        buildUI();
    }

    private void buildUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(6, 6, 6, 6);
        gbc.fill    = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel title = new JLabel("Lab Reservation System", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Email:"), gbc);
        emailField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Password:"), gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordField, gbc);

        // Error label
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panel.add(errorLabel, gbc);

        // Login button
        JButton loginBtn = new JButton("Login");
        gbc.gridy = 4;
        panel.add(loginBtn, gbc);

        loginBtn.addActionListener(e -> handleLogin());
        // Allow pressing Enter to login
        getRootPane().setDefaultButton(loginBtn);

        add(panel);
    }

    private void handleLogin() {
        String email    = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter email and password.");
            return;
        }

        try {
            MaintainUser mu = new MaintainUser();
            mu.load(USERS_CSV);
            User user = mu.findByEmail(email);

            if (user == null || !user.getPassword().equals(password)) {
                errorLabel.setText("Invalid email or password.");
                return;
            }

            // Login successful — open main dashboard
            dispose();
            new MainDashboardUI(user).setVisible(true);

        } catch (Exception ex) {
            errorLabel.setText("Error loading users: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
