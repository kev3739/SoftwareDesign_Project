package strategy;

public class InstitutionalAccountPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing institutional account payment of $" + amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "Institutional Account";
    }
}
