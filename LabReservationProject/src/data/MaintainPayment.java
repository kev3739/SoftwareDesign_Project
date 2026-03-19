package data;

import model.Payment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles loading and saving Payment objects to/from CSV.
 * CSV columns: paymentId, reservationId, userId, amount, paymentMethod, status, timestamp, type
 */
public class MaintainPayment {

    public List<Payment> payments = new ArrayList<>();

    public void load(String path) throws Exception {
        payments.clear();
        File file = new File(path);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            br.readLine(); // skip header
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 8) continue;

                payments.add(new Payment(
                    parts[0].trim(), parts[1].trim(), parts[2].trim(),
                    Double.parseDouble(parts[3].trim()),
                    parts[4].trim(), parts[5].trim(),
                    parts[6].trim(), parts[7].trim()
                ));
            }
        }
    }

    public void update(String path) throws Exception {
        File file = new File(path);
        file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file, false))) {
            pw.println("paymentId,reservationId,userId,amount,paymentMethod,status,timestamp,type");
            for (Payment p : payments) {
                pw.printf("%s,%s,%s,%.2f,%s,%s,%s,%s%n",
                    p.getPaymentId(), p.getReservationId(), p.getUserId(),
                    p.getAmount(), p.getPaymentMethod(),
                    p.getStatus(), p.getTimestamp(), p.getType()
                );
            }
        }
    }

    /** Generates the next payment ID (P001, P002, ...) */
    public String nextId() {
        return String.format("P%03d", payments.size() + 1);
    }
}