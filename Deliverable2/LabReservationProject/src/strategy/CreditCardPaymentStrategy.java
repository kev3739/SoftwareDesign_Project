package strategy;

public class CreditCardPaymentStrategy implements PaymentStrategy {
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing credit card payment of $" + amount);
        return true;
    }

    @Override
    public String getMethodName() {
        return "Credit Card";
    }
}
