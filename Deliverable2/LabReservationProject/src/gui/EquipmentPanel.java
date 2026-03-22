package gui;

import data.MaintainEquipment;
import model.Equipment;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Shows all equipment and their current availability/state.
 */
public class EquipmentPanel extends JPanel {

    private static final String EQUIP_CSV = "data/equipment.csv";

    private       JTable            table;
    private       DefaultTableModel tableModel;

    public EquipmentPanel(User currentUser) {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buildUI();
        loadEquipment();
    }

    private void buildUI() {
        JLabel title = new JLabel("Available Equipment", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 14));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Equipment ID", "Description", "Location", "State", "Available?"};
        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        table.setRowHeight(24);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> loadEquipment());
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private void loadEquipment() {
        tableModel.setRowCount(0);
        try {
            MaintainEquipment me = new MaintainEquipment();
            me.load(EQUIP_CSV);
            for (Equipment eq : me.equipmentList) {
                tableModel.addRow(new Object[]{
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
}
