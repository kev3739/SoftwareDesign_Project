package service;

import strategy.PricingStrategy;
import strategy.PaymentStrategy;

public class BillingService {
    private PricingStrategy pricingStrategy;
    private PaymentStrategy paymentStrategy;

    public void setPricingStrategy(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    public void setPaymentStrategy(PaymentStrategy paymentStrategy) {
        this.paymentStrategy = paymentStrategy;
    }

    public double calculateFee(double hours) {
        if (pricingStrategy == null) {
            throw new IllegalStateException("Pricing strategy not set");
        }
        return hours * pricingStrategy.getRate();
    }

    public double calculateFeeWithDeposit(double hours, boolean depositForfeited) {
        double total = calculateFee(hours);
        if (!depositForfeited) {
            total -= pricingStrategy.getRate(); // deposit = 1 hour rate
        }
        return total;
    }

    public double getDeposit() {
        if (pricingStrategy == null) {
            throw new IllegalStateException("Pricing strategy not set");
        }
        return pricingStrategy.getRate(); // deposit = 1 hour's fee
    }

    public boolean processPayment(double amount) {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return paymentStrategy.processPayment(amount);
    }

    public String getPaymentMethodName() {
        if (paymentStrategy == null) {
            throw new IllegalStateException("Payment strategy not set");
        }
        return paymentStrategy.getMethodName();
    }

    public double getHourlyRate() {
        if (pricingStrategy == null) {
            throw new IllegalStateException("Pricing strategy not set");
        }
        return pricingStrategy.getRate();
    }
}
