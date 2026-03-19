package command;

import data.MaintainPayment;
import data.MaintainReservation;
import model.Equipment;
import model.Payment;
import model.Reservation;
import model.User;
import strategy.PaymentStrategy;
import util.SystemClock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PATTERN: Command — Make Reservation
 *
 * Preconditions (canExecute):
 *   1. Equipment must be available (canReserve() == true)
 *   2. No overlapping reservation for this equipment in the time slot
 *   3. Start time must be in the future
 *
 * Execute:
 *   1. Creates and saves the Reservation to CSV
 *   2. Charges the deposit (1 x hourly rate)
 *   3. Creates and saves a Payment record for the deposit
 */
public class MakeReservationCommand implements ReservationCommand {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final User            user;
    private final Equipment       equipment;
    private final LocalDateTime   startTime;
    private final LocalDateTime   endTime;
    private final String          reservationCsvPath;
    private final String          paymentCsvPath;
    private final PaymentStrategy paymentStrategy;

    private String errorMessage = "";

    public MakeReservationCommand(User user, Equipment equipment,
                                  LocalDateTime startTime, LocalDateTime endTime,
                                  String reservationCsvPath, String paymentCsvPath,
                                  PaymentStrategy paymentStrategy) {
        this.user               = user;
        this.equipment          = equipment;
        this.startTime          = startTime;
        this.endTime            = endTime;
        this.reservationCsvPath = reservationCsvPath;
        this.paymentCsvPath     = paymentCsvPath;
        this.paymentStrategy    = paymentStrategy;
    }

    @Override
    public boolean canExecute() {
        if (!equipment.canReserve()) {
            errorMessage = "Equipment is not available (state: "
                + equipment.getCurrentStateName() + ")";
            return false;
        }
        if (!startTime.isAfter(SystemClock.getInstance().now())) {
            errorMessage = "Start time must be in the future.";
            return false;
        }
        try {
            MaintainReservation mr = new MaintainReservation();
            mr.load(reservationCsvPath);
            if (mr.hasOverlap(equipment.getEquipmentId(), startTime, endTime, null)) {
                errorMessage = "Equipment is already booked during this time slot.";
                return false;
            }
        } catch (Exception e) {
            errorMessage = "Could not check availability: " + e.getMessage();
            return false;
        }
        return true;
    }

    @Override
    public void execute() throws Exception {
        MaintainReservation mr = new MaintainReservation();
        mr.load(reservationCsvPath);

        String reservationId = mr.nextId();
        double deposit       = user.getHourlyRate(); // 1-hour deposit

        Reservation reservation = new Reservation(
            reservationId, user, equipment, startTime, endTime, deposit
        );
        mr.reservations.add(reservation);
        mr.update(reservationCsvPath);

        // Charge deposit
        MaintainPayment mp = new MaintainPayment();
        mp.load(paymentCsvPath);

        String paymentId = mp.nextId();
        String timestamp = SystemClock.getInstance().now().format(FMT);
        boolean success  = paymentStrategy.processPayment(deposit);

        Payment payment = new Payment(
            paymentId, reservationId, user.getIdNumber(),
            deposit, paymentStrategy.getMethodName(),
            success ? "COMPLETED" : "FAILED",
            timestamp, "DEPOSIT"
        );
        mp.payments.add(payment);
        mp.update(paymentCsvPath);

        System.out.printf("[MakeReservation] Reserved %s for user %s | %s → %s | Deposit: $%.2f%n",
            equipment.getName(), user.getIdNumber(),
            startTime.format(FMT), endTime.format(FMT), deposit);
    }

    @Override
    public String getDescription() {
        return "Reserve " + equipment.getName() + " for user " + user.getIdNumber()
            + " from " + startTime.format(FMT) + " to " + endTime.format(FMT);
    }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
