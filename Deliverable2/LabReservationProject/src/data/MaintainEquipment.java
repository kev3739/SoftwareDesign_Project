package data;

import model.DisabledState;
import model.Equipment;
import model.MaintenanceState;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MaintainEquipment {

    public List<Equipment> equipmentList = new ArrayList<>();

    public void load(String path) throws Exception {
        equipmentList.clear();
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 4) continue;

                String equipmentId = parts[0].trim();
                String description = parts[1].trim();
                String labLocation = parts[2].trim();
                String status      = parts[3].trim();

                Equipment equipment = new Equipment(equipmentId, description, labLocation);

                // Restore state silently using setState
                if ("DISABLED".equals(status)) {
                    equipment.setState(new DisabledState());
                    equipment.setStatus(status);
                } else if ("MAINTENANCE".equals(status)) {
                    equipment.setState(new MaintenanceState());
                    equipment.setStatus(status);
                }

                equipmentList.add(equipment);
            }
        }
    }

    public void update(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            pw.println("equipmentId,description,labLocation,status");
            for (Equipment e : equipmentList) {
                pw.printf("%s,%s,%s,%s%n",
                    e.getEquipmentId(),
                    e.getDescription(),
                    e.getLabLocation(),
                    e.getCurrentStateName().toUpperCase()
                );
            }
        }
    }

    public Equipment findById(String equipmentId) {
        return equipmentList.stream()
            .filter(e -> e.getEquipmentId().equals(equipmentId))
            .findFirst()
            .orElse(null);
    }

    public List<Equipment> getAvailableEquipment() {
        return equipmentList.stream()
            .filter(Equipment::canReserve)
            .collect(java.util.stream.Collectors.toList());
    }
}
