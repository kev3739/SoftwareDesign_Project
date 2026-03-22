package gui;

import model.Reservation;
import model.User;
import service.ReservationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Shows the current user's reservations.
 * Allows cancel, modify, and extend actions.
 */
public class ReservationListPanel extends JPanel {

    private static final String RES_CSV = "data/reservations.csv";
    private static final String PAY_CSV = "data/payments.csv";
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final User               currentUser;
    private final ReservationService reservationService;
    private       JTable             table;
    private       DefaultTableModel  tableModel;
    private       List<Reservation>  reservations;

    public ReservationListPanel(User currentUser) {
        this.currentUser        = currentUser;
        this.reservationService = new ReservationService(RES_CSV, PAY_CSV);
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadReservations();
    }

    private void buildUI() {
        // Table
        String[] columns = {"ID", "Equipment", "Start", "End", "Status", "Deposit"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton refreshBtn = new JButton("Refresh");
        JButton cancelBtn  = new JButton("Cancel Reservation");
        JButton modifyBtn  = new JButton("Modify Reservation");
        JButton extendBtn  = new JButton("Extend Reservation");

        btnPanel.add(refreshBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(modifyBtn);
        btnPanel.add(extendBtn);
        add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> loadReservations());
        cancelBtn.addActionListener(e  -> handleCancel());
        modifyBtn.addActionListener(e  -> handleModify());
        extendBtn.addActionListener(e  -> handleExtend());
    }

    private void loadReservations() {
        tableModel.setRowCount(0);
        reservations = reservationService.getReservationsForUser(currentUser.getIdNumber());
        for (Reservation r : reservations) {
            tableModel.addRow(new Object[]{
                r.getReservationId(),
                r.getEquipmentId(),
                r.getStartTime().format(FMT),
                r.getEndTime().format(FMT),
                r.getStatus(),
                String.format("$%.2f", r.getDepositAmount())
            });
        }
    }

    private Reservation getSelectedReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Please select a reservation first.");
            return null;
        }
        return reservations.get(row);
    }

    private void handleCancel() {
        Reservation r = getSelectedReservation();
        if (r == null) return;

        int confirm = JOptionPane.showConfirmDialog(this,
            "Cancel reservation " + r.getReservationId() + "?\n"
            + "Deposit will be refunded if not already forfeited.",
            "Confirm Cancel", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = reservationService.cancelReservation(r);
            if (success) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.");
                loadReservations();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Cannot cancel: reservation has already started.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleModify() {
        Reservation r = getSelectedReservation();
        if (r == null) return;

        JTextField newStart = new JTextField(r.getStartTime().format(FMT));
        JTextField newEnd   = new JTextField(r.getEndTime().format(FMT));

        JPanel form = new JPanel(new GridLayout(2, 2, 5, 5));
        form.add(new JLabel("New Start (yyyy-MM-dd HH:mm):"));
        form.add(newStart);
        form.add(new JLabel("New End (yyyy-MM-dd HH:mm):"));
        form.add(newEnd);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Modify Reservation", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime start = LocalDateTime.parse(newStart.getText().trim(), FMT);
                LocalDateTime end   = LocalDateTime.parse(newEnd.getText().trim(), FMT);
                boolean success = reservationService.modifyReservation(r, start, end);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Reservation modified successfully.");
                    loadReservations();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Cannot modify: reservation has already started or time slot is taken.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use yyyy-MM-dd HH:mm",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleExtend() {
        Reservation r = getSelectedReservation();
        if (r == null) return;

        JTextField newEnd = new JTextField(r.getEndTime().format(FMT));
        JPanel form = new JPanel(new GridLayout(1, 2, 5, 5));
        form.add(new JLabel("New End Time (yyyy-MM-dd HH:mm):"));
        form.add(newEnd);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Extend Reservation", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                LocalDateTime end = LocalDateTime.parse(newEnd.getText().trim(), FMT);
                boolean success = reservationService.extendReservation(r, end);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Reservation extended successfully.");
                    loadReservations();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Cannot extend: equipment is already booked during that time.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Invalid date format. Use yyyy-MM-dd HH:mm",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
