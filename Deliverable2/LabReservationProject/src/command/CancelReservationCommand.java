package command;

import data.MaintainReservation;
import model.Reservation;
import util.SystemClock;

/**
 * PATTERN: Command — Cancel Reservation
 *
 * Preconditions (canExecute):
 *   1. Current time must be BEFORE the reservation's start time
 *   2. Reservation must not already be cancelled
 *
 * Execute: Sets status to CANCELLED. Refunds deposit if not already forfeited.
 */
public class CancelReservationCommand implements ReservationCommand {

    private final Reservation reservation;
    private final String      reservationCsvPath;

    private String errorMessage = "";

    public CancelReservationCommand(Reservation reservation, String reservationCsvPath) {
        this.reservation        = reservation;
        this.reservationCsvPath = reservationCsvPath;
    }

    @Override
    public boolean canExecute() {
        if (!SystemClock.getInstance().now().isBefore(reservation.getStartTime())) {
            errorMessage = "Cannot cancel a reservation after it has started.";
            return false;
        }
        if ("CANCELLED".equals(reservation.getStatus())) {
            errorMessage = "Reservation is already cancelled.";
            return false;
        }
        return true;
    }

    @Override
    public void execute() throws Exception {
        MaintainReservation mr = new MaintainReservation();
        mr.load(reservationCsvPath);

        for (Reservation r : mr.reservations) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                r.setStatus("CANCELLED");
                if (!r.isDepositForfeited()) {
                    System.out.printf("[CancelReservation] Deposit refunded: $%.2f%n",
                        r.getDepositAmount());
                }
                break;
            }
        }
        mr.update(reservationCsvPath);
        System.out.println("[CancelReservation] Cancelled: " + reservation.getReservationId());
    }

    @Override
    public String getDescription() {
        return "Cancel reservation " + reservation.getReservationId();
    }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
