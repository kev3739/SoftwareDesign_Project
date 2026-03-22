package service;

import model.Reservation;
import util.Observer;

import java.time.LocalDateTime;
import java.util.List;

public class LateArrivalObserver implements Observer {
    private List<Reservation> reservations;

    public LateArrivalObserver(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public void update(Object eventData) {
        if (!(eventData instanceof LocalDateTime)) {
            return;
        }

        LocalDateTime currentTime = (LocalDateTime) eventData;

        for (Reservation reservation : reservations) {
            if (!reservation.hasArrived()
                    && !reservation.isDepositForfeited()
                    && currentTime.isAfter(reservation.getStartTime().plusMinutes(20))) {

                reservation.forfeitDeposit();
                System.out.println("Deposit forfeited for reservation: " + reservation.getReservationId());
            }
        }
    }
}
