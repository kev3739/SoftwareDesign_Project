package strategy;

public class ResearchGrantPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing research grant payment of $" + amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "Research Grant";
    }
}
