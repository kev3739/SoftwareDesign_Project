package service;

import command.*;
import data.MaintainReservation;
import model.Equipment;
import model.Reservation;
import model.User;
import strategy.PaymentStrategy;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service layer for all reservation operations.
 * Uses the Command pattern — every action goes through CommandManager
 * so preconditions are always checked before anything is executed.
 */
public class ReservationService {

    private final CommandManager commandManager;
    private final String         reservationCsvPath;
    private final String         paymentCsvPath;

    public ReservationService(String reservationCsvPath, String paymentCsvPath) {
        this.commandManager     = new CommandManager();
        this.reservationCsvPath = reservationCsvPath;
        this.paymentCsvPath     = paymentCsvPath;
    }

    /**
     * Make a new reservation for a user on a piece of equipment.
     * @return true if successful, false if preconditions failed
     */
    public boolean makeReservation(User user, Equipment equipment,
                                   LocalDateTime startTime, LocalDateTime endTime,
                                   PaymentStrategy paymentStrategy) {
        ReservationCommand cmd = new MakeReservationCommand(
            user, equipment, startTime, endTime,
            reservationCsvPath, paymentCsvPath, paymentStrategy
        );
        return commandManager.execute(cmd);
    }

    /**
     * Cancel an existing reservation.
     * Only allowed before the reservation's start time.
     * @return true if successful, false if preconditions failed
     */
    public boolean cancelReservation(Reservation reservation) {
        ReservationCommand cmd = new CancelReservationCommand(
            reservation, reservationCsvPath
        );
        return commandManager.execute(cmd);
    }

    /**
     * Modify the time slot of an existing reservation.
     * Only allowed before the reservation's start time.
     * @return true if successful, false if preconditions failed
     */
    public boolean modifyReservation(Reservation reservation,
                                     LocalDateTime newStartTime,
                                     LocalDateTime newEndTime) {
        ReservationCommand cmd = new ModifyReservationCommand(
            reservation, newStartTime, newEndTime, reservationCsvPath
        );
        return commandManager.execute(cmd);
    }

    /**
     * Extend the end time of an existing reservation.
     * Only allowed if equipment is free during the extension window.
     * @return true if successful, false if preconditions failed
     */
    public boolean extendReservation(Reservation reservation,
                                     LocalDateTime newEndTime) {
        ReservationCommand cmd = new ExtendReservationCommand(
            reservation, newEndTime, reservationCsvPath
        );
        return commandManager.execute(cmd);
    }

    /**
     * Returns all reservations from CSV.
     */
    public List<Reservation> getAllReservations() {
        try {
            MaintainReservation mr = new MaintainReservation();
            mr.load(reservationCsvPath);
            return mr.reservations;
        } catch (Exception e) {
            System.out.println("[ReservationService] Could not load reservations: " + e.getMessage());
            return List.of();
        }
    }

    /**
     * Returns all reservations for a specific user.
     */
    public List<Reservation> getReservationsForUser(String userId) {
        return getAllReservations().stream()
            .filter(r -> r.getUserId().equals(userId))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Returns all reservations for a specific piece of equipment.
     */
    public List<Reservation> getReservationsForEquipment(String equipmentId) {
        return getAllReservations().stream()
            .filter(r -> r.getEquipmentId().equals(equipmentId))
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Prints the full command history (useful for audit log / demo).
     */
    public void printCommandHistory() {
        commandManager.printHistory();
    }

    /**
     * Returns the last error message from the most recent failed command.
     */
    public String getLastError() {
        return "";
    }
}
