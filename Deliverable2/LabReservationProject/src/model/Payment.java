package model;

public class Payment {
    private String paymentId;
    private String reservationId;
    private String userId;
    private double amount;
    private String paymentMethod;
    private String status;     // COMPLETED, FAILED
    private String timestamp;
    private String type;       // DEPOSIT, FINAL

    public Payment(String paymentId, String reservationId, String userId,
                   double amount, String paymentMethod,
                   String status, String timestamp, String type) {
        this.paymentId      = paymentId;
        this.reservationId  = reservationId;
        this.userId         = userId;
        this.amount         = amount;
        this.paymentMethod  = paymentMethod;
        this.status         = status;
        this.timestamp      = timestamp;
        this.type           = type;
    }

    public String getPaymentId()     { return paymentId; }
    public String getReservationId() { return reservationId; }
    public String getUserId()        { return userId; }
    public double getAmount()        { return amount; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus()        { return status; }
    public String getTimestamp()     { return timestamp; }
    public String getType()          { return type; }
}