package command;

import data.MaintainReservation;
import model.Reservation;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * PATTERN: Command — Extend Reservation
 *
 * Preconditions (canExecute):
 *   1. The new end time must be AFTER the current end time
 *   2. Equipment must be available between current end and new end
 *
 * Execute: Updates the endTime to newEndTime, keeps status ACTIVE/MODIFIED.
 */
public class ExtendReservationCommand implements ReservationCommand {

    private static final DateTimeFormatter FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final Reservation   reservation;
    private final LocalDateTime newEndTime;
    private final String        reservationCsvPath;

    private String errorMessage = "";

    public ExtendReservationCommand(Reservation reservation,
                                    LocalDateTime newEndTime,
                                    String reservationCsvPath) {
        this.reservation        = reservation;
        this.newEndTime         = newEndTime;
        this.reservationCsvPath = reservationCsvPath;
    }

    @Override
    public boolean canExecute() {
        if (!newEndTime.isAfter(reservation.getEndTime())) {
            errorMessage = "New end time must be after the current end time.";
            return false;
        }
        try {
            MaintainReservation mr = new MaintainReservation();
            mr.load(reservationCsvPath);
            if (mr.hasOverlap(reservation.getEquipmentId(),
                              reservation.getEndTime(), newEndTime,
                              reservation.getReservationId())) {
                errorMessage = "Equipment is already booked during the extension period.";
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
                LocalDateTime oldEnd = r.getEndTime();
                r.setEndTime(newEndTime);
                System.out.println("[ExtendReservation] " + reservation.getReservationId()
                    + " extended from " + oldEnd.format(FMT) + " → " + newEndTime.format(FMT));
                break;
            }
        }
        mr.update(reservationCsvPath);
    }

    @Override
    public String getDescription() {
        return "Extend reservation " + reservation.getReservationId()
            + " to " + newEndTime.format(FMT);
    }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
