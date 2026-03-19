package model;

import java.time.LocalDateTime;

public class Reservation {
    private String reservationId;
    private User user;
    private Equipment equipment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean arrived;
    private boolean depositForfeited;
    private double depositAmount;

    public Reservation(String reservationId, User user, Equipment equipment,
                       LocalDateTime startTime, LocalDateTime endTime, double depositAmount) {
        this.reservationId = reservationId;
        this.user = user;
        this.equipment = equipment;
        this.startTime = startTime;
        this.endTime = endTime;
        this.depositAmount = depositAmount;
        this.arrived = false;
        this.depositForfeited = false;
    }

    public String getReservationId() {
        return reservationId;
    }

    public User getUser() {
        return user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public boolean hasArrived() {
        return arrived;
    }

    public void markArrived() {
        this.arrived = true;
    }

    public boolean isDepositForfeited() {
        return depositForfeited;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void forfeitDeposit() {
        this.depositForfeited = true;
    }
}
