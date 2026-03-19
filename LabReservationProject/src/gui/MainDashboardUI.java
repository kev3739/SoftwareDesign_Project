package gui;

import model.User;

import javax.swing.*;
import java.awt.*;

/**
 * Main dashboard shown after login.
 * Contains tabs: My Reservations, Make Reservation, Equipment, Admin (lab managers only)
 */
public class MainDashboardUI extends JFrame {

    private final User currentUser;

    public MainDashboardUI(User currentUser) {
        super("Lab Reservation System — " + currentUser.getName()
            + " (" + currentUser.getUserType() + ")");
        this.currentUser = currentUser;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();

        // Tab 1: My Reservations
        tabs.addTab("My Reservations", new ReservationListPanel(currentUser));

        // Tab 2: Make Reservation
        tabs.addTab("Make Reservation", new MakeReservationPanel(currentUser));

        // Tab 3: Equipment (view availability)
        tabs.addTab("Equipment", new EquipmentPanel(currentUser));

        // Tab 4: Admin panel — only for LabManager
        if ("LabManager".equals(currentUser.getUserType())) {
            tabs.addTab("Admin", new AdminPanel(currentUser));
        }

        // Top bar showing logged-in user
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        topBar.setBackground(new Color(52, 73, 94));

        JLabel userLabel = new JLabel("Logged in as: " + currentUser.getName()
            + " | " + currentUser.getUserType()
            + " | Rate: $" + currentUser.getHourlyRate() + "/hr");
        userLabel.setForeground(Color.WHITE);
        topBar.add(userLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
        topBar.add(logoutBtn, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);
    }
}
