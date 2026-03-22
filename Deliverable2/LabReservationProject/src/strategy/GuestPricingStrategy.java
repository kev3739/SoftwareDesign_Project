package strategy;

public class GuestPricingStrategy implements PricingStrategy {
    @Override
    public double getRate() {
        return 30.0;
    }
}
