package gui;

import data.MaintainEquipment;
import data.MaintainReservation;
import model.Equipment;
import model.Reservation;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Admin panel — only visible to LabManagers.
 * Allows managing equipment states and viewing all reservations.
 */
public class AdminPanel extends JPanel {

    private static final String EQUIP_CSV = "Deliverable2/LabReservationProject/data/equipment.csv";
    private static final String RES_CSV   = "Deliverable2/LabReservationProject/data/reservations.csv";
    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    // Equipment tab
    private JTable            equipTable;
    private DefaultTableModel equipModel;
    private List<Equipment>   equipmentList;

    // Reservations tab
    private JTable            resTable;
    private DefaultTableModel resModel;

    public AdminPanel(User currentUser) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
    }

    private void buildUI() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Manage Equipment", buildEquipmentTab());
        tabs.addTab("All Reservations", buildReservationsTab());
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel buildEquipmentTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"ID", "Description", "Location", "State", "Available?"};
        equipModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        equipTable = new JTable(equipModel);
        equipTable.setRowHeight(24);
        equipTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(equipTable), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton refreshBtn     = new JButton("Refresh");
        JButton enableBtn      = new JButton("Enable");
        JButton disableBtn     = new JButton("Disable");
        JButton maintenanceBtn = new JButton("Mark Maintenance");
        JButton addBtn         = new JButton("Add Equipment");

        btnPanel.add(refreshBtn);
        btnPanel.add(enableBtn);
        btnPanel.add(disableBtn);
        btnPanel.add(maintenanceBtn);
        btnPanel.add(addBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e     -> loadEquipment());
        enableBtn.addActionListener(e      -> handleEquipmentAction("enable"));
        disableBtn.addActionListener(e     -> handleEquipmentAction("disable"));
        maintenanceBtn.addActionListener(e -> handleEquipmentAction("maintenance"));
        addBtn.addActionListener(e         -> handleAddEquipment());

        loadEquipment();
        return panel;
    }

    private void loadEquipment() {
        equipModel.setRowCount(0);
        try {
            MaintainEquipment me = new MaintainEquipment();
            me.load(EQUIP_CSV);
            equipmentList = me.equipmentList;
            for (Equipment eq : equipmentList) {
                equipModel.addRow(new Object[]{
                    eq.getEquipmentId(),
                    eq.getName(),
                    eq.getLabLocation(),
                    eq.getCurrentStateName(),
                    eq.canReserve() ? "Yes" : "No"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Could not load equipment: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleEquipmentAction(String action) {
        int row = equipTable.getSelectedRow();
        if (row < 0 || equipmentList == null) {
            JOptionPane.showMessageDialog(this, "Please select a piece of equipment first.");
            return;
        }
        Equipment eq = equipmentList.get(row);
        switch (action) {
            case "enable":      eq.enable();               break;
            case "disable":     eq.disable();              break;
            case "maintenance": eq.markUnderMaintenance(); break;
        }
        try {
            MaintainEquipment me = new MaintainEquipment();
            me.load(EQUIP_CSV);
            for (Equipment e : me.equipmentList) {
                if (e.getEquipmentId().equals(eq.getEquipmentId())) {
                    me.equipmentList.set(me.equipmentList.indexOf(e), eq);
                    break;
                }
            }
            me.update(EQUIP_CSV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Could not save equipment state: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
        loadEquipment();
    }

    private void handleAddEquipment() {
        JTextField idField       = new JTextField();
        JTextField descField     = new JTextField();
        JTextField locationField = new JTextField();

        JPanel form = new JPanel(new GridLayout(3, 2, 5, 5));
        form.add(new JLabel("Equipment ID:"));  form.add(idField);
        form.add(new JLabel("Description:"));   form.add(descField);
        form.add(new JLabel("Lab Location:"));  form.add(locationField);

        int result = JOptionPane.showConfirmDialog(this, form,
            "Add Equipment", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String id       = idField.getText().trim();
            String desc     = descField.getText().trim();
            String location = locationField.getText().trim();

            if (id.isEmpty() || desc.isEmpty() || location.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.");
                return;
            }
            try {
                MaintainEquipment me = new MaintainEquipment();
                me.load(EQUIP_CSV);
                me.equipmentList.add(new Equipment(id, desc, location));
                me.update(EQUIP_CSV);
                JOptionPane.showMessageDialog(this, "Equipment added successfully.");
                loadEquipment();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Could not add equipment: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel buildReservationsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        String[] cols = {"Res ID", "User", "Equipment", "Start", "End", "Status", "Deposit"};
        resModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        resTable = new JTable(resModel);
        resTable.setRowHeight(24);
        panel.add(new JScrollPane(resTable), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadAllReservations());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(refreshBtn);
        panel.add(btnPanel, BorderLayout.SOUTH);

        loadAllReservations();
        return panel;
    }

    private void loadAllReservations() {
        resModel.setRowCount(0);
        try {
            MaintainReservation mr = new MaintainReservation();
            mr.load(RES_CSV);
            for (Reservation r : mr.reservations) {
                resModel.addRow(new Object[]{
                    r.getReservationId(),
                    r.getUserId(),
                    r.getEquipmentId(),
                    r.getStartTime().format(FMT),
                    r.getEndTime().format(FMT),
                    r.getStatus(),
                    String.format("$%.2f", r.getDepositAmount())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Could not load reservations: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
