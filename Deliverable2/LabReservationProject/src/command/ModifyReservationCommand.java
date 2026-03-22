package command;

import data.MaintainReservation;
import model.Reservation;
import util.SystemClock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PATTERN: Command — Modify Reservation
 *
 * Preconditions (canExecute):
 *   1. Current time must be BEFORE the reservation's start time
 *   2. No overlap for the new time slot (excluding this reservation)
 *
 * Execute: Updates startTime and endTime, sets status to MODIFIED.
 */
public class ModifyReservationCommand implements ReservationCommand {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Reservation   reservation;
    private final LocalDateTime newStartTime;
    private final LocalDateTime newEndTime;
    private final String        reservationCsvPath;

    private String errorMessage = "";

    public ModifyReservationCommand(Reservation reservation,
                                    LocalDateTime newStartTime, LocalDateTime newEndTime,
                                    String reservationCsvPath) {
        this.reservation        = reservation;
        this.newStartTime       = newStartTime;
        this.newEndTime         = newEndTime;
        this.reservationCsvPath = reservationCsvPath;
    }

    @Override
    public boolean canExecute() {
        if (!SystemClock.getInstance().now().isBefore(reservation.getStartTime())) {
            errorMessage = "Cannot modify a reservation after it has started.";
            return false;
        }
        try {
            MaintainReservation mr = new MaintainReservation();
            mr.load(reservationCsvPath);
            if (mr.hasOverlap(reservation.getEquipmentId(), newStartTime, newEndTime,
                              reservation.getReservationId())) {
                errorMessage = "The new time slot overlaps an existing reservation.";
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

        for (Reservation r : mr.reservations) {
            if (r.getReservationId().equals(reservation.getReservationId())) {
                r.setStartTime(newStartTime);
                r.setEndTime(newEndTime);
                r.setStatus("MODIFIED");
                break;
            }
        }
        mr.update(reservationCsvPath);

        System.out.println("[ModifyReservation] " + reservation.getReservationId()
            + " → " + newStartTime.format(FMT) + " to " + newEndTime.format(FMT));
    }

    @Override
    public String getDescription() {
        return "Modify reservation " + reservation.getReservationId()
            + " to " + newStartTime.format(FMT) + " → " + newEndTime.format(FMT);
    }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
