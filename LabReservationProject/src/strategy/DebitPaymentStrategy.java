package strategy;

public class DebitPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing debit payment of $" + amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "Debit";
    }
}
