package data;

import model.Reservation;
import model.Equipment;
import model.User;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving Reservation objects to/from CSV.
 * CSV columns: reservationId, userId, equipmentId, startTime, endTime, status, depositAmount, depositForfeited
 */
public class MaintainReservation {

    public List<Reservation> reservations = new ArrayList<>();

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void load(String path) throws Exception {
        reservations.clear();
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;

                String reservationId   = parts[0].trim();
                String userId          = parts[1].trim();
                String equipmentId     = parts[2].trim();
                LocalDateTime start    = LocalDateTime.parse(parts[3].trim(), FMT);
                LocalDateTime end      = LocalDateTime.parse(parts[4].trim(), FMT);
                String status          = parts[5].trim();
                double depositAmount   = Double.parseDouble(parts[6].trim());
                boolean depositForfeit = Boolean.parseBoolean(parts[7].trim());

                // Lightweight stubs so we can work with CSV data without full objects
                User stubUser = new StubUser(userId);
                Equipment stubEquipment = new Equipment(equipmentId, equipmentId, "");

                Reservation r = new Reservation(
                    reservationId, stubUser, stubEquipment, start, end, depositAmount
                );
                r.setStatus(status);
                if (depositForfeit) r.forfeitDeposit();

                reservations.add(r);
            }
        }
    }

    public void update(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            pw.println("reservationId,userId,equipmentId,startTime,endTime,status,depositAmount,depositForfeited");
            for (Reservation r : reservations) {
                pw.printf("%s,%s,%s,%s,%s,%s,%.2f,%b%n",
                    r.getReservationId(),
                    r.getUserId(),
                    r.getEquipmentId(),
                    r.getStartTime().format(FMT),
                    r.getEndTime().format(FMT),
                    r.getStatus(),
                    r.getDepositAmount(),
                    r.isDepositForfeited()
                );
            }
        }
    }

    /** Returns true if any ACTIVE/MODIFIED reservation for this equipment overlaps the window */
    public boolean hasOverlap(String equipmentId, LocalDateTime start, LocalDateTime end,
                               String excludeReservationId) {
        for (Reservation r : reservations) {
            if (!r.getEquipmentId().equals(equipmentId)) continue;
            if ("CANCELLED".equals(r.getStatus())) continue;
            if (excludeReservationId != null &&
                r.getReservationId().equals(excludeReservationId)) continue;

            // Overlap: not (end <= r.start || start >= r.end)
            if (!(end.compareTo(r.getStartTime()) <= 0 ||
                  start.compareTo(r.getEndTime()) >= 0)) {
                return true;
            }
        }
        return false;
    }

    /** String-based overlap check (delegates to LocalDateTime version) */
    public boolean hasOverlap(String equipmentId, String start, String end,
                               String excludeReservationId) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return hasOverlap(equipmentId,
            LocalDateTime.parse(start, fmt),
            LocalDateTime.parse(end, fmt),
            excludeReservationId);
    }

    /** Generates the next reservation ID (R001, R002, ...) */
    public String nextId() {
        return String.format("R%03d", reservations.size() + 1);
    }
}
