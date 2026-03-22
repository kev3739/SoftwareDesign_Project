package gui;

import data.MaintainEquipment;
import model.Equipment;
import model.User;
import service.ReservationService;
import strategy.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel for making a new reservation.
 * User picks equipment, time slot, and payment method.
 */
public class MakeReservationPanel extends JPanel {

    private static final String RES_CSV  = "data/reservations.csv";
    private static final String PAY_CSV  = "data/payments.csv";
    private static final String EQUIP_CSV = "data/equipment.csv";
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final User               currentUser;
    private final ReservationService reservationService;

    private JComboBox<String> equipmentDropdown;
    private JTextField        startField;
    private JTextField        endField;
    private JComboBox<String> paymentDropdown;
    private JLabel            rateLabel;
    private JLabel            depositLabel;
    private List<Equipment>   availableEquipment;

    public MakeReservationPanel(User currentUser) {
        this.currentUser        = currentUser;
        this.reservationService = new ReservationService(RES_CSV, PAY_CSV);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        buildUI();
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        int row = 0;

        // Title
        JLabel title = new JLabel("Make a New Reservation");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = row++; gbc.gridwidth = 2;
        form.add(title, gbc);
        gbc.gridwidth = 1;

        // Equipment
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Equipment:"), gbc);
        equipmentDropdown = new JComboBox<>();
        loadEquipment();
        gbc.gridx = 1;
        form.add(equipmentDropdown, gbc);
        row++;

        // Start time
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Start Time (yyyy-MM-dd HH:mm):"), gbc);
        startField = new JTextField(20);
        startField.setText(LocalDateTime.now().plusHours(1).format(FMT));
        gbc.gridx = 1;
        form.add(startField, gbc);
        row++;

        // End time
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("End Time (yyyy-MM-dd HH:mm):"), gbc);
        endField = new JTextField(20);
        endField.setText(LocalDateTime.now().plusHours(3).format(FMT));
        gbc.gridx = 1;
        form.add(endField, gbc);
        row++;

        // Payment method
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Payment Method:"), gbc);
        paymentDropdown = new JComboBox<>(new String[]{
            "Credit Card", "Debit", "Institutional Account", "Research Grant"
        });
        gbc.gridx = 1;
        form.add(paymentDropdown, gbc);
        row++;

        // Rate info
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Your Hourly Rate:"), gbc);
        rateLabel = new JLabel("$" + currentUser.getHourlyRate() + "/hr");
        gbc.gridx = 1;
        form.add(rateLabel, gbc);
        row++;

        // Deposit info
        gbc.gridx = 0; gbc.gridy = row;
        form.add(new JLabel("Deposit Required:"), gbc);
        depositLabel = new JLabel("$" + currentUser.getHourlyRate() + " (1 hour rate)");
        gbc.gridx = 1;
        form.add(depositLabel, gbc);
        row++;

        // Submit button
        JButton submitBtn = new JButton("Make Reservation");
        submitBtn.setBackground(new Color(46, 139, 87));
        submitBtn.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
        form.add(submitBtn, gbc);

        submitBtn.addActionListener(e -> handleSubmit());
        add(form, BorderLayout.NORTH);
    }

    private void loadEquipment() {
        equipmentDropdown.removeAllItems();
        try {
            MaintainEquipment me = new MaintainEquipment();
            me.load(EQUIP_CSV);
            availableEquipment = me.getAvailableEquipment();
            for (Equipment eq : availableEquipment) {
                equipmentDropdown.addItem(eq.getEquipmentId() + " — " + eq.getName()
                    + " (" + eq.getLabLocation() + ")");
            }
            if (availableEquipment.isEmpty()) {
                equipmentDropdown.addItem("No equipment available");
            }
        } catch (Exception e) {
            equipmentDropdown.addItem("Could not load equipment");
        }
    }

    private PaymentStrategy getSelectedPaymentStrategy() {
        switch ((String) paymentDropdown.getSelectedItem()) {
            case "Debit":                return new DebitPaymentStrategy();
            case "Institutional Account": return new InstitutionalAccountPaymentStrategy();
            case "Research Grant":        return new ResearchGrantPaymentStrategy();
            default:                      return new CreditCardPaymentStrategy();
        }
    }

    private void handleSubmit() {
        if (availableEquipment == null || availableEquipment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No equipment available to reserve.");
            return;
        }

        int selectedIndex = equipmentDropdown.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= availableEquipment.size()) return;

        Equipment equipment = availableEquipment.get(selectedIndex);

        try {
            LocalDateTime start = LocalDateTime.parse(startField.getText().trim(), FMT);
            LocalDateTime end   = LocalDateTime.parse(endField.getText().trim(), FMT);

            if (!end.isAfter(start)) {
                JOptionPane.showMessageDialog(this,
                    "End time must be after start time.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = reservationService.makeReservation(
                currentUser, equipment, start, end, getSelectedPaymentStrategy()
            );

            if (success) {
                JOptionPane.showMessageDialog(this,
                    "Reservation made successfully!\nDeposit of $"
                    + currentUser.getHourlyRate() + " charged.");
                // Reset fields
                startField.setText(LocalDateTime.now().plusHours(1).format(FMT));
                endField.setText(LocalDateTime.now().plusHours(3).format(FMT));
                loadEquipment();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Could not make reservation.\nEquipment may already be booked or start time is in the past.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Invalid date format. Use yyyy-MM-dd HH:mm",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
